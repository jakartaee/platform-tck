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
 @(#)webApiRemoteUser1.jsp	1.4 06/02/19
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

    // web_api_remoteuser: Assertions:
    //
    // 1. If getRemoteUser returns null (which means that no
    // user has been authenticated), the isUserInRole method will always
    // return false and getUserPrincipal() will always return null.
    //
    // 2. The getRemoteUser() method returns the user name that the client
    // authenticated with.
    //
    // The first assertion can be checked here.  We will assume
    // web_not_in_role passed, which means isUserInRole returned false for all
    // roles.  Also, we will assume that web_no_authz passed, which means
    // that getUserPrincipal() returns null.  Therefore, we need only show
    // that getRemoteUser() returns null when the user is unauthenticated,
    // as is the case here.
    String testName = "test_web_api_remoteuser_1";
    try {

        String remoteUser = request.getRemoteUser();
        out.println( testName + ": getRemoteUser(): " + remoteUser );

        if( remoteUser != null ) {
            out.println( testName + ": " + FAILSTRING +
                " - expecting null" );
            fail = true;
        }

        if( !fail ) {
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
