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

package org.restcomm.protocols.ss7.tcap.asn;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import org.restcomm.protocols.ss7.tcap.TCAPTestUtils;
import org.restcomm.protocols.ss7.tcap.asn.comp.ASNReturnResultParameterImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.BaseComponent;
import org.restcomm.protocols.ss7.tcap.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCodeType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultLast;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogAbortAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogRequestAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogResponseAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.TCEndMessageImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

@Test(groups = { "asn" })
public class TcEndTest {

	ASNParser parser=new ASNParser();
	
	@BeforeClass
	public void setUp()
	{
		parser.loadClass(TCEndMessageImpl.class);
        
		parser.clearClassMapping(ASNReturnResultParameterImpl.class);
		parser.registerAlternativeClassMapping(ASNReturnResultParameterImpl.class, TCEndTestASN.class);
		
		parser.clearClassMapping(ASNDialogPortionObjectImpl.class);
    	parser.registerAlternativeClassMapping(ASNDialogPortionObjectImpl.class, DialogRequestAPDUImpl.class);
    	parser.registerAlternativeClassMapping(ASNDialogPortionObjectImpl.class, DialogResponseAPDUImpl.class);
    	parser.registerAlternativeClassMapping(ASNDialogPortionObjectImpl.class, DialogAbortAPDUImpl.class);
	}
	
    @Test(groups = { "functional.encode", "functional.decode" })
    public void testTCEndMessage_No_Dialog() throws ASNException {
    	// no idea how to check rest...?

        // created by hand
        byte[] b = new byte[] {
                // TCEnd
                0x64, 65,
                // dialog portion
                // empty
                // dtx
                // DestTran ID (full)............ 144965633
                0x49, 4, 8, (byte) 0xA4, 0, 1,
                // comp portion
                0x6C, 57,
                // invoke
                (byte) 0xA1, 6,
                // invoke ID
                0x02, 0x01, 0x01,
                // op code
                0x02, 0x01, 0x37,
                // return result last
                (byte) 0xA2, 47,
                // inoke id
                0x02, 0x01, 0x02,
                // sequence start
                0x30, 42,
                // local operation
                0x02, 0x02, 0x00, (byte) 0xFF,
                // parameter
                0x30, 36, (byte) 0xA0,// some tag.1
                17, (byte) 0x80,// some tag.1.1
                2, 0x11, 0x11, (byte) 0xA1,// some tag.1.2
                04, (byte) 0x82, // some tag.1.3 ?
                2, 0x00, 0x00, (byte) 0x82,
                // some tag.1.4
                1, 12, (byte) 0x83, // some tag.1.5
                2, 0x33, 0x33, (byte) 0xA1,// some trash here
                // tension indicator 2........ ???
                // use value.................. ???
                // some tag.2
                3, (byte) 0x80,// some tag.2.1
                1, -1, (byte) 0xA2, // some tag.3
                3, (byte) 0x80,// some tag.3.1
                1, -1, (byte) 0xA3,// some tag.4
                5, (byte) 0x82,// some tag.4.1
                3, (byte) 0xAB,// - 85 serviceKey................... 123456 // dont care about this content, lets just make len
                               // correct
                (byte) 0xCD, (byte) 0xEF };

        Object output=parser.decode(Unpooled.wrappedBuffer(b)).getResult();
        assertTrue(output instanceof TCEndMessageImpl, "Expected TCEnd");
        TCEndMessageImpl tcm = (TCEndMessageImpl)output;

        assertNull(tcm.getDialogPortion(), "Dialog portion should not be present");
        // assertEquals(144965633L, tcm.getDestinationTransactionId(),"Destination transaction id does not match");
        assertTrue(InvokeTest.byteBufEquals(tcm.getDestinationTransactionId(), Unpooled.wrappedBuffer(new byte[] { 8, (byte) 0xA4, 0, 1, })),
                "Destination transaction id does not match");

        // comp portion
        assertNotNull(tcm.getComponents(), "Component portion should be present");
        assertEquals(2, tcm.getComponents().size(), "Component count is wrong");
        BaseComponent c = tcm.getComponents().get(0);
        assertTrue(c instanceof Invoke, "Wrong component type");
        Invoke i = (Invoke)c;
        assertEquals(new Integer(1), i.getInvokeId(), "Wrong invoke ID");
        assertNull(i.getLinkedId(), "Linked ID is not null");

        c = tcm.getComponents().get(1);
        assertTrue(c instanceof ReturnResultLast, "Wrong component type");
        ReturnResultLast rrl = (ReturnResultLast)c;
        assertEquals(new Integer(2), rrl.getInvokeId(), "Wrong invoke ID");
        assertNotNull(rrl.getOperationCode(), "Operation code should not be null");

        OperationCode ocs = rrl.getOperationCode();

        assertEquals(OperationCodeType.Local, ocs.getOperationType(), "Wrong Operation Code type");
        assertEquals(new Integer(0x00FF), ocs.getLocalOperationCode(), "Wrong Operation Code");

        assertNotNull(rrl.getParameter(), "Parameter should not be null");

        ByteBuf buffer=parser.encode(tcm);
        byte[] encoded = buffer.array();

        TCAPTestUtils.compareArrays(b, encoded);

    }

