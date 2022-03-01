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

<%@ taglib prefix="sql" uri="jakarta.tags.sql" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<%@ page import="javax.sql.*, java.lang.Integer" %>

   <tck:test testName="positiveUpdateRowsResultTest">


   <c:set var="expectedRowsAffected" value="${1}" />

   <h1>Validate sql:update action var attribute equals the number of rows affected by the SQL statement </h1>

   <%-- Clear out our table and insert one row prior to starting the test --%>
   <sql:update var="updateCount" dataSource="${applicationScope.jstlDS}">
       <c:out value="${sqlProps.Delete_AllRows_Query}" />
   </sql:update>
   <sql:update var="updateCount" dataSource="${applicationScope.jstlDS}">
       <c:out value="${sqlProps.Insert_Row_Query}" escapeXml="false" />
   </sql:update>


   <!-- Validate the exported var attribute returns a value equal
             to the number of rows affected by the SQL statement -->

   <h1>Validate sql:update action var attribute equals the number of rows affected by the SQL statement </h1>

   <sql:update var="updateCount2"
                  dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>' >
       <c:out value="${sqlProps.Delete_AllRows_Query}" />
   </sql:update>

   <c:choose>
       <c:when test="${updateCount2 == expectedRowsAffected}">
          The SQL statement returned the correct update count of <strong>
          <c:out value="${expectedRowsAffected}" /></strong>.<p>
       </c:when>
       <c:otherwise>
          <strong>Error:</strong> The SQL statement "<strong>
          <c:out value="${sqlProps.Delete_AllRows_Query}" />
          </strong>" resulted in an update count of <strong>
          <c:out value="${updateCount2}" /></strong> and the
          the expected update count was <strong>
          <c:out value="${expectedRowsAffected}" /></strong>!<p>
       </c:otherwise>
   </c:choose>

</tck:test>
