/*
 * Copyright (c) 1996, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.lib.util.sec.security.provider;

import java.io.*;
import java.util.*;
import java.security.*;

/**
 * An implementation of IdentityScope as a persistent identity database.
 *
 * @see Identity
 * @see Key
 *
 * @author Benjamin Renaud
 */
public class IdentityDatabase extends IdentityScope implements Serializable {

  /** use serialVersionUID from JDK 1.1. for interoperability */
  private static final long serialVersionUID = 4923799573357658384L;

  /* Are we debugging? */
  private static final boolean debug = false;

  /* Are we printing out error messages? */
  private static final boolean error = true;

  /* The source file, if any, for this database. */
  File sourceFile;

  /* The private representation of the database. */
  Hashtable<String, Identity> identities;

  IdentityDatabase() throws InvalidParameterException {
    this("restoring...");
  }

  /**
   * Construct a new, empty database with a specified source file.
   *
   * @param file
   *          the source file.
   */
  public IdentityDatabase(File file) throws InvalidParameterException {
    this(file.getName());
    sourceFile = file;
  }

  /**
   * Construct a new, empty database.
   */
  public IdentityDatabase(String name) throws InvalidParameterException {
    super(name);
    identities = new Hashtable<String, Identity>();
  }

  /**
   * Initialize an identity database from a stream. The stream should contain
   * data to initialized a serialized IdentityDatabase object.
   *
   * @param is
   *          the input stream from which to restore the database.
   *
   * @exception IOException
   *              if a stream IO exception occurs
   */
  public static IdentityDatabase fromStream(InputStream is) throws IOException {
    IdentityDatabase db = null;
    try {
      ObjectInputStream ois = new ObjectInputStream(is);
      db = (IdentityDatabase) ois.readObject();
    } catch (ClassNotFoundException e) {
      // this can't happen.
      debug("This should not be happening.", e);
      error("The version of the database is obsolete. Cannot initialize.");

    } catch (InvalidClassException e) {
      // this may happen in developers workspaces happen.
      debug("This should not be happening.", e);
      error("Unable to initialize system identity scope: "
          + " InvalidClassException. \nThis is most likely due to "
          + "a serialization versioning problem: a class used in "
          + "key management was obsoleted");

    } catch (StreamCorruptedException e) {
      debug("The serialization stream is corrupted. Unable to load.", e);
      error("Unable to initialize system identity scope."
          + " StreamCorruptedException.");
    }

    if (db == null) {
      db = new IdentityDatabase("uninitialized");
    }

    return db;
  }

  /**
   * Initialize an IdentityDatabase from file.
   *
   * @param f
   *          the filename where the identity database is stored.
   *
   * @exception IOException
   *              a file-related exception occurs (e.g. the directory of the
   *              file passed does not exists, etc.
   *
   * @IOException if a file IO exception occurs.
   */
  public static IdentityDatabase fromFile(File f) throws IOException {
    FileInputStream fis = new FileInputStream(f);
    IdentityDatabase edb = fromStream(fis);
    edb.sourceFile = f;
    return edb;
  }

  /**
   * @return the number of identities in the database.
   */
  public int size() {
    return identities.size();
  }

  /**
   * @param name
   *          the name of the identity to be retrieved.
   *
   * @return the identity named name, or null if there are no identities named
   *         name in the database.
   */
  public Identity getIdentity(String name) {
    Identity id = identities.get(name);
    if (id instanceof Signer) {
      localCheck("get.signer");
    }
    return id;
  }

  /**
   * Get an identity by key.
   *
   * @param name
   *          the key of the identity to be retrieved.
   *
   * @return the identity with a given key, or null if there are no identities
   *         with that key in the database.
   */
  public Identity getIdentity(PublicKey key) {
    if (key == null) {
      return null;
    }
    Enumeration<Identity> e = identities();
    while (e.hasMoreElements()) {
      Identity i = e.nextElement();
      PublicKey k = i.getPublicKey();
      if (k != null && keyEqual(k, key)) {
        if (i instanceof Signer) {
          localCheck("get.signer");
        }
        return i;
      }
    }
    return null;
  }

  private boolean keyEqual(Key key1, Key key2) {
    if (key1 == key2) {
      return true;
    } else {
      return MessageDigest.isEqual(key1.getEncoded(), key2.getEncoded());
    }
  }

  /**
   * Adds an identity to the database.
   *
   * @param identity
   *          the identity to be added.
   *
   * @exception KeyManagementException
   *              if a name or key clash occurs, or if another exception occurs.
   */
  public void addIdentity(Identity identity) throws KeyManagementException {
    localCheck("add.identity");
    Identity byName = getIdentity(identity.getName());
    Identity byKey = getIdentity(identity.getPublicKey());
    String msg = null;

    if (byName != null) {
      msg = "name conflict";
    }
    if (byKey != null) {
      msg = "key conflict";
    }
    if (msg != null) {
      throw new KeyManagementException(msg);
    }
    identities.put(identity.getName(), identity);
  }

