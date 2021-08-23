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


<tck:test testName="positiveSetDataSourceTxURLTest">

   <c:set var='driverInfo'
          value="${header['jstl.db.url']},${header['jstl.db.driver']},${header['jstl.db.user']},${header['jstl.db.password']}" />
   <c:set var='url' value="${header['jstl.db.url']}" />
   <c:set var='driver' value="${header['jstl.db.driver']}" />
   <c:set var='user' value="${header['jstl.db.user']}" />
   <c:set var='password' value="${header['jstl.db.password']}" />

  <!-- Validate sql:transaction and sql:setDataSource actions by specifying
         the url, driver, user, password attributes -->

   <h1>Validate sql:transaction and sql:setDataSource actions by specifying the url,
    driver, user, password attributes </h1>
   <p>

 <c:catch var="ex3"  >

      <sql:setDataSource
           url='<%=(String) pageContext.getAttribute("url", PageContext.PAGE_SCOPE) %>'
           driver='<%=(String) pageContext.getAttribute("driver", PageContext.PAGE_SCOPE) %>'
           user='<%=(String) pageContext.getAttribute("user", PageContext.PAGE_SCOPE) %>'
           password='<%=(String) pageContext.getAttribute("password", PageContext.PAGE_SCOPE) %>'
          var='driverInfoDS2'/>
  </c:catch>

   <c:choose>
      <c:when test="${!empty ex3}">
         <H2>ERROR:</H2>
         Could not create a dataSource using a <strong>DataSource Object
         </strong> for the dataSource attribute! The Exception that was raised
         is: <strong><c:out value='${ex3}'  escapeXml='false' /></strong>.
         <p>
      </c:when>
      <c:otherwise>
         Successfully created a DataSource.
         <p>
      </c:otherwise>
   </c:choose>


  <c:catch var="ex4"  >
    <sql:transaction dataSource='<%=(DataSource) pageContext.getAttribute("driverInfoDS2", PageContext.PAGE_SCOPE) %>'  >
       <sql:query var="resultSet2" >
            <c:out value="${sqlProps.Simple_Select_Query}" />
      </sql:query>
    </sql:transaction>

   </c:catch>

   <c:choose>
      <c:when test="${!empty ex4}">
         <H2>ERROR:</H2>
         Could not execute the query <strong><c:out value="${sqlProps.Simple_Select_Query}" />
         </strong> when using  a <strong>DataSource Object</strong> for
         the dataSource attribute! The Exception that was raised is:
         <strong><c:out value='${ex2}' escapeXml='false' /></strong>.
         <p>
      </c:when>
      <c:otherwise>
         Successfully executed the query when the dataSource attribute was
         provided a DataSource Object.
         <p>
      </c:otherwise>
   </c:choose>

</tck:test>
