package org.restcomm.protocols.ss7.map.service.sms;

import org.restcomm.protocols.ss7.map.api.service.sms.SMDeliveryOutcome;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNSMDeliveryOutcome extends ASNEnumerated {
	public ASNSMDeliveryOutcome() {
		super("SMDeliveryOutcome",0,2,false);
	}
	
	public ASNSMDeliveryOutcome(SMDeliveryOutcome t) {
		super(t.getCode(),"SMDeliveryOutcome",0,2,false);
	}
	
	public SMDeliveryOutcome getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return SMDeliveryOutcome.getInstance(realValue);
	}
}