    @Test(groups = { "functional.encode", "functional.decode" })
    public void testTCEndMessage_No_Component() throws ASNException, ParseException {
    	// created by hand
        byte[] b = new byte[] {
                // TCEnd
                0x64, 50,
                // oidTx
                // OrigTran ID (full)............ 145031169
                0x49, 4, 8, (byte) 0xA5, 0, 1,
                // dialog portion
                0x6B, 42,
                // extrnal tag
                0x28, 40,
                // oid
                0x06, 7, 0, 17, (byte) 134, 5, 1, 1, 1, (byte) 160, // asn

                29, 0x61, // dialog response
                27,
                // protocol version
                (byte) 0x80, // protocol version

                2, 7, (byte) 0x80, (byte) 161,// acn
                9,
                // oid
                6, 7, 4, 0, 1, 1, 1, 3, 0,
                // result
                (byte) 0xA2, 0x03, 0x2, 0x1, (byte) 0x0,
                // result source diagnostic
                (byte) 0xA3, 5, (byte) 0x0A2, // provider
                3, 0x02,// int 2
                0x01, (byte) 0x2
        // no user info?
        };

        Object output=parser.decode(Unpooled.wrappedBuffer(b)).getResult();
        assertTrue(output instanceof TCEndMessageImpl, "Expected TCEnd");
        TCEndMessageImpl tcm = (TCEndMessageImpl)output;

        assertNull(tcm.getComponents(), "Component portion should not be present");
        assertNotNull(tcm.getDialogPortion(), "Dialog portion should not be null");
        // assertEquals(145031169L, tcm.getDestinationTransactionId(),"Destination transaction id does not match");
        assertTrue(InvokeTest.byteBufEquals(tcm.getDestinationTransactionId(), Unpooled.wrappedBuffer(new byte[] { 8, (byte) 0xA5, 0, 1, })),
                "Destination transaction id does not match");

        assertFalse(tcm.getDialogPortion().isUnidirectional(), "Dialog should not be Uni");
        DialogAPDU _dapd = tcm.getDialogPortion().getDialogAPDU();
        assertEquals(DialogAPDUType.Response, _dapd.getType(), "Wrong dialog APDU type!");

        DialogResponseAPDUImpl dapd = (DialogResponseAPDUImpl) _dapd;

        // check nulls first
        assertNull(dapd.getUserInformation(), "UserInformation should not be present");

        // not nulls
        assertNotNull(dapd.getResult(), "Result should not be null");
        Result r = dapd.getResult();
        assertEquals(ResultType.Accepted, r.getResultType(), "Wrong result");

        assertNotNull(dapd.getResultSourceDiagnostic(), "Result Source Diagnostic should not be null");

        ResultSourceDiagnostic rsd = dapd.getResultSourceDiagnostic();
        assertNull(rsd.getDialogServiceUserType(), "User diagnostic should not be present");
        assertEquals(DialogServiceProviderType.NoCommonDialogPortion, rsd.getDialogServiceProviderType(),
                "Wrong provider diagnostic type");

        ByteBuf buffer=parser.encode(tcm);
        byte[] encoded = buffer.array();

        TCAPTestUtils.compareArrays(b, encoded);
    }

