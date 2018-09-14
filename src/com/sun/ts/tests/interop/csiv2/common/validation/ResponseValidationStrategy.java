/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/*
 * $Id$
 */

package com.sun.ts.tests.interop.csiv2.common.validation;

import com.sun.ts.tests.interop.csiv2.common.parser.*;
import com.sun.ts.lib.util.*;
import java.util.*;

/**
 * Invocation reply validation strategy. Assumes that the client interceptor is
 * involved on the last invocation. This strategy verifies the following fields
 * for a given invocation reply:
 * <p>
 * <ul>
 * <li>Whether the reply was a CompleteEstablishContext, or a ContextError</li>
 * </ul>
 *
 * @author Mark Roth
 */
public class ResponseValidationStrategy {

  /** Log for organizing output */
  private OutputLog log = new OutputLog();

  /** True if we are expecting a reply service context, false if not */
  private boolean expectingServiceContext;

  /** True if we are expecting a ContextError reply, false if not */
  private boolean errorExpected;

  /**
   * Creates a new invocation reply validation strategy
   *
   * @param expectingServiceContext
   *          True if we are expecting a reply service context, or false if not.
   *          If false, the errorExpected parameter is ignored.
   * @param errorExpected
   *          True if we are expecting a ContextError on the reply, or false if
   *          not.
   */
  public ResponseValidationStrategy(boolean expectingServiceContext,
      boolean errorExpected) {
    this.expectingServiceContext = expectingServiceContext;
    this.errorExpected = errorExpected;
  }

  /**
   * Returns true if the associated client node is valid, or false if not.
   *
   * @param entry
   *          The ClientEntry of the response we are validating.
   */
  public boolean validate(ClientEntry entry) throws ValidationException {
    boolean result = true;
    boolean valid;

    TestUtil.logTrace("Validating the following invocation:\n"
        + "-------------------------------------------\n" + entry.toString()
        + "-------------------------------------------");

    // Validate presence / absence of reply service context:
    valid = verifyServiceContextPresence(entry);
    result = result && valid;
    if (!valid) {
      if (expectingServiceContext) {
        log.logMismatch("Expecting service context.");
      } else {
        log.logMismatch("Not expecting service context.");
      }
    }

    if (expectingServiceContext) {
      // Validate the port:
      valid = verifyResponseMessageType(entry);
      result = result && valid;
      if (!valid) {
        log.logMismatch("Mismatched response message type.");
      }
    } else {
      log.logInfo("Not expecting service context - skipping "
          + "response message type validation.");
    }

    return result;
  }

  private boolean verifyServiceContextPresence(ClientEntry entry)
      throws ValidationException {
    boolean result = true;

    ReplyServiceContextEntry replyServiceContext = findReplyServiceContextEntry(
        entry);
    boolean present = (replyServiceContext != null)
        && replyServiceContext.isPresent();

    if (expectingServiceContext) {
      result = present;
    } else {
      result = !present;
    }

    return result;
  }

  private boolean verifyResponseMessageType(ClientEntry entry)
      throws ValidationException {
    boolean result = true;

    ReplyServiceContextEntry replyServiceContext = findReplyServiceContextEntry(
        entry);

    if ((replyServiceContext == null) || !replyServiceContext.isPresent()) {
      log.logMismatch("Error: Reply service context expected "
          + "to be present for reply.");
      result = false;
    } else {
      if (errorExpected) {
        result = replyServiceContext.getContextError() != null;
        if (!result) {
          log.logMismatch("Error: Expecting ContextError message");
        }
      } else {
        CompleteEstablishContextEntry completeEstablishContext = replyServiceContext
            .getCompleteEstablishContext();
        result = completeEstablishContext != null;
        if (!result) {
          log.logMismatch(
              "Error: Expecting " + "CompleteEstablishContext message");
        }
      }
    }

    return result;
  }

  private ReplyServiceContextEntry findReplyServiceContextEntry(
      ClientEntry entry) throws ValidationException {
    ReplyServiceContextEntry result = null;

    ClientInterceptorEntry clientInterceptor = entry.getClientInterceptor();

    if (clientInterceptor == null) {
      throw new ValidationException(
          "Error: Expecting client-interceptor element.");
    }

    while ((clientInterceptor != null)
        && clientInterceptor.isLocationForward()) {
      clientInterceptor = clientInterceptor.getClientInterceptor();
    }

    if (clientInterceptor == null) {
      throw new ValidationException(
          "Error: Could not find client-interceptor element with "
              + "no locationForward.");
    }

    return clientInterceptor.getReplyServiceContext();
  }

}
