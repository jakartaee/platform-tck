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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<tck:test testName="positiveUrlParamsBodyTest">

    <!-- Validate the action can properly handled nested param subtags -->
    <c:url var="rUrl" value="/jstl">
        <c:param name="parm1" value="value1"/>
    </c:url>
    <%
        String retVal = (String) pageContext.getAttribute("rUrl");
        if (retVal.indexOf("parm1=value1") > -1) {
            out.println("Expected parameter found. Test PASSED!<br>");
        } else {
            out.println("Test FAILED!<br>\n" +
                      "Unable to find request parameter 'parm1'<br>\n" +
                      "Returned value: " + retVal + "<br>");
        }
    %>
    <c:url var="rcUrl" value="/jstl" context="/jstltck-core">
        <c:param name="parm1" value="value1"/>
    </c:url>
    <%
        retVal = (String) pageContext.getAttribute("rcUrl");
        if (retVal.indexOf("parm1=value1") > -1) {
            out.println("Expected parameter found. Test PASSED!<br>");
        } else {
            out.println("Test FAILED!<br>\n" +
                      "Unable to find request parameter 'parm1'<br>\n" +
                      "Returned value: " + retVal + "<br>");
        }
    %>
</tck:test>