    @Test(groups = { "functional.encode", "functional.decode" })
    public void testTCEndMessage_No_Nothing() throws ASNException {
    	// no idea how to check rest...?

        // created by hand
        byte[] b = new byte[] {
                // TCEnd
                0x64, 6,
                // oidTx
                // OrigTran ID (full)............ 145031169
                0x49, 4, 8, (byte) 0xA5, 0, 1,

        };

        Object output=parser.decode(Unpooled.wrappedBuffer(b)).getResult();
        assertTrue(output instanceof TCEndMessageImpl, "Expected TCEnd");
        TCEndMessageImpl tcm = (TCEndMessageImpl)output;

        assertNull(tcm.getDialogPortion(), "Dialog portion should be null");
        assertNull(tcm.getComponents(), "Component portion should not be present");
        // assertEquals(145031169L, tcm.getDestinationTransactionId(),"Destination transaction id does not match");
        assertTrue(InvokeTest.byteBufEquals(tcm.getDestinationTransactionId(), Unpooled.wrappedBuffer(new byte[] { 8, (byte) 0xA5, 0, 1, })),
                "Destination transaction id does not match");

        ByteBuf buffer=parser.encode(tcm);
        byte[] encoded = buffer.array();

        TCAPTestUtils.compareArrays(b, encoded);

    }

