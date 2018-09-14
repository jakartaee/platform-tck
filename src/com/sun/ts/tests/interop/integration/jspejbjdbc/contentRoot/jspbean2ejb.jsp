<%--

    Copyright (c) 2006, 2018 Oracle and/or its affiliates. All rights reserved.

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

<!--
 @(#)jspbean2ejb.jsp	1.7 06/02/19
-->

<% { %>
<jsp:useBean id="hello" scope="session" class="com.sun.ts.tests.interop.integration.jspejbjdbc.AccessJSPBean" />

<%
   java.util.Properties harnessProps = null;
   java.util.Enumeration enum1 = request.getParameterNames();
   if (enum1.hasMoreElements())
	harnessProps = new java.util.Properties();
   while (enum1.hasMoreElements()) {
	String name = (String) enum1.nextElement();
	String value = request.getParameter(name);
	harnessProps.setProperty(name, value);
   }

   hello.setup(harnessProps);

   java.util.Properties transactionProps = hello.getTransactionProps();
   StringBuffer lep = new StringBuffer();
   java.util.Enumeration key = transactionProps.keys();
   String name; 
   while (key.hasMoreElements())
    {
     name = (String)key.nextElement();
     lep.append(name+"="+transactionProps.getProperty(name)+"\n");
    }
%>
<%= lep.toString() %>
<% } %>

