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
 * $Id:
 */

package com.sun.ts.tests.jsf.spec.jstl.foreachtag;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;

public class BandBean {

  private String[] firstNames = { "Geddy", "Alex", "Neil" };

  private List<String> lastNames = new ArrayList<String>();

  private Vector<String> albums = new Vector<String>();

  private LinkedList<String> songs = new LinkedList<String>();

  private HashSet<String> releaseYears = new HashSet<String>();

  private TreeSet<String> ratings = new TreeSet<String>();

  public BandBean() {
    this.initialSetup();
  }

  private void initialSetup() {
    // ArrayList
    lastNames.add("Lee");
    lastNames.add("Lifeson");
    lastNames.add("Peart");

    // Vector
    albums.add("Exit Stage Left");
    albums.add("Hemispheres");
    albums.add("Farewell To Kings");

    // LinkedList
    songs.add("Freewill");
    songs.add("2112");
    songs.add("Subdivisions");

    // HashSet
    releaseYears.add("1971");
    releaseYears.add("1972");
    releaseYears.add("1973");

    // TreeSet
    ratings.add("8");
    ratings.add("9");
    ratings.add("10");
  }

  /**
   * @return the firstNames
   */
  public String[] getFirstNames() {
    return firstNames;
  }

  /**
   * @param firstNames
   *          the firstNames to set
   */
  public void setFirstNames(String[] firstNames) {
    this.firstNames = firstNames;
  }

  /**
   * @return the lastNames
   */
  public List<String> getLastNames() {
    return lastNames;
  }

  /**
   * @param lastNames
   *          the lastNames to set
   */
  public void setLastNames(List<String> lastNames) {
    this.lastNames = lastNames;
  }

  /**
   * @return the albums
   */
  public Vector getAlbums() {
    return albums;
  }

  /**
   * @param albums
   *          the albums to set
   */
  public void setAlbums(Vector albums) {
    this.albums = albums;
  }

  /**
   * @return the songs
   */
  public LinkedList getSongs() {
    return songs;
  }

  /**
   * @param songs
   *          the songs to set
   */
  public void setSongs(LinkedList songs) {
    this.songs = songs;
  }

  /**
   * @return the releaseYears
   */
  public HashSet getReleaseYears() {
    return releaseYears;
  }

  /**
   * @param releaseDate
   *          the releaseYears to set
   */
  public void setReleaseYears(HashSet releaseYears) {
    this.releaseYears = releaseYears;
  }

  /**
   * @return the ratings
   */
  public TreeSet getRatings() {
    return ratings;
  }

  /**
   * @param rating
   *          the ratings to set
   */
  public void setRatings(TreeSet ratings) {
    this.ratings = ratings;
  }
}
