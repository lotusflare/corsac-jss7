package org.restcomm.protocols.ss7.map.primitives;

import org.restcomm.protocols.ss7.map.api.primitives.ExtProtocolId;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

public class ASNExtProtocolIDImpl extends ASNEnumerated {
	public ASNExtProtocolIDImpl() {
		super("ExtProtocolId",1,1,false);
	}
	
	public ASNExtProtocolIDImpl(ExtProtocolId t) {
		super(t.getCode(),"ExtProtocolId",1,1,false);
	}
	
	public ExtProtocolId getType() {
		Integer realValue=super.getIntValue();
		if(realValue==null)
			return null;
		
		return ExtProtocolId.getExtProtocolId(realValue);
	}
}
