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

package org.restcomm.protocols.ss7.commonapp.primitives;

import org.restcomm.protocols.ss7.commonapp.api.primitives.Burst;
import org.restcomm.protocols.ss7.commonapp.api.primitives.BurstList;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
*
* @author sergey vetyutnev
*
*/
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class BurstListImpl implements BurstList {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNInteger warningPeriod;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1, defaultImplementation = BurstImpl.class)
    private Burst bursts;

    public BurstListImpl() {
    }

    public BurstListImpl(Integer warningPeriod, Burst burst) {
    	if(warningPeriod!=null)
    		this.warningPeriod = new ASNInteger(warningPeriod,"WarningPeriod",1,1200,false);
    		
        this.bursts = burst;
    }

    public Integer getWarningPeriod() {
    	if(warningPeriod==null)
    		return 30;
    	
        return warningPeriod.getIntValue();
    }

    public Burst getBursts() {
        return bursts;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("BurstList [");
        if (this.warningPeriod != null) {
            sb.append("warningPeriod=");
            sb.append(warningPeriod.getValue());
            sb.append(", ");
        }
        if (this.bursts != null) {
            sb.append("bursts=");
            sb.append(bursts);
            sb.append(", ");
        }
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(bursts==null)
			throw new ASNParsingComponentException("bursts should be set for bursts list", ASNParsingComponentExceptionReason.MistypedParameter);				
	}
}
