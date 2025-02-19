package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmTriggerDetectionPoint;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNTBcsmTriggerDetectionPoint extends ASNEnumerated {
	public ASNTBcsmTriggerDetectionPoint() {
		super("TBcsmTriggerDetectionPoint",12,14,false);
	}
	
	public ASNTBcsmTriggerDetectionPoint(TBcsmTriggerDetectionPoint t) {
		super(t.getCode(),"TBcsmTriggerDetectionPoint",12,14,false);
	}
	
	public TBcsmTriggerDetectionPoint getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return TBcsmTriggerDetectionPoint.getInstance(realValue);
	}
}