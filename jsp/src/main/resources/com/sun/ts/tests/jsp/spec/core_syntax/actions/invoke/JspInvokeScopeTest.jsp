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

<%@ page import="com.sun.ts.tests.jsp.common.util.JspTestUtil,
                 java.io.Reader"%>
<%@ page contentType="text/plain" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>

<tags:ScopeTag>
    <jsp:attribute name="attr1">
        attribute value
    </jsp:attribute>
</tags:ScopeTag>

<%
    final int[] scopes = {
        PageContext.REQUEST_SCOPE,
        PageContext.SESSION_SCOPE,
        PageContext.APPLICATION_SCOPE
    };

    final String[] attrNames = {
        "requestVar", "sessionVar", "applicationVar"
    };

    for (int i = 0; i < scopes.length; i++) {
        Object o = pageContext.getAttribute(attrNames[i], scopes[i]);
        if (o != null) {
            if (o instanceof Reader) {
                Reader r = (Reader) o;
                StringBuffer sb = new StringBuffer();
                for (int read = r.read(); read != -1; read = r.read()) {
                    sb.append((char) read);
                }
                o = sb.toString();
            }

            if (o.equals("attribute value")) {
                out.println("Test PASSED");
            } else {
                out.println("Test FAILED.  Attribute found in the " +
                    JspTestUtil.getScopeName(scopes[i]) + ", but the value" +
                    " of the attribute was not 'attribute value'.  Received" +
                    ": " + o);
            }
        } else {
            out.println("Test FAILED.  Unable to find attribute '" +
                attrNames[i] + "' in the " + JspTestUtil.getScopeName(scopes[i]) +
                " scope.");
        }
    }

%>
