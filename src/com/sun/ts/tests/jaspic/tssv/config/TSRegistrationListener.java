/*
 * Copyright (c) 2010, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaspic.tssv.config;

import javax.security.auth.message.config.RegistrationListener;
import javax.security.auth.message.config.AuthConfigFactory;

public class TSRegistrationListener implements RegistrationListener {

  String profileLayer;

  String appContext;

  boolean notified = false;

  AuthConfigFactory acf = null;

  public TSRegistrationListener() {
    this.profileLayer = null;
    this.appContext = null;
    acf = AuthConfigFactory.getFactory();
  }

  public TSRegistrationListener(String profileLayer, String appContext) {
    this.profileLayer = profileLayer;
    this.appContext = appContext;
    acf = AuthConfigFactory.getFactory();
  }

  public String getProfileLayer() {
    return this.profileLayer;
  }

  public void setProfileLayer(String val) {
    this.profileLayer = val;
  }

  public String getAppContext() {
    return this.appContext;
  }

  public void setAppContext(String val) {
    this.appContext = val;
  }

  public void resetNotifyFlag() {
    notified = false;
  }

  public boolean notified() {
    return notified;
  }

  /*
   * check if notifications should occur by verifying if our actual notification
   * (ie wasNotified) matches with our expected notification (ie notified())
   * return true on success and false otherwise.
   */
  public boolean check(String layer, String context) {
    boolean match = false;
    boolean rval = true;

    if ((layer == null
        || ((profileLayer != null) && profileLayer.equals(layer)))
        && (context == null
            || ((appContext != null) && appContext.equals(context)))) {
      match = true;
    }

    String msg = "TSRegistrationListener:  layer=" + profileLayer
        + ",  appcontext=";
    msg += appContext + " ExpectedVal=" + notified() + "  actualVal=" + match
        + "for layer=" + layer + " context=" + context;

    if (match && notified()) {
      // should have been notification - which matches whats in notified()
      rval = true;
    } else if (match && !notified()) {
      rval = false;
      debug("in check():  " + msg);
    }

    return rval;
  }

  /*
   * Notify the listener that a registration with which it was associated was
   * replaced or unregistered.
   */
  @Override
  public void notify(String layer, String context) {
    notified = true;
    boolean bLayersMatch = (profileLayer == layer)
        || profileLayer.equals(layer);
    boolean bContextsMatch = (appContext == context)
        || appContext.equals(context);

    if (bLayersMatch && bContextsMatch) {
      // successful notification
      debug(
          "successful notification for layer=" + layer + " context=" + context);
    } else {
      // error - notify had problem
      String msg = "ERROR - listener notified at wrong profileLayer: " + layer
          + " or context: " + context;
      debug(msg);
    }
  }

  private void debug(String out) {
    System.out.println(out);
  }
}
