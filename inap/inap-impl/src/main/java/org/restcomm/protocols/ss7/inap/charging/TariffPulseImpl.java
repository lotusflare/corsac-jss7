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

package org.restcomm.protocols.ss7.inap.charging;

import org.restcomm.protocols.ss7.inap.api.charging.TariffPulse;
import org.restcomm.protocols.ss7.inap.api.charging.TariffPulseFormat;
import org.restcomm.protocols.ss7.inap.api.charging.TariffSwitchPulse;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class TariffPulseImpl implements TariffPulse {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true, index=-1, defaultImplementation = TariffPulseFormatImpl.class)
    private TariffPulseFormat currentTariffPulse;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true, index=-1, defaultImplementation = TariffSwitchPulseImpl.class)
    private TariffSwitchPulse tariffSwitchPulse;

    public TariffPulseImpl() {
    }

    public TariffPulseImpl(TariffPulseFormat currentTariffPulse,TariffSwitchPulse tariffSwitchPulse) {
    	this.currentTariffPulse=currentTariffPulse; 
    	this.tariffSwitchPulse=tariffSwitchPulse;
    }

    public TariffPulseFormat getCurrentTariffPulse() {
    	return currentTariffPulse;
    }

    public TariffSwitchPulse getTariffSwitchPulse() {
    	return tariffSwitchPulse;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("TariffPulse [");

        if (this.currentTariffPulse != null) {
            sb.append(", currentTariffPulse=");
            sb.append(currentTariffPulse);
        }

        if (this.tariffSwitchPulse != null) {
            sb.append(", tariffSwitchPulse=");
            sb.append(tariffSwitchPulse);
        }
        
        sb.append("]");

        return sb.toString();
    }
}