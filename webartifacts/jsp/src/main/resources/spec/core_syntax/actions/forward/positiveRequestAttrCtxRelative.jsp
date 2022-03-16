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
<title>positiveRequestAttrAbs</title>
<body>
<% /** 	Name : positiveRequestAttrCtxRelative
	Description :     We are testing for request time dynamic inclusion 
	using <jsp:forward page > tag. We test if we get the forwarded jsp got
        parsed at request time and forwarded to the output using a 
        context-relative path.
**/ %>	 
<% String test="/forwardcommon"; %>
<!-- Request-time Dynamic inclusion, with a context-relative path -->
<jsp:forward page= '<%= test+".jsp" %>' />
</body>
</html>
