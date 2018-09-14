<%--

    Copyright (c) 2004, 2018 Oracle and/or its affiliates. All rights reserved.

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
  Object jspException = request.getAttribute("javax.servlet.jsp.jspException");
  Object error_exception = 
    request.getAttribute("javax.servlet.error.exception");
  if(jspException == null) { 
    out.println("Test FAILED. javax.servlet.jsp.jspException is null");
  } else if(error_exception == null) {
    out.println("Test FAILED. javax.servlet.error.exception is null");
  } else if(error_exception != jspException) {
    out.println("Test FAILED. javax.servlet.error.exception and javax.servlet.jsp.jspException are not the same");
  } else {
    out.println("Test PASSED. javax.servlet.error.exception and javax.servlet.jsp.jspException are the same");
    out.println(jspException.toString());
  }
%>
