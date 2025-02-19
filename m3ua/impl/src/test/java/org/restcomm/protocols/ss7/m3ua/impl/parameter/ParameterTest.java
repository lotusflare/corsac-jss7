/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.protocols.ss7.m3ua.impl.parameter;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.util.Arrays;

import org.restcomm.protocols.ss7.m3ua.parameter.DeregistrationStatus;
import org.restcomm.protocols.ss7.m3ua.parameter.LocalRKIdentifier;
import org.restcomm.protocols.ss7.m3ua.parameter.RegistrationStatus;
import org.restcomm.protocols.ss7.m3ua.parameter.RoutingContext;
import org.restcomm.protocols.ss7.m3ua.parameter.Status;
import org.restcomm.protocols.ss7.m3ua.parameter.CongestedIndication.CongestionLevel;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author amit bhayani
 * @author kulikov
 */
public class ParameterTest {

    private ParameterFactoryImpl factory = new ParameterFactoryImpl();
    private ByteBuf out = null;

    public ParameterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUp() {
        out = Unpooled.buffer(8192);
    }

    @AfterMethod
    public void tearDown() {
    }

    private short getTag(byte[] data) {
        return (short) ((data[0] & 0xff) << 8 | (data[1] & 0xff));
    }

    private short getLen(byte[] data) {
        return (short) ((data[2] & 0xff) << 8 | (data[3] & 0xff));
    }

    private ByteBuf getValue(byte[] data) {
        // reduce 4 for Tag + length bytes
        short length = (short) (getLen(data) - 4);
        byte[] value = new byte[length];
        System.arraycopy(data, 4, value, 0, length);
        return Unpooled.wrappedBuffer(value);
    }

    @Test
    public void testProtocolData() throws IOException {

        // Trace from wireshark
        byte[] userData = new byte[] { 0x09, (byte) 0x80, 0x03, 0x0c, 0x15, 0x09, 0x12, 0x05, 0x00, 0x12, 0x04, 0x55, 0x16,
                0x09, (byte) 0x90, 0x09, 0x12, 0x07, 0x00, 0x12, 0x04, 0x55, 0x16, 0x09, 0x00, 0x5f, 0x62, 0x5d, 0x48, 0x04,
                0x3a, (byte) 0x8c, 0x10, 0x04, 0x6b, 0x3f, 0x28, 0x3d, 0x06, 0x07, 0x00, 0x11, (byte) 0x86, 0x05, 0x01, 0x01,
                0x01, (byte) 0xa0, 0x32, 0x60, 0x30, (byte) 0x80, 0x02, 0x07, (byte) 0x80, (byte) 0xa1, 0x09, 0x06, 0x07, 0x04,
                0x00, 0x00, 0x01, 0x00, 0x13, 0x02, (byte) 0xbe, 0x1f, 0x28, 0x1d, 0x06, 0x07, 0x04, 0x00, 0x00, 0x01, 0x01,
                0x01, 0x01, (byte) 0xa0, 0x12, (byte) 0xa0, 0x10, (byte) 0x80, 0x07, (byte) 0x91, 0x55, 0x16, 0x28,
                (byte) 0x81, 0x00, 0x70, (byte) 0x81, 0x05, (byte) 0x91, 0x55, 0x16, 0x09, 0x00, 0x6c, 0x14, (byte) 0xa1, 0x12,
                0x02, 0x01, 0x00, 0x02, 0x01, 0x3b, 0x30, 0x0a, 0x04, 0x01, 0x0f, 0x04, 0x05, 0x2a, (byte) 0xd9, (byte) 0x8c,
                0x36, 0x02 };

        byte[] protocolData = new byte[userData.length + 12];

        System.arraycopy(userData, 0, protocolData, 12, userData.length);
        protocolData[0] = 0x00;
        protocolData[1] = 0x00;
        protocolData[2] = 0x1e;
        protocolData[3] = (byte) 0xd4;
        protocolData[4] = 0x00;
        protocolData[5] = 0x00;
        protocolData[6] = 0x08;
        protocolData[7] = (byte) 0x98;
        protocolData[8] = 0x03;
        protocolData[9] = 0x03;
        protocolData[10] = 0x00;
        protocolData[11] = 0x0f;

        int si = 3;
        int mp = 0;
        int ni = 3;
        int dpc = 2200;
        int opc = 7892;
        int sls = 15;
        ProtocolDataImpl p1 = (ProtocolDataImpl) factory.createProtocolData(opc, dpc, si, ni, mp, sls, Unpooled.wrappedBuffer(userData));

        ByteBuf buf=p1.getValue();
        byte[] p1Arr=new byte[buf.readableBytes()];
        buf.readBytes(p1Arr);
        assertTrue(Arrays.equals(protocolData, p1Arr));

        ProtocolDataImpl p2 = (ProtocolDataImpl) factory.createProtocolData(Unpooled.wrappedBuffer(protocolData));

        assertEquals(p1.getTag(), p2.getTag());
        assertEquals(p1.getOpc(), p2.getOpc());
        assertEquals(p1.getDpc(), p2.getDpc());
        assertEquals(p2.getSI(), p2.getSI());
        assertEquals(p2.getNI(), p2.getNI());
        assertEquals(p2.getMP(), p2.getMP());
        assertEquals(p2.getSLS(), p2.getSLS());

        buf=p1.getValue();
        p1Arr=new byte[buf.readableBytes()];
        buf.readBytes(p1Arr);
        
        ByteBuf buf2=p2.getValue();
        byte[] p2Arr=new byte[buf2.readableBytes()];
        buf2.readBytes(p2Arr);
        boolean isDataCorrect = Arrays.equals(p1Arr, p2Arr);
        assertTrue(isDataCorrect, "Data mismatch");
    }

