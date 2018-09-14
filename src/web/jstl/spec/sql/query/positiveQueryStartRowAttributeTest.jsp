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

<tck:test testName="positiveQueryStartRowAttributeTest">

   <%
      pageContext.setAttribute("noStartRow", new Integer("0"));
      pageContext.setAttribute("startingRow", new Integer("1"));
      pageContext.setAttribute("maxRows", new Integer("8"));
   %>

   <c:set var="expectedRows" value="${JSTL_TAB1_ROWS - startingRow}" />

   <!-- Validate sql:query action utilizing the
              the startRow attribute -->

   <h1>Validating sql:query action startRow attribute </h1>
   <p>


   <sql:query var="resultSet5"
                 dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'  >
       <%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Select_Jstl_Tab1_By_Id_Query") %>
   </sql:query>

   <c:choose>
      <c:when test="${resultSet5.rowCount != JSTL_TAB1_ROWS}">
         <H2>ERROR:</H2>
         The startRow attribute was <strong>NOT</strong> specified.  The
          expected number of rows <strong>
          "<c:out value="${JSTL_TAB1_ROWS}" />"</strong> was not
         returned. The actual number of rows returned was
          <strong>"<c:out value="${resultSet5.rowCount}" />"</strong>.
         <p>
      </c:when>
      <c:otherwise>
         The startRow attribute was <strong>NOT</strong> specified and
         a Result Object that contained
         <strong><c:out value="${JSTL_TAB1_ROWS}" /></strong> rows was
         returned as expected.
         <p>
      </c:otherwise>
   </c:choose>


   <sql:query var="resultSet6"
                 dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'
                 startRow='<%=((Integer) pageContext.getAttribute("noStartRow")).intValue() %>' >
       <%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Select_Jstl_Tab1_By_Id_Query") %>
   </sql:query>

   <c:choose>
      <c:when test="${resultSet6.rowCount != JSTL_TAB1_ROWS}">
         <H2>ERROR:</H2>
         The startRow attribute was set to <strong>
         <c:out value="${noStartRow}" /></strong>. The expected
         number of rows <strong>
         "<c:out value="${JSTL_TAB1_ROWS}" />"</strong> was not
         returned. The actual number of rows returned was
          <strong>"<c:out value="${resultSet6.rowCount}" />"</strong>.
         <p>
      </c:when>
      <c:otherwise>
         The startRow attribute was set to <strong>
         <c:out value="${noStartRow}" /></strong>
         and a Result Object that contained
         <strong><c:out value="${JSTL_TAB1_ROWS}" /></strong> rows was
         returned as expected.
         <p>
      </c:otherwise>
   </c:choose>

   <sql:query var="resultSet7"
                 dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'
                 startRow='<%=((Integer) pageContext.getAttribute("startingRow")).intValue() %>' >
         <%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Select_Jstl_Tab1_By_Id_Query") %>
   </sql:query>

    <c:set var="rows" value="${resultSet7.rowsByIndex}" />

   <c:choose>
      <c:when test="${resultSet7.rowCount != expectedRows}">
         <H2>ERROR:</H2>
         The startRow attribute was set to <strong>
         <c:out value="${startingRow}" /></strong>.
         The expected number of rows returned was
          <strong>"<c:out value="${expectedRows}" />"</strong>.
         The actual number of rows returned was
          <strong>"<c:out value="${resultSet7.rowCount}" />"</strong>.
         <p>
      </c:when>
      <c:when test="${rows[0][0] != (startingRow+1)}">
         <H2>ERROR:</H2>
         The startRow attribute was set to <strong>
         <c:out value="${startingRow}" /></strong>.
         The query <strong>
         "<c:out value="${sqlProps.Select_Jstl_Tab1_By_Id_Query}" />"</strong>
         should have resulted in the first row having a value of
         <strong>"<c:out value="${(startingRow+1)}" />"</strong> for the
         first column.
         <p>
      </c:when>
      <c:otherwise>
         The startRow attribute was set to <strong>
         <c:out value="${startingRow}" /></strong>.
         The query  resulted in the first row having a value of
         <strong>"<c:out value="${rows[0][0]}" />"</strong> for the
         first column as expected.
         <p>
      </c:otherwise>
   </c:choose>



   <sql:query var="resultSet8"
                 dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'
                 startRow='1' >
         <%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Select_Jstl_Tab1_By_Id_Query") %>
   </sql:query>

   <c:set var="rows" value="${resultSet8.rowsByIndex}" />

   <c:choose>
      <c:when test="${resultSet8.rowCount != expectedRows}">
         <H2>ERROR:</H2>
         The startRow attribute was set to <strong>
         <c:out value="${startingRow}" /></strong>.
         The expected number of rows returned was
          <strong>"<c:out value="${expectedRows}" />"</strong>.
         The actual number of rows returned was
          <strong>"<c:out value="${resultSet8.rowCount}" />"</strong>.
         <p>
      </c:when>
      <c:when test="${rows[0][0] != (startingRow+1)}">
         <H2>ERROR:</H2>
         The startRow attribute was set to <strong>
         <c:out value="${startingRow}" /></strong>.
         The query <strong>
         "<c:out value="${sqlProps.Select_Jstl_Tab1_By_Id_Query}" />"</strong>
         should have resulted in the first row having a value of
         <strong>"<c:out value="${(startingRow+1)}" />"</strong> for the
         first column.
         <p>
      </c:when>
      <c:otherwise>
         The startRow attribute was set to <strong>
         <c:out value="${startingRow}" /></strong>.
         The query resulted in the first row having a value of
         <strong>"<c:out value="${rows[0][0]}" />"</strong> for the
         first column as expected.
         <p>
      </c:otherwise>
   </c:choose>


   <sql:query var="resultSet9"
                 dataSource='<%=(DataSource) pageContext.getAttribute("jstlDS", PageContext.APPLICATION_SCOPE) %>'
                 startRow='<%=((Integer) pageContext.getAttribute("startingRow")).intValue() %>'
                 maxRows='<%=((Integer) pageContext.getAttribute("maxRows")).intValue() %>' >
         <%=((Properties)pageContext.getAttribute("sqlProps",PageContext.APPLICATION_SCOPE)).getProperty("Select_Jstl_Tab1_By_Id_Query") %>
