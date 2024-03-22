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
<title>positiveForwardAbshtml.jsp</title>
<body>
<% /** 	Name : positiveForwardCtxRelativeHtml.jsp
	Description :We are testing for forwarding to a html page using
        <jsp:forward page > tag. We test if the forwarded static content
        got  forwarded in the output. Here we use a context-relative path.
**/ %>	 
<!-- testing context-relative forwarding to an HTML page -->
<jsp:forward page= "/forwardcommon.html" />
</body>
</html>
