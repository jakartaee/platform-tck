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
 * Invocation request validation strategy. Assumes the server interceptor is
 * involved in the invocation. This strategy verifies the following fields for a
 * given invocation:
 * <p>
 * <ul>
 * <li>Type of SAS identity token identity (EstablishContext message)</li>
 * <li>Presence of SAS client authentication principal (EstablishContext
 * message)</li>
 * <li>Presence of tranposrt client principal (SSL)</li>
 * </ul>
 * 
 * @author Mark Roth
 */
public class RequestValidationStrategy {

  /** Log for organizing output messages */
  private OutputLog log = new OutputLog();

  /** True if we are expecting at least one transport client principal */
  private boolean expectingTransportClientPrincipals;

  /** True if we are expecting a request service context */
  private boolean expectingServiceContext;

  /** True if we are expecting an SAS client authentication principal */
  private boolean expectingSASClientAuthenticationPrincipal;

  /** A bitmap of valid SAS Identity token types */
  private int validSASIdentityTokenTypes;

  /**
   * SAS Identity token type constants. Note that these are intentionally
   * different than those defined in the CSIv2 spec. The intent is to allow a
   * bitmask of acceptable values to be passed in.
   */
  public static final int ITTAbsent = 1;

  public static final int ITTAnonymous = 2;

  public static final int ITTPrincipalName = 4;

  public static final int ITTX509CertChain = 8;

  public static final int ITTDistinguishedName = 16;

  // Use this to return a pass status for any token type:
  public static final int ITTAllowAll = ITTAbsent | ITTAnonymous
      | ITTPrincipalName | ITTX509CertChain | ITTDistinguishedName;

  /** English descriptions of token types */
  private String[] tokenTypeDescriptions = { "ITTAbsent", "ITTAnonymous",
      "ITTPrincipalName", "ITTX509CertChain", "ITTDistinguishedName" };

  /**
   * Creates a new invocation request validation strategy
   *
   * @param expectingTransportClientPrincipals
   *          True if we are expecting at least one transport client principal
   *          to be present. False if we are expecting exactly 0.
   * @param expectingServiceContext
   *          True if we are expecting a request service context, or false if we
   *          are not. If not, then the following two parameters are ignored.
   * @param expectingSASClientAuthenticationPrincipal
   *          True if we are expecting a SAS client authentication principal to
   *          be present. False if we are expecting it not to be there.
   * @param validSASIdentityTokenTypes
   *          A logical OR of all valid SAS Identity token types for this case.
   *          If an identity token type appears whose bit is not set in this
   *          parameter, the validation will fail.
   */
  public RequestValidationStrategy(boolean expectingTransportClientPrincipals,
      boolean expectingServiceContext,
      boolean expectingSASClientAuthenticationPrincipal,
      int validSASIdentityTokenTypes) {
    this.expectingTransportClientPrincipals = expectingTransportClientPrincipals;
    this.expectingServiceContext = expectingServiceContext;
    this.expectingSASClientAuthenticationPrincipal = expectingSASClientAuthenticationPrincipal;
    this.validSASIdentityTokenTypes = validSASIdentityTokenTypes;
  }

  /**
   * Returns true if the associated invocation is valid, or false if not.
   *
   * @param entry
   *          The ClientEntry of the invocation we are validating.
   */
  public boolean validate(ClientEntry entry) {
    boolean result = true;
    boolean valid;

    TestUtil.logTrace("Validating the following invocation:\n"
        + "-------------------------------------------\n" + entry.toString()
        + "-------------------------------------------");

    // Validate the transport client principals:
    valid = verifyTransportClientPrincipals(entry);
    result = result && valid;
    if (!valid) {
      log.logMismatch("Mismatched transport client principals.");
    }

    // Validate whether request service context is present.
    valid = verifyServiceContextPresent(entry);
    result = result && valid;
    if (!valid) {
      if (expectingServiceContext) {
        log.logMismatch("Expecting request service context.");
      } else {
        log.logMismatch("Not expecting request service context.");
      }
    }

    // Validate SAS, if expecting service context:
    if (expectingServiceContext) {
      // Validate client authentication principal:
      valid = verifySASClientAuthenticationPrincipal(entry);
      result = result && valid;
      if (!valid) {
        log.logMismatch("Mismatched SAS Client Authentication principal.");
      }

      // Validate SAS identity token type:
      valid = verifyIdentityTokenType(entry);
      result = result && valid;
      if (!valid) {
        log.logMismatch("Mismatched SAS Identity Token Type.");
      }
    } else {
      log.logInfo(
          "Not expecting service context - " + "skipping SAS validation.");
    }

    return result;
  }

