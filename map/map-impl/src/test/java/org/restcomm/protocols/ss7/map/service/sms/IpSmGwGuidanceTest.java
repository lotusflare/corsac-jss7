/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.restcomm.protocols.ss7.map.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.service.sms.IpSmGwGuidanceImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author eva ogallar
 * @author sergey vetyutnev
 *
 */
public class IpSmGwGuidanceTest {

    private byte[] getEncodedData() {
        return new byte[] {48,6,2,1,30,2,1,40};
    }

    private byte[] getEncodedDataFull() {
        return new byte[] {48, 53, 2, 1, 30, 2, 1, 40, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33};
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(IpSmGwGuidanceImpl.class);
    	        
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof IpSmGwGuidanceImpl);
        IpSmGwGuidanceImpl ipSmGwGuidance = (IpSmGwGuidanceImpl)result.getResult();
        
        assertEquals(ipSmGwGuidance.getMinimumDeliveryTimeValue(), 30);
        assertEquals(ipSmGwGuidance.getRecommendedDeliveryTimeValue(), 40);

        rawData = getEncodedDataFull();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof IpSmGwGuidanceImpl);
        ipSmGwGuidance = (IpSmGwGuidanceImpl)result.getResult();

        assertEquals(ipSmGwGuidance.getMinimumDeliveryTimeValue(), 30);
        assertEquals(ipSmGwGuidance.getRecommendedDeliveryTimeValue(), 40);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ipSmGwGuidance.getExtensionContainer()));
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(IpSmGwGuidanceImpl.class);
        
        IpSmGwGuidanceImpl liw = new IpSmGwGuidanceImpl(30, 40, null);
        ByteBuf buffer=parser.encode(liw);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        liw = new IpSmGwGuidanceImpl(30, 40, MAPExtensionContainerTest.GetTestExtensionContainer());
        buffer=parser.encode(liw);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedDataFull();
        assertTrue(Arrays.equals(rawData, encodedData));

    }
}
