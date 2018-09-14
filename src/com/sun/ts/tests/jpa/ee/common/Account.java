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

package com.sun.ts.tests.jpa.ee.common;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ACCOUNT")
public class Account implements java.io.Serializable {

  // Instance variable
  @Id
  private Integer id;

  private double balance;

  private double deposit;

  private double withdraw;

  // constructors

  public Account() {
    // No-arg
  }

  public Account(Integer id, double balance) {
    this.id = id;
    this.balance = balance;
  }

  // ===========================================================
  // Account Business Methods

  public int id() {
    return id;
  }

  public double balance() {
    return balance;
  }

  public double deposit(double v) {
    balance += v;
    return balance;
  }

  public double withdraw(double v) {
    balance -= v;
    return balance;
  }

  // ===========================================================
  public boolean equals(Object o) {
    Account other;
    boolean same = true;

    if (!(o instanceof Account)) {
      return false;
    }
    other = (Account) o;

    if (this.id() == other.id()
        && (Double.compare(this.balance(), other.balance()) == 0)) {
      same = true;
    }

    return same;
  }

  public int hashCode() {
    int myHash;

    myHash = this.id;

    return myHash;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append(this.getClass().getSimpleName() + "[");
    result.append("id: " + id());
    result.append(",  balance: " + balance());
    result.append("]");
    return result.toString();
  }
}
