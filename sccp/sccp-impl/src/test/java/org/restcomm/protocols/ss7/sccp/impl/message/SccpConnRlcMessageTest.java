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

package org.restcomm.protocols.ss7.sccp.impl.message;

import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.LongMessageRuleType;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.EncodingResultData;
import org.restcomm.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnRlcMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.LocalReferenceImpl;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class SccpConnRlcMessageTest {

    private Logger logger;
    private SccpStackImpl stack = new SccpStackImpl("SccpConnRlcMessageTestStack");
    private MessageFactoryImpl messageFactory;

    @BeforeMethod
    public void setUp() {
        this.messageFactory = new MessageFactoryImpl(stack);
        this.logger = Logger.getLogger(SccpStackImpl.class.getCanonicalName());
    }

    @AfterMethod
    public void tearDown() {
    }

    public ByteBuf getDataRlc() {
        return Unpooled.wrappedBuffer(new byte[]{ 0x05, 0x00, 0x00, 0x02, 0x00, 0x00, 0x03 });
    }

    @Test(groups = {"SccpMessage", "functional.decode"})
    public void testDecode() throws Exception {
    	ByteBuf buf = this.getDataRlc();
        int type = buf.readByte();
        SccpConnRlcMessageImpl testObjectDecoded = (SccpConnRlcMessageImpl) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);

        assertNotNull(testObjectDecoded);
        assertEquals(testObjectDecoded.getDestinationLocalReferenceNumber().getValue(), 2);
        assertEquals(testObjectDecoded.getSourceLocalReferenceNumber().getValue(), 3);
    }

    @Test(groups = {"SccpMessage", "functional.encode"})
    public void testEncode() throws Exception {
        SccpConnRlcMessageImpl original = new SccpConnRlcMessageImpl(0, 0);
        original.setDestinationLocalReferenceNumber(new LocalReferenceImpl(2));
        original.setSourceLocalReferenceNumber(new LocalReferenceImpl(3));

        EncodingResultData encoded = original.encode(stack, LongMessageRuleType.LONG_MESSAGE_FORBBIDEN, 272, logger, false, SccpProtocolVersion.ITU);

        MessageSegmentationTest.assertByteBufs(encoded.getSolidData(), this.getDataRlc());
    }
}
