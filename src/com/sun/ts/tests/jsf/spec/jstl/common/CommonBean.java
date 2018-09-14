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
package com.sun.ts.tests.jsf.spec.jstl.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class CommonBean {

  private String escapeString = "><&'\"";

  private String nullString = null;

  private String trimString = "  \t  trimmed string  \n";

  private String[] joinString = { "one", "two", "three" };

  private List<Boolean> fList;

  private Collection<String> fColl;

  private ArrayList<Integer> fAList;

  private Map<String, String> fMap;

  private Hashtable<String, String> fHash;

  public CommonBean() {
    fList = new ArrayList<Boolean>();
    fList.add(true);
    fList.add(false);
    fList.add(true);

    fColl = new ArrayList<String>();
    fColl.add("A");
    fColl.add("B");
    fColl.add("C");

    fAList = new ArrayList<Integer>();
    fAList.add(1);
    fAList.add(2);
    fAList.add(3);

    fMap = new HashMap<String, String>();
    fMap.put("key1", "value1");
    fMap.put("key2", "value2");
    fMap.put("key3", "value3");

    fHash = new Hashtable<String, String>();
    fHash.put("key1", "value1");
    fHash.put("key2", "value2");
    fHash.put("key3", "value3");
  }

  /**
   * @return the escapeString
   */
  public String getEscapeString() {
    return escapeString;
  }

  /**
   * @param escapeString
   *          the escapeString to set
   */
  public void setEscapeString(String escapeString) {
    this.escapeString = escapeString;
  }

  /**
   * @return the nullString
   */
  public String getNullString() {
    return nullString;
  }

  /**
   * @param nullString
   *          the nullString to set
   */
  public void setNullString(String nullString) {
    this.nullString = nullString;
  }

  /**
   * @return the joinString
   */
  public String[] getJoinString() {
    return joinString;
  }

  /**
   * @param joinString
   *          the joinString to set
   */
  public void setJoinString(String[] joinString) {
    this.joinString = joinString;
  }

  /**
   * @return the fList
   */
  public List<Boolean> getfList() {
    return fList;
  }

  /**
   * @param fList
   *          the fList to set
   */
  public void setfList(List<Boolean> fList) {
    this.fList = fList;
  }

  /**
   * @return the fColl
   */
  public Collection<String> getfColl() {
    return fColl;
  }

  /**
   * @param fColl
   *          the fColl to set
   */
  public void setfColl(Collection<String> fColl) {
    this.fColl = fColl;
  }

  /**
   * @return the fAList
   */
  public ArrayList<Integer> getfAList() {
    return fAList;
  }

  /**
   * @param fAList
   *          the fAList to set
   */
  public void setfAList(ArrayList<Integer> fAList) {
    this.fAList = fAList;
  }

  /**
   * @return the fMap
   */
  public Map<String, String> getfMap() {
    return fMap;
  }

  /**
   * @param fMap
   *          the fMap to set
   */
  public void setfMap(Map<String, String> fMap) {
    this.fMap = fMap;
  }

  /**
   * @return the fHash
   */
  public Hashtable<String, String> getfHash() {
    return fHash;
  }

  /**
   * @param fHash
   *          the fHash to set
   */
  public void setfHash(Hashtable<String, String> fHash) {
    this.fHash = fHash;
  }

  /**
   * @return the trimString
   */
  public String getTrimString() {
    return trimString;
  }

  /**
   * @param trimString
   *          the trimString to set
   */
  public void setTrimString(String trimString) {
    this.trimString = trimString;
  }
}
