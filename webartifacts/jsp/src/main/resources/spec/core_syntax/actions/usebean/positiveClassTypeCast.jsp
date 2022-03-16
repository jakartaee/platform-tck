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
<title>positiveClassTypeCast</title>
<body>
<% /** 	Name : positiveClassTypeCast
	Description : we are using 'class' and 'type' together and 'class' 
		      is assignable to 'type'. 
	Result :we should get the expected page without an error
**/ %>
<!-- we are using 'class' and 'type' together and 'class' is assignable to type-->
<jsp:useBean id="ncounter"  class="com.sun.ts.tests.jsp.spec.core_syntax.actions.usebean.NewCounter"
type="com.sun.ts.tests.jsp.spec.core_syntax.actions.usebean.Counter" />
<% out.println( ncounter.getCount());  %>
</body>
</html>
