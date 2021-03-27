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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.util.List;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCodeImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author daniel bichara
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ExtCallBarInfoImpl {
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0)
	private SSCodeImpl ssCode = null;	
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=1)
	private ExtCallBarringFeatureListWrapperImpl callBarringFeatureList = null;
    private MAPExtensionContainerImpl extensionContainer = null;

    public ExtCallBarInfoImpl() {
    }

    /**
     *
     */
    public ExtCallBarInfoImpl(SSCodeImpl ssCode, List<ExtCallBarringFeatureImpl> callBarringFeatureList,
            MAPExtensionContainerImpl extensionContainer) {
        this.ssCode = ssCode;
        
        if(callBarringFeatureList!=null)
        	this.callBarringFeatureList = new ExtCallBarringFeatureListWrapperImpl(callBarringFeatureList);
        
        this.extensionContainer = extensionContainer;
    }

    public SSCodeImpl getSsCode() {
        return this.ssCode;
    }

    public List<ExtCallBarringFeatureImpl> getCallBarringFeatureList() {
    	if(this.callBarringFeatureList==null)
    		return null;
    	
        return this.callBarringFeatureList.getExtCallBarringFeature();
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExtCallBarInfo [");

        if (this.ssCode != null) {
            sb.append("ssCode=");
            sb.append(this.ssCode.toString());
            sb.append(", ");
        }

        if (this.callBarringFeatureList != null && this.callBarringFeatureList.getExtCallBarringFeature()!=null) {
            sb.append("callBarringFeatureList=[");
            boolean firstItem = true;
            for (ExtCallBarringFeatureImpl be : this.callBarringFeatureList.getExtCallBarringFeature()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("]");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
