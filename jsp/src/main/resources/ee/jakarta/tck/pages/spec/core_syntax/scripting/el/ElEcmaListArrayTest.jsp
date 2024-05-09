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
<%@ page import="java.util.ArrayList,
                 java.util.List"%>
<%@ taglib uri="http://java.sun.com/tck/jsp/el" prefix="el" %>

<%
    List list = new ArrayList();
    list.add("element1");
    list.add("element2");
    String[] sArray = new String[] { "value1", "value2" };
    pageContext.setAttribute("tckArray", sArray);
    pageContext.setAttribute("tckList", list);
%>

<%-- validate failure of expr-b coersion to int --%>

<% try { %>
    ${tckList['asdf']}
    Test FAILED.  Expression 'tckList['asdf']' should not have been evaluated.
<% } catch (Throwable t) { } %>

<% try { %>
   ${tckArray['asdf']}
   Test FAILED.  Expression 'tckArray['asdf']' should not have been evaluated.
<% } catch (Throwable t) { } %>


<%-- Validate return values --%>
<el:checkEcma name="Array ([])" control='<%= null %>' object="${tckArray['20']}">
    <el:checkEcma name="List ([])" control='<%= null %>' object="${tckList['20']}">
        <el:checkEcma name="Array ([])" control='<%= "value2" %>' object="${tckArray['1']}">
            <el:checkEcma name="List ([])" control='<%= "element1" %>' object="$tckList['0']}"
                          display="true"/>
        </el:checkEcma>
    </el:checkEcma>
</el:checkEcma>
