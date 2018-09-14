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

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<%@ page import="java.util.Date" %>
<tck:test testName="positivePDTimeZonePrecedenceTest">
    <c:set var="dte" value="Nov 21, 2000 3:45 AM"/>
    <fmt:setTimeZone value="EST"/>
    <fmt:setLocale value="en_US"/>

    <!--  The time zone to be used when formatting operates
              with the following order of presendence:
              - If timeZone specified, use that value.
              - If wrapped by a timeZone action, use that 
                value.
              - Use the value of the scoped attribute
                javax.servlet.jsp.jstl.fmt.timeZone. -->
    <br>TimeZone attribute specified with a value of PST:<br>
      Wrapped by &lt;fmt:timeZone&gt; action with MST.  Time should be offset by 3 hours:<br>
      <fmt:timeZone value="MST">
        <fmt:parseDate value='<%= (String) pageContext.getAttribute("dte") %>' timeZone="PST" type="both" timeStyle="short" var="rd1"/>
      </fmt:timeZone>
      <fmt:formatDate value="${rd1}" timeZone="EST" type="both" timeStyle="short"/><br>

      Not wrapped.  Page has a time zone of EST, timeZone attribute specified.  Time should be offset by 3 hours:<br>
      <fmt:parseDate value='<%= (String) pageContext.getAttribute("dte") %>' timeZone="PST" type="both" timeStyle="short" var="rd2"/><br>
      <fmt:formatDate value="${rd2}" timeZone="EST" type="both" timeStyle="short"/><br>

    <br>No TimeZone attribute specified:<br>
      Wrapped by &lt;fmt:timeZone&gt; action with MST.  Time should be offset by 2 hours:<br>
      <fmt:timeZone value="MST">
        <fmt:parseDate value='<%= (String) pageContext.getAttribute("dte") %>' type="both" timeStyle="short" var="rd3"/><br>
      </fmt:timeZone>
      <fmt:formatDate value="${rd3}" timeZone="EST" type="both" timeStyle="short"/><br>
      
      Not wrapped.  Page has a time zone of EST.  Time should not be offset:<br>
      <fmt:parseDate value='<%= (String) pageContext.getAttribute("dte") %>' type="both" timeStyle="short" var="rd4"/><br>
      <fmt:formatDate value="${rd4}" timeZone="EST" type="both" timeStyle="short"/><br>
    <br>
</tck:test>
