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

<tck:test testName="positiveTxIsolationAttributeRead_CommittedTest">


   <%
        pageContext.setAttribute("isoLevel", new Integer(2), PageContext.APPLICATION_SCOPE);
   %>

   <c:set var='theIsolationLevel' value='read_committed' />

   <!-- Validate sql:transaction action which specifies read_committed for the
          isolation attribute  -->

   <h1>Validate sql:transaction action which specifies read_committed for the
          isolation attribute </h1>
   <p>

   <sql:transaction dataSource='<%=(DataSource) pageContext.getAttribute("logDS", PageContext.APPLICATION_SCOPE) %>'
                       isolation='<%=(String) pageContext.getAttribute("theIsolationLevel", PageContext.PAGE_SCOPE) %>' >

      <sql:update var="updateCount2"
                    sql='<%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Delete_AllRows_Query") %>' />
   </sql:transaction>


    <strong>Dump LifeCycle for sql:transaction action setting isolation attribute</strong>
    <p>
    <c:forEach var='entry' items='${applicationScope.connLog}'>
       <c:out value='${entry}' />
       <br>
   </c:forEach>

   <c:remove var='connLog' scope= 'application' />

</tck:test>
