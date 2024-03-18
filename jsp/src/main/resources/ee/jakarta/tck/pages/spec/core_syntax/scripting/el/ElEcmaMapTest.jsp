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

<%@ page contentType="text/plain" %>
<%@ page import="java.util.HashMap,
                 java.util.Map"%>
<%@ taglib uri="http://java.sun.com/tck/jsp/el" prefix="el" %>

<%
    Map map = new HashMap();
    map.put("key1", "value1");
    pageContext.setAttribute("tckMap", map);
%>

<el:checkEcma name="Map key (.)" control='<%= null %>' object="${tckMap.nonKey}">
    <el:checkEcma name="Map key (.)" control='<%= "value1" %>' object="${tckMap.key1}">
        <el:checkEcma name="Map key ([])" control='<%= null %>' object="${tckMap['nonkey']}">
            <el:checkEcma name="Map key ([])" control='<%= "value1" %>' object="${tckMap['key1']}"
                display="true"/>
        </el:checkEcma>
    </el:checkEcma>
</el:checkEcma>
