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
<%@ page import="javax.sql.*, java.util.*" %>

   <tck:test testName="negativeUpdateBodyContentTest">

   <!-- Validate the sql:update action throws a JspException
            when an invalid a SQL statement is provided
            as body content -->

   <h1>Validate sql:update action throws JspException when an invalid SQL statement
   is passed as body content </h1>

     <tck:catch var="e2" exception= "javax.servlet.jsp.JspException"
             checkRootCause='true'
              exceptionText='<%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Invalid_SQL_Query") %>' >

      <sql:update var="updateCount2"
                     dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>' >
           <c:out value="${sqlProps.Invalid_SQL_Query}" />
      </sql:update>
   </tck:catch>

   <c:out value="${e2}" escapeXml="false"/>


</tck:test>
