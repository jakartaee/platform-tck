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

<tck:test testName="positiveResultGetRowsCountTest">

 <!-- Validate ability to utilize Result.getRows() to access the
           returned rows. Check that the correct row count is returned -->

   <h1>Validate ability to utilize Result.getRows() to access the
       returned rows. Check that the correct row count is returned </h1>
   <p>
   <sql:query var="resultSet2"
                 dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'
                 sql='<%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Simple_Select_Query") %>' />

  <c:set var='rows2' value='${resultSet2.rows}' />

  <%
      SortedMap[] rows = (SortedMap[]) pageContext.getAttribute("rows2");
      pageContext.setAttribute("rowsReturned2", new Integer(rows.length));
  %>

  <c:choose>
      <c:when test="${rowsReturned2 == initParam.JSTL_TAB1_ROWS}">
         The Correct number of rows <strong>was</strong>
         returned from Result.getRows().
         <p>
      </c:when>
      <c:otherwise>
         <h2>Error</h2> The Expected number of rows was not returned from
         Result.getRows. The expected number of columns was
         <strong><c:out value='${initParam.JSTL_TAB1_ROWS}' /></strong>,
         the returned number of rows was <strong>
         <c:out value='${rowsReturned2}' /></strong> using the query
         <strong><c:out value='${sqlProps.Simple_Select_Query}'/></strong>.
         <p>
      </c:otherwise>
   </c:choose>

</tck:test>
