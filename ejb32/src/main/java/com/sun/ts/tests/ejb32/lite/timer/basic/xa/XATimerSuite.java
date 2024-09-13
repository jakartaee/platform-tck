/*
 * Copyright (c) 2022 Eclipse Foundation and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb32.lite.timer.basic.xa;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;


/**
 * A Junit5 suite that runs all the tests in the com.sun.ts.tests.ejb32.lite.timer.basic.xa package. This should
 * be extended by a vendor to include the startup of the database backing the ejblite-pu/jta-data-source jdbc/DBTimer
 * datasource.
 */
@Suite
@SelectClasses({
        ClientEjblitejspTest.class,
        ClientEjbliteservlet2Test.class,
        ClientEjbliteservletTest.class,
        JsfClientEjblitejsfTest.class,
})
public class XATimerSuite {
}
