/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.lite.singleton.lifecycle.bean;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
// BSingletonIF is its only local business interface, and Serializable and
// Externalizable are ignored when determining business interfaces
public class BSingletonBean extends BeanBase
    implements BSingletonIF, Serializable, Externalizable {

  public void writeExternal(ObjectOutput arg0) throws IOException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void readExternal(ObjectInput arg0)
      throws IOException, ClassNotFoundException {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
