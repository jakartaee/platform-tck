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

<%@ attribute name="attr1" required="true" fragment="true" %>

<jsp:invoke fragment="attr1" var="defaultPage" />
<jsp:invoke fragment="attr1" var="pageVar" scope="page"/>
<jsp:invoke fragment="attr1" varReader="requestVar" scope="request"/>
<jsp:invoke fragment="attr1" var="sessionVar" scope="session"/>
<jsp:invoke fragment="attr1" varReader="applicationVar" scope="application"/>

<%
    Object o = jspContext.getAttribute("defaultPage", PageContext.PAGE_SCOPE);
    if (o != null) {
        if (o.equals("attribute value")) {
            out.println("Test PASSED");
        } else {
            out.println("Test FAILED.  The page scoped attribute, 'defaultPage' " +
                "was found, but had an incorrect value.  Expected 'attribute value'" +
                ", received: " + o);
        }
    } else {
        out.println("Test FAILED.  Unable to find the page scoped attribute '" +
            "defaultPage' in the tag file's page context.");
    }

    o = jspContext.getAttribute("pageVar", PageContext.PAGE_SCOPE);
    if (o != null) {
        if (o.equals("attribute value")) {
            out.println("Test PASSED");
        } else {
            out.println("Test FAILED.  The page scoped attribute, 'pageVar' " +
                "was found, but had an incorrect value.  Expected 'attribute value'" +
                ", received: " + o);
        }
    } else {
        out.println("Test FAILED.  Unable to find the page scoped attribute '" +
            "pageVar' in the tag file's page context.");
    }
%>
