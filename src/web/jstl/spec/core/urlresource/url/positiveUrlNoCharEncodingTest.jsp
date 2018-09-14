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
<tck:test testName="positiveUrlNoCharEncodingTest">
    <c:set var="eUrl" value="/jstl_core_web/jstl?foo=test this&foo1=5%&foo2='5=5'"/>
    <!-- No character encoding should be performed by the action -->
    <c:url var="rVar" value='<%= (String) pageContext.getAttribute("eUrl") %>'/>
    <%
        String expVal = (String) pageContext.getAttribute("eUrl");
        String retVal = (String) pageContext.getAttribute("rVar");
        expVal = expVal.substring(expVal.indexOf('?'));
        retVal = retVal.substring(retVal.indexOf('?'));
        if (expVal.equals(retVal)) {
            out.println("No character encoding occurred.<br>");
        } else {
            out.println("Character encoding occurred!<br>\n" +
                        "expected: " + expVal + "<br>\n" +
                        "returned: " + retVal + "<br>");
        }
    %>
</tck:test>
