package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.LCSFormatIndicator;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNLCSFormatIndicator extends ASNEnumerated {
	public ASNLCSFormatIndicator() {
		super("LCSFormatIndicator",0,4,false);
	}
	
	public ASNLCSFormatIndicator(LCSFormatIndicator t) {
		super(t.getIndicator(),"LCSFormatIndicator",0,4,false);
	}
	
	public LCSFormatIndicator getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return LCSFormatIndicator.getLCSFormatIndicator(realValue);
	}
}