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
<title>checkRequest</title>
<body>
<% /** Name:checkRequest
	   Description: Checks whether 'request' is an instance 
	   		of javax.servlet.ServletRequest and uses the request
	   		object for getting the protocol and getting parameter 
	   		value passing Years.
		Result:returns true,HTTP/1.0,2 	
**/ %>
<!-- checking for request object type -->
<%= (request instanceof javax.servlet.ServletRequest) %>

<br>
<!-- request object used to access getProtocol -->
<% out.println(request.getProtocol()); %>
<br>

<!-- request object used to access getParameter -->
<!-- Parameter "Years" is passed to request object -->
<% out.println(request.getParameter("Years")); %><br>
</body>
</html>
