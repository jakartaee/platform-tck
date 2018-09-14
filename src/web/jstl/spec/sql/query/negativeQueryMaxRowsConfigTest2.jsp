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

<tck:test testName="negativeQueryMaxRowsConfigTest2">

     <%-- configure javax.servlet.jsp.jstl.sql.maxRows  --%>
     <tck:config configVar="maxrows" op="set" value="-5" />

 <!-- Validate sql:query action that specifies a value less than -1 for
        javax.servlet.jsp.jstl.sql.maxRows throws JspException -->

    <h1>Validate sql:query action that specifies less than -1 for
        javax.servlet.jsp.jstl.sql.maxRows throws JspException </h1>
    <p>

  <tck:catch var="e2" exception= "javax.servlet.jsp.JspException" >
     <sql:query var="resultSet2"
                    dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'  >
          <%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Simple_Select_Query") %>
      </sql:query>
   </tck:catch>

   <c:out value="${e2}" escapeXml="false"/>

</tck:test>
