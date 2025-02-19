package org.restcomm.protocols.ss7.commonapp.primitives;

import org.restcomm.protocols.ss7.commonapp.api.primitives.CriticalityType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNCriticalityType extends ASNEnumerated {
	public ASNCriticalityType() {
		super("CriticalityType",0,1,false);
	}
	
	public ASNCriticalityType(CriticalityType t) {
		super(t.getCode(),"CriticalityType",0,1,false);
	}
	
	public CriticalityType getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return CriticalityType.getInstance(realValue);
	}
}
