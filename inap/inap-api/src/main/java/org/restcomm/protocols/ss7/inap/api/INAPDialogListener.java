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
package org.restcomm.protocols.ss7.inap.api;

import org.restcomm.protocols.ss7.inap.api.dialog.INAPGeneralAbortReason;
import org.restcomm.protocols.ss7.inap.api.dialog.INAPNoticeProblemDiagnostic;
import org.restcomm.protocols.ss7.inap.api.dialog.INAPUserAbortReason;
import org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType;
/**
 * @author yulian.oifa
 *
 */
public interface INAPDialogListener {
   /**
   * Called after all components has been processed.
   */
   void onDialogDelimiter(INAPDialog inapDialog);

   /**
   * When TC-BEGIN received. If INAP user rejects this dialog it should call INAPDialog.abort()
   */
   void onDialogRequest(INAPDialog inapDialog);

   /**
   * When TC-CONTINUE or TC-END received with dialogueResponse DialoguePDU (AARE-apdu) (dialog accepted) this is called before
   * ComponentPortion is called
   */
   void onDialogAccept(INAPDialog inapDialog);

   /**
   * When TC-ABORT received with user abort userReason is defined only if generalReason=UserSpecific
   */
   void onDialogUserAbort(INAPDialog inapDialog, INAPGeneralAbortReason generalReason, INAPUserAbortReason userReason);

   /**
   * When TC-ABORT received with provider abort
   *
   */
   void onDialogProviderAbort(INAPDialog inapDialog, PAbortCauseType abortCause);

   /**
   * When TC-END received
   */
   void onDialogClose(INAPDialog inapDialog);

   /**
   * Called when the INAP Dialog has been released
   *
   * @param inapDialog
   */
   void onDialogRelease(INAPDialog inapDialog);

   /**
   * Called when the INAP Dialog is about to aborted because of TimeOut
   *
   * @param inapDialog
   */
   void onDialogTimeout(INAPDialog inapDialog);

   /**
   * Called to notice of abnormal cases
   *
   */
   void onDialogNotice(INAPDialog capDialog, INAPNoticeProblemDiagnostic noticeProblemDiagnostic);
}
