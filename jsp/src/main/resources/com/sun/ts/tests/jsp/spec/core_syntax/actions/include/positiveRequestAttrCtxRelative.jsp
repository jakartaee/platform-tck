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
<title>positiveRequestAttrCtxRelative</title>
<body>
<% /** 	Name : positiveRequestAttrCtxRelative
	Description :     We are testing for request time dynamic inclusion 
	using <jsp:include page > tag. We test if we get the included jsp got
        parsed at request time and included to the output. we use absolute
        path from contex-root root as url
**/ %>
<% String test="/includecommon"; %>
<!-- Request-time Dynamic inclusion,with absolute url from context-root -->
<jsp:include page= '<%= test+".jsp" %>' flush="true" />
</body>
</html>
