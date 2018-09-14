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

<%@ tag dynamic-attributes="dynMap" import="java.util.Map" %>

<%
    String attr1Value = (String) ((Map) jspContext.getAttribute("dynMap")).get("attr1");
    if ("attr1Value".equals(attr1Value)) {
        out.println("Test PASSED");
    } else {
        out.println("Test FAILED.  A page scoped variable was not created" +
            " or was the incorrect value for the dynamic attribute passed" +
            " in at invocation time.  Expected: attr1Value, received: " + attr1Value);
    }
%>
