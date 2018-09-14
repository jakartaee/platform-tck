/*
 * Copyright (c) 2002, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.lib.util.sec.misc;

import java.util.jar.JarFile;
import java.io.Console;
import java.io.File;
import java.io.FileDescriptor;
import java.security.ProtectionDomain;

import java.security.AccessController;

/**
 * A repository of "shared secrets", which are a mechanism for calling
 * implementation-private methods in another package without using reflection. A
 * package-private class implements a public interface and provides the ability
 * to call package-private methods within that package; the object implementing
 * that interface is provided through a third package to which access is
 * restricted. This framework avoids the primary disadvantage of using
 * reflection for this purpose, namely the loss of compile-time checking.
 */

public class SharedSecrets {
  private static final Unsafe unsafe = Unsafe.getUnsafe();

  private static JavaUtilJarAccess javaUtilJarAccess;

  private static JavaLangAccess javaLangAccess;

  private static JavaIOAccess javaIOAccess;

  private static JavaIODeleteOnExitAccess javaIODeleteOnExitAccess;

  private static JavaNetAccess javaNetAccess;

  private static JavaIOFileDescriptorAccess javaIOFileDescriptorAccess;

  private static JavaSecurityProtectionDomainAccess javaSecurityProtectionDomainAccess;

  private static JavaSecurityAccess javaSecurityAccess;

  public static JavaUtilJarAccess javaUtilJarAccess() {
    if (javaUtilJarAccess == null) {
      // Ensure JarFile is initialized; we know that that class
      // provides the shared secret
      unsafe.ensureClassInitialized(JarFile.class);
    }
    return javaUtilJarAccess;
  }

  public static void setJavaUtilJarAccess(JavaUtilJarAccess access) {
    javaUtilJarAccess = access;
  }

  public static void setJavaLangAccess(JavaLangAccess jla) {
    javaLangAccess = jla;
  }

  public static JavaLangAccess getJavaLangAccess() {
    return javaLangAccess;
  }

  public static void setJavaNetAccess(JavaNetAccess jna) {
    javaNetAccess = jna;
  }

  public static JavaNetAccess getJavaNetAccess() {
    return javaNetAccess;
  }

  public static void setJavaIOAccess(JavaIOAccess jia) {
    javaIOAccess = jia;
  }

  public static JavaIOAccess getJavaIOAccess() {
    if (javaIOAccess == null) {
      unsafe.ensureClassInitialized(Console.class);
    }
    return javaIOAccess;
  }

  public static void setJavaIODeleteOnExitAccess(
      JavaIODeleteOnExitAccess jida) {
    javaIODeleteOnExitAccess = jida;
  }

  public static JavaIODeleteOnExitAccess getJavaIODeleteOnExitAccess() {
    if (javaIODeleteOnExitAccess == null) {
      unsafe.ensureClassInitialized(File.class);
    }
    return javaIODeleteOnExitAccess;
  }

  public static void setJavaIOFileDescriptorAccess(
      JavaIOFileDescriptorAccess jiofda) {
    javaIOFileDescriptorAccess = jiofda;
  }

  public static JavaIOFileDescriptorAccess getJavaIOFileDescriptorAccess() {
    if (javaIOFileDescriptorAccess == null)
      unsafe.ensureClassInitialized(FileDescriptor.class);

    return javaIOFileDescriptorAccess;
  }

  public static void setJavaSecurityProtectionDomainAccess(
      JavaSecurityProtectionDomainAccess jspda) {
    javaSecurityProtectionDomainAccess = jspda;
  }

  public static JavaSecurityProtectionDomainAccess getJavaSecurityProtectionDomainAccess() {
    if (javaSecurityProtectionDomainAccess == null)
      unsafe.ensureClassInitialized(ProtectionDomain.class);

    return javaSecurityProtectionDomainAccess;
  }

  public static void setJavaSecurityAccess(JavaSecurityAccess jsa) {
    javaSecurityAccess = jsa;
  }

  public static JavaSecurityAccess getJavaSecurityAccess() {
    if (javaSecurityAccess == null) {
      unsafe.ensureClassInitialized(AccessController.class);
    }
    return javaSecurityAccess;
  }
}
