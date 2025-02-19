package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.map.api.errors.CallBarringCause;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNCallBaringCauseImpl extends ASNEnumerated {
	public ASNCallBaringCauseImpl() {
		super("CallBarringCause",0,1,false);
	}
	
	public ASNCallBaringCauseImpl(CallBarringCause t) {
		super(t.getCode(),"CallBarringCause",0,1,false);
	}
	
	public CallBarringCause getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return CallBarringCause.getInstance(realValue);
	}
}
