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
package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AllocationRetentionPriorityImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSQoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSClassIdentifier;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class EPSQoSSubscribedTest {

    public byte[] getData() {
        return new byte[] { 48, 108, -128, 1, 1, -95, 56, -128, 1, 1, -127, 1, -1, -126, 1, -1, -93, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(EPSQoSSubscribedImpl.class);
    	                
        byte[] data = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EPSQoSSubscribedImpl);
        EPSQoSSubscribedImpl prim = (EPSQoSSubscribedImpl)result.getResult(); 
        
        AllocationRetentionPriorityImpl allocationRetentionPriority = prim.getAllocationRetentionPriority();
        MAPExtensionContainerImpl extensionContainer = prim.getExtensionContainer();
        assertEquals(allocationRetentionPriority.getPriorityLevel(), 1);
        assertTrue(allocationRetentionPriority.getPreEmptionCapability());
        assertTrue(allocationRetentionPriority.getPreEmptionVulnerability());
        assertNotNull(allocationRetentionPriority.getExtensionContainer());
        ;
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(allocationRetentionPriority.getExtensionContainer()));
        assertNotNull(extensionContainer);
        assertEquals(prim.getQoSClassIdentifier(), QoSClassIdentifier.QCI_1);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(EPSQoSSubscribedImpl.class);
    	                
        MAPExtensionContainerImpl extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        QoSClassIdentifier qoSClassIdentifier = QoSClassIdentifier.QCI_1;
        AllocationRetentionPriorityImpl allocationRetentionPriority = new AllocationRetentionPriorityImpl(1, Boolean.TRUE,
                Boolean.TRUE, extensionContainer);
        EPSQoSSubscribedImpl prim = new EPSQoSSubscribedImpl(qoSClassIdentifier, allocationRetentionPriority,
                extensionContainer);

        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData=this.getData();
        assertTrue(Arrays.equals(encodedData, rawData));
    }

}