    /**
     * Test of getOpc method, of class ProtocolDataImpl.
     */
    @Test
    public void testProtocolData1() throws IOException {
        ProtocolDataImpl p1 = (ProtocolDataImpl) factory.createProtocolData(1408, 14150, 1, 1, 0, 1, Unpooled.wrappedBuffer(new byte[] { 1, 2, 3, 4 }));
        p1.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        ProtocolDataImpl p2 = (ProtocolDataImpl) factory.createParameter(getTag(data), getValue(data));

        assertEquals(p1.getTag(), p2.getTag());
        assertEquals(p1.getOpc(), p2.getOpc());
        assertEquals(p1.getDpc(), p2.getDpc());
        assertEquals(p1.getSI(), p2.getSI());
        assertEquals(p1.getNI(), p2.getNI());
        assertEquals(p1.getMP(), p2.getMP());
        assertEquals(p1.getSLS(), p2.getSLS());

        ByteBuf p1Buf=p1.getData();
        ByteBuf p2Buf=p2.getData();
        byte[] p1Arr=new byte[p1Buf.readableBytes()];
        p1Buf.readBytes(p1Arr);
        byte[] p2Arr=new byte[p2Buf.readableBytes()];
        p2Buf.readBytes(p2Arr);
        boolean isDataCorrect = Arrays.equals(p1Arr, p2Arr);
        assertTrue(isDataCorrect, "Data mismatch");
    }

    @Test
    public void testCorrelationId() throws IOException {
        CorrelationIdImpl crrId = (CorrelationIdImpl) factory.createCorrelationId(4294967295l);
        crrId.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        CorrelationIdImpl crrId2 = (CorrelationIdImpl) factory.createParameter(getTag(data), getValue(data));

        assertEquals(4294967295l, crrId2.getCorrelationId());
    }

    @Test
    public void testAffectedPointCode() throws IOException {

        AffectedPointCodeImpl affectedPc = (AffectedPointCodeImpl) factory.createAffectedPointCode(new int[] { 123 },
                new short[] { 0 });
        affectedPc.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        AffectedPointCodeImpl affectedPc2 = (AffectedPointCodeImpl) factory.createParameter(getTag(data), getValue(data));

        assertTrue(Arrays.equals(new int[] { 123 }, affectedPc2.getPointCodes()));
        assertTrue(Arrays.equals(new short[] { 0 }, affectedPc2.getMasks()));
    }

    @Test
    public void testAffectedPointCodes() throws IOException {
        AffectedPointCodeImpl affectedPc = (AffectedPointCodeImpl) factory.createAffectedPointCode(new int[] { 123, 456 },
                new short[] { 0, 1 });
        affectedPc.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        AffectedPointCodeImpl affectedPc2 = (AffectedPointCodeImpl) factory.createParameter(getTag(data), getValue(data));

        assertTrue(Arrays.equals(new int[] { 123, 456 }, affectedPc2.getPointCodes()));
        assertTrue(Arrays.equals(new short[] { 0, 1 }, affectedPc2.getMasks()));
    }

    @Test
    public void testInfoString() throws IOException {
        InfoStringImpl infoStr = (InfoStringImpl) factory.createInfoString("Hello World");
        infoStr.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        InfoStringImpl infoStr2 = (InfoStringImpl) factory.createParameter(getTag(data), getValue(data));

        assertEquals("Hello World", infoStr2.getString());

    }

