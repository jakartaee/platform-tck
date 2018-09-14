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
<tck:test testName="positivePDTimeStyleTest">
    <c:set var="def" value="default"/>
    <c:set var="sho" value="short"/>
    <c:set var="med" value="medium"/>
    <c:set var="lon" value="long"/>
    <c:set var="ful" value="full"/>
    <fmt:setLocale value="en_US"/>
    <fmt:setTimeZone value="EST"/>

     <!-- timeStyle specifies formatting style that determines how
             the provided value will be parsed. timeStyle should be
             applied when type is not specified or is set to date or
             both.  -->
     <br>'type' not specified -- timeStyle should not be applied. If it is, a parse exception would occur.<br>
     <fmt:parseDate value="Nov 21, 2000"/><br>
     <fmt:parseDate value="Nov 21, 2000"/><br>
     <fmt:parseDate value="Nov 21, 2000"
                       timeStyle='<%= (String) pageContext.getAttribute("def") %>'/><br>
     <fmt:parseDate value="Nov 21, 2000" timeStyle="default"/><br>
     <fmt:parseDate value="Nov 21, 2000"
                       timeStyle='<%= (String) pageContext.getAttribute("sho") %>'/><br>
     <fmt:parseDate value="Nov 21, 2000" timeStyle="short"/><br>
     <fmt:parseDate value="Nov 21, 2000"
                       timeStyle='<%= (String) pageContext.getAttribute("med") %>'/><br>
     <fmt:parseDate value="Nov 21, 2000" timeStyle="medium"/><br>
     <fmt:parseDate value="Nov 21, 2000"
                       timeStyle='<%= (String) pageContext.getAttribute("lon") %>'/><br>
     <fmt:parseDate value="Nov 21, 2000" timeStyle="long"/><br>
     <fmt:parseDate value="Nov 21, 2000"
                       timeStyle='<%= (String) pageContext.getAttribute("ful") %>'/><br>
     <fmt:parseDate value="Nov 21, 2000" timeStyle="full"/><br>

     <br>'type' set to 'date' -- timeStyle should not be applied. If it is, a parse exception would occur.<br>
     <fmt:parseDate value="Nov 21, 2000" type="date"/><br>
     <fmt:parseDate value="Nov 21, 2000" type="date"/><br>
     <fmt:parseDate value="Nov 21, 2000"
                       timeStyle='<%= (String) pageContext.getAttribute("def") %>' type="date"/><br>
     <fmt:parseDate value="Nov 21, 2000" timeStyle="default" type="date"/><br>
     <fmt:parseDate value="Nov 21, 2000"
                       timeStyle='<%= (String) pageContext.getAttribute("sho") %>' type="date"/><br>
     <fmt:parseDate value="Nov 21, 2000" timeStyle="short" type="date"/><br>
     <fmt:parseDate value="Nov 21, 2000"
                       timeStyle='<%= (String) pageContext.getAttribute("med") %>' type="date"/><br>
     <fmt:parseDate value="Nov 21, 2000" timeStyle="medium" type="date"/><br>
     <fmt:parseDate value="Nov 21, 2000"
                       timeStyle='<%= (String) pageContext.getAttribute("lon") %>' type="date"/><br>
     <fmt:parseDate value="Nov 21, 2000" timeStyle="long" type="date"/><br>
     <fmt:parseDate value="Nov 21, 2000"
                       timeStyle='<%= (String) pageContext.getAttribute("ful") %>' type="date"/><br>
     <fmt:parseDate value="Nov 21, 2000" timeStyle="full" type="date"/><br>

     <br>'type' set to 'time' -- timeStyle should be applied.<br>
     <fmt:parseDate value="3:45:03 AM" type="time"/><br>
     <fmt:parseDate value="3:45:03 AM" type="time"/><br>
     <fmt:parseDate value="3:45:03 AM"
                       timeStyle='<%= (String) pageContext.getAttribute("def") %>' type="time"/><br>
     <fmt:parseDate value="3:45:03 AM" timeStyle="default" type="time"/><br>
     <fmt:parseDate value="3:45 AM"
                       timeStyle='<%= (String) pageContext.getAttribute("sho") %>' type="time"/><br>
     <fmt:parseDate value="3:45 AM" timeStyle="short" type="time"/><br>
     <fmt:parseDate value="3:45:02 AM"
                       timeStyle='<%= (String) pageContext.getAttribute("med") %>' type="time"/><br>
     <fmt:parseDate value="3:45:03 AM" timeStyle="medium" type="time"/><br>
     <fmt:parseDate value="3:45:03 AM EST"
                       timeStyle='<%= (String) pageContext.getAttribute("lon") %>' type="time"/><br>
     <fmt:parseDate value="3:45:03 AM EST" timeStyle="long" type="time"/><br>
     <fmt:parseDate value="3:45:03 AM EST"
                       timeStyle='<%= (String) pageContext.getAttribute("ful") %>' type="time"/><br>
     <fmt:parseDate value="3:45:03 AM EST" timeStyle="full" type="time"/><br>

     <br>'type' set to 'both' -- timeStyle should be applied.<br>
     <fmt:parseDate value="Nov 21, 2000 3:45:02 AM" type="both"/><br>
     <fmt:parseDate value="Nov 21, 2000 3:45:02 AM" type="both"/><br>
     <fmt:parseDate value="Nov 21, 2000 3:45:02 AM"
                       timeStyle='<%= (String) pageContext.getAttribute("def") %>' type="both"/><br>
     <fmt:parseDate value="Nov 21, 2000 3:45:02 AM" timeStyle="default" type="both"/><br>
     <fmt:parseDate value="Nov 21, 2000 3:45 AM"
                       timeStyle='<%= (String) pageContext.getAttribute("sho") %>' type="both"/><br>
     <fmt:parseDate value="Nov 21, 2000 3:45 AM" timeStyle="short" type="both"/><br>
     <fmt:parseDate value="Nov 21, 2000 3:45:02 AM"
                       timeStyle='<%= (String) pageContext.getAttribute("med") %>' type="both"/><br>
     <fmt:parseDate value="Nov 21, 2000 3:45:02 AM" timeStyle="medium" type="both"/><br>
     <fmt:parseDate value="Nov 21, 2000 3:45:02 AM EST"
                       timeStyle='<%= (String) pageContext.getAttribute("lon") %>' type="both"/><br>
     <fmt:parseDate value="Nov 21, 2000 3:45:02 AM EST" timeStyle="long" type="both"/><br>
     <fmt:parseDate value="Nov 21, 2000 3:45:02 AM EST"
                       timeStyle='<%= (String) pageContext.getAttribute("ful") %>' type="both"/><br>
     <fmt:parseDate value="Nov 21, 2000 3:45:02 AM EST"
                       timeStyle="full" type="both"/><br>
</tck:test>
