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
 * $Id: MDB_Q_Test.java 59995 2009-10-14 12:05:29Z af70133 $
 */
package com.sun.ts.tests.jms.commonee;

import javax.ejb.Remote;
import java.util.Properties;

@Remote
public interface MDB_Q_Test {
  boolean askMDBToRunATest(String typeOfTest);

  boolean askMDBToSendAMessage(String messageType);

  boolean checkOnResponse(String prop);

  boolean isThereSomethingInTheQueue();

  void setup(Properties p);

  void cleanTheQueue();

  void remove();

}
