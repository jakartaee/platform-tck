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

<%
    if (application.getAttribute("app") != null) {
        out.println("Application: Available<br>");
    } else {
        out.println("Application: Not Available<br>");
    }
    if (session.getAttribute("sess") != null) {
        out.println("Session: Avaliable<br>");
    } else {
        out.println("Session: Not Available<br>");
    }
    if (request.getAttribute("req") != null) {
        out.println("Request: Available<br>");
    } else {
        out.println("Request: Not Available<br>");
    }
    if (request.getParameter("reqpar") != null) {
        out.println("Request Param: Available<br>");
    } else {
        out.println("Request Param: Not Available<br>");
    }
%>
