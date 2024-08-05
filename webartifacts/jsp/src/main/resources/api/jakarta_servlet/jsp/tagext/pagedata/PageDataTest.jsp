<%--

    Copyright (c) 2003, 2020 Oracle and/or its affiliates. All rights reserved.

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

<%@ page contentType="text/plain"
         import="com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.tagext.pagedata.PageDataValidator" %>
<%@ taglib uri="http://java.sun.com/tck/jsp/pagedata" prefix="pagedata"  %>
<%!
    public boolean validatorWasCalled() {
        boolean result = PageDataValidator.wasCalled();
        PageDataValidator.reset();
        return result;
    }
%>
<pagedata:test test="PageData" dynAttribute='<%= "dynamic" %>' />
<%
    if (validatorWasCalled()) {
        out.println("Validator Called.");
    } else {
        out.println("Test FAILED.  Validator wasn't called.");
    }
%>
<%= "Expression Text" %>
<%@ include file="template.txt"  %>
