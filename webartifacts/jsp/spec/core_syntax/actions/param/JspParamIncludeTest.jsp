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

<%--
     validate the interaction of jsp:include with nested jsp:param elements.
--%>

<jsp:include page="Resource.jsp">
    <jsp:param name="param1" value="value2" />
    <jsp:param name="param2" value="value2" />
</jsp:include>

<%-- Validate that param1 value2 and param2 are no longer present in the request --%>
<%
    String[] param1Values = request.getParameterValues("param1");
    String[] param2Values = request.getParameterValues("param2");

    if (param1Values.length == 1) {
      if (param2Values == null) {
        if ("value1".equals(param1Values[0])) {
            out.println("Test PASSED");
        } else {
            out.println("Test FAILED.  Expected the value of param1 to be 'param2'" +
                " after the include completed.  Actual: " + param1Values[0]);
        }
      } else {
          out.println("Test FAILED.  The parameter, param2, should not exist in" +
              "the request after the included completed.  However, values were found:" +
              " " + JspTestUtil.getAsString(param2Values));
      }
    } else {
        out.println("Test FAILED.  Expected param1 to only have 1 value.  The " +
            "actual number is " + param1Values.length);
    }

%>
