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

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<tck:test testName="positiveFNCodeSymbolTest">
    <tck:checkRuntime var="is14"/>
    <fmt:setLocale value="en_US"/>

    <!--  If currencyCode and currencySymbol are both defined and
              the container's runtime is 1.4 or greater, currencyCode
              takes precedence over currencySymbol, otherwise, 
              currencySymbol takes precedence over currencyCode. -->
    <fmt:formatNumber value="1234" var="rtRes" type="currency" currencyCode="USD" currencySymbol="@"/>
    <c:choose>
        <c:when test="${is14}">
            <%
                String val = (String) pageContext.getAttribute("rtRes");
                if (val.equals("$1,234.00")) {
                    out.println("Value properly formatted as a currency.");
                } else {
                    out.println("Value not properly formatted as a currency.");
                    out.println("Expected: $1,234.00");
                    out.println("Received: " + val);
                }
            %>
        </c:when>
        <c:otherwise>
            <%
                String val = (String) pageContext.getAttribute("rtRes");
                if (val.equals("@1,234.00")) {
                    out.println("Value properly formatted as a currency.");
                } else {
                    out.println("Value not properly formatted as a currency.");
                    out.println("Expected: @1,234.00");
                    out.println("Received: " + val);
                }
            %>
        </c:otherwise>
    </c:choose>        
</tck:test>
