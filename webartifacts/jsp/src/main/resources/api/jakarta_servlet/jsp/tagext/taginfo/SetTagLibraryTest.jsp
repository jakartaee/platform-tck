<%--

    Copyright (c) 2004, 2018 Oracle and/or its affiliates. All rights reserved.

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

<%@ page import="com.sun.ts.tests.jsp.common.util.BaseTCKExtraInfo"%>

<%@ page contentType="text/plain" %>
<%@ taglib uri="http://java.sun.com/tck/jsp/taginfo" prefix="taginfo" %>
<taginfo:test test="setTagLibraryTest" />
<%
    if (BaseTCKExtraInfo.teiWasCalled()) {
        out.println("Test PASSED.");
    } else {
        out.println("Test FAILED.  Configured TEI was not called by container.");
    }
    BaseTCKExtraInfo.reset();
%>
