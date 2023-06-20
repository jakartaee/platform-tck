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
<title>negativeDuplicateIDFatalTranslation</title>
<body>
<% /** 	Name : negativeDuplicateIDFatalTranslation
	Description : Verify that duplicate useBean id's
                  within the same translation unit yeild
                  a fatal translation error.
	Result : Fatal translation error
**/ %>
<!-- Declare two beans with the same id -->
<jsp:useBean id="myBean" scope="page" class="com.sun.ts.tests.jsp.spec.core_syntax.actions.usebean.String_IntBean" />
<jsp:useBean id="myBean" scope="page" class="com.sun.ts.tests.jsp.spec.core_syntax.actions.usebean.String_IntBean" />
</body>
</html>
