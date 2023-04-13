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
<title>positiveIncludeCtxRelativeHtml</title>
<body>
<% /** 	Name : positiveIncludeCtxRelativeHtml
	Description :We are testing for static inclusion of html page using
        <jsp:include page > tag. We test if we get the included static content
        got  included in the output. Here we use absolute path from current 
        context  as   url.
**/ %>
<!-- testing absolute inclusion of html relative to from current context-->
<jsp:include page= "/includecommon.html" flush="true" />
</body>
</html>
