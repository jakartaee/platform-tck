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
  @(#)AccessToAll.jsp	1.2  06/02/19
-->

<%@ page language="java" %>
 
<html>
    <head><title>JSP with WildCard Auth Constraint</title></head>
    <body>
        <h2>JSP with WildCard Auth Constraint</h2>

        <% 
        
        out.println("The user principal is: " + request.getUserPrincipal().getName() + "<BR>");
        out.println("getRemoteUser(): " + request.getRemoteUser() + "<BR>" );
        
        if (request.isUserInRole("ADM")){
            out.println("USR_IN_ROLE_ADM");
        }else
            out.println("USR_NOT_IN_ROLE_ADM");
        
        if (request.isUserInRole("MGR")){
            out.println("USR_IN_ROLE_MGR");
        }else
            out.println("USR_NOT_IN_ROLE_MGR");
        
        if (request.isUserInRole("EMP")){
            out.println("USR_IN_ROLE_EMP");
        }else
            out.println("USR_NOT_IN_ROLE_EMP");
        
        if (request.isUserInRole("VP")){
            out.println("USR_IN_ROLE_VP");
        }else
            out.println("USR_NOT_IN_ROLE_VP");

        %>

    </body>
</html>
