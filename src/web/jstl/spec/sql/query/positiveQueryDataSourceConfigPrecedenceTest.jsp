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
<%@ page import="javax.sql.*,java.util.*" %>

<tck:test testName="positiveQueryDataSourceConfigPrecedenceTest">

   <!-- Validate sql:query action  that the dataSource attribute takes
            precedence over the javax.servlet.jsp.jstl.sql.dataSource
            configuration parameter -->

   <h1>Validate sql:query action  that the dataSource attribute takes
            precedence over the javax.servlet.jsp.jstl.sql.dataSource
            configuration parameter </h1>
   <p>

   <%-- Validate that the dataSource attribute overrides the config param --%>
   <tck:config configVar="datasource" op="set" value='<%= new Integer(1) %>'/>

   <c:catch var="ex2"  >

       <sql:query var="resultSet2"
           dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'  >
          <%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Simple_Select_Query") %>
       </sql:query>

   </c:catch>


   <c:choose>
      <c:when test="${!empty ex2}">
         <H2>ERROR:</H2>
         Could not execute the query <strong><c:out value="${sqlProps.Select_Jstl_Tab1_By_Id_Query}" />
         </strong> when using <strong><c:out value='${driverInfo}'/></strong> for
          javax.servlet.jsp.jstl.sql.dataSource! The Exception that was raised is:
         <strong><c:out value='${ex2}' escapeXml='false' /></strong>.
         <p>
      </c:when>
      <c:otherwise>
         Successfully executed the query when javax.servlet.jsp.jstl.sql.dataSource
          was provided a DataSource Object.
         <p>
      </c:otherwise>
   </c:choose>



  <tck:config configVar="datasource" op="remove" />

</tck:test>
