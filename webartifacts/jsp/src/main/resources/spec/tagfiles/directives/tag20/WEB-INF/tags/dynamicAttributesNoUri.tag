<%--

    Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.

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

<%@ tag import="java.util.Map" %>
<%@ tag dynamic-attributes="ds" %>
<%
    Map map = (Map) jspContext.getAttribute("ds");
    out.println(map.get("d1"));
    if(map.containsKey("d2") || map.containsKey("my:d2")) {
        out.println("Test FAILED. dynamic attributes with uri should not be placed into map.");
    } else {
        out.println("Dynamic attributes with uri are not placed into map.");
    }
%>
