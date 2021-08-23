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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<%@ page import="javax.sql.*, java.util.*" %>

<tck:test testName="positiveTxQueryCommitTest">

   <!-- Validate sql:transaction and sql:query actions allow for a query
            to be executed successfully -->

   <h1>Validate sql:transaction and sql:query actions allow for a query
            to be executed successfully </h1>
   <p>

   <sql:transaction
                 dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'  >
      <sql:query var="resultSet2"
                    sql='<%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Simple_Select_Query") %>' />
   </sql:transaction>

   <c:choose>
      <c:when test="${resultSet2.rowCount != JSTL_TAB1_ROWS}">
         <H2>ERROR:</H2>
         The query: <strong>
         "<c:out value="${sqlProps.Simple_Select_Query}" />"
         </strong> returned <strong>"<c:out value="${resultSet2.rowCount}" />"
         </strong> rows and the expected number of rows was
         <strong>"<c:out value="${JSTL_TAB1_ROWS}" />"</strong>.
         <p>
      </c:when>
      <c:otherwise>
         The query did return a Result Object that contained
         <strong><c:out value="${JSTL_TAB1_ROWS}" /></strong> rows.
         <p>
      </c:otherwise>
   </c:choose>


</tck:test>
