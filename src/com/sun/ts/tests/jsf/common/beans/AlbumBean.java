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

package com.sun.ts.tests.jsf.common.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.event.ActionEvent;

public class AlbumBean implements Serializable {

  private String artist;

  private String album;

  private List<String> songs;

  private String comments;

  public AlbumBean() {
    this.initialSetup();
  }

  /**
   * @return the artist
   */
  public String getArtist() {
    return artist;
  }

  /**
   * @param artist
   *          the artist to set
   */
  public void setArtist(String artist) {
    this.artist = artist;
  }

  /**
   * @return the albumName
   */
  public String getAlbum() {
    return album;
  }

  /**
   * @param album
   *          the albumName to set
   */
  public void setAlbum(String album) {
    this.album = album;
  }

  /**
   * @return the songTitles
   */
  public List<String> getSongs() {
    return songs;
  }

  /**
   * @param songs
   *          the songs to set
   */
  public void setSongs(List<String> song) {
    this.songs = song;
  }

  /**
   * @return the comments
   */
  public String getComments() {
    return comments;
  }

  /**
   * @param comments
   *          the comments to set
   */
  public void setComments(String comments) {
    this.comments = comments;
  }

  public void eraseComments(ActionEvent ae) {
    comments = "You Pressed ERASE!";
  }

  // ---------------------------------------------------------- private methods
  private void initialSetup() {
    this.artist = "Rush";
    this.album = "Hemispheres";

    this.songs = new ArrayList<String>();
    this.songs.add("Cygnus X-1 Book II");
    this.songs.add("Circumstances");
    this.songs.add("The Trees");
    this.songs.add("La Villa Strangiato");
  }
}
