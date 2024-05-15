<%--

    Copyright (c) 2006, 2024 Oracle and/or its affiliates. All rights reserved.

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
 @(#)ejb_to_ejb.jsp	1.10 06/02/19
-->

<%@ page language="java" %>
<%@ page import="javax.naming.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="javax.sql.*" %>
<%@ page import="com.sun.ts.lib.util.*" %>
<%@ page import="com.sun.ts.lib.porting.*" %>
<%@ page import="com.sun.ts.tests.integration.sec.propagation.*" %>

<html>
<head><title>Ejb To Ejb Auth Test</title></head>
<body>
 
<%
    TSNamingContextInterface nctx = null;

    String testLookup1 = "java:comp/env/ejb/Bean1";

    Bean1       bean1Ref  = null;

    String web = null;
    String ejb1 = null;
    String ejb2 = null;

    if( request.getUserPrincipal() != null ) {
	web = request.getUserPrincipal().getName();
    }

    try {
        nctx = new TSNamingContext();

	bean1Ref = (Bean1)nctx.lookup( testLookup1, Bean1.class );
	ejb1 = bean1Ref.getCallerPrincipalName();
	ejb2 = bean1Ref.getPropagatedPrincipalName();

	out.println( web );
	out.println( "<BR>" );
	out.println( ejb1 );
	out.println( "<BR>" );
	out.println( ejb2 );
	out.println( "<BR>" );
    }
    catch( Exception e ) {
	e.printStackTrace();
	out.println( e.getMessage() );
    }

%>

</body>
</html>
