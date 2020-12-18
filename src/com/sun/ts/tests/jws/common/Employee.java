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
 */

/*
 * $Id$
 */

package com.sun.ts.tests.jws.common;

public class Employee {

  private Name name;

  private Department dept;

  private Salary salary;

  private Address address;

  private String title;

  private int type;

  public Employee(Name name, Department dept, Salary salary, Address address,
      String title, int type) {
    this.name = name;
    this.dept = dept;
    this.salary = salary;
    this.address = address;
    this.title = title;
    this.type = type;
  }

  public Employee() {
    name = new Name();
    dept = new Department();
    salary = new Salary();
    address = new Address();
    title = "";
    type = EmployeeType.PERMANENT;
  }

  public Name getName() throws NameException {
    return name;
  }

  public void setName(Name name) throws NameException {
    this.name = name;
  }

  public Department getDept() throws DepartmentException {
    return dept;
  }

  public void setDept(Department dept) throws DepartmentException {
    this.dept = dept;
  }

  public Salary getSalary() throws SalaryException {
    return salary;
  }

  public void setSalary(Salary salary) throws SalaryException {
    this.salary = salary;
  }

  public com.sun.ts.tests.jws.common.Address getAddress()
      throws AddressException {
    return address;
  }

  public void setAddress(Address address) throws AddressException {
    this.address = address;
  }

  public String getTitle() throws TitleException {
    return title;
  }

  public void setTitle(String title) throws TitleException {
    this.title = title;
  }

  public int getType() throws TypeException {
    return type;
  }

  public void setType(int type) throws TypeException {
    this.type = type;
  }

  public String toString() {
    return name + "::" + dept + "::" + salary + "::" + address + "::" + title
        + "::" + type;
  }

}
