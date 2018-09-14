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

<html>
<title>checkSession Test </title>
<body>
<% /**	Name:checkSession
		Description: Checking 'session' is of type javax.servlet.HttpSession
			  and also whether an identifier is assigned to this session
			  or not.
		Result: returns true		
**/ %>
<!-- checking for Session object state -->
<%= (session instanceof javax.servlet.http.HttpSession) %>
<br>
<br>
<% 
   if(session.getId()!=null) {
%> true <% }
   else {
%> false <% } %>
<br>
</body>
</html>