    @Test
    public void testConcernedDPC() throws IOException {
        ConcernedDPCImpl concernedDPC = (ConcernedDPCImpl) factory.createConcernedDPC(123);
        concernedDPC.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        ConcernedDPCImpl concernedDPC2 = (ConcernedDPCImpl) factory.createParameter(getTag(data), getValue(data));

        assertEquals(concernedDPC.getPointCode(), concernedDPC2.getPointCode());

    }

    @Test
    public void testCongestedIndication() throws IOException {
        CongestedIndicationImpl congIndImpl = (CongestedIndicationImpl) factory
                .createCongestedIndication(CongestionLevel.LEVEL2);
        congIndImpl.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        CongestedIndicationImpl congIndImpl2 = (CongestedIndicationImpl) factory.createParameter(getTag(data), getValue(data));

        assertEquals(congIndImpl.getCongestionLevel(), congIndImpl2.getCongestionLevel());

    }

    @Test
    public void testUserCause() throws IOException {
        UserCauseImpl usrCa = (UserCauseImpl) factory.createUserCause(5, 0);
        usrCa.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        UserCauseImpl usrCa2 = (UserCauseImpl) factory.createParameter(getTag(data), getValue(data));

        assertEquals(usrCa.getUser(), usrCa2.getUser());

        assertEquals(usrCa.getCause(), usrCa2.getCause());

    }

    @Test
    public void testASPIdentifier() throws IOException {
        ASPIdentifierImpl rc = (ASPIdentifierImpl) factory.createASPIdentifier(12234445);
        rc.write(out);
        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        ASPIdentifierImpl rc2 = (ASPIdentifierImpl) factory.createParameter(getTag(data), getValue(data));

        assertEquals(12234445l, rc2.getAspId());
    }

    @Test
    public void testRegistrationStatus() throws IOException {
        RegistrationStatusImpl crrId = (RegistrationStatusImpl) factory.createRegistrationStatus(11);
        crrId.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        RegistrationStatusImpl crrId2 = (RegistrationStatusImpl) factory.createParameter(getTag(data), getValue(data));

        assertEquals(11, crrId2.getStatus());
    }

    @Test
    public void testRegistrationResult() throws IOException {
        LocalRKIdentifier localRkId = factory.createLocalRKIdentifier(12);
        RoutingContext rc = factory.createRoutingContext(new long[] { 1 });
        RegistrationStatus status = factory.createRegistrationStatus(0);

        RegistrationResultImpl routKey = (RegistrationResultImpl) factory.createRegistrationResult(localRkId, status, rc);
        routKey.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        RegistrationResultImpl rc2 = (RegistrationResultImpl) factory.createParameter(getTag(data), getValue(data));

        assertEquals(localRkId.getId(), rc2.getLocalRKIdentifier().getId());
        assertTrue(Arrays
                .equals(routKey.getRoutingContext().getRoutingContexts(), rc2.getRoutingContext().getRoutingContexts()));
        assertEquals(status.getStatus(), rc2.getRegistrationStatus().getStatus());
    }

    @Test
    public void testDeregistrationStatus() throws IOException {
        DeregistrationStatusImpl crrId = (DeregistrationStatusImpl) factory.createDeregistrationStatus(5);
        crrId.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        DeregistrationStatusImpl crrId2 = (DeregistrationStatusImpl) factory.createParameter(getTag(data), getValue(data));

        assertEquals(5, crrId2.getStatus());
    }

    @Test
    public void testDeregistrationResult() throws IOException {
        RoutingContext rc = factory.createRoutingContext(new long[] { 1 });
        DeregistrationStatus status = factory.createDeregistrationStatus(0);

        DeregistrationResultImpl routKey = (DeregistrationResultImpl) factory.createDeregistrationResult(rc, status);
        routKey.write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        DeregistrationResultImpl rc2 = (DeregistrationResultImpl) factory.createParameter(getTag(data), getValue(data));

        assertTrue(Arrays
                .equals(routKey.getRoutingContext().getRoutingContexts(), rc2.getRoutingContext().getRoutingContexts()));
        assertEquals(status.getStatus(), rc2.getDeregistrationStatus().getStatus());
    }

    @Test
    public void testStatus() throws IOException {

        Status routKey = (Status) factory.createStatus(1, 4);
        ((StatusImpl) routKey).write(out);

        int length = out.readableBytes();
        byte[] data = new byte[length];
        out.getBytes(out.readerIndex(), data);

        StatusImpl rc2 = (StatusImpl) factory.createParameter(getTag(data), getValue(data));

        assertEquals(routKey.getType(), rc2.getType());
        assertEquals(routKey.getInfo(), rc2.getInfo());
    }
}