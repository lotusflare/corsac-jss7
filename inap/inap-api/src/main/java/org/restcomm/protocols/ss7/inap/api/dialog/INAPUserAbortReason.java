/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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
package org.restcomm.protocols.ss7.inap.api.dialog;
/**
 * INAP-U-ABORT-REASON
 *
 * INAP-U-ABORT-REASON ::= ENUMERATED { no-reason-given (1), application-timer-expired (2), not-allowed-procedures (3),
 * abnormal-processing (4), congestion (5), invalid-reference (6), missing-reference (7), overlapping-dialogue (8) } --
 * application-timer-expired shall be set when application timer (e.g. Tssf) is expired. -- not-allowed-procedures shall be set
 * when received signal is not allowed in INAP -- procedures. -- For example, when a class 4 operation is received from the --
 * gsmSCF and the operation is not allowed in gsmSSF FSM. -- (gsmSSF FSM cannot continue state transition). (e.g. ReleaseCall --
 * operation received in Waiting for End of Temporary Connection -- state.) -- abnormal-processing shall be set when abnormal
 * procedures occur at entity action. -- congestion shall be set when requested resource is unavailable due to -- congestion at
 * TC user (INAP) level. -- invalid-reference shall be set if the received destinationReference is unknown  
 * -- missing-reference shall be set when the destinationReference or the
 * -- originationReference is absent in the received message but is -- required to be present according to the procedures in --
 * subclause 14.1.7. -- overlapping-dialogue shall be used by
 * the gprsSSF to indicate to the gsmSCF that a -- specific instance already has a TC dialogue open. This error -- cause is
 * typically obtained when both the gsmSCF and gprsSSF -- open a new dialogue at the same time. -- no-reason-given shall be set
 * when any other reasons above do not apply END - of INAP-U-ABORT-Data
 *
 * @author yulian.oifa
 *
 */
public enum INAPUserAbortReason {
   no_reason_given(1),
   application_timer_expired(2),
   not_allowed_procedures(3),
   abnormal_processing(4),
   congestion(5),
   invalid_reference(6),
   missing_reference(7),
   overlapping_dialogue(8);

   private int code;

   private INAPUserAbortReason(int code) {
      this.code = code;
   }

   public static INAPUserAbortReason getInstance(int code) {
      switch(code) {
      case 1:
         return no_reason_given;
      case 2:
         return application_timer_expired;
      case 3:
         return not_allowed_procedures;
      case 4:
         return abnormal_processing;
      case 5:
         return congestion;
      case 6:
         return invalid_reference;
      case 7:
         return missing_reference;
      case 8:
         return overlapping_dialogue;
      default:
         return no_reason_given;
      }
   }

   public int getCode() {
      return this.code;
   }
}
