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
 @(#)secured.jsp	1.3 06/02/19  
-->

<%@ page language="java" %>
 
<html>
<head><title>JSP with Security Constraint</title></head>
<body>
<h2>JSP with Security Constraint</h2>

<% 

out.println("The user principal is: " + request.getUserPrincipal().getName() + "<BR>");
out.println("getRemoteUser(): " + request.getRemoteUser() + "<BR>" );

// Surround these with !'s so they are easier to search for.
// (i.e. we can search for !true! or !false!)
out.println("isUserInRole(\"ADM\"): !" + request.isUserInRole("ADM") + "!<BR>");
out.println("isUserInRole(\"MGR\"): !" + request.isUserInRole("MGR") + "!<BR>");
out.println("isUserInRole(\"VP\"): !" + request.isUserInRole("VP") + "!<BR>");
out.println("isUserInRole(\"EMP\"): !" + request.isUserInRole("EMP") + "!<BR>");
out.println("isUserInRole(\"Administrator\"): !" + request.isUserInRole("Administrator") + "!<BR>");

%>

</body>
</html>


