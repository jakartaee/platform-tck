/*
 * Copyright (c) 1998, 2018 Oracle and/or its affiliates. All rights reserved.
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

import java.io.File;
import java.io.IOException;
import java.io.FilePermission;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.MalformedURLException;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.AccessControlContext;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.ProtectionDomain;
import java.security.CodeSource;
import com.sun.ts.lib.util.sec.security.action.GetPropertyAction;
import com.sun.ts.lib.util.sec.security.util.SecurityConstants;
import com.sun.ts.lib.util.sec.net.www.ParseUtil;

/**
 * This class is used by the system to launch the main application. Launcher
 */
public class Launcher {
  private static URLStreamHandlerFactory factory = new Factory();

  private static Launcher launcher = new Launcher();

  public static Launcher getLauncher() {
    return launcher;
  }

  private ClassLoader loader;

  public Launcher() {

    // Now create the class loader to use to launch the application
    try {
      loader = AppClassLoader.getAppClassLoader(new ExtClassLoader(null));
    } catch (IOException e) {
      throw new InternalError("Could not create application class loader");
    }

    // Also set the context class loader for the primordial thread.
    Thread.currentThread().setContextClassLoader(loader);

    // Finally, install a security manager if requested
    String s = System.getProperty("java.security.manager");
    if (s != null) {
      SecurityManager sm = null;
      if ("".equals(s) || "default".equals(s)) {
        sm = new java.lang.SecurityManager();
      } else {
        try {
          sm = (SecurityManager) loader.loadClass(s).newInstance();
        } catch (IllegalAccessException e) {
        } catch (InstantiationException e) {
        } catch (ClassNotFoundException e) {
        } catch (ClassCastException e) {
        }
      }
      if (sm != null) {
        System.setSecurityManager(sm);
      } else {
        throw new InternalError("Could not create SecurityManager: " + s);
      }
    }
  }

  /*
   * Returns the class loader used to launch the main application.
   */
  public ClassLoader getClassLoader() {
    return loader;
  }

  /*
   * The class loader used for loading installed extensions.
   */
  static class ExtClassLoader extends URLClassLoader {

    void addExtURL(URL url) {
      super.addURL(url);
    }

    /*
     * Creates a new ExtClassLoader for the specified directories.
     */
    public ExtClassLoader(File[] ignore) throws IOException {
      super(new URL[0], null, factory);
    }

    /*
     * Searches the installed extension directories for the specified library
     * name. For each extension directory, we first look for the native library
     * in the subdirectory whose name is the value of the system property
     * <code>os.arch</code>. Failing that, we look in the extension directory
     * itself.
     */
    public String findLibrary(String name) {
      return null;
    }

    private static AccessControlContext getContext(File[] dirs)
        throws IOException {
      PathPermissions perms = new PathPermissions(dirs);

      ProtectionDomain domain = new ProtectionDomain(new CodeSource(
          perms.getCodeBase(), (java.security.cert.Certificate[]) null), perms);

      AccessControlContext acc = new AccessControlContext(
          new ProtectionDomain[] { domain });

      return acc;
    }
  }

  /**
   * The class loader used for loading from java.class.path. runs in a
   * restricted security context.
   */
  static class AppClassLoader extends URLClassLoader {

    public static ClassLoader getAppClassLoader(final ClassLoader extcl)
        throws IOException {
      final String s = System.getProperty("java.class.path");
      final File[] path = (s == null) ? new File[0] : getClassPath(s);

      // Note: on bugid 4256530
      // Prior implementations of this doPrivileged() block supplied
      // a rather restrictive ACC via a call to the private method
      // AppClassLoader.getContext(). This proved overly restrictive
      // when loading classes. Specifically it prevent
      // accessClassInPackage.sun.* grants from being honored.
      //
      return (AppClassLoader) AccessController
          .doPrivileged(new PrivilegedAction() {
            public Object run() {
              URL[] urls = (s == null) ? new URL[0] : pathToURLs(path);
              return new AppClassLoader(urls, extcl);
            }
          });
    }

