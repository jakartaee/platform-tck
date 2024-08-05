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
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.common.taglibsig.validation;

import java.util.ArrayList;
import java.util.List;

import com.sun.ts.tests.common.taglibsig.TagLibraryDescriptor;

/**
 * Performs validation of TagLibraryDescriptor <code>&lt;uri&gt;</code>
 * elements.
 */
public class URIValidator implements Validator {

  public List validate(TagLibraryDescriptor control,
      TagLibraryDescriptor underTest) {
    List messages = new ArrayList();
    if (control == null || underTest == null) {
      messages.add("[URIValidator] Null arguments.");
    } else {
      String controlUri = control.getURI();
      String underTestUri = underTest.getURI();
      if (!(controlUri.equals(underTestUri))) {
        messages.add("Incorrect URI found.  Expected: '" + controlUri + "',"
            + " received: '" + underTestUri + "'.");
      }
    }
    return messages;
  }
}
