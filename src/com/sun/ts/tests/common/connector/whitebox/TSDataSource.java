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

package com.sun.ts.tests.common.connector.whitebox;

import java.util.Vector;

public interface TSDataSource extends TSConnectionFactory {

  /*
   * @name checkConnectionManager
   * 
   * @desc return true if ConnectionManager is Serializable
   * 
   * @return boolean
   */
  public boolean checkConnectionManager();

  /*
   * @name getLog
   * 
   * @desc returns the Log.
   * 
   * @return Log
   */
  public Vector getLog();

  /*
   * @name clearLog
   * 
   * @desc Empties the Log
   */
  public void clearLog();

  /*
   * @name setLogFlag
   * 
   * @desc Turns logging on/off
   */
  public void setLogFlag(boolean b);

  /*
   * @name getStateLog
   * 
   * @desc returns the Log.
   * 
   * @return Log
   */
  public Vector getStateLog();

}