    @Test(groups = { "functional.encode", "functional.decode" })
    public void testTCEndMessage_All() throws ASNException, ParseException {
    	// no idea how to check rest...?

        // created by hand
        byte[] b = new byte[] {
                // TCEnd
                0x64, 68 + 41,
                // dtx
                // DestTran ID (full)............ 144965633
                0x49, 4, 8, (byte) 0xA4, 0, 1,
                // dialog portion
                0x6B, 42,
                // extrnal tag
                0x28, 40,
                // oid
                0x06, 7, 0, 17, (byte) 134, 5, 1, 1, 1, (byte) 160, // asn

                29, 0x61, // dialog response
                27,
                // protocol version
                (byte) 0x80, // protocol version

                2, 7, (byte) 0x80, (byte) 161,// acn
                9,
                // oid
                6, 7, 4, 0, 1, 1, 1, 3, 0,
                // result
                (byte) 0xA2, 0x03, 0x2, 0x1, (byte) 0x01,
                // result source diagnostic
                (byte) 0xA3, 5, (byte) 0x0A2, // provider
                3, 0x02,// int 2
                0x01, (byte) 0x00,
                // no user info?
                // comp portion
                0x6C, 57,
                // invoke
                (byte) 0xA1, 6,
                // invoke ID
                0x02, 0x01, 0x01,
                // op code
                0x02, 0x01, 0x37,
                // return result last
                (byte) 0xA2, 47,
                // invoke id
                0x02, 0x01, 0x02,
                // sequence start
                0x30, 42,
                // local operation
                0x02, 0x02, 0x01, (byte) 0xFF,
                // parameter
                0x30, 36, (byte) 0xA0,// some tag.1
                17, (byte) 0x80,// some tag.1.1
                2, 0x11, 0x11, (byte) 0xA1,// some tag.1.2
                04, (byte) 0x82, // some tag.1.3 ?
                2, 0x00, 0x00, (byte) 0x82,
                // some tag.1.4
                1, 12, (byte) 0x83, // some tag.1.5
                2, 0x33, 0x33, (byte) 0xA1,// some trash here
                // tension indicator 2........ ???
                // use value.................. ???
                // some tag.2
                3, (byte) 0x80,// some tag.2.1
                1, -1, (byte) 0xA2, // some tag.3
                3, (byte) 0x80,// some tag.3.1
                1, -1, (byte) 0xA3,// some tag.4
                5, (byte) 0x82,// some tag.4.1
                3, (byte) 0xAB,// - 85 serviceKey................... 123456 // dont care about this content, lets just make len
                               // correct
                (byte) 0xCD, (byte) 0xEF };

        Object output=parser.decode(Unpooled.wrappedBuffer(b)).getResult();
        assertTrue(output instanceof TCEndMessageImpl, "Expected TCEnd");
        TCEndMessageImpl tcm = (TCEndMessageImpl)output;

        // universal
        // assertEquals(144965633L, tcm.getDestinationTransactionId(),"Destination transaction id does not match");
        assertTrue(InvokeTest.byteBufEquals(tcm.getDestinationTransactionId(), Unpooled.wrappedBuffer(new byte[] { 8, (byte) 0xA4, 0, 1, })),
                "Destination transaction id does not match");

        // dialog portion
        assertNotNull(tcm.getDialogPortion(), "Dialog portion should not be null");
        // assertEquals(144965633L, tcm.getDestinationTransactionId(),"Destination transaction id does not match");

        assertFalse(tcm.getDialogPortion().isUnidirectional(), "Dialog should not be Uni");
        DialogAPDU _dapd = tcm.getDialogPortion().getDialogAPDU();
        assertEquals(DialogAPDUType.Response, _dapd.getType(), "Wrong dialog APDU type!");

        DialogResponseAPDUImpl dapd = (DialogResponseAPDUImpl) _dapd;

        // check nulls first
        assertNull(dapd.getUserInformation(), "UserInformation should not be present");

        // not nulls
        assertNotNull(dapd.getResult(), "Result should not be null");
        Result r = dapd.getResult();
        assertEquals(ResultType.RejectedPermanent, r.getResultType(), "Wrong result");

        assertNotNull(dapd.getResultSourceDiagnostic(), "Result Source Diagnostic should not be null");

        ResultSourceDiagnostic rsd = dapd.getResultSourceDiagnostic();
        assertNull(rsd.getDialogServiceUserType(), "User diagnostic should not be present");
        assertEquals(DialogServiceProviderType.Null, rsd.getDialogServiceProviderType(), "Wrong provider diagnostic type");

        // comp portion
        assertNotNull(tcm.getComponents(), "Component portion should be present");
        assertEquals(2, tcm.getComponents().size(), "Component count is wrong");
        BaseComponent c = tcm.getComponents().get(0);
        assertTrue(c instanceof Invoke, "Wrong component type");
        Invoke i = (Invoke)c;
        assertEquals(new Integer(1), i.getInvokeId(), "Wrong invoke ID");
        assertNull(i.getLinkedId(), "Linked ID is not null");

        c = tcm.getComponents().get(1);
        assertTrue(c instanceof ReturnResultLast, "Wrong component type");
        ReturnResultLast rrl = (ReturnResultLast)c;
        assertEquals(new Integer(2), rrl.getInvokeId(), "Wrong invoke ID");
        assertNotNull(rrl.getOperationCode(), "Operation code should not be null");

        OperationCode ocs = rrl.getOperationCode();

        assertEquals(OperationCodeType.Local, ocs.getOperationType(), "Wrong Operation Code type");
        assertEquals(new Integer(511), ocs.getLocalOperationCode(), "Wrong Operation Code");

        assertNotNull(rrl.getParameter(), "Parameter should not be null");

        ByteBuf buffer=parser.encode(tcm);
        byte[] encoded = buffer.array();
        TCAPTestUtils.compareArrays(b, encoded);
    }
}