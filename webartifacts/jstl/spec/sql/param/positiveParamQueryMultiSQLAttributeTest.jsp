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

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sql" uri="jakarta.tags.sql" %>

<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<%@ page import="javax.sql.*, java.util.*" %>

<tck:test testName="positiveParamQueryMultiSQLAttributeTest">
   <%
      pageContext.setAttribute("key", new Integer("1"));
      pageContext.setAttribute("lastName", "Andersen");
   %>
   <c:set var="expectedRows" value="${1}" />

   <!-- Validate ability to utilize the multiple sql:param actions and
             a sql attribute to execute the query-->

   <h1>Validating sql:query action by specifying multiple sql:param actions and
   the query by a sql attribute via RT</h1>
   <p>

   <sql:query var="resultSet2"
                 dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'

                 sql='<%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Select_Jstl_Tab1_MultiParam_Query") %>' >

      <sql:param value='<%=(Integer) pageContext.getAttribute("key") %>' />
      <sql:param value='<%=(String) pageContext.getAttribute("lastName") %>' />
   </sql:query>

   <c:set var="rows2" value="${resultSet2.rowsByIndex}" />

   <c:choose>
      <c:when test="${resultSet2.rowCount != expectedRows}">
         <H2>ERROR:</H2>
         The query: <strong>
         "<c:out value="${sqlProps.Select_Jstl_Tab1_MultiParam_Query}" />"
         </strong> which was executed with the parameter values of  <strong>
         <c:out value='${key}' /></strong> and <strong>
         <c:out value='${lastName}'/></strong> returned <strong>
         <c:out value="${resultSet2.rowCount}" /></strong> rows and the
         expected number of rows was
         <strong><c:out value="${expectedRows}" /></strong>.
         <p>
      </c:when>
      <c:when test="${rows2[0][0] != key}">
         <H2>ERROR:</H2>
         The query: <strong>
         "<c:out value="${sqlProps.Select_Jstl_Tab1_MultiParam_Query}" />"
         </strong> which was executed with the parameter values of  <strong>
         <c:out value='${key}' /></strong> and <strong>
         <c:out value='${lastName}'/></strong> returned the wrong row.
         <p>
      </c:when>
      <c:otherwise>
         The query did return a Result Object that contained
         <strong><c:out value="${expectedRows}" /></strong> row as expected.
         <p>
      </c:otherwise>
   </c:choose>

</tck:test>
