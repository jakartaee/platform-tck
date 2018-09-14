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
 * @(#)IORValidationStrategy.java	1.14 03/05/16
 */

package com.sun.ts.tests.interop.csiv2.common.validation;

import com.sun.ts.tests.interop.csiv2.common.parser.*;

import com.sun.corba.ee.org.omg.CSIIOP.*;
//import com.sun.corba.ee.org.omg.CSI.*;
import com.sun.ts.lib.util.*;
import java.util.*;

/**
 * Base class for IOR validation strategies.
 *
 * @author Mark Roth
 */
abstract public class IORValidationStrategy {

  /** For organizing test output */
  protected OutputLog log = new OutputLog();

  /**
   * Constants that were originally in com.sun.corba.ee.org.omg.CSIIOP. We
   * cannot import these since they are RI-specific. When they move to
   * org.omg.CSIIOP and all vendors are required to have this package, we can
   * then eliminate this dependency.
   */
  protected static final int NO_PROTECTION = 1;

  protected static final int INTEGRITY = 2;

  protected static final int CONFIDENTIALITY = 4;

  protected static final int DETECT_REPLAY = 8;

  protected static final int DETECT_MISORDERING = 16;

  protected static final int ESTABLISH_TRUST_IN_TARGET = 32;

  protected static final int ESTABLISH_TRUST_IN_CLIENT = 64;

  protected static final int NO_DELEGATION = 128;

  protected static final int SIMPLE_DELEGATION = 256;

  protected static final int COMPOSITE_DELEGATION = 512;

  protected static final int IDENTITY_ASSERTION = 1024;

  protected static final int DELEGATION_BY_CLIENT = 2048;

  public static final int ITTAbsent = 0;

  public static final int ITTAnonymous = 1;

  public static final int ITTPrincipalName = 2;

  public static final int ITTX509CertChain = 4;

  public static final int ITTDistinguishedName = 8;

  private static final String printLine = "-------------------------------------------";

  private static final Hashtable<Integer, String> assocOptions;

  static {
    assocOptions = new Hashtable<Integer, String>();
    assocOptions.put(Integer.valueOf(NO_PROTECTION), "NO_PROTECTION");
    assocOptions.put(Integer.valueOf(INTEGRITY), "INTEGRITY");
    assocOptions.put(Integer.valueOf(CONFIDENTIALITY), "CONFIDENTIALITY");
    assocOptions.put(Integer.valueOf(ESTABLISH_TRUST_IN_TARGET),
        "ESTABLISH_TRUST_IN_TARGET");
    assocOptions.put(Integer.valueOf(ESTABLISH_TRUST_IN_CLIENT),
        "ESTABLISH_TRUST_IN_CLIENT");
    assocOptions.put(Integer.valueOf(IDENTITY_ASSERTION), "IDENTITY_ASSERTION");
    assocOptions.put(Integer.valueOf(DELEGATION_BY_CLIENT),
        "DELEGATION_BY_CLIENT");
  }

  private static final Hashtable<Integer, String> identityTokenTypes;

  static {
    identityTokenTypes = new Hashtable<Integer, String>();
    identityTokenTypes.put(Integer.valueOf(ITTAbsent), "Absent");
    identityTokenTypes.put(Integer.valueOf(ITTAnonymous), "Anonymous");
    identityTokenTypes.put(Integer.valueOf(ITTPrincipalName), "PrincipalName");
    identityTokenTypes.put(Integer.valueOf(ITTX509CertChain), "X509CertChain");
    identityTokenTypes.put(Integer.valueOf(ITTDistinguishedName),
        "DistinguishedName");
  }

  protected String getTargetOptions(int requiresOrSupports) {

    StringBuffer strbuf = new StringBuffer();
    Enumeration<Integer> keys = assocOptions.keys();
    while (keys.hasMoreElements()) {
      Integer j = keys.nextElement();
      if (isSet(requiresOrSupports, j.intValue())) {
        strbuf.append(assocOptions.get(j) + " ");
      }
    }

    return strbuf.toString();
  }

  protected String getSupportedIdentityTypes(int identityTokenType) {
    StringBuffer strbuf = new StringBuffer();

    long map = identityTokenType;
    Enumeration<Integer> keys = identityTokenTypes.keys();
    while (keys.hasMoreElements()) {
      Integer j = keys.nextElement();
      if (isSet(identityTokenType, j.intValue())) {
        strbuf.append(identityTokenTypes.get(j) + " ");
        map = map - j.intValue();
      }
    }
    if (map > 0) {
      strbuf.append("custom bits set: " + map);
    }

    return strbuf.toString();
  }

  /**
   * Returns true if the associated IOR is valid, or false if not.
   *
   * @param ior
   *          The IOREntry of the IOR we are validating.
   */
  public boolean validate(IOREntry ior) throws ValidationException {
    boolean result = true;
    boolean valid;

    log.logInfo("Validating received IOR against " + getName());
    TestUtil.logTrace(ior.toString() + printLine);

    // Validate the port:
    valid = verifyIIOPProfilePort(ior.getPort());
    result = result && valid;
    if (!valid) {
      log.logMismatch("Mismatched IIOPProfile port.");
    }

    // Do not validate whether stateful=false.

    // Validate that at least one compound sec mech is correct:
    Vector compoundSecMechs = ior.getCompoundSecMechs();
    boolean atLeastOneCorrect = false;
    for (int i = 0; i < compoundSecMechs.size(); i++) {
      log.logInfo("Testing CompoundSecMech " + (i + 1) + " of "
          + compoundSecMechs.size() + "...");
      CompoundSecMechEntry mech = (CompoundSecMechEntry) compoundSecMechs
          .elementAt(i);
      valid = isValid(mech);
      atLeastOneCorrect = atLeastOneCorrect || valid;
      if (valid) {
        log.logMatch("This CompoundSecMech matches " + getName());
      } else {
        log.logMismatch("This CompoundSecMech does not match " + getName());
      }
    }
    if (atLeastOneCorrect) {
      log.logMatch("At least one compound sec mech matched " + getName() + ".");
    } else {
      log.logMismatch(
          "None of the compound sec mechs matched " + getName() + ".");
    }
    result = result && atLeastOneCorrect;

    return result;
  }

  /**
   * Returns true if the given CompoundSecMech is valid
   */
  private boolean isValid(CompoundSecMechEntry mech)
      throws ValidationException {
    boolean result = true;
    boolean valid;

    // Verify target requires:
    valid = verifyTargetRequires(mech.getTargetRequires());
    if (!valid) {
      log.logMismatch("Mismatch on target requires.");
    }
    result = result && valid;

    // Verify transport mech:
    IORTransportMechEntry iorTransportMech = mech.getIorTransportMech();
    if (iorTransportMech == null) {
      throw new ValidationException("No IOR Transport mech found.");
    } else {
      valid = verifyIORTransportMech(iorTransportMech);
      if (!valid) {
        log.logMismatch("Mismatch on IOR Transport Mech");
      }
    }
    result = result && valid;

    // Verify AS Context mech:
    IORASContextEntry iorASContext = mech.getIorASContext();
    if (iorASContext == null) {
      throw new ValidationException("No IOR ASContext found.");
    } else {
      valid = verifyASContext(iorASContext);
      if (!valid) {
        log.logMismatch("Mismatch on AS Context");
      }
    }
    result = result && valid;

    // Verify SAS Context mech:
    IORSASContextEntry iorSASContext = mech.getIorSASContext();
    if (iorSASContext == null) {
      throw new ValidationException("No IOR SASContext found.");
    } else {
      valid = verifySASContext(mech.getIorSASContext());
      if (!valid) {
        log.logMismatch("Mismatch on SAS Context");
      }
    }
    result = result && valid;

    return result;
  }

  /**
   * Validate that at least one transport address is valid
   *
   * @param addrs
   *          The Vector of TransportAddressEntry of TransportAddress we are
   *          validating.
   */
  public boolean atLeastOneValidAddr(Vector transAddrs)
      throws ValidationException {
    boolean atLeastOneValid = false;
    boolean valid;
    for (int i = 0; i < transAddrs.size(); i++) {
      log.logInfo("Testing Transport Address " + (i + 1) + " of "
          + transAddrs.size() + "...");
      TransportAddressEntry addr = (TransportAddressEntry) transAddrs
          .elementAt(i);
      valid = isValidAddr(addr);
      atLeastOneValid = atLeastOneValid || valid;
      if (valid) {
        log.logMatch("This transport address matches " + getName());
      } else {
        log.logMismatch("This transport address does not match " + getName());
      }
    }
    if (atLeastOneValid) {
      log.logMatch("At least one transport address matched " + getName() + ".");
    } else {
      log.logMismatch(
          "None of the transport address  matched " + getName() + ".");
    }
    return atLeastOneValid;
  }

  /**
   * Validate the transport address is valid
   */
  private boolean isValidAddr(TransportAddressEntry addr)
      throws ValidationException {
    boolean result = true;

    // verify hostname not empty
    String hostname = addr.getHostname();
    if (hostname == null || hostname.trim().length() == 0) {
      result = false;
      log.logMismatch("Mismatch on hostname.");
    }

    // verify port non-zero
    int port = addr.getPort();
    if (port == 0) {
      result = false;
      log.logMismatch("Mismatch on port.");
    }

    return result;
  }

  /**
   * Validate that at least one naming mechanisms is valid
   *
   * @param addrs
   *          The Vector of SupportedNamingMechanisms we are validating.
   */
  public boolean atLeastOneValidNamingMech(Vector mechs)
      throws ValidationException {
    boolean atLeastOneValid = false;
    boolean valid;
    for (int i = 0; i < mechs.size(); i++) {
      log.logInfo("Testing Naming Mechanisms " + (i + 1) + " of " + mechs.size()
          + "...");
      byte[] mech = (byte[]) mechs.elementAt(i);
      valid = isValidNamingMech(mech);
      atLeastOneValid = atLeastOneValid || valid;
      if (valid) {
        log.logMatch("This naming mechanism matches " + getName());
      } else {
        log.logMismatch("This naming mechanisms does not match " + getName());
      }
    }
    if (atLeastOneValid) {
      log.logMatch("At least one naming mechanism matched " + getName() + ".");
    } else {
      log.logMismatch(
          "None of the naming mechanisms  matched " + getName() + ".");
    }
    return atLeastOneValid;
  }

  /**
   * Validate the naming mechanism is valid
   */
  private boolean isValidNamingMech(byte[] namingMech)
      throws ValidationException {
    boolean result = true;

    // Verify naming mech:
    try {
      long[] namingMechComponents = GSSUtils
          .extractComponentsFromOID(namingMech, 0);
      String namingMechString = GSSUtils.formatComponents(namingMechComponents);
      boolean verify = namingMechString.equals(GSSUtils.GSSUPMechOID);
      if (!verify) {
        log.logMismatch("Naming-mech does not verify: " + "Got "
            + namingMechString + "  Expected: " + GSSUtils.GSSUPMechOID);
      }
      result = result && verify;
    } catch (GSSUtils.GSSUtilsException e) {
      TestUtil.printStackTrace(e);
      log.logMismatch("Error verifying auth-mech: " + e);
      result = false;
    }

    return result;
  }

  private static boolean isSet(int val1, int val2) {
    if ((val1 & val2) == val2) {
      return true;
    }
    return false;
  }

  /**
   * Returns the name of this validation strategy (e.g. IOR.0)
   */
  public abstract String getName();

  /**
   * Returns true if the given IIOPProfile port is valid.
   */
  public abstract boolean verifyIIOPProfilePort(int port)
      throws ValidationException;

  /**
   * Returns true if the given targetRequires is valid.
   */
  public abstract boolean verifyTargetRequires(int targetRequires)
      throws ValidationException;

  /**
   * Returns true if the given IOR Transport Mech is valid.
   */
  public abstract boolean verifyIORTransportMech(
      IORTransportMechEntry iorTransportMech) throws ValidationException;

  /**
   * Returns true if the given AS Context is valid.
   */
  public abstract boolean verifyASContext(IORASContextEntry asContext)
      throws ValidationException;

  /**
   * Returns true if the given SAS Context is valid.
   */
  public abstract boolean verifySASContext(IORSASContextEntry sasContext)
      throws ValidationException;
}
