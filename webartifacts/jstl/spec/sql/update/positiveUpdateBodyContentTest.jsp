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
<%@ page import="javax.sql.*" %>

   <tck:test testName="positiveUpdateBodyContentTest">

   <!-- Validate the sql:update action supports a SQL
             statement as body content -->

   <h1>Validate sql:update action supports a SQL statement as body content </h1>

   <sql:update var="updateCount2"
                  dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>' >
       <c:out value="${sqlProps.Delete_NoRows_Query}" />
   </sql:update>


   <c:choose>
       <c:when test="${updateCount2 == 0}">
          The correct <strong>update count</strong> was received for the
          query.<p>
       </c:when>
       <c:otherwise>
          <h2>Error:</h2> The query "<strong>
          <c:out value="${sqlProps.Delete_NoRows_Query}" />
          </strong>" resulted in an update count of <strong>
          <c:out value="${updateCount2}" /></strong> and the
          the expected update count was <strong>0</strong>!<p>
       </c:otherwise>
   </c:choose>

</tck:test>
