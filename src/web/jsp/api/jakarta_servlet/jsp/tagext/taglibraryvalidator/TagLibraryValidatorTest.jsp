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

<%@ page import="com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.taglibraryvalidator.APIValidator"%>
<%@ page contentType="text/plain" %>

<%@ taglib uri="http://java.sun.com/tck/jsp/tlv" prefix="tlv1" %>
<%@ taglib uri="http://java.sun.com/tck/jsp/tlv" prefix="tlv2" %>

<%
    int count = APIValidator.callCount;
    if (count != 1) {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
            "Test FAILED.  The TagLibraryValidator was called " + count +
            " time(s) for the URI 'http://java.sun.com/tck/jsp/tlv', expected a count of 1");
    } 
%>
Test PASSED.  Validator call count was 1.
