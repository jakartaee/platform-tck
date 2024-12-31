<%--

    Copyright (c) 2006, 2018 Oracle and/or its affiliates. All rights reserved.

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

<!--
 @(#)webNotInRole.jsp	1.4 06/02/19
-->

<%@ page language="java" %>
<%@ page import="java.util.*" %>

 
<html>
<body>

<% 
    String FAILSTRING = "FAILED"; // must be the same for all jsps.
    String PASSSTRING = "PASSED"; // must be the same for all jsps.

    // Start test.  
    boolean fail = false;

    // web_not_in_role: When isCallerInRole is called from an unprotected web
    // resource, and the caller is not authenticated, isCallerInRole must
    // return false.
    //
    // Both of these preconditions hold true here.  Therefore, this is a good
    // point to perform this test.  Check to see whether the user is in any of
    // these known or unknown roles:
    String testName = "test_web_not_in_role";
    try {

        String[] roles = { "ADM", "MGR", "VP", "EMP" };
        int i;
        for( i = 0; i < roles.length; i++ ) {
            String role = roles[i];
            boolean result = request.isUserInRole( role );
            out.println( testName + ": isRole" + role + ": " + result );

            if( result ) fail = true;
        }

        if( fail ) {
            out.println( testName + ": " + FAILSTRING +
                " - all must be false." );
        }
        else {
            out.println( testName + ": " + PASSSTRING );
        }
    }
    catch( Exception e ) {
        out.println( testName + ": " + FAILSTRING +
            " - Exception: " + e.getMessage() );
    }

%>
</body>
</html>
