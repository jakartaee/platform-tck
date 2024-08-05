<%--

    Copyright (c) 2003, 2020 Oracle and/or its affiliates. All rights reserved.

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

<tck:test testName="positiveTxDataSourceConfigDriverManagerTest">

   <c:set var='driverInfo'
          value="${header['jstl.db.url']},${header['jstl.db.driver']},${header['jstl.db.user']},${header['jstl.db.password']}" />
  <tck:config configVar="datasource" op="set"
               value='<%= (String) pageContext.getAttribute("driverInfo", PageContext.PAGE_SCOPE) %>'/>

   <!-- Validate sql:transaction action  by specifying a String
       with DriverManager parameters to jakarta.servlet.jsp.jstl.sql.dataSource -->

   <h1>Validate sql:transaction by specifying a String with DriverManager
       parameters to jakarta.servlet.jsp.jstl.sql.dataSource </h1>
   <p>


  <c:catch var="ex4"  >
    <sql:transaction>
       <sql:query var="resultSet2"  >
            <c:out value="${sqlProps.Simple_Select_Query}" />
       </sql:query>
    </sql:transaction>
   </c:catch>

   <c:choose>
      <c:when test="${!empty ex4}">
         <H2>ERROR:</H2>
         Could not execute the query <strong><c:out value="${sqlProps.Simple_Select_Query}" />
         </strong> when using <strong><c:out value='${driverInfo}'/></strong> for
         jakarta.servlet.jsp.jstl.sql.dataSource! The Exception that was raised is:
         <strong><c:out value='${ex4}' escapeXml='false' /></strong>.
         <p>
      </c:when>
      <c:otherwise>
         Successfully executed the query when jakarta.servlet.jsp.jstl.sql.dataSource
          was provided DriverManager parameters.
         <p>
      </c:otherwise>
   </c:choose>

</tck:test>
