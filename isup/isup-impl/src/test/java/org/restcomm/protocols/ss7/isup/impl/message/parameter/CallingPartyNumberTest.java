/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.isup.message.parameter.CallingPartyNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.NAINumber;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CallingPartyNumberTest {
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    private ByteBuf getData() {
        return Unpooled.wrappedBuffer(new byte[] { (byte) 0x83, (byte) 0xC2, 0x21, 0x43, 0x05 });
    }

    private ByteBuf getData2() {
        return Unpooled.wrappedBuffer(new byte[] { (byte) 0x03, (byte) 0xC2, 0x21, 0x43, 0x65 });
    }

    private ByteBuf getData3() {
        return Unpooled.wrappedBuffer(new byte[] { (byte) 0x00, 0x0B });
    }

    @Test(groups = { "functional.decode", "parameter" })
    public void testDecode() throws Exception {

        CallingPartyNumberImpl prim = new CallingPartyNumberImpl();
        prim.decode(getData());

        assertEquals(prim.getAddress(), "12345");
        assertEquals(prim.getNatureOfAddressIndicator(), NAINumber._NAI_NATIONAL_SN);
        assertEquals(prim.getNumberingPlanIndicator(), CallingPartyNumber._NPI_TELEX);
        assertEquals(prim.getNumberIncompleteIndicator(), CallingPartyNumber._NI_INCOMPLETE);
        assertEquals(prim.getAddressRepresentationRestrictedIndicator(), CallingPartyNumber._APRI_ALLOWED);
        assertEquals(prim.getScreeningIndicator(), CallingPartyNumber._SI_USER_PROVIDED_FAILED);
        assertTrue(prim.isOddFlag());

        prim = new CallingPartyNumberImpl();
        prim.decode(getData2());

        assertEquals(prim.getAddress(), "123456");
        assertEquals(prim.getNatureOfAddressIndicator(), NAINumber._NAI_NATIONAL_SN);
        assertEquals(prim.getNumberingPlanIndicator(), CallingPartyNumber._NPI_TELEX);
        assertEquals(prim.getNumberIncompleteIndicator(), CallingPartyNumber._NI_INCOMPLETE);
        assertEquals(prim.getAddressRepresentationRestrictedIndicator(), CallingPartyNumber._APRI_ALLOWED);
        assertEquals(prim.getScreeningIndicator(), CallingPartyNumber._SI_USER_PROVIDED_FAILED);
        assertFalse(prim.isOddFlag());

        prim = new CallingPartyNumberImpl();
        prim.decode(getData3());

        assertEquals(prim.getAddress(), "");
        assertEquals(prim.getNatureOfAddressIndicator(), 0);
        assertEquals(prim.getNumberingPlanIndicator(), 0);
        assertEquals(prim.getNumberIncompleteIndicator(), 0);
        assertEquals(prim.getAddressRepresentationRestrictedIndicator(), CallingPartyNumber._APRI_NOT_AVAILABLE);
        assertEquals(prim.getScreeningIndicator(), CallingPartyNumber._SI_NETWORK_PROVIDED);
        assertFalse(prim.isOddFlag());
    }

    @Test(groups = { "functional.encode", "parameter" })
    public void testEncode() throws Exception {

        CallingPartyNumberImpl prim = new CallingPartyNumberImpl(NAINumber._NAI_NATIONAL_SN, "12345",
                CallingPartyNumber._NPI_TELEX, CallingPartyNumber._NI_INCOMPLETE, CallingPartyNumber._APRI_ALLOWED,
                CallingPartyNumber._SI_USER_PROVIDED_FAILED);
        // int natureOfAddresIndicator, String address, int numberingPlanIndicator, int numberIncompleteIndicator,
        // int addressRepresentationREstrictedIndicator, int screeningIndicator

        ByteBuf data = getData();
        ByteBuf encodedData=Unpooled.buffer();
        prim.encode(encodedData);

        assertTrue(ParameterHarness.byteBufEquals(data, encodedData));

        prim = new CallingPartyNumberImpl(NAINumber._NAI_NATIONAL_SN, "123456", CallingPartyNumber._NPI_TELEX,
                CallingPartyNumber._NI_INCOMPLETE, CallingPartyNumber._APRI_ALLOWED,
                CallingPartyNumber._SI_USER_PROVIDED_FAILED);

        data = getData2();
        encodedData=Unpooled.buffer();
        prim.encode(encodedData);

        assertTrue(ParameterHarness.byteBufEquals(data, encodedData));

        prim = new CallingPartyNumberImpl(NAINumber._NAI_NATIONAL_SN, "123456", CallingPartyNumber._NPI_TELEX,
                CallingPartyNumber._NI_INCOMPLETE, CallingPartyNumber._APRI_NOT_AVAILABLE,
                CallingPartyNumber._SI_USER_PROVIDED_FAILED);

        data = getData3();
        encodedData=Unpooled.buffer();
        prim.encode(encodedData);

        assertTrue(ParameterHarness.byteBufEquals(data, encodedData));
    }
}
