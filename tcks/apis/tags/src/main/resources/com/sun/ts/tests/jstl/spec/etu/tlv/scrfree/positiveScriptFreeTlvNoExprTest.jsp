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

<%@ taglib prefix="c_rt" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/noexpr" %>
<tck:test testName="positiveScriptFreeTlvNoExprTest">
    <!-- Use a taglibrary with allowExpressions set to false.
         and all other properties, allowDeclarations, allowRTExpressions,
         and allowScriptlets are set to true.  Using declarations, rtexpressios,
         and scriptlets should not generate a translation error. -->
    <%
        out.println("Output from a scriptlet.<br>");
    %>
    <%! int i = 0; %>
    <c_rt:out value='<%= new String("Output from an RTExpression") %>'/>
</tck:test>
