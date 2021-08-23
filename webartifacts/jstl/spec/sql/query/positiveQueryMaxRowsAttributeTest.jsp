<%--

    Copyright (c) 2003, 2018 Oracle and/or its affiliates. All rights reserved.

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

<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<%@ page import="javax.sql.*, java.util.*" %>

<tck:test testName="positiveQueryMaxRowsAttributeTest">

   <%
      pageContext.setAttribute("noMaxRows", new Integer("-1"));
      pageContext.setAttribute("maxRowsLimit", new Integer("5"));
   %>


   <!-- Validate that a sql:query action will return
              the number of rows specified by the maxRows attribute -->

   <h1>Validating sql:query action MaxRows attribute </h1>
   <p>

   <sql:query var="resultSet4"
                 dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'  >
       <%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Simple_Select_Query") %>
   </sql:query>

   <c:choose>
      <c:when test="${resultSet4.rowCount != JSTL_TAB1_ROWS}">
         <H2>ERROR:</H2>
         The maxRows attribute was <strong>NOT</strong> specified.  The
          expected number of rows <strong>
          "<c:out value="${JSTL_TAB1_ROWS}" />"</strong> was not
         returned. The actual number of rows returned was
          <strong>"<c:out value="${resultSet4.rowCount}" />"</strong>.
         <p>
      </c:when>
      <c:otherwise>
         The maxRows attribute was <strong>NOT</strong> specified and
         a Result object that contained
         <strong><c:out value="${JSTL_TAB1_ROWS}" /></strong> rows was
         returned as expected.
         <p>
      </c:otherwise>
   </c:choose>


   <sql:query var="resultSet5"
                 dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'
                 maxRows='<%=((Integer) pageContext.getAttribute("noMaxRows")).intValue() %>' >
      <%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Simple_Select_Query") %>
   </sql:query>

   <c:choose>
      <c:when test="${resultSet5.rowCount != JSTL_TAB1_ROWS}">
         <H2>ERROR:</H2>
         The maxRows attribute was set to <strong>
         <c:out value="${noMaxRows}" /></strong>. The expected
         number of rows <strong>
         "<c:out value="${JSTL_TAB1_ROWS}" />"</strong> was not
         returned. The actual number of rows returned was
          <strong>"<c:out value="${resultSet5.rowCount}" />"</strong>.
         <p>
      </c:when>
      <c:otherwise>
         The maxRows attribute was set to <strong>
         <c:out value="${noMaxRows}" /></strong>
         and a Result object that contained
         <strong><c:out value="${JSTL_TAB1_ROWS}" /></strong> rows was
         returned as expected.
         <p>
      </c:otherwise>
   </c:choose>

   <sql:query var="resultSet6"
                 dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'
                 maxRows='<%=((Integer) pageContext.getAttribute("maxRowsLimit")).intValue() %>' >
      <%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Simple_Select_Query") %>
   </sql:query>

   <c:choose>
      <c:when test="${resultSet6.rowCount != maxRowsLimit}">
         <H2>ERROR:</H2>
         The maxRows attribute was set to <strong>
         <c:out value="${maxRowsLimit}" /></strong>.
         The actual number of rows returned was <strong>
          "<c:out value="${resultSet6.rowCount}" />"</strong>.
         <p>
      </c:when>
      <c:otherwise>
         The maxRows attribute was set to <strong>
         <c:out value="${maxRowsLimit}" /></strong>
         and a Result object that contained this row count
          was returned as expected.
         <p>
      </c:otherwise>
   </c:choose>


   <%-- hard code maxRows attribute value to maxRowsLimit value --%>

   <sql:query var="resultSet7"
                 dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'
                 maxRows='5' >
      <%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Simple_Select_Query") %>
   </sql:query>

   <c:choose>
      <c:when test="${resultSet7.rowCount != maxRowsLimit}">
         <H2>ERROR:</H2>
         The maxRows attribute was set to <strong>
         <c:out value="${maxRowsLimit}" /></strong>.
         The actual number of rows returned was <strong>
          "<c:out value="${resultSet7.rowCount}" />"</strong>.
         <p>
      </c:when>
      <c:otherwise>
         The maxRows attribute was set to <strong>
         <c:out value="${maxRowsLimit}" /></strong>
         and a Result object that contained this row count
          was returned as expected.
         <p>
      </c:otherwise>
   </c:choose>

</tck:test>

