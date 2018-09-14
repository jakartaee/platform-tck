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
 @(#)authorized.jsp	1.23 06/02/19
-->

<%@ page language="java" %>
<%@ page import="java.util.*" %>

<html>
<body>
<% 
    boolean fail = false;
    String FAILSTRING = "FAILED"; // must be the same for all jsps.
    String PASSSTRING = "PASSED"; // must be the same for all jsps.

    // web_api_remoteuser: Assertions:
    //
    // 1. If getRemoteUser returns null (which means that no
    // user has been authenticated), the isUserInRole method will always 
    // return false and getUserPrincipal() will always return null.
    //
    // 2. The getRemoteUser() method returns the user name that the client
    // authenticated with.
    //
    // The second assertion can be checked here.  The user must have 
    // authenticated as j2ee in order to gain access to this page.
    String testName = "test_web_api_remoteuser_2";
    try {
	fail = false;

	// NOTE: Do not modify this line without also modifying Client.java.  
	// See comments below.
	String remoteUser = request.getRemoteUser();
	out.println( "Remote User: "+remoteUser );

        // Note: We cannot test whether getRemoveUser() is correct here
        // because we do not know what the correct principal is.  Instead, we
        // will log "<userName>" and  check it from Client.java, 
	// which can get the correct setting from ts.jte.
       if(remoteUser == null)   {
            out.println( testName + ": " + FAILSTRING +
                " - expecting j2ee"  );
            fail = true;
        }

	if( !fail ) {
	    out.println( testName + ": " + PASSSTRING + 
		" - initial pass only.  Requires additional verification." );
	}    
    }
    catch( Exception e ) {
        out.println( testName + ": " + FAILSTRING +
	    " - Exception: " + e.getMessage() );
    }

%>
</body>
</html>
