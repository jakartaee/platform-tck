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

<tck:test testName="negativeSetDataSourceDataSourceAttributeDriverManagerTest">

   <c:set var='driverInfo'
          value="invalid,invalid,invalid,invalid" />

   <!-- Validate sql:query and setDataSource actions by specifying a String
       with invalid DriverManager parameters as the dataSource attribute-->

   <h1>Validate sql:query and setDataSource actions by specifying a String with
       invalid DriverManager parameters as the dataSource attribute </h1>
   <p>

  <tck:catch var="ex2" exception="javax.servlet.jsp.JspException" >
      <sql:setDataSource
          dataSource='<%=(String) pageContext.getAttribute("driverInfo", PageContext.PAGE_SCOPE) %>'
          var='driverInfoDS2'/>
  </tck:catch>

<c:out value="${ex2}" escapeXml="false"/>



</tck:test>