    /*
     * Creates a new AppClassLoader
     */
    AppClassLoader(URL[] urls, ClassLoader parent) {
      super(urls, parent, factory);
    }

    /**
     * Override loadClass so we can checkPackageAccess.
     */
    public synchronized Class loadClass(String name, boolean resolve)
        throws ClassNotFoundException {
      int i = name.lastIndexOf('.');
      if (i != -1) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
          sm.checkPackageAccess(name.substring(0, i));
        }
      }
      return (super.loadClass(name, resolve));
    }

    /**
     * allow any classes loaded from classpath to exit the VM.
     */
    protected PermissionCollection getPermissions(CodeSource codesource) {
      PermissionCollection perms = super.getPermissions(codesource);
      perms.add(new RuntimePermission("exitVM"));
      return perms;
    }

    /**
     * This class loader supports dynamic additions to the class path at
     * runtime.
     *
     * @see java.lang.instrument.Instrumentation#appendToSystemClassPathSearch
     */
    private void appendToClassPathForInstrumentation(String path) {
      assert (Thread.holdsLock(this));

      // addURL is a no-op if path already contains the URL
      super.addURL(getFileURL(new File(path)));
    }

    /**
     * create a context that can read any directories (recursively) mentioned in
     * the class path. In the case of a jar, it has to be the directory
     * containing the jar, not just the jar, as jar files might refer to other
     * jar files.
     */

    private static AccessControlContext getContext(File[] cp)
        throws java.net.MalformedURLException {
      PathPermissions perms = new PathPermissions(cp);

      ProtectionDomain domain = new ProtectionDomain(new CodeSource(
          perms.getCodeBase(), (java.security.cert.Certificate[]) null), perms);

      AccessControlContext acc = new AccessControlContext(
          new ProtectionDomain[] { domain });

      return acc;
    }
  }

  public static URLClassPath getBootstrapClassPath() {
    String prop = AccessController
        .doPrivileged(new GetPropertyAction("sun.boot.class.path"));
    URL[] urls;
    if (prop != null) {
      final String path = prop;
      urls = (URL[]) AccessController.doPrivileged(new PrivilegedAction() {
        public Object run() {
          File[] classPath = getClassPath(path);
          int len = classPath.length;
          Set seenDirs = new HashSet();
          for (int i = 0; i < len; i++) {
            File curEntry = classPath[i];
            // Negative test used to properly handle
            // nonexistent jars on boot class path
            if (!curEntry.isDirectory()) {
              curEntry = curEntry.getParentFile();
            }
            if (curEntry != null && seenDirs.add(curEntry)) {
              MetaIndex.registerDirectory(curEntry);
            }
          }
          return pathToURLs(classPath);
        }
      });
    } else {
      urls = new URL[0];
    }
    return new URLClassPath(urls, factory);
  }

  private static URL[] pathToURLs(File[] path) {
    URL[] urls = new URL[path.length];
    for (int i = 0; i < path.length; i++) {
      urls[i] = getFileURL(path[i]);
    }
    // DEBUG
    // for (int i = 0; i < urls.length; i++) {
    // System.out.println("urls[" + i + "] = " + '"' + urls[i] + '"');
    // }
    return urls;
  }

  private static File[] getClassPath(String cp) {
    File[] path;
    if (cp != null) {
      int count = 0, maxCount = 1;
      int pos = 0, lastPos = 0;
      // Count the number of separators first
      while ((pos = cp.indexOf(File.pathSeparator, lastPos)) != -1) {
        maxCount++;
        lastPos = pos + 1;
      }
      path = new File[maxCount];
      lastPos = pos = 0;
      // Now scan for each path component
      while ((pos = cp.indexOf(File.pathSeparator, lastPos)) != -1) {
        if (pos - lastPos > 0) {
          path[count++] = new File(cp.substring(lastPos, pos));
        } else {
          // empty path component translates to "."
          path[count++] = new File(".");
        }
        lastPos = pos + 1;
      }
      // Make sure we include the last path component
      if (lastPos < cp.length()) {
        path[count++] = new File(cp.substring(lastPos));
      } else {
        path[count++] = new File(".");
      }
      // Trim array to correct size
      if (count != maxCount) {
        File[] tmp = new File[count];
        System.arraycopy(path, 0, tmp, 0, count);
        path = tmp;
      }
    } else {
      path = new File[0];
    }
    // DEBUG
    // for (int i = 0; i < path.length; i++) {
    // System.out.println("path[" + i + "] = " + '"' + path[i] + '"');
    // }
    return path;
  }

  private static URLStreamHandler fileHandler;

  static URL getFileURL(File file) {
    try {
      file = file.getCanonicalFile();
    } catch (IOException e) {
    }

    try {
      return ParseUtil.fileToEncodedURL(file);
    } catch (MalformedURLException e) {
      // Should never happen since we specify the protocol...
      throw new InternalError();
    }
  }

  /*
   * The stream handler factory for loading system protocol handlers.
   */
  private static class Factory implements URLStreamHandlerFactory {
    private static String PREFIX = "sun.net.www.protocol";

    public URLStreamHandler createURLStreamHandler(String protocol) {
      String name = PREFIX + "." + protocol + ".Handler";
      try {
        Class c = Class.forName(name);
        return (URLStreamHandler) c.newInstance();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
      throw new InternalError(
          "could not load " + protocol + "system protocol handler");
    }
  }
}