</sql:query>

   <c:set var="rows" value="${resultSet9.rowsByIndex}" />

   <c:choose>
      <c:when test="${resultSet9.rowCount != maxRows}">
         <H2>ERROR:</H2>
         The startRow attribute was set to <strong>
         <c:out value="${startingRow}" /></strong>.
         The maxRows attribute was set to <strong>
          <strong>"<c:out value="${maxRows}" />"</strong>.
         The actual number of rows returned was
          <strong>"<c:out value="${resultSet9.rowCount}" />"</strong>.
         <p>
      </c:when>
      <c:when test="${rows[0][0] != (startingRow+1)}">
         <H2>ERROR:</H2>
         The startRow attribute was set to <strong>
         <c:out value="${startingRow}" /></strong>.
         The query <strong>
         "<c:out value="${sqlProps.Select_Jstl_Tab1_By_Id_Query}" />"</strong>
         should have resulted in the first row having a value of
         <strong>"<c:out value="${(startingRow+1)}" />"</strong> for the
         first column.
         <p>
      </c:when>
      <c:otherwise>
         The startRow attribute was set to <strong>
         <c:out value="${startingRow}" /></strong>.
         The maxRows attribute was set to <strong>
          "<c:out value="${maxRows}" />"</strong>.
         The query resulted in the first row having a value of
         <strong>"<c:out value="${rows[0][0]}" />"</strong> for the
         first column as expected.
         <p>
      </c:otherwise>
   </c:choose>

</tck:test>

