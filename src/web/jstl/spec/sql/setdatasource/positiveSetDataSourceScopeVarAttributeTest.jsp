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

<tck:test testName="positiveSetDataSourceScopeVarAttributeTest">

   <!-- Validate the explicit/implicit behavior of the
             action when scope is and isn't provided (var attribute specified) -->

   <h1>Validating sql:setDataSource action scope attributes </h1>
   <p>

   <sql:setDataSource
       dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'
       var='riPage' />
   <tck:checkScope varName="riPage" />

   <sql:setDataSource
       dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'
       var='rePage' scope='page'   />
   <tck:checkScope varName="rePage"  inScope='page'/>

     <sql:setDataSource
       dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'
       var='reSession' scope='session'   />
   <tck:checkScope varName="reSession" inScope="session" />

    <sql:setDataSource
       dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'
       var='reRequest' scope='request'   />
   <tck:checkScope varName="reRequest"  inScope='request'/>

     <sql:setDataSource
       dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'
       var='reApplication' scope='application'   />
   <tck:checkScope varName="reApplication" inScope='application'/>

</tck:test>
