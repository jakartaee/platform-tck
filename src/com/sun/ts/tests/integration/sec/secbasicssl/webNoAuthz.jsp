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
 @(#)webNoAuthz.jsp	1.4 06/02/19
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

    // web_no_authz: User must be granted access to an unprotected web
    // resource independent of authentication status.  
    //
    //    If they are unauthenticated, they must not be forced to 
    //    authenticate.  They must not be denied access.
    //
    // We can test (1) here, as webNoAuthz.jsp is an unprotected web resource,
    // and the user is not yet authenticated.  If the user was forced to
    // authenticate, then the user principal cannot be null and the test
    // will fail.  If they were denied access, they cannot start the test
    // and therefore they cannot pass it.
    //
    //
    String testName = "test_web_no_authz";
    try {
	java.security.Principal userPrincipal = request.getUserPrincipal();
	String userPrincipalName = null;
	if( userPrincipal != null ) {
	    userPrincipalName = userPrincipal.getName();
	}

        out.println( testName + ": getUserPrincipal(): " + userPrincipalName );

	if( userPrincipal != null ) {
            out.println( testName + ": " + FAILSTRING +
                " - expecting null." );
	    fail = true;
	}

	if( !fail ) 
	    out.println( testName + ": " + PASSSTRING );
    else
	    out.println( testName + ": " + FAILSTRING );
	
    }
    catch( Exception e ) {
	out.println( testName + ": " + FAILSTRING + 
	    " - Exception: " + e.getMessage() );
    }

%>
</body>
</html>
