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
<title>checkConfig Test </title>
<body>
<% /**	Name:checkConfig
		Description: Checks whether configuration information
			 is being passed to the server page. Verify that the
             implicit config object is an instance of
             javax.servlet.ServletConfig and then print out the 
             values of two servlet initialization parameters.
		Result: Display true and the values of configParam1 and configParam2 
**/ %>
<!-- checking for config object state -->
<%= (config instanceof javax.servlet.ServletConfig) %>
<br>
Value of param1 is: <%= config.getInitParameter( "configParam1" ) %><br>
Value of param2 is: <%= config.getInitParameter( "configParam2" ) %><br>
</body>
</html>
