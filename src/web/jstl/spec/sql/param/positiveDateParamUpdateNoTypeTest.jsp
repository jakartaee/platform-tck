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

<tck:test testName="positiveDateParamUpdateNoTypeTest">

 <%
      pageContext.setAttribute("theDate", new java.util.Date( 101,7,30,19,19,19));
   %>

   <%-- Number of rows to be affected by the DML statements --%>
   <c:set var="expectedRows" value="${1}" />

   <!-- Validate sql:update, sql:dateParam actions without specifying the type
       attribute defaults to timestamp -->

   <h1>Validate sql:update, sql:dateParam actions without specifying the type
       attribute defaults to timestamp </h1>
   <p>
   <%-- Clear out our table prior to starting the test --%>
   <sql:update var="updateCount" dataSource="${applicationScope.jstlDS}"
               sql="${sqlProps.Delete_Jstl_Tab3_AllRows_Query}" />


   <sql:update var="updateCount2"
                  dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'
                  sql='<%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Insert_Jstl_Tab3_Timestamp_Query") %>' >
      <sql:dateParam value='<%= (java.util.Date)pageContext.getAttribute("theDate") %>'  />
   </sql:update>

   <c:choose>
       <c:when test="${updateCount2 == expectedRows}">
        The SQL statement executed correctly and  returned the correct
        update count of <strong><c:out value="${expectedRows}" />
        </strong>.<p>
       </c:when>
       <c:otherwise>
          <strong>Error:</strong> The SQL statement "<strong>
          <c:out value="${sqlProps.Insert_Jstl_Tab3_Timestamp_Query}" escapeXml='false'/>
          </strong>" resulted in an update count of <strong>
          <c:out value="${updateCount2}" /></strong> and the
          the expected update count was <strong>
          <c:out value="${expectedRows}" /></strong>!<p>
       </c:otherwise>
   </c:choose>

 <sql:query var="resultSet2" dataSource="${applicationScope.jstlDS}"
              sql="${sqlProps.Select_Jstl_Tab3_Timestamp_Query}" >
      <sql:dateParam value="${theDate}" type='timestamp'/>
   </sql:query>

   <c:set var="rows2" value="${resultSet2.rowsByIndex}" />
   <c:set var='column2' value='${resultSet2.columnNames}' />

   <c:choose>
      <c:when test="${resultSet2.rowCount != expectedRows}">
         <H2>ERROR:</H2>
         The query: <strong>
         "<c:out value="${sqlProps.Select_Jstl_Tab3_Timestamp_Query}" />"
         </strong> returned <strong>"<c:out value="${resultSet2.rowCount}" />"
         </strong> rows and the expected number of rows was
         <strong>"<c:out value="${expectedRows}" />"</strong>.
         <p>
      </c:when>
      <c:otherwise>
         The query did return a Result Object that contained
         <strong><c:out value="${expectedRows}" /></strong> row as expected.
         <p>
      </c:otherwise>
   </c:choose>


</tck:test>
