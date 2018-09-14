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

<tck:test testName="positiveTxRollbackLifeCycleTest">


   <!-- Validate sql:transaction action Lifecycle for a rolledback transaction
    -->

   <h1>Validate sql:transaction action LifeCycle for a rolledback transaction </h1>
   <p>
  <c:catch var="e2"  >
      <sql:transaction dataSource='<%=(DataSource) pageContext.getAttribute("logDS", PageContext.APPLICATION_SCOPE) %>' >
         <sql:update var="updateCount2"
                       sql='<%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Failed_Insert_Query") %>' />
      </sql:transaction>
   </c:catch>


    <strong>Dump LifeCycle for sql:transaction action for commited transaction </strong>
    <p>
    <c:forEach var='entry' items='${applicationScope.connLog}'>
       <c:out value='${entry}' />
       <br>
   </c:forEach>

  <c:remove var='connLog' scope= 'application' />

</tck:test>
