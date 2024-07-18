/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * $Id: MemberSubmissionAddressingFeature.java 51109 2006-09-28 15:28:12Z lschwenk $
 */

package com.sun.ts.tests.jaxws.wsa.common;

import jakarta.xml.ws.soap.AddressingFeature;
import jakarta.xml.ws.WebServiceFeature;

public class MemberSubmissionAddressingFeature extends WebServiceFeature {
  /**
   * Constant value identifying the MemberSubmissionAddressingFeature
   */
  public static final String ID = "https://jakarta.ee/xml/ns/jaxws/2004/08/addressing";

  /**
   * Constant ID for the <code>required</code> feature parameter
   */
  public static final String IS_REQUIRED = "ADDRESSING_IS_REQUIRED";

  private boolean required;

  private boolean enabled;

  /**
   * Create an MemberSubmissionAddressingFeature The instance created will be
   * enabled.
   */
  public MemberSubmissionAddressingFeature() {
  }

  /**
   * Create an MemberSubmissionAddressingFeature
   *
   * @param enabled
   *          specifies whether this feature should be enabled or not.
   */
  public MemberSubmissionAddressingFeature(boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * Create an <code>MemberSubmissionAddressingFeature</code>
   *
   * @param enabled
   *          specifies whether this feature should be enabled or not.
   * @param required
   *          specifies the value that will be used for the
   *          <code>required</code> attribute on the
   *          <code>wsaw:UsingAddressing</code> element.
   */
  public MemberSubmissionAddressingFeature(boolean enabled, boolean required) {
    this.enabled = enabled;
    this.required = required;
  }

  public String getID() {
    return ID;
  }

  public boolean isRequired() {
    return required;
  }

  public void setRequired(boolean required) {
    this.required = required;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}
