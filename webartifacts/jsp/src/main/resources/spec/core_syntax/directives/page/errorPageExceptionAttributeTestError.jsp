<%--

    Copyright (c) 2004, 2020 Oracle and/or its affiliates. All rights reserved.

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
<%@ page isErrorPage="true"  %>

<%
  Object jspException = request.getAttribute("jakarta.servlet.jsp.jspException");
  Object error_exception = 
    request.getAttribute("jakarta.servlet.error.exception");
  if(jspException == null) { 
    out.println("Test FAILED. jakarta.servlet.jsp.jspException is null");
  } else if(error_exception == null) {
    out.println("Test FAILED. jakarta.servlet.error.exception is null");
  } else if(error_exception != jspException) {
    out.println("Test FAILED. jakarta.servlet.error.exception and jakarta.servlet.jsp.jspException are not the same");
  } else {
    out.println("Test PASSED. jakarta.servlet.error.exception and jakarta.servlet.jsp.jspException are the same");
    out.println(jspException.toString());
  }
%>
