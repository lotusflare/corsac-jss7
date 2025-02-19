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

package org.restcomm.protocols.ss7.tcap.asn.comp;

import org.restcomm.protocols.ss7.tcap.asn.ParseException;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author baranowb
 *
 */
@ASNTag(asnClass=ASNClass.PRIVATE,tag=0x00,constructed=false,lengthIndefinite=false)
public interface Problem {

    // this is a hell of a combo
    ProblemType getType();

    // now depending on type, one of below values must not be null

    // mandatory, one for each type
    void setGeneralProblemType(GeneralProblemType t);

    GeneralProblemType getGeneralProblemType() throws ParseException;

    void setInvokeProblemType(InvokeProblemType t);

    InvokeProblemType getInvokeProblemType() throws ParseException;

    void setReturnErrorProblemType(ReturnErrorProblemType t);

    ReturnErrorProblemType getReturnErrorProblemType() throws ParseException;

    void setReturnResultProblemType(ReturnResultProblemType t);

    ReturnResultProblemType getReturnResultProblemType() throws ParseException;

}