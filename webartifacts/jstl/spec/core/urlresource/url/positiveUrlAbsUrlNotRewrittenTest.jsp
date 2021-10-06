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
<tck:test testName="positiveUrlAbsUrlNotRewrittenTest">
    <tck:localAbsUrl var="url" path="/jstltck-core/non.jsp"/>
    <!-- If the provided URL is an absolute URL, no
              URL rewritting will occur. -->
    No JSESSIONID information should be present in the rewritten URL:<br>
    <c:url value='<%= (String) pageContext.getAttribute("url") %>' var="rUrl"/>
    <%
        String url1 = (String) pageContext.getAttribute("rUrl");
        if(url1.toLowerCase().indexOf("jsessionid") < 0 ) {
            out.println("URL not rewritten.  Test PASSED!<br>");
        } else {
            out.println("URL incorrectly rewritten: " + url1 + " <br>");
        }
    %>
</tck:test>
