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

<%@ page import="java.io.PrintWriter,
                 com.sun.ts.tests.jsp.common.util.JspTestUtil"%>

<%@ page contentType="text/plain" %>
<%@ taglib uri="http://java.sun.com/tck/jsp/simpletagsupport" prefix="simtag" %>
<%@ taglib tagdir="/WEB-INF/tags/simpletagsupport" prefix="tagfile" %>

<%--
    Sync variables declared by TEI.
    SimpleTag instance will set page context attributes
    begin to 1 and end to 2.  This should be synchronized
    to scripting variables begin and end respectively.
--%>
<simtag:sync />
<%
    if (begin.intValue() != 1) {
        out.println("Test FAILED.  Expected begin to be '1' after tag " +
            "completion.  Actual value: " + begin);
    }
    if (end.intValue() != 2) {
        out.println("Test FAILED.  Expected end to be '2' after tag " +
            "completion.  Actual value: " + end);
    }
%>

<%--
    Sync variables declared by variable elements in TLD
    SimpleTag instance will set page context attributes
    begin to 2 and end to 3.  This should be synchronized
    to scripting variables begin and end respectively.
--%>
<simtag:sync1 />
<%
    if (begin.intValue() != 2) {
        out.println("Test FAILED.  Expected begin to be '2' after tag " +
            "completion.  Actual value: " + begin);
    }
    if (end.intValue() != 3) {
        out.println("Test FAILED.  Expected end to be '3' after tag " +
            "completion.  Actual value: " + end);
    }
%>

<%-- Sync variables declared in Tag file --%>
<%
    // If a page scoped attribute has the same name
    // as a declared NESTED variable in the tag, then
    // the calling page scoped attribute needs to be restored
    // after the invocation of the tag.  No synchronization
    // should occur.
    Integer nested = new Integer(5);
    pageContext.setAttribute("nested", nested);
%>
<tagfile:Sync />
<%
    if (begin.intValue() != 3) {
        out.println("Test FAILED.  Expected begin to be '3' after tag " +
            "completion.  Actual value: " + begin);
    }
    if (end.intValue() != 1) {
        out.println("Test FAILED.  Expected end to be '1' after tag " +
            "completion.  Actual value: " + end);
    }
    if (nested.intValue() != 5) {
        out.println("Test FAILED.  Variable synchronization incorrectly occured" +
            "for a nested variable declared by the Tag file.  The original value" +
            "of '5' should have been restored after doTag() returned.");
    }
%>
