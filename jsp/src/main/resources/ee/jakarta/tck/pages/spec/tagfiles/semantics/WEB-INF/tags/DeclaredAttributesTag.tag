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

<%@ attribute name="attr1" required="true" %>
<%@ attribute name="attr2" required="false" %>

<%
    String attr1Value = (String) jspContext.getAttribute("attr1");
    String attr2Value = (String) jspContext.getAttribute("attr2");
    if ("attrValue".equals(attr1Value)) {
        if ("attr2Value".equals(attr2Value)) {
            out.println("Test PASSED");
        } else {
            out.println("Test FAILED.  Expected the value of attr2 to be 'attrValue'." +
                "  Received: " + attr2Value);
        }
    } else {
        out.println("Test FAILED.  Expected the value of attr1 to be 'attrValue'." +
            "  Received: " + attr1Value);
    }
%>