  /**
   * Verifies the presence / absence of the transport client principals
   *
   * @param entry
   *          The ClientEntry, expected to contain a ServerInterceptor node
   *          which contains the transport client principals.
   */
  private boolean verifyTransportClientPrincipals(ClientEntry entry) {
    boolean result = true;

    ServerInterceptorEntry serverInterceptor = entry.getServerInterceptor();

    if (serverInterceptor == null) {
      log.logMismatch("Error: Expecting server interceptor element.");
      result = false;
    } else {
      TransportClientPrincipalsEntry transportClientPrincipals = serverInterceptor
          .getTransportClientPrincipals();
      if (transportClientPrincipals == null) {
        log.logMismatch(
            "Error: Expecting tranport client principals log entry.");
        result = false;
      } else {
        Vector principals = transportClientPrincipals.getPrincipals();
        if (expectingTransportClientPrincipals) {
          if (principals.size() == 0) {
            log.logMismatch("Error: Expecting at least one "
                + "transport client principal.");
            result = false;
          } else {
            log.logMatch(
                "Match: At least one transport " + "client principal found");
          }
        } else {
          if (principals.size() != 0) {
            log.logMismatch(
                "Error: Expecting no " + "transport client principals.  Found "
                    + principals.size() + ".");
            result = false;
          } else {
            log.logMatch(
                "Match: Transport client " + "principals absent, as expected.");
          }
        }
      }
    }

    return result;
  }

  private boolean verifyServiceContextPresent(ClientEntry entry) {
    boolean result = true;

    ServerInterceptorEntry serverInterceptor = entry.getServerInterceptor();
    RequestServiceContextEntry requestServiceContext = null;

    if (serverInterceptor == null) {
      log.logMismatch("Error: Expecting server interceptor element.");
    } else {
      requestServiceContext = serverInterceptor.getRequestServiceContext();
      if ((requestServiceContext == null)
          || !requestServiceContext.isPresent()) {
        result = !expectingServiceContext;
      } else {
        result = expectingServiceContext;
      }
    }

    return result;
  }

  private boolean verifySASClientAuthenticationPrincipal(ClientEntry entry) {
    boolean result = true;

    EstablishContextEntry establishContext = findEstablishContext(entry);
    if (establishContext == null) {
      result = false;
    } else {
      byte[] token = establishContext.getClientAuthToken();
      if ((token == null) || (token.length == 0)) {
        if (expectingSASClientAuthenticationPrincipal) {
          log.logMismatch(
              "Error: Expecting SAS Client " + "authentication principal.");
          result = false;
        } else {
          log.logMatch("Match: SAS Client principal absent.");
        }
      } else {
        if (!expectingSASClientAuthenticationPrincipal) {
          log.logMismatch(
              "Error: Not expecting SAS " + "Client authentication principal.");
          result = false;
        } else {
          log.logMatch(
              "Match: SAS Client principal present, " + "as expected.");
        }
      }
    }

    return result;
  }

  private boolean verifyIdentityTokenType(ClientEntry entry) {
    boolean result = true;

    EstablishContextEntry establishContext = findEstablishContext(entry);

    if (establishContext == null) {
      result = false;
    } else {
      IdentityTokenEntry tokenEntry = establishContext.getIdentityToken();
      if (tokenEntry == null) {
        log.logMismatch("Identity token entry not found.");
        result = false;
      } else {
        IdentityToken token = tokenEntry.getIdentityToken();
        if (token == null) {
          log.logMismatch("Unknown identity token.");
          result = false;
        } else {
          int type = 0;
          if (token instanceof ITAbsent) {
            type = ITTAbsent;
          } else if (token instanceof ITAnonymous) {
            type = ITTAnonymous;
          } else if (token instanceof ITPrincipalName) {
            type = ITTPrincipalName;
          } else if (token instanceof ITCertificateChain) {
            type = ITTX509CertChain;
          } else if (token instanceof ITDistinguishedName) {
            type = ITTDistinguishedName;
          }

          // If it doesn't match any of the expected types:
          if ((type & validSASIdentityTokenTypes) == 0) {
            log.logMismatch("Identity Token Type is invalid.  "
                + "Expecting one of the following:");
            log.logInfo(describeTokenTypes(validSASIdentityTokenTypes));
            log.logInfo(" Found:");
            log.logInfo(describeTokenTypes(type));
            result = false;
          } else {
            log.logMatch("Match: Identity Token Type is valid.");
          }
        }
      }
    }

    return result;
  }

  /**
   * Private utility method to return the EstablishContext message from the
   * given ClientEntry.
   */
  private EstablishContextEntry findEstablishContext(ClientEntry entry) {
    boolean okay = true;
    EstablishContextEntry result = null;

    ServerInterceptorEntry serverInterceptor = entry.getServerInterceptor();
    RequestServiceContextEntry requestServiceContext = null;

    if (serverInterceptor == null) {
      log.logMismatch("Error: Expecting server interceptor element.");
      okay = false;
    }

    if (okay) {
      requestServiceContext = serverInterceptor.getRequestServiceContext();
      if ((requestServiceContext == null)
          || !requestServiceContext.isPresent()) {
        log.logMismatch("Error: Request service context not present.");
        okay = false;
      }
    }

    if (okay) {
      result = requestServiceContext.getEstablishContext();

      if (result == null) {
        log.logMismatch("Error: EstablishContext message not found.");
      }
    }

    return result;
  }

  private String describeTokenTypes(int type) {
    String result = "";
    int bit = 1;
    for (int i = 0; i <= tokenTypeDescriptions.length; i++) {
      if ((type & bit) > 0) {
        result += "  * " + tokenTypeDescriptions[i] + "\n";
      }
      bit *= 2;
    }

    if (result.equals("")) {
      result = "  * No token type.";
    }

    return result;
  }
}
