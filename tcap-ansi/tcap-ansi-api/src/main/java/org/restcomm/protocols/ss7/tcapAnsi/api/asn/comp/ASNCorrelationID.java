package org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp;

/*
 * Mobius Software LTD
 * Copyright 2019, Mobius Software LTD and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

/**
*
* @author yulian oifa
*
*/

import io.netty.buffer.ByteBuf;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNDecode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNEncode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNLength;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

@ASNTag(asnClass=ASNClass.PRIVATE,tag=15,constructed=false,lengthIndefinite=false)
public class ASNCorrelationID {
	private Byte firstValue;
	private Byte secondValue;
	
	public Byte getFirstValue() {
		return firstValue;
	}

	public Byte getSecondValue() {
		return secondValue;
	}

	public void setFirstValue(Byte value) {
		this.firstValue = value;
	}

	public void setSecondValue(Byte value) {
		this.secondValue = value;
	}

	@ASNLength
	public Integer getLength() {
		if(firstValue==null)
			return 0;
		
		if(secondValue==null)
			return 1;
		
		return 2;
	}
	
	@ASNEncode
	public void encode(ByteBuf buffer) {
		if(firstValue!=null)
			buffer.writeByte(firstValue);
		
		if(secondValue!=null)
			buffer.writeByte(secondValue);				
	}
	
	@ASNDecode
	public Boolean decode(ByteBuf buffer,Boolean skipErrors) {
		if(buffer.readableBytes()>0)
			firstValue=buffer.readByte();
		
		if(buffer.readableBytes()>0)
			secondValue=buffer.readByte();
		
		return false;
	}
}