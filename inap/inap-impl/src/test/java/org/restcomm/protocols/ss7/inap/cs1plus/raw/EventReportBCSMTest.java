package org.restcomm.protocols.ss7.inap.cs1plus.raw;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.EventReportBCSMRequestImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.Unpooled;

public class EventReportBCSMTest 
{
	protected final transient Logger logger=Logger.getLogger(EventReportBCSMTest.class);

	private byte[] message1=new byte[] {0x30,0x18,(byte)0x80,0x01,0x07,(byte)0xa2,0x09,(byte)0xa5,
			0x07,(byte)0xc1,0x01,0x06,(byte)0xc2,0x02,0x00,0x00,(byte)0xa3,0x03,(byte)0x81,0x01,
			0x02,(byte)0xa4,0x03,(byte)0x80,0x01,0x00 };
	
	private byte[] message2=new byte[] {0x30,0x15,(byte)0x80,0x01,0x09,(byte)0xa2,0x06,(byte)0xa7,
			0x04,(byte)0x80,0x02,(byte)0x80,(byte)0x90,(byte)0xa3,0x03,(byte)0x81,0x01,0x01,
			(byte)0xa4,0x03,(byte)0x80,0x01,0x00 };
	
	private byte[] message3=new byte[] {0x30,0x18,(byte)0x80,0x01,0x07,(byte)0xa2,0x09,(byte)0xa5,
			0x07,(byte)0xc1,0x01,0x01,(byte)0xc2,0x02,0x00,0x14,(byte)0xa3,0x03,(byte)0x81,0x01,0x02,
			(byte)0xa4,0x03,(byte)0x80,0x01,0x00 };
	
	private byte[] message4=new byte[] {0x30,0x15,(byte)0x80,0x01,0x09,(byte)0xa2,0x06,(byte)0xa7,
			0x04,(byte)0x80,0x02,(byte)0x80,(byte)0x90,(byte)0xa3,0x03,(byte)0x81,0x01,0x02,
			(byte)0xa4,0x03,(byte)0x80,0x01,0x00 };

	
	
	@BeforeClass
	public static void initTests()
	{
		BasicConfigurator.configure();
	}
	
	@Test(groups = { "functional.decode", "circuitSwitchedCall" })
	public void testDecode() throws Exception {
		ASNParser parser=new ASNParser(false);
		parser.replaceClass(EventReportBCSMRequestImpl.class);
	    	
		byte[] rawData = this.message1;
		ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof EventReportBCSMRequestImpl);
	        
		EventReportBCSMRequestImpl elem = (EventReportBCSMRequestImpl)result.getResult();
		logger.info(elem);	
		
		rawData = this.message2;
		result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof EventReportBCSMRequestImpl);
	        
		elem = (EventReportBCSMRequestImpl)result.getResult();
		logger.info(elem);	
		
		rawData = this.message3;
		result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof EventReportBCSMRequestImpl);
	        
		elem = (EventReportBCSMRequestImpl)result.getResult();
		logger.info(elem);
		
		rawData = this.message4;
		result=parser.decode(Unpooled.wrappedBuffer(rawData));

		assertFalse(result.getHadErrors());
		assertTrue(result.getResult() instanceof EventReportBCSMRequestImpl);
	        
		elem = (EventReportBCSMRequestImpl)result.getResult();
		logger.info(elem);
	}
}