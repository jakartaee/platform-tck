/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.misc.moduleName.twojars;

import javax.annotation.ManagedBean;
import javax.annotation.Resource;

import com.sun.ts.tests.ejb30.assembly.appres.common.AppResBeanBase;
import com.sun.ts.tests.ejb30.assembly.appres.common.AppResRemoteIF;

@ManagedBean("ModuleMBean")
public class ModuleMBean extends AppResBeanBase implements AppResRemoteIF {
  @Resource(lookup = "java:module/ModuleName")
  private String moduleName;

  @Resource(lookup = "java:app/AppName")
  private String appName;

  public String getModuleName() {
    return moduleName;
  }

  public String getAppName() {
    return appName;
  }
}
