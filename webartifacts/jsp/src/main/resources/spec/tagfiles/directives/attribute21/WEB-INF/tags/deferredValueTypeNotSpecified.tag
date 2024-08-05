<%--

    Copyright (c) 2006, 2020 Oracle and/or its affiliates. All rights reserved.

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

<%@ tag import="jakarta.el.ELContext" %>
<%@ tag import="jakarta.el.ValueExpression" %>

<%@ attribute name="x" deferredValue="true" %>
<%
    ELContext elContext = jspContext.getELContext();
    ValueExpression vexp =
        elContext.getVariableMapper().resolveVariable("x");
    Object o = vexp.getValue(elContext);
    out.println("Test PASSED.");
%>
