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
 @(#)rolereverse.jsp	1.19 06/02/19
-->

<%@ page language="java" %>
<%@ page import="java.util.*" %>

<html>
<body>

<% 

    String FAILSTRING = "FAILED!"; // must be the same for all jsps.
    String PASSSTRING = "PASSED!"; // must be the same for all jsps.

    // web_roleref_scope[2]: Assertions:
    //
    // Given two servlets in the same application, each of which calls 
    // isUserInRole( X ), and where X is linked to different roles in the
    // scope of each of the servlets (i.e. R1 for servlet 1 and R2 for
    // servlet 2), then a user whose identity is mapped to R1 but not R2
    // shall get a true return value from isUserInRole( X ) in servlet 1, 
    // and a false return value from servlet 2.
    //
    // Consider rolereverse.jsp the second servlet.  This test will call
    // isUserInRole to determine if we are in the correct roles.
    String testName = "test_web_roleref_scope_2";
    try {
	boolean fail = false;

	// Expected results:
	// isUserInRole( "ADM" ): false
	// isUserInRole( "MGR" ): true
	// isUserInRole( "VP" ): false
	// isUserInRole( "EMP" ): true
	boolean isADM = request.isUserInRole( "ADM" );
	boolean isMGR = request.isUserInRole( "MGR" );
	boolean isVP = request.isUserInRole( "VP" );
	boolean isEMP = request.isUserInRole( "EMP" );

	out.println( testName + ": isUserInRole( \"ADM\" ): " + isADM );
	out.println( testName + ": isUserInRole( \"MGR\" ): " + isMGR );
	out.println( testName + ": isUserInRole( \"VP\" ): " + isVP );
	out.println( testName + ": isUserInRole( \"EMP\" ): " + isEMP );

	if( isADM ) {
	    out.println( testName + ": Return value for ADM incorrect." );
	    fail = true;
	}
	if( !isMGR ) {
	    out.println( testName + ": Return value for MGR incorrect." );
	    fail = true;
	}
	if( isVP ) {
	    out.println( testName + ": Return value for VP incorrect." );
	    fail = true;
	}
	if( !isEMP ) {
	    out.println( testName + ": Return value for EMP incorrect." );
	    fail = true;
	}

	if( !fail ) {
	    out.println( testName + ": " + PASSSTRING );
	}
	else {
	    out.println( testName + ": " + FAILSTRING + 
		" - isUserInRole returned incorrect value." );
	}
    }
    catch( Exception e ) {
        out.println( testName + ": " + FAILSTRING + 
	    " - Exception: " + e.getMessage() );
    }
%>

</body>
</html>
