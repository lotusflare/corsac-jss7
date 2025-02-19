package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.IntraCUGOptions;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNIntraCUGOptions extends ASNEnumerated {
	public ASNIntraCUGOptions() {
		super("IntraCUGOptions",0,2,false);
	}
	
	public ASNIntraCUGOptions(IntraCUGOptions t) {
		super(t.getCode(),"IntraCUGOptions",0,2,false);
	}
	
	public IntraCUGOptions getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return IntraCUGOptions.getInstance(realValue);
	}
}