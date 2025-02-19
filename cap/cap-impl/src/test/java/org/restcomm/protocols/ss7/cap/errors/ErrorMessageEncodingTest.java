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

package org.restcomm.protocols.ss7.cap.errors;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.api.errors.CancelProblem;
import org.restcomm.protocols.ss7.cap.api.errors.RequestedInfoErrorParameter;
import org.restcomm.protocols.ss7.cap.api.errors.TaskRefusedParameter;
import org.restcomm.protocols.ss7.cap.api.errors.UnavailableNetworkResource;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ErrorMessageEncodingTest {

    public byte[] getDataTaskRefused() {
        return new byte[] { 10, 1, 2 };
    }

    public byte[] getDataSystemFailure() {
        return new byte[] { 10, 1, 3 };
    }

    public byte[] getDataRequestedInfoError() {
        return new byte[] { 10, 1, 1 };
    }

    public byte[] getDataCancelFailed() {
        return new byte[] { 10, 1, 1 };
    }

    @Test(groups = { "functional.decode", "errors.primitive" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CAPErrorMessageTaskRefusedImpl.class);
    	
    	byte[] rawData = this.getDataTaskRefused();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CAPErrorMessageTaskRefusedImpl);
        
        CAPErrorMessageTaskRefusedImpl elem = (CAPErrorMessageTaskRefusedImpl)result.getResult();        
        assertEquals(elem.getTaskRefusedParameter(), TaskRefusedParameter.congestion);

        parser.replaceClass(CAPErrorMessageSystemFailureImpl.class);
    	rawData = this.getDataSystemFailure();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CAPErrorMessageSystemFailureImpl);
        
        CAPErrorMessageSystemFailureImpl elem2 = (CAPErrorMessageSystemFailureImpl)result.getResult();    
        assertEquals(elem2.getUnavailableNetworkResource(), UnavailableNetworkResource.resourceStatusFailure);
        
        parser.replaceClass(CAPErrorMessageRequestedInfoErrorImpl.class);
    	rawData = this.getDataRequestedInfoError();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CAPErrorMessageRequestedInfoErrorImpl);
        
        CAPErrorMessageRequestedInfoErrorImpl elem3 = (CAPErrorMessageRequestedInfoErrorImpl)result.getResult();    
        assertEquals(elem3.getRequestedInfoErrorParameter(), RequestedInfoErrorParameter.unknownRequestedInfo);

        parser.replaceClass(CAPErrorMessageCancelFailedImpl.class);
    	rawData = this.getDataCancelFailed();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CAPErrorMessageCancelFailedImpl);
        
        CAPErrorMessageCancelFailedImpl elem4 = (CAPErrorMessageCancelFailedImpl)result.getResult();    
        assertEquals(elem4.getCancelProblem(), CancelProblem.tooLate);
    }

    @Test(groups = { "functional.encode", "errors.primitive" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	
        CAPErrorMessageTaskRefusedImpl elem = new CAPErrorMessageTaskRefusedImpl(TaskRefusedParameter.congestion);
        byte[] rawData = this.getDataTaskRefused();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        CAPErrorMessageSystemFailureImpl elem2 = new CAPErrorMessageSystemFailureImpl(
                UnavailableNetworkResource.resourceStatusFailure);
        rawData = this.getDataSystemFailure();
        buffer=parser.encode(elem2);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
        
        CAPErrorMessageRequestedInfoErrorImpl elem3 = new CAPErrorMessageRequestedInfoErrorImpl(
                RequestedInfoErrorParameter.unknownRequestedInfo);
        rawData = this.getDataRequestedInfoError();
        buffer=parser.encode(elem3);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        CAPErrorMessageCancelFailedImpl elem4 = new CAPErrorMessageCancelFailedImpl(CancelProblem.tooLate);
        rawData = this.getDataCancelFailed();
        buffer=parser.encode(elem4);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
