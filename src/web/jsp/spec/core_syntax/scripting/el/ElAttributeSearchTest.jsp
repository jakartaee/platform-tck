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

<%@ page contentType="text/plain" %>
<%@ taglib uri="http://java.sun.com/tck/jsp/el" prefix="el" %>

<%
    pageContext.setAttribute("pageScoped", "page");
    pageContext.setAttribute("requestScoped", "request", PageContext.REQUEST_SCOPE);
    pageContext.setAttribute("sessionScoped", "session", PageContext.SESSION_SCOPE);
    pageContext.setAttribute("applicationScoped", "application", PageContext.APPLICATION_SCOPE);
%>

<el:checkLiteral name="PageScope" control='<%= "page" %>' object="${pageScoped}">
 <el:checkLiteral name="RequestScope" control='<%= "request" %>' object="${requestScoped}">
  <el:checkLiteral name="SessionScope" control='<%= "session" %>' object="${sessionScoped}">
   <el:checkLiteral name="ApplicationScope" control='<%= "application" %>' object="${applicationScoped}">
    <el:checkLiteral name="Null" control='<%= null %>' object="${nonExistingObject}"
                    display="true"/>
   </el:checkLiteral>
  </el:checkLiteral>
 </el:checkLiteral>
</el:checkLiteral>
