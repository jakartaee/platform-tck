<%--

    Copyright (c) 2018, 2020 Oracle and/or its affiliates. All rights reserved.

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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Vector" %>
<%@ page import="jakarta.el.ValueExpression" %>

<% Vector v = new Vector();
   v.add("One"); v.add("Two"); v.add("Three");
   pageContext.setAttribute("foo", v);
%>

<tck:test testName="positiveForEachDeferredValueTest1">
   <c:forEach var="item" items="#{foo}">
      <tck:save attr="#{item}"/>
   </c:forEach>
   <%
      ArrayList al = (ArrayList) pageContext.getAttribute("alist", PageContext.APPLICATION_SCOPE);
      for (int i = 0; i < al.size(); i++) {
          ValueExpression ve = (ValueExpression) al.get(i);
   %>
          <%= i %>
          <%= ve.getValue(pageContext.getELContext()) %>
          <br/>
   <%
       }
       pageContext.setAttribute("alist", null, PageContext.APPLICATION_SCOPE);
   %>
</tck:test>
