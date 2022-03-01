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

<%@ taglib prefix="sql" uri="jakarta.tags.sql" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<%@ page import="javax.sql.*, java.util.*" %>

<tck:test testName="positiveUpdateScopeAttributeTest">

   <!-- Validate the explicit/implicit behavior of the
             action when scope is and isn't provided -->

   <h1>Validating sql:update action scope attributes </h1>

   <sql:update var="riPage"
                  dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'  >
       <%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Delete_NoRows_Query") %>
   </sql:update>
   <tck:checkScope varName="riPage" inScope="page" />

   <sql:update var="rePage"
                  dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'
                  scope = 'page'>
       <%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Delete_NoRows_Query") %>
   </sql:update>
   <tck:checkScope varName="rePage" inScope="page" />

   <sql:update var="reSession"
                  dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'
                  scope = 'session'>
       <%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Delete_NoRows_Query") %>
   </sql:update>
   <tck:checkScope varName="reSession" inScope="session" />

   <sql:update var="reRequest"
                  dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'
                  scope = 'request'>
       <%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Delete_NoRows_Query") %>
   </sql:update>
   <tck:checkScope varName="reRequest" inScope="request" />

   <sql:update var="reApplication"
                  dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'
                  scope = 'application'>
       <%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Delete_NoRows_Query") %>
   </sql:update>

   <tck:checkScope varName="reApplication" inScope="application" />
   <c:remove var="reApplication" scope="application"/>

</tck:test>
