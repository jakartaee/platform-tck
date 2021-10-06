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
 * $Id: TestConstants.java 52501 2007-01-24 02:29:49Z lschwenk $
 */

package com.sun.ts.tests.jaxws.wsa.w2j.document.literal.action;

public class TestConstants {
  public static final String ADD_NUMBERS_IN_ACTION = "http://example.com/AddNumbersPortType/add";

  public static final String ADD_NUMBERS_OUT_ACTION = "http://example.com/AddNumbersPortType/addResponse";

  public static final String ADD_NUMBERS2_IN_ACTION = "http://example.com/AddNumbersPortType/add2";

  public static final String ADD_NUMBERS2_OUT_ACTION = "http://example.com/AddNumbersPortType/addResponse2";

  public static final String ADD_NUMBERS3_IN_ACTION = "http://example.com/AddNumbersPortType/addNumbers3Request";

  public static final String ADD_NUMBERS3_OUT_ACTION = "http://example.com/AddNumbersPortType/addNumbers3Response";

  public static final String ADD_NUMBERS4_IN_ACTION = "http://example.com/AddNumbersPortType/addNumbers4Request";

  public static final String ADD_NUMBERS4_OUT_ACTION = "http://example.com/AddNumbersPortType/addNumbers4Response";

  public static final String ADD_NUMBERS5_IN_ACTION = "ExplicitInput5";

  public static final String ADD_NUMBERS5_OUT_ACTION = "ExplicitOutput5";

  public static final String ADD_NUMBERS6_IN_ACTION = "ExplicitInput6";

  public static final String ADD_NUMBERS6_OUT_ACTION = "http://example.com/AddNumbersPortType/addNumbers6Response";

  public static final String ADD_NUMBERS_ADDNUMBERS_ACTION = "http://example.com/AddNumbersPortType/addNumbers/Fault/addFault";

  public static final String ADD_NUMBERS_TOOBIGNUMBERS_ACTION = "http://example.com/AddNumbersPortType/addNumbers/Fault/tooBigFault";

  public static final String ADD_NUMBERS2_ADDNUMBERS_ACTION = "add2fault";

  public static final String ADD_NUMBERS2_TOOBIGNUMBERS_ACTION = "toobig2fault";

  public static final String ADD_NUMBERS3_ADDNUMBERS_ACTION = "add3fault";

  public static final String ADD_NUMBERS3_TOOBIGNUMBERS_ACTION = "http://example.com/AddNumbersPortType/addNumbers3/Fault/tooBig3Fault";

  public static final String ADD_NUMBERS4_ADDNUMBERS_ACTION = "http://example.com/AddNumbersPortType/addNumbers4/Fault/add4Fault";

  public static final String ADD_NUMBERS4_TOOBIGNUMBERS_ACTION = "toobig4fault";

  public static final String ADD_NUMBERS5_ADDNUMBERS_ACTION = "fault5";

  public static final String ADD_NUMBERS6_ADDNUMBERS_ACTION = "http://example.com/AddNumbersPortType/addNumbers6/Fault/add6Fault";
}
