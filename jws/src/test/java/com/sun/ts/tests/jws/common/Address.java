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

public class Address {

  private String email = "";

  private String phone = "";

  private String street = "";

  private String city = "San Francisco";

  private String state = "CA";

  private String zipcode = "94104";

  private String country = "U.S.";

  public Address(String email, String phone, String street, String city,
      String state, String zipcode, String country) {
    this.email = email;
    this.phone = phone;
    this.street = street;
    this.city = city;
    this.state = state;
    this.zipcode = zipcode;
    this.country = country;
  }

  public Address() {
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getZipcode() {
    return zipcode;
  }

  public void setZipcode(String zipcode) {
    this.zipcode = zipcode;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String toString() {
    return "email:" + email + " phone:" + phone + " street:" + street + " city:"
        + city + " state:" + state + " zipcode:" + zipcode + " country:"
        + country;
  }
}
