<%--

    Copyright (c) 2003, 2018 Oracle and/or its affiliates. All rights reserved.

    This program and the accompanying materials are made available under the
    terms of the Eclipse Public License v. 2.0, which is available at
    http://www.eclipse.org/legal/epl-2.0.

    This Source Code may also be made available under the following Secondary
    Licenses when the conditions for such availability set forth in the
    Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
    version 2 with the GNU Classpath Exception, which is available at
    https://www.gnu.org/software/classpath/license.html.

    SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0

--%>

<%@ page import="com.sun.ts.tests.jsp.common.util.JspTestUtil"%>

<%@ page contentType="text/plain" %>
<%
    String[] param1Values = request.getParameterValues("param1");
    String[] param2Values = request.getParameterValues("param2");

    if (param1Values != null) {
        if (param2Values != null) {
            if (param1Values.length == 2) {
                if (param2Values.length == 1) {
                    if ("value2".equals(param1Values[0])
                        && "value1".equals(param1Values[1])) {
                        if ("value2".equals(param2Values[0])) {
                            out.println("Test PASSED");
                        } else {
                            out.println("Test FAILED.  Expected the value of param2 to be value2." +
                                "  Value(s) received: " + JspTestUtil.getAsString(param2Values));
                        }
                    } else {
                        out.println("Test FAILED.  Expected the first value of param1 to be 'value2'" +
                            " and the second value to be 'value1'.  Values as received: " +
                            JspTestUtil.getAsString(param1Values));
                    }
                } else {
                    out.println("Test FAILED.  Expected one value for param2: value1." +
                        "  Actual param values received: " +
                        JspTestUtil.getAsString(param2Values));
                }
            } else {
                out.println("Test FAILED.  Expected two values for param1: value2, value1." +
                    "  Actual param values received: " +
                    JspTestUtil.getAsString(param1Values));
            }
        } else {
            out.println("Test FAILED.  Request parameter 'param2' not available.");
        }
    } else {
        out.println("Test FAILED.  Request parameter 'param1' not available.");
    }

%>
