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
 * $Id$ 
 */

package com.sun.ts.tests.jstl.common;

public interface JstlTckConstants {
  public final String JSTL_DB_URL = "jstl.db.url";

  public final String JSTL_DB_USER = "jstl.db.user";

  public final String JSTL_DB_PASSWORD = "jstl.db.password";

  public final String JSTL_DB_DRIVER = "jstl.db.driver";

  public final String[] JSTL_DB_PROPS = new String[] { JSTL_DB_URL,
      JSTL_DB_USER, JSTL_DB_PASSWORD, JSTL_DB_DRIVER };

  /**
   * JSTL TCK DataSource name reference
   */
  public final String JSTLDS = "jstlDS";

  /**
   * JSTL TCK Connection wrapped DataSource name reference
   */
  public final String LOGDS = "logDS";

  /**
   * SQL Properties name reference
   */
  public final String SQLPROPS = "sqlProps";

  /**
   * URI to sql.properties file
   */
  public final String PROPS_URI = "/WEB-INF/jstl-sql.properties";

  /**
   * Current number of rows in the table jstl_tab1
   */
  public final String JSTL_TAB1_ROWS = "JSTL_TAB1_ROWS";
}
