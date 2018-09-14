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
<tck:test testName="positiveFDTimeStyleTest">
    <%  
        Date date = new Date(883192294202L);
        pageContext.setAttribute("dte", date);
    %>
    <c:set var="def" value="default"/>
    <c:set var="sho" value="short"/>
    <c:set var="med" value="medium"/>
    <c:set var="lon" value="long"/>
    <c:set var="ful" value="full"/>
    <fmt:setLocale value="en_US"/>
    <fmt:setTimeZone value="EST"/>

     <!-- timeStyle specifies what time style the formated value
             will be returned in.  This will only be applied
             when type is set to time or both. If typeStyle is not
             specified, the default of 'default' will be applied
             to the formatted value, if applicable. -->
     <br>'type' not specified -- timeStyle should not be applied<br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle='<%= (String) pageContext.getAttribute("def") %>'/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle="default"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle='<%= (String) pageContext.getAttribute("sho") %>'/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle="short"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle='<%= (String) pageContext.getAttribute("med") %>'/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle="medium"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle='<%= (String) pageContext.getAttribute("lon") %>'/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle="long"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle='<%= (String) pageContext.getAttribute("ful") %>'/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>' timeStyle="full"/><br>

     <br>'type' set to 'date' -- timeStyle should not be applied<br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>' type="date"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>' type="date"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle='<%= (String) pageContext.getAttribute("def") %>' type="date"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle="default" type="date"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle='<%= (String) pageContext.getAttribute("sho") %>' type="date"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle="short" type="date"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle='<%= (String) pageContext.getAttribute("med") %>' type="date"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle="medium" type="date"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle='<%= (String) pageContext.getAttribute("lon") %>' type="date"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle="long" type="date"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle='<%= (String) pageContext.getAttribute("ful") %>' type="date"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle="full" type="date"/><br>

     <br>'type' set to 'time' -- timeStyle should be applied<br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>' type="time"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>' type="time"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle='<%= (String) pageContext.getAttribute("def") %>' type="time"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle="default" type="time"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle='<%= (String) pageContext.getAttribute("sho") %>' type="time"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle="short" type="time"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle='<%= (String) pageContext.getAttribute("med") %>' type="time"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle="medium" type="time"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle='<%= (String) pageContext.getAttribute("lon") %>' type="time"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle="long" type="time"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle='<%= (String) pageContext.getAttribute("ful") %>' type="time"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle="full" type="time"/><br>

     <br>'type' set to 'both' -- timeStyle should be applied<br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>' type="both"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>' type="both"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle='<%= (String) pageContext.getAttribute("def") %>' type="both"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle="default" type="both"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle='<%= (String) pageContext.getAttribute("sho") %>' type="both"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle="short" type="both"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle='<%= (String) pageContext.getAttribute("med") %>' type="both"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle="medium" type="both"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle='<%= (String) pageContext.getAttribute("lon") %>' type="both"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle="long" type="both"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle='<%= (String) pageContext.getAttribute("ful") %>' type="both"/><br>
     <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'
                        timeStyle="full" type="both"/><br>
</tck:test>
