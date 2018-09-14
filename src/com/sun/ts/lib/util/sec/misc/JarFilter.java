/*
 * Copyright (c) 2001, 2018 Oracle and/or its affiliates. All rights reserved.
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
import java.io.FilenameFilter;

/**
 * <p>
 * This class checks that only jar and zip files are included in the file list.
 * This class is used in extension installation support (ExtensionDependency).
 * <p>
 *
 * @author Michael Colburn
 */
public class JarFilter implements FilenameFilter {

  public boolean accept(File dir, String name) {
    String lower = name.toLowerCase();
    return lower.endsWith(".jar") || lower.endsWith(".zip");
  }
}
