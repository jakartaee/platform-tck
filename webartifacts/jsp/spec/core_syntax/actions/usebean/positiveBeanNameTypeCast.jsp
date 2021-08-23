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
<title>positiveBeanNameTypeCast</title>
<body>
<% /** 	Name : positiveBeanNameTypeCast
	Description : Declaring the bean using beanName.  
    When using beanName, the bean can either be a serialized
    Object, or a fully qualified class.  When the class is loaded,
    it is cast to the type specified by the "type" attribute.  
    This test uses a fully qualfied class.
	Result :We should get page output without error
**/ %>
<!-- Declaring the bean with beanName as a class -->
<jsp:useBean id="myBean" scope="request"
beanName="com.sun.ts.tests.jsp.spec.core_syntax.actions.usebean.NewCounter"
type="com.sun.ts.tests.jsp.spec.core_syntax.actions.usebean.Counter" />
<!-- accessing the bean thru a scriptlet -->
<%
 out.println(myBean.getCount());
%>
</body>
</html>
