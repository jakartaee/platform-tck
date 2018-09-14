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
 * @(#)IOR1ValidationStrategy.java	1.17 03/05/16
 */

package com.sun.ts.tests.interop.csiv2.common.validation;

import com.sun.ts.tests.interop.csiv2.common.parser.*;
import com.sun.ts.lib.util.*;
import java.util.*;

/**
 * Validates That an IOR has the following structure:
 * 
 * <pre>
 *  CompoundSecMechList { 
 *	stateful = FALSE; 
 *	mechanism_list = { 
 *	    CompoundSecMec { 
 *		target_requires = {Integrity, Confidentiality, 
 *		    EstablishTrustInClient}; 
 *		transport_mech = TAG_SSL_SEC_TRANS { 
 *		    target_supports = {Integrity, Confidentiality, 
 *			EstablishTrustInTarget}; 
 *		    target_requires = {Integrity, Confidentiality}; 
 *                  addresses = {
 *                      TransportAddress {
 *                          host_name = x;
 *                          port = y;
 *                      };
 *                  };
 *		}; 
 *		as_context_mech = { 
 *		    target_supports = {EstablishTrustInClient}; 
 *		    target_requires = {EstablishTrustInClient}; 
 *		    client_authentication_mech = GSSUP_OID; 
 *		    target_name = {GSSUP,"default"}; 
 *		    ... 
 *		}; 
 *		sas_context_mech = { 
 *		    target_supports = {}; 
 *		    ... 
 *                  supported_naming_mechanisms = {};
 *                  supported_identity_types = {};
 *		}; 
 *	    }; 
 *	}; 
 *   };
 * </pre>
 *
 * We are doing exact match for following fields: target_requires
 * transport_mech.target_requires transport_mech.addresses
 * as_context_mech.target_supports as_context_mech.target_requires
 * as_context_mech.client_authentication_mech as_context_mech.target_name OID
 * sas_context_mech.target_supports sas_context_mech.supported_naming_mechanisms
 * sas_context_mech.supported_identity_types
 *
 * We are doing subset match for following fields: stateful
 * transport_mech.target_supports
 *
 *
 * @author Mark Roth
 */
public class IOR1ValidationStrategy extends IORValidationStrategy {
  /** Are we expecting a nonzero port? */
  private boolean nonzeroPort;

  /**
   * Creates an IOR validation strategy that compares against IOR.1.
   *
   * @param nonzeroPort
   *          True if we require a non-zero port in the transport mech, or false
   *          if we require a zero port.
   */
  public IOR1ValidationStrategy(boolean nonzeroPort) {
    this.nonzeroPort = nonzeroPort;
  }

  /**
   * Returns the name of this validation strategy (e.g. IOR.1)
   */
  public String getName() {
    return "IOR.1";
  }

  /**
   * Returns true if the given IIOPProfile port is valid.
   */
  public boolean verifyIIOPProfilePort(int port) {
    boolean result;
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
    boolean result;
    result = (targetRequires == (INTEGRITY | CONFIDENTIALITY
        | ESTABLISH_TRUST_IN_CLIENT));
    if (result == false) {
      log.logInfo(
          "TargetRequires : Expected : ( INTEGRITY  CONFIDENTIALITY  ESTABLISH_TRUST_IN_CLIENT ) : received : "
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

    log.logInfo("Validating IORTransportMech...");
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
            "TargetSupports : Expected : ( INTEGRITY  CONFIDENTIALITY  ESTABLISH_TRUST_IN_CLIENT ) : received : "
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
    int targetRequires = asContext.getTargetRequires();
    byte[] authMech = asContext.getClientAuthenticationMech();
    byte[] targetName = asContext.getTargetName();

    result = result && (targetSupports == (ESTABLISH_TRUST_IN_CLIENT));

    if (result == false) {
      log.logInfo(
          "TargetSupports : Expected : ( ESTABLISH_TRUST_IN_CLIENT ) : received : "
              + getTargetOptions(targetSupports));
    }

    result = result && (targetRequires == (ESTABLISH_TRUST_IN_CLIENT));

    if (result == false) {
      log.logInfo(
          "TargetRequires : Expected : ( ESTABLISH_TRUST_IN_CLIENT ) : received : "
              + getTargetOptions(targetRequires));
    }

    // Verify auth mech:
    try {
      long[] authMechComponents = GSSUtils.extractComponentsFromOID(authMech,
          0);
      String authMechString = GSSUtils.formatComponents(authMechComponents);

      boolean verify = authMechString.equals(GSSUtils.GSSUPMechOID);
      if (!verify) {
        log.logMismatch("Auth-mech does not verify: " + "Got " + authMechString
            + "  Expected: " + GSSUtils.GSSUPMechOID);
      }
      result = result && verify;
    } catch (GSSUtils.GSSUtilsException e) {
      TestUtil.printStackTrace(e);
      log.logMismatch("Error verifying auth-mech: " + e);
      result = false;
    }

    // Verify target name:
    try {
      byte[] targetNameOID = GSSUtils.extractOIDFromExportedName(targetName);
      byte[] targetNameName = GSSUtils.extractNameFromExportedName(targetName);

      // We cannot verify the name, but we can verify that the
      // OID is GSSUP.
      long[] targetNameOIDComponents = GSSUtils
          .extractComponentsFromOID(targetNameOID, 0);
      String targetNameOIDString = GSSUtils
          .formatComponents(targetNameOIDComponents);
      boolean verify = targetNameOIDString.equals(GSSUtils.GSSUPMechOID);
      if (!verify) {
        log.logMismatch("Target name OID does not verify: " + "Got "
            + targetNameOIDString + "  Expected: " + GSSUtils.GSSUPMechOID);
      }
      result = result && verify;

    } catch (GSSUtils.GSSUtilsException e) {
      TestUtil.printStackTrace(e);
      log.logMismatch("Error parsing target-name: " + e);
      result = false;
    }

    log.logInfo("ASContext " + (result ? "Valid." : "Invalid."));

    return result;
  }

  /**
   * Returns true if the given SAS Context is valid.
   */
  public boolean verifySASContext(IORSASContextEntry sasContext) {
    boolean result = true;

    log.logInfo("Validating SASContext...");
    int targetSupports = sasContext.getTargetSupports();
    Vector supportedNamingMechanisms = sasContext
        .getSupportedNamingMechanisms();
    int supportedIdentityTypes = sasContext.getSupportedIdentityTypes();

    result = result && (targetSupports == 0);
    if (result == false) {
      log.logInfo("TargetSupports : Expected : None : received : "
          + getTargetOptions(targetSupports));
    }

    result = result && (supportedNamingMechanisms.size() == 0);
    if (result == false) {
      log.logInfo("SupportedNamingMechanisms : Expected : 0 : received : "
          + supportedNamingMechanisms.size());
    }

    result = result && (supportedIdentityTypes == 0);
    if (result == false) {
      log.logInfo("SupportedIdentityTypes : Expected :  : ITTAbsent : "
          + getSupportedIdentityTypes(supportedIdentityTypes));
    }

    log.logInfo("SASContext " + (result ? "Valid." : "Invalid."));

    return result;

  }
}
