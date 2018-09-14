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

import java.net.URL;
import java.io.File;
import com.sun.ts.lib.util.sec.net.www.ParseUtil;

/**
 * (Windows) Platform specific handling for file: URLs . In particular deals
 * with network paths mapping them to UNCs.
 *
 * @author Michael McMahon
 */

public class FileURLMapper {

  URL url;

  String file;

  public FileURLMapper(URL url) {
    this.url = url;
  }

  /**
   * @returns the platform specific path corresponding to the URL, and in
   *          particular returns a UNC when the authority contains a hostname
   */

  public String getPath() {
    if (file != null) {
      return file;
    }
    String host = url.getHost();
    if (host != null && !host.equals("")
        && !"localhost".equalsIgnoreCase(host)) {
      String rest = url.getFile();
      String s = host + ParseUtil.decode(url.getFile());
      file = "\\\\" + s.replace('/', '\\');
      return file;
    }
    String path = url.getFile().replace('/', '\\');
    file = ParseUtil.decode(path);
    return file;
  }

  public boolean exists() {
    String path = getPath();
    File f = new File(path);
    return f.exists();
  }
}
