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
package com.sun.ts.tests.jsf.spec.appconfigresources.common.beans;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.FactoryFinder;
import javax.faces.event.PhaseListener;

public class OrderingBean {

  private String[] suffixes;

  private int total;

  // Absolute Ordering
  public boolean isAbsoluteOrderCorrect() {
    suffixes = new String[] { "B", "C", "A", "D" };
    total = 4;

    return isOrderCorrect();
  }

  // Relative Ordering test 1
  public boolean isRelativeOneOrderCorrect() {
    suffixes = new String[] { "B", "C", "A" };
    total = 3;

    return isOrderCorrect();
  }

  // ---------------------------------------------------------- private methods
  private boolean isOrderCorrect() {

    LifecycleFactory factory = (LifecycleFactory) FactoryFinder
        .getFactory(FactoryFinder.LIFECYCLE_FACTORY);
    Lifecycle l = factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
    PhaseListener[] listeners = l.getPhaseListeners();
    List<PhaseListener> list = new ArrayList<PhaseListener>();
    for (PhaseListener listener : listeners) {
      if (listener.getClass().getName()
          .contains("com.sun.ts.tests.jsf.spec.appconfigresources."
              + "common.listeners.PhaseListener")) {
        list.add(listener);
      }
    }
    listeners = list.toArray(new PhaseListener[list.size()]);
    if (listeners.length != total) {
      System.out.println("INCORRECT LISTENER COUNT: " + listeners.length);

      for (int i = 0; listeners.length != i; i++) {
        System.out
            .println("LISTENER FOUND: " + listeners[i].getClass().getName());
      }
      return false;
    }

    for (int i = 0; i < listeners.length; i++) {
      if (!listeners[i].getClass().getName().endsWith(suffixes[i])) {
        System.out.println(
            "INCORRECT DOCUMENT ORDERING: " + Arrays.toString(listeners));

        return false;
      }
    }

    return true;

  }
}
