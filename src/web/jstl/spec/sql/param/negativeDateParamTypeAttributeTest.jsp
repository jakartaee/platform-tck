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

<tck:test testName="negativeDateParamTypeAttributeTest">


   <%
      pageContext.setAttribute("theDate", new java.util.Date( 101,7,30,19,19,19));
   %>

  <!-- Validate  sql:dateParam action specifying and invalid
            value for the type attribute throws a JspException -->

   <h1>Validate  sql:dateParam action specifying and invalid
    value for the type attribute throws a JspException</h1>
   <p>

   <tck:catch var="e" exception= "javax.servlet.jsp.JspException" >
      <sql:query var="resultSet2"
                    dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'
                    sql='<%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Select_Jstl_Tab3_Date_Query") %>' >

      <sql:dateParam value='<%= (java.util.Date) pageContext.getAttribute("theDate") %>' type='invalidType'/>
   </sql:query>

   </tck:catch>

   <c:out value="${e}" escapeXml="false"/>

</tck:test>
