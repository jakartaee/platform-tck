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
package com.sun.ts.tests.jsf.spec.coretags.common;

import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

public class SelectItemsBean {

  private List<String> myList;

  {
    myList = new ArrayList<String>();
    myList.add("Rush");
    myList.add("Yes");
    myList.add("Doors");
  }

  // List of SelectItems
  public List<SelectItem> getListNames() {
    List<SelectItem> listNames = new ArrayList<SelectItem>();

    for (String s : myList) {
      listNames.add(new SelectItem(s));
    }

    return listNames;
  }

  // String[] of SelectItems
  public SelectItem[] getArrayNames() {
    SelectItem[] arrayNames = new SelectItem[3];

    int i = 0;
    for (String s : myList) {
      arrayNames[i] = new SelectItem(s);
      i++;
    }

    return arrayNames;
  }

  public List<Band> getMyBands() {
    List<Band> bands = new ArrayList<Band>();

    bands.add(new Band("><&", "Escape Characters", "Escape Characters"));
    // the below option is here as debug only.
    // bands.add(new Band("Tool", "Great Band", "&<>\'"));

    return bands;
  }

  // -------------------------------------------------------- Inner classes
  public static class Band {

    private String name;

    private String description;

    private String genre;

    public Band(String name, String description, String genre) {
      this.name = name;
      this.description = description;
      this.genre = genre;
    }

    /**
     * @return the name of the band
     */
    public String getName() {
      return name;
    }

    /**
     * @param bandname
     *          the name of the band.
     */
    public void setName(String bandname) {
      this.name = bandname;
    }

    /**
     * @return the description
     */
    public String getDescription() {
      return description;
    }

    /**
     * @param description
     *          the description to set
     */
    public void setDescription(String description) {
      this.description = description;
    }

    /**
     * @return the genre
     */
    public String getGenre() {
      return genre;
    }

    /**
     * @param genre
     *          the genre to set
     */
    public void setGenre(String genre) {
      this.genre = genre;
    }
  }// -- End Band Class
}
