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

<tck:test testName="negativeParamUpdateBodyContentTest">

   <!-- Validate sql:update action that specifies a query with placeholders
             throws a JspException if there is no sql:param action specified -->

   <h1> Validate sql:update action that specifies a query with placeholders
        throws a JspException if there is no sql:param  action specified</h1>
   <p>

   <tck:catch var="e2" exception= "javax.servlet.jsp.JspException"            checkRootCause='true'
              exceptionText='<%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Delete_Jstl_Tab2_Using_Param_Query") %>' >
       <sql:update var="updateCount2"
                      dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>' >
             <c:out value="${sqlProps.Delete_Jstl_Tab2_Using_Param_Query}" />
       </sql:update>
   </tck:catch>

   <c:out value="${e2}" escapeXml="false"/>


</tck:test>
