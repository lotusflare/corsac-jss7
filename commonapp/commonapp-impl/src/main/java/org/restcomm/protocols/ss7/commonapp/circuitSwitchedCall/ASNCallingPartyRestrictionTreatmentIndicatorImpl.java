package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CallingPartyRestrictionIndicator;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNCallingPartyRestrictionTreatmentIndicatorImpl extends ASNEnumerated {
	public ASNCallingPartyRestrictionTreatmentIndicatorImpl() {
		super("CallingPartyRestrictionIndicator",1,2,false);
	}
	
	public ASNCallingPartyRestrictionTreatmentIndicatorImpl(CallingPartyRestrictionIndicator t) {
		super(t.getCode(),"CallingPartyRestrictionIndicator",1,2,false);
	}
	
	public CallingPartyRestrictionIndicator getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return CallingPartyRestrictionIndicator.getInstance(realValue);
	}
}
