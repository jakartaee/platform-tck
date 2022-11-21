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
 */

/*
 * $Id$
 */

package com.sun.ts.tests.jws.common;

public class Salary {

  private int salary = 0;

  private int bonusPercentage = 0;

  private String currency = "USD";

  public Salary(int salary, int bonusPercentage, String currency) {
    this.salary = salary;
    this.bonusPercentage = bonusPercentage;
    this.currency = currency;
  }

  public Salary(int salary, int bonusPercentage) {
    this.salary = salary;
    this.bonusPercentage = bonusPercentage;
  }

  public Salary() {
  }

  public int getSalary() {
    return salary;
  }

  public void setSalary(int salary) {
    this.salary = salary;
  }

  public int getBonusPercentage() {
    return bonusPercentage;
  }

  public void setBonusPercentage(int bonusPercentage) {
    this.bonusPercentage = bonusPercentage;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String toString() {
    return "Salary:$" + salary + " Bonus Percentage:%" + bonusPercentage
        + " Currency:" + currency;
  }

}
