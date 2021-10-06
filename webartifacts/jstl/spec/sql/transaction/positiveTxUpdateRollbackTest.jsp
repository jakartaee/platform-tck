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

<tck:test testName="positiveTxUpdateRollbackTest">

   <%-- Number of rows to be returned by the DML statements --%>
   <c:set var="expectedRowsAffected" value="${0}" />

  <!-- Validate sql:transaction and sql:update actions correctly rollback
           a series of updates when a SQL Exception occurs -->

   <h1>Validate sql:transaction and sql:update actions correctly rollback
           a series of updates when a SQL Exception occurs </h1>
   <p>

   <%-- Clear out our table prior to starting the test --%>
   <sql:update var="updateCount" dataSource="${applicationScope.jstlDS}">
       <c:out value="${sqlProps.Delete_AllRows_Query}" />
   </sql:update>

  <%-- Execute our update within a transaction and make sure that the update
        is rolledback --%>
   <c:catch var="ex2"  >
      <sql:transaction dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>' >
         <sql:update var="updateCount2"
                       sql='<%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Insert_Row_Query") %>' />
         <sql:update var="updateCount3"
                       sql='<%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Failed_Insert_Query") %>' />
      </sql:transaction>
   </c:catch>

    <c:choose>
      <c:when test="${empty ex2}">
         <H2>ERROR:</H2>
         An Exception was <strong>did not occur</strong> when the query <strong>
         <c:out value='${sqlProps.Failed_Insert_Query}' escapeXml='false' />
        </strong> was executed.
         <p>
      </c:when>
      <c:otherwise>
         An Exception <strong>was</strong> generated as Expected.
         <p>
      </c:otherwise>
   </c:choose>

   <!-- Validate that the Transaction was rolled back -->
   <sql:query var="resultSet2" dataSource="${applicationScope.jstlDS}"
              sql="${sqlProps.Select_Jstl_Tab2_AllRows_Query}" />

   <c:choose>
       <c:when test="${resultSet2.rowCount == expectedRowsAffected}">
        The transaction was <strong>rolled back</strong>.<p>
       </c:when>
       <c:otherwise>
          <strong>Error:</strong> The SQL statement "<strong>
          <c:out value="${sqlProps.Insert_Row_Query}" />
          </strong>" resulted in <strong> <c:out value='${resultSet2.rowCount}'/>
           </strong> rows being inserted.  No rows should be added!
       </c:otherwise>
   </c:choose>


</tck:test>
