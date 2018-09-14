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
<tck:test testName="negativeRedirectContextUrlInvalidValueTest">

    <!-- If the context attribute is provided an invalid
             value, i.e, a context without a leading slash, an
             Exception is thrown.  Additionally, if context
             is specified and the path specified doesn't begin
             with a leading slash, an Exception is thrown. -->
    Context value specified without leading slash:<br>
    <c:catch var="rictx">
        <c:redirect url="/test" context="invalid"/>
    </c:catch>
    <% 
        Object o = pageContext.getAttribute("rictx");
        if (o != null) {
            if (o instanceof Throwable) {
                out.println("Invalid value provided to context attribute, and Exception was thrown.  Test PASSED!<br>");
            } else {
                out.println("[Error]: Var was exported but was not an instance of Throwable!<br>");
            }
        } else {
            out.println("[Error]: No Exception thrown!<br>");
        }
    %>
    Context specified relative path without leading slash:<br>
    <c:catch var="rival">
        <c:redirect url="test" context="/jstltck-fmt"/>
    </c:catch>
    <% 
        o = pageContext.getAttribute("rival");
        if (o != null) {
            if (o instanceof Throwable) {
                out.println("Invalid value provided to value attribute, and Exception was thrown.  Test PASSED!<br>");
            } else {
                out.println("[Error]: Var was exported but was not an instance of Throwable!<br>");
            }
        } else {
            out.println("[Error]: No Exception thrown!<br>");
        }
    %>
</tck:test>
