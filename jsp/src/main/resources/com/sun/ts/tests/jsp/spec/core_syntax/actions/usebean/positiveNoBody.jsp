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
<title>positiveNoBody</title>
<body>
<% /** 	Name : positiveNoBody
	Description : Create a new instance of a bean with no body.
                  Access the name property.
	Result : Returns the default instance value of the name property: hello.
**/ %>
<!-- Declaring the bean with out body -->
<jsp:useBean id="myBean" scope="request" class="com.sun.ts.tests.jsp.spec.core_syntax.actions.usebean.String_IntBean" />
<jsp:getProperty name="myBean" property="name" />
</body>
</html>
