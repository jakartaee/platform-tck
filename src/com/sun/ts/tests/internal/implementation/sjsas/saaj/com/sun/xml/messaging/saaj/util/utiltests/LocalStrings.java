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
 * $Id$
 */

package com.sun.ts.tests.internal.implementation.sjsas.saaj.com.sun.xml.messaging.saaj.util.utiltests;

import java.util.ListResourceBundle;

public class LocalStrings extends ListResourceBundle {
  public Object[][] getContents() {
    return contents;
  }

  static final Object[][] contents = { { "message1", "This is message1." },
      { "message2", "This is message2 [{0} {1} {2} {3}]." },
      { "message3", "This is message3 with arg {0} and arg {1}." },
      { "message4", "This is message4 with just arg {0}." },
      { "message5", "This is message5 [{0} {1} {2}]." } };
}
