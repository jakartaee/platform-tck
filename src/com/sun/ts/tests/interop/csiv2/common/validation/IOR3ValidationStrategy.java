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
 * Validates That an IOR has the following structure:
 * 
 * <pre>
 * CompoundSecMechList {
 *     stateful = FALSE;
 *     mechanism_list = {
 *         CompoundSecMec {
 *             target_requires = {Integrity, Confidentiality};
 *             transport_mech = TAG_SSL_SEC_TRANS {
 *                 target_supports = {Integrity, Confidentiality, 
 *                     EstablishTrustInTarget};
 *                 target_requires = {Integrity, Confidentiality};
 *                 addresses = {
 *                      TransportAddress {      
 *                          host_name = x;
 *                          port = y;
 *                      };
 *                  };           
 *             };
 *             as_context_mech = {
 *                 target_supports = {};
 *                 ...
 *             };
 *             sas_context_mech = {
 *                 target_supports = {IdentityAssertion};
 *                 target_requires = {};
 *                 ...
 *                 supported_naming_mechanisms = {GSSMechOID};
 *                 supported_identity_types = {ITTPrincipalName};
 *             };
 *         };
 *     };
 * };
 * </pre>
 *
 * We are doing exact match for following fields: target_requires
 * transport_mech.target_requires transport_mech.addresses
 * as_context_mech.target_supports sas_context_mech.target_requires
 *
 * We are doing subset match for following fields: stateful
 * transport_mech.target_supports sas_context_mech.target_supports
 * sas_context_mech.supported_naming_mechanisms
 * sas_context_mech.supported_identity_types
 *
 *
 * @author Mark Roth
 */
public class IOR3ValidationStrategy extends IORValidationStrategy {
  /** Are we expecting a nonzero port? */
  private boolean nonzeroPort;

  /** What identity token type are we expecting? */
  private int expectedTokenType;

  /**
   * Creates an IOR validation strategy that compares against IOR.1.
   *
   * @param nonzeroPort
   *          True if we require a non-zero port in the transport mech, or false
   *          if we require a zero port.
   * @param expectedTokenType
   *          The identity token type we are expecting.
   */
  public IOR3ValidationStrategy(boolean nonzeroPort, int expectedTokenType) {
    this.nonzeroPort = nonzeroPort;
    this.expectedTokenType = expectedTokenType;
  }

  /**
   * Returns the name of this validation strategy (e.g. IOR.3)
   */
  public String getName() {
    return "IOR.3";
  }

  /**
   * Returns true if the given IIOPProfile port is valid.
   */
  public boolean verifyIIOPProfilePort(int port) {
    boolean result = false;
    result = !((port != 0) ^ nonzeroPort);

    if (result == false) {
      log.logInfo("IIOPProfilePort : Expected : 0 : received : " + port);
    }
    return result;
  }

  /**
   * Returns true if the given targetRequires is valid.
   */
  public boolean verifyTargetRequires(int targetRequires) {
    boolean result = false;
    result = (targetRequires == (INTEGRITY | CONFIDENTIALITY));
    if (result == false) {
      log.logInfo(
          "TargetRequires : Expected : ( INTEGRITY  CONFIDENTIALITY ) : received : "
              + getTargetOptions(targetRequires));
    }

    return result;
  }

  /**
   * Returns true if the given IOR Transport Mech is valid.
   */
  public boolean verifyIORTransportMech(IORTransportMechEntry iorTransportMech)
      throws ValidationException {
    boolean result = true;

    log.logInfo("Validating IORTransportMech ...");
    TLSTransEntry tlsTrans = iorTransportMech.getTlsTrans();
    if (tlsTrans == null) {
      log.logMismatch("Expecting TLS transport.");
      result = false;
    } else {
      int targetSupports = tlsTrans.getTargetSupports();
      int targetRequires = tlsTrans.getTargetRequires();

      result = result && ((targetSupports & (INTEGRITY | CONFIDENTIALITY
          | ESTABLISH_TRUST_IN_TARGET)) == (INTEGRITY | CONFIDENTIALITY
              | ESTABLISH_TRUST_IN_TARGET));
      if (result == false) {
        log.logInfo(
            "TargetSupports : Expected : ( INTEGRITY  CONFIDENTIALITY  ESTABLISH_TRUST_IN_TARGET ) : received : "
                + getTargetOptions(targetSupports));
      }

      result = result && (targetRequires == (INTEGRITY | CONFIDENTIALITY));
      if (result == false) {
        log.logInfo(
            "TargetRequires : Expected : ( INTEGRITY  CONFIDENTIALITY ) : received : "
                + getTargetOptions(targetRequires));
      }

      // Validate that at least one transport address is valid:
      Vector transAddrs = tlsTrans.getTransAddrs();
      result = result && atLeastOneValidAddr(transAddrs);

    }
    log.logInfo("IORTransportMech " + (result ? "Valid." : "Invalid."));
    return result;
  }

  /**
   * Returns true if the given AS Context is valid.
   */
  public boolean verifyASContext(IORASContextEntry asContext) {
    boolean result = true;

    log.logInfo("Validating ASContext...");
    int targetSupports = asContext.getTargetSupports();
    result = result && (targetSupports == 0);
    if (result == false) {
      log.logInfo("TargetSupports : Expected : None : received : "
          + getTargetOptions(targetSupports));
    }

    log.logInfo("ASContext " + (result ? "Valid." : "Invalid."));
    return result;
  }

  /**
   * Returns true if the given SAS Context is valid.
   */
  public boolean verifySASContext(IORSASContextEntry sasContext)
      throws ValidationException {
    boolean result = true;
    boolean valid1, valid2;

    log.logInfo("Validating SASContext...");
    int targetSupports = sasContext.getTargetSupports();
    int targetRequires = sasContext.getTargetRequires();
    Vector supportedNamingMechanisms = sasContext
        .getSupportedNamingMechanisms();
    int supportedIdentityTypes = sasContext.getSupportedIdentityTypes();

    // The value in supported_identity_types shall be non-zero if
    // and only if the IdentityAssertion bit is non-zero in
    // target_supports. The bit corresponding to the ITTPrincipalName
    // identity token type shall be non-zero in supported_identity_types
    // if and only if the value in supported_naming_mechanisms contains
    // at least one element. CSIv2 Spec (01-06-11 OMG Finalized Specification)
    // Section 16.5.1

    result = result
        && ((targetSupports & IDENTITY_ASSERTION) == IDENTITY_ASSERTION)
        && (supportedIdentityTypes != 0);
    if (result == false) {
      log.logInfo(
          "TargetRequires : Expected : ( IDENTITY_ASSERTION ) : received : "
              + getTargetOptions(targetSupports));
    }

    result = result && (targetRequires == 0);
    if (result == false) {
      log.logInfo("TargetRequires : Expected : None : received : "
          + getTargetOptions(targetRequires));
    }

    valid1 = supportedNamingMechanisms.size() > 0
        && ((supportedIdentityTypes & ITTPrincipalName) == ITTPrincipalName);

    valid2 = supportedNamingMechanisms.size() == 0
        && ((supportedIdentityTypes & ITTPrincipalName) == 0);

    result = result && (valid1 ^ valid2);

    result = result
        && ((expectedTokenType & supportedIdentityTypes) == expectedTokenType);

    // verify the naming mechanism to be GSSUPMechOID for user
    // password authentication case
    if (expectedTokenType == ITTPrincipalName) {
      result = result && atLeastOneValidNamingMech(supportedNamingMechanisms);
    }

    log.logInfo("SASContext " + (result ? "Valid." : "Invalid."));
    return result;
  }
}
