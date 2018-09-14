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
<title>checkException Test </title>
<body>
<% /**	Name:checkExceptionTest
		Description: Cause a java.lang.ArithmeticException by 
                     dividing by zero.  The Exception should
                     be passed to the error page as specified
                     by the errorPage attribute of the page
                     directive.  The errorpage will verify the
                     type of exception.
			 type java.lang.Throwable
        Result: Errorpage is called up, where this check is done.
                Should return true.					 
**/ %>
<!-- errorpage -->
<%@ page errorPage="Errorpage.jsp" %>

<%
 int i=0; 
 int j=9;
 int k=j/i;
%>
</body>
</html>
