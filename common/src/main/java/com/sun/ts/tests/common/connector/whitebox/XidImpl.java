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

package com.sun.ts.tests.common.connector.whitebox;

import javax.transaction.xa.Xid;

public class XidImpl implements Xid {

  private static int ID = 0;

  public int formatID; // Format identifier
                       // (-1) means that the XidImpl is null

  public int branchQualifier;

  public int globalTxID;

  static public final int MAXGTRIDSIZE = 64;

  static public final int MAXBQUALSIZE = 64;

  public XidImpl() {
    int foo = ++ID;
    formatID = foo;
    branchQualifier = foo;
    globalTxID = foo;
  }

  public boolean equals(Object o) {
    XidImpl other; // The "other" XidImpl
    int L; // Combined gtrid_length + bqual_length
    int i;

    if (!(o instanceof XidImpl)) // If the other XidImpl isn't an XidImpl
    {
      return false; // It can't be equal
    }

    other = (XidImpl) o; // The other XidImpl, now properly cast

    if (this.formatID == other.formatID
        && this.branchQualifier == other.branchQualifier
        && this.globalTxID == other.globalTxID) {
      return true;
    }

    return false;
  }

  /**
   * Compute the hash code.
   *
   * @return the computed hashcode
   */
  public int hashCode() {
    if (formatID == (-1)) {
      return (-1);
    }

    return formatID + branchQualifier + globalTxID;

  }

  /*
   * Convert to String
   *
   * <p> This is normally used to display the XidImpl when debugging.
   */

  /**
   * Return a string representing this XidImpl.
   *
   * @return the string representation of this XidImpl
   */
  public String toString() {

    String s = new String(
        "{XidImpl: " + "formatID(" + formatID + "), " + "branchQualifier ("
            + branchQualifier + "), " + "globalTxID(" + globalTxID + ")}");

    return s;
  }

  /*
   * Return branch qualifier
   */

  /**
   * Returns the branch qualifier for this XidImpl.
   *
   * @return the branch qualifier
   */
  public byte[] getBranchQualifier() {
    String foo = (new Integer(branchQualifier)).toString();
    return foo.getBytes();
  }

  public int getFormatId() {
    return formatID;
  }

  public byte[] getGlobalTransactionId() {
    String foo = (new Integer(globalTxID)).toString();
    return foo.getBytes();
  }
}
