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
 @(#)web_to_ejb_noauth.jsp	1.9 06/02/19
-->

<%@ page language="java" %>
<%@ page import="javax.naming.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="javax.sql.*" %>
<%@ page import="com.sun.ts.lib.util.*" %>
<%@ page import="com.sun.ts.lib.porting.*" %>
<%@ page import="com.sun.ts.tests.integration.sec.propagation.*" %>

<html>
<head><title>Web To Ejb Noauth Test</title></head>
<body>
 
<%
    TSNamingContextInterface nctx = null;

    String testLookup1 = "java:comp/env/ejb/Bean1";

    Bean1Home   bean1Home = null;
    Bean1       bean1Ref  = null;

    String web1 = null;
    String ejb = null;
    String web2 = null;

    if( request.getUserPrincipal() != null ) {
	web1 = request.getUserPrincipal().getName();
    }

    try {
        nctx = new TSNamingContext();

	    bean1Home = (Bean1Home)nctx.lookup( testLookup1, Bean1Home.class );
    	bean1Ref = bean1Home.create();
        if ( bean1Ref.getCallerPrincipalName() != null )
	        ejb = bean1Ref.Test();
	    bean1Ref.remove();

	    if( request.getUserPrincipal() != null ) {
	        web2 = request.getUserPrincipal().getName();
	    }
    } catch( Exception e ) {
        e.printStackTrace();
        out.println( e.getMessage() );
    } finally {
	    out.println( "web1: " + web1 );
	    out.println( "<BR>" );
	    out.println( "ejb: " + ejb );
	    out.println( "<BR>" );
	    out.println( "web2: " + web2 );
	    out.println( "<BR>" );
    }
%>

</body>
</html>
