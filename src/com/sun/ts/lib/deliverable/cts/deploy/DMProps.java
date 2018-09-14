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

package com.sun.ts.lib.deliverable.cts.deploy;

import java.lang.String;
import java.util.Properties;

// Harness imports
import com.sun.ts.lib.util.TestUtil;

public class DMProps {

  private String jarfile;

  private String uri;

  private String uname;

  private String passwd;

  public DMProps(String jarfile, String uri, String uname, String passwd) {
    this.jarfile = jarfile;
    this.uri = uri;
    this.uname = uname;
    this.passwd = passwd;
  }

  public boolean equals(DMProps dm2) {
    return (this.jarfile.equals(dm2.jarfile) && this.uri.equals(dm2.uri)
        && this.uname.equals(dm2.uname) && this.passwd.equals(dm2.passwd));
  }

  public void setJarFile(String jarfile) {
    this.jarfile = jarfile;
  }

  public void setURI(String uri) {
    this.uri = uri;
  }

  public void setUname(String uname) {
    this.uname = uname;
  }

  public void setPasswd(String passwd) {
    this.passwd = passwd;
  }

  public String getJarFile() {
    return (jarfile);
  }

  public String getURI() {
    return (uri);
  }

  public String getUname() {
    return (uname);
  }

  public String getPasswd() {
    return (passwd);
  }

}
