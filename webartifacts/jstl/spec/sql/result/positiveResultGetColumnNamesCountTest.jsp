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

<tck:test testName="positiveResultGetColumnNamesCountTest">

  <!-- Validate ability to utilize Result.getColumnNames() to access the
           returned column values -->

   <h1>Validate ability to utilize Result.getColumnNames() to access the
           returned column values </h1>
   <p>

   <sql:query var="resultSet2"
                 dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'
                 sql='<%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Select_Jstl_Tab1_OneRow_Query") %>' />

  <c:set var='column2' value='${resultSet2.columnNames}' />

  <%
      String[] columns = (String[]) pageContext.getAttribute("column2");
      pageContext.setAttribute("columnsReturned2", new Integer(columns.length));
  %>

  <c:choose>
      <c:when test="${columnsReturned2 == initParam.JSTL_TAB1_COLUMNS}">
         The Correct number of columns <strong>was</strong>
         returned from Result.getColumnNames().
         <p>
      </c:when>
      <c:otherwise>
         <h2>Error</h2> The Expected number of columns was not returned from
         Result.getColumNames. The expected number of columns was
         <strong><c:out value='${initParam.JSTL_TAB1_COLUMNS}' /></strong>,
         the returned number of columns was <strong>
         <c:out value='${columnsReturned2}' /></strong> using the query
         <strong><c:out value='${sqlProps.Select_Jstl_Tab1_OneRow_Query}'/>
         </strong>.
         <p>
      </c:otherwise>
   </c:choose>

</tck:test>
