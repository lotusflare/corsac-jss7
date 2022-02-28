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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AOCBeforeAnswer;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AOCSubsequent;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAMELSCIBillingChargingCharacteristicsAlt;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.SCIBillingChargingCharacteristics;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 4,constructed = false,lengthIndefinite = false)
public class SCIBillingChargingCharacteristicsImpl implements SCIBillingChargingCharacteristics {
	@ASNChoise
    private CamelSCIBillingChargingCharacteristicsImpl camelSCIBillingChargingCharacteristicsImpl;
    
    public SCIBillingChargingCharacteristicsImpl() {
    }

    public SCIBillingChargingCharacteristicsImpl(AOCBeforeAnswer aocBeforeAnswer) {
        this.camelSCIBillingChargingCharacteristicsImpl = new CamelSCIBillingChargingCharacteristicsImpl(aocBeforeAnswer);
    }

    public SCIBillingChargingCharacteristicsImpl(AOCSubsequent aocSubsequent) {
        this.camelSCIBillingChargingCharacteristicsImpl = new CamelSCIBillingChargingCharacteristicsImpl(aocSubsequent);
    }

    public SCIBillingChargingCharacteristicsImpl(CAMELSCIBillingChargingCharacteristicsAlt aocExtension) {
        this.camelSCIBillingChargingCharacteristicsImpl = new CamelSCIBillingChargingCharacteristicsImpl(aocExtension);
    }

    public AOCBeforeAnswer getAOCBeforeAnswer() {
    	if(this.camelSCIBillingChargingCharacteristicsImpl==null)
    		return null;
    	
        return camelSCIBillingChargingCharacteristicsImpl.getAOCBeforeAnswer();
    }

    public AOCSubsequent getAOCSubsequent() {
    	if(this.camelSCIBillingChargingCharacteristicsImpl==null)
    		return null;
    	
        return camelSCIBillingChargingCharacteristicsImpl.getAOCSubsequent();
    }

    public CAMELSCIBillingChargingCharacteristicsAlt getAOCExtension() {
    	if(this.camelSCIBillingChargingCharacteristicsImpl==null)
    		return null;
    	
        return camelSCIBillingChargingCharacteristicsImpl.getAOCExtension();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("SCIBillingChargingCharacteristics [");

        if (this.camelSCIBillingChargingCharacteristicsImpl != null) {
            sb.append("aocBeforeAnswer=");
            sb.append(camelSCIBillingChargingCharacteristicsImpl.toString());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(camelSCIBillingChargingCharacteristicsImpl==null)
			throw new ASNParsingComponentException("camel SCI billing charging characteristics should be set for SCI billing charging characteristics ", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