  /**
   * Removes an identity to the database.
   */
  public void removeIdentity(Identity identity) throws KeyManagementException {
    localCheck("remove.identity");
    String name = identity.getName();
    if (identities.get(name) == null) {
      throw new KeyManagementException(
          "there is no identity named " + name + " in " + this);
    }
    identities.remove(name);
  }

  /**
   * @return an enumeration of all identities in the database.
   */
  public Enumeration<Identity> identities() {
    return identities.elements();
  }

  /**
   * Set the source file for this database.
   */
  void setSourceFile(File f) {
    sourceFile = f;
  }

  /**
   * @return the source file for this database.
   */
  File getSourceFile() {
    return sourceFile;
  }

  /**
   * Save the database in its current state to an output stream.
   *
   * @param os
   *          the output stream to which the database should be serialized.
   *
   * @exception IOException
   *              if an IO exception is raised by stream operations.
   */
  public void save(OutputStream os) throws IOException {
    try {
      ObjectOutputStream oos = new ObjectOutputStream(os);
      oos.writeObject(this);
      oos.flush();
    } catch (InvalidClassException e) {
      debug("This should not be happening.", e);
      return;
    }
  }

  /**
   * Save the database to a file.
   *
   * @exception IOException
   *              if an IO exception is raised by stream operations.
   */
  void save(File f) throws IOException {
    setSourceFile(f);
    FileOutputStream fos = new FileOutputStream(f);
    save(fos);
  }

  /**
   * Saves the database to the default source file.
   *
   * @exception KeyManagementException
   *              when there is no default source file specified for this
   *              database.
   */
  public void save() throws IOException {
    if (sourceFile == null) {
      throw new IOException("this database has no source file");
    }
    save(sourceFile);
  }

  /**
   * This method returns the file from which to initialize the system database.
   */
  private static File systemDatabaseFile() {

    // First figure out where the identity database is hiding, if anywhere.
    String dbPath = Security.getProperty("identity.database");
    // if nowhere, it's the canonical place.
    if (dbPath == null) {
      dbPath = System.getProperty("user.home") + File.separatorChar
          + "identitydb.obj";
    }
    return new File(dbPath);
  }

  /* This block initializes the system database, if there is one. */
  static {
    java.security.AccessController
        .doPrivileged(new java.security.PrivilegedAction<Void>() {
          public Void run() {
            initializeSystem();
            return null;
          }
        });
  }

  /**
   * This method initializes the system's identity database. The canonical
   * location is <user.home>/identitydatabase.obj. This is settable through the
   * identity.database property.
   */
  private static void initializeSystem() {

    IdentityDatabase systemDatabase;
    File dbFile = systemDatabaseFile();

    // Second figure out if it's there, and if it isn't, create one.
    try {
      if (dbFile.exists()) {
        debug("loading system database from file: " + dbFile);
        systemDatabase = fromFile(dbFile);
      } else {
        systemDatabase = new IdentityDatabase(dbFile);
      }
      IdentityScope.setSystemScope(systemDatabase);
      debug("System database initialized: " + systemDatabase);
    } catch (IOException e) {
      debug("Error initializing identity database: " + dbFile, e);
      return;
    } catch (InvalidParameterException e) {
      debug("Error trying to instantiate a system identities db in " + dbFile,
          e);
      return;
    }
  }

  /*
   * private static File securityPropFile(String filename) { // maybe check for
   * a system property which will specify where to // look. String sep =
   * File.separator; return new File(System.getProperty("java.home") + sep +
   * "lib" + sep + "security" + sep + filename); }
   */

  public String toString() {
    return "sun.security.provider.IdentityDatabase, source file: " + sourceFile;
  }

  private static void debug(String s) {
    if (debug) {
      System.err.println(s);
    }
  }

  private static void debug(String s, Throwable t) {
    if (debug) {
      t.printStackTrace();
      System.err.println(s);
    }
  }

  private static void error(String s) {
    if (error) {
      System.err.println(s);
    }
  }

  void localCheck(String directive) {
    SecurityManager security = System.getSecurityManager();
    if (security != null) {
      directive = this.getClass().getName() + "." + directive + "."
          + localFullName();
      security.checkSecurityAccess(directive);
    }
  }

  /**
   * Returns a parsable name for identity: identityName.scopeName
   */
  String localFullName() {
    String parsable = getName();
    if (getScope() != null) {
      parsable += "." + getScope().getName();
    }
    return parsable;
  }

  /**
   * Serialization write.
   */
  private synchronized void writeObject(java.io.ObjectOutputStream stream)
      throws IOException {
    localCheck("serialize.identity.database");
    stream.writeObject(identities);
    stream.writeObject(sourceFile);
  }
}
