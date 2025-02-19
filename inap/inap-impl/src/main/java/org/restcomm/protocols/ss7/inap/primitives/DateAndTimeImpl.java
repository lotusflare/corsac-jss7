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

package org.restcomm.protocols.ss7.inap.primitives;

import org.restcomm.protocols.ss7.inap.api.primitives.DateAndTime;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 *
 * @author yulian.oifa
 *
 */
public class DateAndTimeImpl extends ASNOctetString implements DateAndTime {
	public DateAndTimeImpl() {
		super("DateAndTime",6,6,false);
    }

	public DateAndTimeImpl(int year, int month, int day, int hour, int minute, int second) {
		super(translate(year, month, day, hour, minute, second),"DateAndTime",6,6,false);
	}
	
    public static ByteBuf translate(int year, int month, int day, int hour, int minute, int second) {
    	ByteBuf data = Unpooled.buffer(6);
        data.writeByte((byte) encodeByte(year % 100));
        data.writeByte((byte) encodeByte(month));
        data.writeByte((byte) encodeByte(day));
        data.writeByte((byte) encodeByte(hour));
        data.writeByte((byte) encodeByte(minute));
        data.writeByte((byte) encodeByte(second));        
        return data;
    }

    public int getYear() {
    	ByteBuf data=getValue();
        if (data == null || data.readableBytes() != 6)
            return 0;

        return (int) decodeByte(data.readByte());
    }

    public int getMonth() {
    	ByteBuf data=getValue();
        if (data == null || data.readableBytes() != 6)
            return 0;

        data.skipBytes(1);
        return decodeByte((int) data.readByte());
    }

    public int getDay() {
    	ByteBuf data=getValue();
        if (data == null || data.readableBytes() != 6)
            return 0;

        data.skipBytes(2);
        return decodeByte((int) data.readByte());
    }

    public int getHour() {
    	ByteBuf data=getValue();
        if (data == null || data.readableBytes() != 6)
            return 0;

        data.skipBytes(3);
        return decodeByte((int) data.readByte());
    }

    public int getMinute() {
    	ByteBuf data=getValue();
        if (data == null || data.readableBytes() != 6)
            return 0;

        data.skipBytes(4);
        return decodeByte((int) data.readByte());
    }

    public int getSecond() {
    	ByteBuf data=getValue();
        if (data == null || data.readableBytes() != 6)
            return 0;

        data.skipBytes(5);
        return decodeByte((int) data.readByte());
    }

    private static int decodeByte(int bt) {
        return (bt & 0x0F) * 10 + ((bt & 0xF0) >> 4);
    }

    private static int encodeByte(int val) {
        return (val / 10) | (val % 10) << 4;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("DateAndTime [");
        if (getValue() != null) {
            sb.append("year=");
            sb.append(this.getYear());
            sb.append(", month=");
            sb.append(this.getMonth());
            sb.append(", day=");
            sb.append(this.getDay());
            sb.append(", hour=");
            sb.append(this.getHour());
            sb.append(", minite=");
            sb.append(this.getMinute());
            sb.append(", second=");
            sb.append(this.getSecond());
        }
        sb.append("]");

        return sb.toString();
    }
}
