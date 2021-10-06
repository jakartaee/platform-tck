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

<tck:test testName="positiveTxUpdateParamCommitTest">

   <%
      pageContext.setAttribute("key", new Integer("1"));
   %>
   <%-- Number of rows to be affected by the DML statements --%>
   <c:set var="expectedRowsAffected" value="${1}" />

   <!-- Validate sql:transaction, sql:update sql:param actions
            allow for the execution of a SQL statement -->

   <h1>Validate sql:transaction, sql:update sql:param actions
            allow for the execution of a SQL statement </h1>
   <p>
   <%-- Clear out our table prior to starting the test --%>
   <sql:update var="updateCount" dataSource="${applicationScope.jstlDS}"
               sql="${sqlProps.Delete_AllRows_Query}" />

   <sql:update var="updateCount" dataSource="${applicationScope.jstlDS}"
               sql="${sqlProps.Insert_Row_Query}" />

   <sql:transaction
              dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>' >
         <sql:update var="updateCount2"
                     sql='<%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Delete_Jstl_Tab2_Using_Param_Query") %>' >
            <sql:param value='<%= pageContext.getAttribute("key") %>' />
      </sql:update>
   </sql:transaction>

   <c:choose>
       <c:when test="${updateCount2 == expectedRowsAffected}">
        The SQL statement executed correctly and  returned the correct
        update count of <strong><c:out value="${expectedRowsAffected}" />
        </strong>.<p>
       </c:when>
       <c:otherwise>
          <strong>Error:</strong> The SQL statement "<strong>
          <c:out value="${sqlProps.Delete_Jstl_Tab2_Using_Param_Query}" escapeXml='false'/>
          </strong>" resulted in an update count of <strong>
          <c:out value="${updateCount2}" /></strong> and the
          the expected update count was <strong>
          <c:out value="${expectedRowsAffected}" /></strong>!<p>
       </c:otherwise>
   </c:choose>


</tck:test>
