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

<%@ tag import="com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.pagedata.TagFilePageDataValidator" %>
<%@ attribute name="att1" %>
<%@ variable name-given="var1" %>
<%@ taglib uri="http://java.sun.com/tck/jsp/pagedatatagfile" prefix="pagedatatagfile"  %>
<%!
    public boolean validatorWasCalled() {
        boolean result = TagFilePageDataValidator.wasCalled();
        TagFilePageDataValidator.reset();
        return result;
    }
%>
<pagedatatagfile:test test="TagFilePageData" dynAttribute='<%= "dynamic" %>' />
<%
    if (validatorWasCalled()) {
        out.println("Validator Called.");
    } else {
        out.println("Test FAILED.  Validator wasn't called.");
    }
%>
<%= "Expression Text in tag file" %>
<%@ include file="template.txt"  %>
