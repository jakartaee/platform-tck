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

<tck:test testName="positiveTxDataSourceConfigDataSourceTest">

   <!-- Validate sql:transaction, sql:query actions and javax.servlet.jsp.jstl.sql.dataSource
            configuration parameter  specifying a DataSource Object -->

   <h1>Validating sql:transaction, sql:query actions, the javax.servlet.jsp.jstl.sql.dataSource
   configuration parameter  specifying a DataSource Object </h1>
   <p>

 <%-- Configure javax.servlet.jsp.jstl.sql.dataSource --%>
   <tck:config configVar="datasource" op="set"
               value='<%= (DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'/>

       <sql:transaction>
          <sql:query var="resultSet2"  >
             <%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Simple_Select_Query") %>
          </sql:query>
       </sql:transaction>

   <c:choose>
      <c:when test="${resultSet2.rowCount != JSTL_TAB1_ROWS}">
         <H2>ERROR:</H2>
         While using the configuration parameter <strong>
         javax.servlet.jsp.jstl.sql.dataSource</strong>, specifying a DataSource
         Object,  the query: <strong>
         "<c:out value="${sqlProps.Simple_Select_Query}" />" , the expected
         number of rows <strong>"<c:out value="${JSTL_TAB1_ROWS}" />"</strong>
         was not returned. The actual number of rows returned was
         <strong>"<c:out value="${resultSet2.rowCount}" />"<strong>.
         <p>
      </c:when>
      <c:otherwise>
         While using the configuration parameter <strong>
         javax.servlet.jsp.jstl.sql.dataSource</strong>, specifying a DataSource
         Object, the query succeeded.
         <p>
      </c:otherwise>
   </c:choose>


  <tck:config configVar="datasource" op="remove" />

</tck:test>
