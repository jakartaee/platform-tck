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
<tck:test testName="positiveUrlRelUrlRewrittenTest">

    <!-- The url action will rewrite, where appropriate,
             all relative URLs.  Since the test client doesn't
             accept cookies, and all JSP pages by default participate
             in a session, the resulting URL should be encoded. -->
    <c:url value="/encoded/test.jsp" var="rRes"/>
    <%
        String url = (String) pageContext.getAttribute("rRes");
        if (url.toLowerCase().indexOf("jsessionid") > -1) {
            out.println("Context-relative path properly rewritten with session information.<br>");
        } else {
            out.println("[ERROR]:  URL not rewritten: " + url + "<br>");
        }
    %>
    Page-relative path:<br>
    <c:url value="test.jsp" var="eRes"/>
    <%
        url = (String) pageContext.getAttribute("eRes");
        if (url.toLowerCase().indexOf("jsessionid") > -1) {
            out.println("Page-relative path properly rewritten with session information.<br>");
        } else {
            out.println("[ERROR]:  URL not rewritten: " + url + "<br>");
        }
    %><br>
    
</tck:test>
