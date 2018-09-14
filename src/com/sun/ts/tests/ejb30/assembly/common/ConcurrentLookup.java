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
 * $Id$
 */

package com.sun.ts.tests.ejb30.assembly.common;

import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import java.util.ArrayList;
import java.util.Vector;
import javax.naming.NamingException;

/**
 * Multiple threads performing lookup of EJB and DataSource, and each thread has
 * its own instance of InitialContext. See glassfish issue
 * https://glassfish.dev.java.net/issues/show_bug.cgi?id=2672
 */
public class ConcurrentLookup {
  private final static Integer DEFAULT_LOOKUP_COUNT = 10;

  private Vector lookupResults = new Vector();

  public ConcurrentLookup() {
  }

  public String concurrentLookup(String shortLookupName, Integer count)
      throws TestFailedException {
    if (count == null) {
      count = DEFAULT_LOOKUP_COUNT;
    }
    ArrayList<Thread> threads = new ArrayList<Thread>();
    for (int i = 0; i < count; i++) {
      threads.add((new LookupThread(shortLookupName)));
    }
    for (Thread t : threads) {
      t.start();
    }
    for (Thread t : threads) {
      try {
        t.join();
      } catch (InterruptedException ex) {
        ex.printStackTrace();
      }
    }
    if (lookupResults.size() != count) {
      throw new TestFailedException(
          "Expecting " + count + " lookups, but only got "
              + lookupResults.size() + " results: " + lookupResults);
    }
    for (Object obj : lookupResults) {
      if (obj == null || obj instanceof Throwable) {
        throw new TestFailedException(
            "At least one lookup returned null or Throwable:" + lookupResults);
      }
    }
    return "All " + count + " lookups returned good results: " + lookupResults;
  }

  class LookupThread extends Thread {
    private String shortLookupName;

    public LookupThread() {
      super();
    }

    public LookupThread(String shortLookupName) {
      super();
      this.shortLookupName = shortLookupName;
    }

    @Override
    public void run() {
      Object result = null;
      try {
        if (shortLookupName.startsWith("java:global/")) {
          result = ServiceLocator.lookup(shortLookupName);
        } else {
          result = ServiceLocator.lookupByShortName(shortLookupName);
        }
      } catch (NamingException ex) {
        result = ex;
      }
      lookupResults.add(result);
    }
  }
}
