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

<%@ page import="java.io.Reader"%>

<%@ page contentType="text/plain" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>

<tags:JspAttributeTag>
    <jsp:attribute name="attr1">
        attribute value
    </jsp:attribute>
</tags:JspAttributeTag>

<%
    Object o = pageContext.getAttribute("requestVar", PageContext.REQUEST_SCOPE);
    if (o != null) {
        if (o.equals("attribute value")) {
            o = pageContext.getAttribute("requestVarReader", PageContext.REQUEST_SCOPE);
            if (o != null) {
                Reader r = (Reader) o;
                StringBuffer sb = new StringBuffer();
                for (int read = r.read(); read != -1; read = r.read()) {
                    sb.append((char) read);
                }
                if (sb.toString().equals("attribute value")) {
                    out.println("Test PASSED");
                } else {
                    out.println("Test FAILED.  The exported requestVarReader contained " +
                        "incorrrect data.  Expected 'attribute value', received " +
                        o);
                }
            } else {
                out.println("Test FAILED.  Unable to find the requestVarReader attribute");
            }
        } else {
            out.println("Test FAILED.  Expected the exported variable requestVar was" +
                " incorrect.  Expected the value to be 'attribute value', received: " +
                o);
        }
    } else {
        out.println("Test FAILED.  Unable to find the requestVar attribute");
    }

%>
