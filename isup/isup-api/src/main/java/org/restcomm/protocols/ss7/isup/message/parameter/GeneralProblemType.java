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

package org.restcomm.protocols.ss7.isup.message.parameter;

import org.restcomm.protocols.ss7.isup.ParameterException;


/**
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public enum GeneralProblemType {

    /**
     * The component type is not recognized as being one of those defined in 3.1. (Invoke, ReturnResult, ReturnResultLast,
     * ReturnError, Reject) This code is generated by the TCAP layer.
     */
    UnrecognizedComponent(0),

    /**
     * The elemental structure of a component does not conform to the structure of that component as defined in 3.1/Q.773. This
     * code is generated by the TCAP layer.
     */
    MistypedComponent(1),

    /**
     * The contents of the component do not conform to the encoding rules defined in 4.1/Q.773. This code is generated by the
     * TCAP layer.
     */
    BadlyStructuredComponent(2);

    private int type = -1;

    GeneralProblemType(int l) {
        this.type = l;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    public static GeneralProblemType getFromInt(int t) throws ParameterException {
        if (t == 0) {
            return UnrecognizedComponent;
        } else if (t == 1) {
            return MistypedComponent;
        } else if (t == 2) {
            return BadlyStructuredComponent;
        }

        throw new ParameterException("Wrong value of type: " + t);
    }
}