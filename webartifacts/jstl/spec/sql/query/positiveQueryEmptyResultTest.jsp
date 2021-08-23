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

<tck:test testName="positiveQueryEmptyResultTest">

   <!-- Validate that a Simple SQL Query will return
              an empty Result object when query returns 0 rows -->

   <h1>Validating sql:query action returns empty Result object </h1>
   <p>


   <sql:query var="resultSet2"
                 dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'  >
       <%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Select_NoRows_Query") %>
   </sql:query>

   <c:choose>
      <c:when test="${resultSet2.rowCount != 0}">
         <H2>ERROR:</H2>
         The query: <strong>
         "<c:out value="${sqlProps.Select_NoRows_Query}" />"
         </strong> returned <strong>"<c:out value="${resultSet2.rowCount}" />"
         </strong>  rows and the expected number of
          rows was <strong>"0" </strong>
         <p>
      </c:when>
      <c:otherwise>
         The query did return a Result object that contained
         <strong>0</strong> rows.
         <p>
      </c:otherwise>
   </c:choose>

</tck:test>
