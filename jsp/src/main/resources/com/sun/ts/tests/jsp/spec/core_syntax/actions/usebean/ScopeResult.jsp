<%--

    Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.

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

<%@ page contentType="text/plain;charset=ISO-8859-1" %>
<%--
    Simple JSP to valid scoped objects created with jsp:useBean
--%>

<%! 
     public void sendPass( HttpServletResponse resp ) {
        resp.addHeader( "status", "Test Status=PASSED" );
        return;
    }
%>

<%!
    public void sendFail( HttpServletResponse resp, String msg ) {
        resp.addHeader( "status", "Test Status=FAILED - " + msg );
        return;
    }
%>

<%
    String _scope = request.getParameter( "scope" );
    String _objId = request.getParameter( "objId" );
   

    if ( _scope.equals( "application" ) ) {
        if ( ( application.getAttribute( _objId ) ) != null ) {
            sendPass( response );
        } else {
            sendFail( response, "Application scoped object not found in ServletContext" );
        }
    }

    if ( _scope.equals( "session" ) ) {
        if ( ( session.getAttribute( _objId ) ) != null ) {
            sendPass( response );
        } else {
            sendFail( response, "Session scoped object not found in HttpSession" );
        }
    }

    if ( _scope.equals( "request" ) ) {
        if ( ( request.getAttribute( _objId ) ) != null ) {
            sendPass( response );
        } else {
            sendFail( response, "Request scoped object not found in HttpServletResponse" );
        }
    }

    if ( _scope.equals( "page" ) ) {
        if ( ( pageContext.getAttribute( _objId ) ) == null ) {
            sendPass( response );
        } else {
            sendFail( response, "Page scoped object found in current page and should not be" );
        }
    }
%>

