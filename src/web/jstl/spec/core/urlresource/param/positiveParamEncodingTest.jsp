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
<tck:test testName="positiveParamEncodingTest">

    <!-- param names and values should be URL encoded if necessary -->
    <c:url var="rUrl" value="/jstltck-core/jstl">
        <c:param name="test" value="received 5"/>
        <c:param name="new test">
            0=0
        </c:param>
    </c:url>
    <%
        String url = (String) pageContext.getAttribute("rUrl");
        if (url.toLowerCase().indexOf("received+5") > -1 && 
            url.toLowerCase().indexOf("0%3d0") > -1) {
            out.println("Params properly encoded<br>");
        } else {
            out.println("Params not encoded!<br>\n" +
                        "url -> " + url + "<br>");
        }
    %>
</tck:test>
