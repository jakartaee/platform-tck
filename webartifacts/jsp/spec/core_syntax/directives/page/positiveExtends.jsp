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
<title>positiveExtends</title>
<body>
<% /** 
       Name : positiveExtends
       Description: we use extends directive to point our own defined class
                    as super class to the jsp
       Result: we should get true to the expression after the directive
  */ %>
<%@ page extends="com.sun.ts.tests.jsp.spec.core_syntax.directives.page.SuperPage" %>
<%= (this instanceof com.sun.ts.tests.jsp.spec.core_syntax.directives.page.SuperPage ) %>
</body>
</html>
