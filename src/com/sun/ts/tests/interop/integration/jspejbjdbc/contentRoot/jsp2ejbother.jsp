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
 @(#)jsp2ejbother.jsp	1.6 06/02/19
-->

<% { %>
<jsp:useBean id="hello" scope="session" class="com.sun.ts.tests.interop.integration.jspejbjdbc.AccessJSPBean" />

<% response.setContentType("text/plain"); %>

<% out.println("This is what the Bean had to say : ");
   hello.setup(null);
   String msg = hello.getMsg(); %>

<%= msg %>

<% out.println();
   out.println(); %>

<% } %>
