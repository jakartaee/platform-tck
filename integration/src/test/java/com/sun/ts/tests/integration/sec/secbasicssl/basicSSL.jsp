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
 @(#)basicSSL.jsp	1.4 06/02/19
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

    // login_basic_over_ssl: HTTP Basic authentication over SSL is 
    // supported.
    //
    // Check that isSecure() returns true, indicating that this is indeed
    // an SSL session.  Also, check that the request URL begins with https.
    // We cannot do much more than this from here 
    // to guarantee that it is truly SSL, but this is a good failsafe.  
    String testName = "test_login_basic_over_ssl";
    try {
	boolean isSecure = request.isSecure();
        out.println( testName + ": isSecure(): " + isSecure );
	if( !isSecure ) {
            out.println( testName + ": " + FAILSTRING +
                " - expecting SSL connection" );
	    fail = true;
	}

	String requestURL = HttpUtils.getRequestURL( request ).toString();
	boolean isHttps = requestURL.toLowerCase().startsWith( "https" );
	if( !isHttps ) {
            out.println( testName + ": " + FAILSTRING +
                " - must start with https." );

	    fail = true;
	}

	if( !fail ) {
	    out.println( testName + ": " + PASSSTRING );
	}else
	    out.println( testName + ": " + FAILSTRING );
	
    }
    catch( Exception e ) {
	out.println( testName + ": " + FAILSTRING);
	out.println( e.getMessage() );
    }

%>
</body>
</html>