class PathPermissions extends PermissionCollection {
  // use serialVersionUID from JDK 1.2.2 for interoperability
  private static final long serialVersionUID = 8133287259134945693L;

  private File path[];

  private Permissions perms;

  URL codeBase;

  PathPermissions(File path[]) {
    this.path = path;
    this.perms = null;
    this.codeBase = null;
  }

  URL getCodeBase() {
    return codeBase;
  }

  public void add(java.security.Permission permission) {
    throw new SecurityException("attempt to add a permission");
  }

  private synchronized void init() {
    if (perms != null)
      return;

    perms = new Permissions();

    // this is needed to be able to create the classloader itself!
    perms.add(SecurityConstants.CREATE_CLASSLOADER_PERMISSION);

    // add permission to read any "java.*" property
    perms.add(new java.util.PropertyPermission("java.*",
        SecurityConstants.PROPERTY_READ_ACTION));

    AccessController.doPrivileged(new PrivilegedAction() {
      public Object run() {
        for (int i = 0; i < path.length; i++) {
          File f = path[i];
          String path;
          try {
            path = f.getCanonicalPath();
          } catch (IOException ioe) {
            path = f.getAbsolutePath();
          }
          if (i == 0) {
            codeBase = Launcher.getFileURL(new File(path));
          }
          if (f.isDirectory()) {
            if (path.endsWith(File.separator)) {
              perms.add(new FilePermission(path + "-",
                  SecurityConstants.FILE_READ_ACTION));
            } else {
              perms.add(new FilePermission(path + File.separator + "-",
                  SecurityConstants.FILE_READ_ACTION));
            }
          } else {
            int endIndex = path.lastIndexOf(File.separatorChar);
            if (endIndex != -1) {
              path = path.substring(0, endIndex + 1) + "-";
              perms.add(
                  new FilePermission(path, SecurityConstants.FILE_READ_ACTION));
            } else {
              // XXX?
            }
          }
        }
        return null;
      }
    });
  }

  public boolean implies(java.security.Permission permission) {
    if (perms == null)
      init();
    return perms.implies(permission);
  }

  public java.util.Enumeration elements() {
    if (perms == null)
      init();
    synchronized (perms) {
      return perms.elements();
    }
  }

  public String toString() {
    if (perms == null)
      init();
    return perms.toString();
  }
}
