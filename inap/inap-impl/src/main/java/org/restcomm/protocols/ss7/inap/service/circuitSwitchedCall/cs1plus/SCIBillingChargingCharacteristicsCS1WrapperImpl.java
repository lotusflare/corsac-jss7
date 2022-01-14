/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus;

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.SCIBillingChargingCharacteristicsCS1;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SCIBillingChargingCharacteristicsCS1WrapperImpl {
	
	@ASNChoise
    private SCIBillingChargingCharacteristicsCS1Impl sciBillingChargingCharacteristics;

    public SCIBillingChargingCharacteristicsCS1WrapperImpl() {
    }

    public SCIBillingChargingCharacteristicsCS1WrapperImpl(SCIBillingChargingCharacteristicsCS1 sciBillingChargingCharacteristics) {
    	if(sciBillingChargingCharacteristics instanceof SCIBillingChargingCharacteristicsCS1Impl)
    		this.sciBillingChargingCharacteristics=(SCIBillingChargingCharacteristicsCS1Impl)sciBillingChargingCharacteristics;
    	else if(sciBillingChargingCharacteristics!=null) {
    		if(sciBillingChargingCharacteristics.getChargingInformation()!=null)
    			this.sciBillingChargingCharacteristics = new SCIBillingChargingCharacteristicsCS1Impl(sciBillingChargingCharacteristics.getChargingInformation());
    		else if(sciBillingChargingCharacteristics.getChargingAnalysisInputData()!=null)
    			this.sciBillingChargingCharacteristics = new SCIBillingChargingCharacteristicsCS1Impl(sciBillingChargingCharacteristics.getChargingAnalysisInputData());
    	}
    }

    public SCIBillingChargingCharacteristicsCS1 getSCIBillingChargingCharacteristics() {
    	return sciBillingChargingCharacteristics;
    }
}