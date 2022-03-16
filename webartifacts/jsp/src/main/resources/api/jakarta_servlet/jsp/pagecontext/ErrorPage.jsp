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

<%@ page contentType="text/plain" isErrorPage="true"%>

<%
    if (request.getAttribute("com.sun.ts.tests.jsp.api.checkException") != null) {
        Exception e = pageContext.getException();
        if (e != null) {
            if (e == exception) {
                out.println("Test PASSED (getException)");
            } else {
                out.println("Test FAILED.  The Exception as returned by " +
                    "PageContext.getException is not the same as the " +
                    "exception scripting variable.");
            }
        } else {
            out.println("Test FAILED.  PageContext.getException() returned null.");
        }
    } else {
        out.println("Test PASSED.  Error page invoked.");
        out.println(exception.getClass().getName());
        out.println(exception.getMessage());
        if (request.getAttribute("com.sun.ts.tests.jsp.api.checkErrorData") != null) {
            ErrorData data = pageContext.getErrorData();
            if (data != null) {
                out.println("ErrorData object obtained.");
            }
        }
    }
%>

