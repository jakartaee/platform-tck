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
<tck:test testName="positivePNTypeTest">
    <c:set var="num" value="number"/>
    <c:set var="cur" value="currency"/>
    <c:set var="per" value="percent"/>
    <fmt:setLocale value="en_US"/>

    <!--  The type attribute specifies how to parse the provided value.
              Test the behavior of the action when specifying different types. 
              If type is not specified, the default value of number is used. -->
    Number: <fmt:parseNumber value="1,234"/><br>
    Number: <fmt:parseNumber value="1,234" type='<%= (String) pageContext.getAttribute("num") %>'/>
    Number: <fmt:parseNumber value="1,234" type="number"/>
    Currency: <fmt:parseNumber value="$1,234.00" type='<%= (String) pageContext.getAttribute("cur") %>'/>
    Currency: <fmt:parseNumber value="$1,234.00" type="currency"/>
    Percent: <fmt:parseNumber value="1.5%" type='<%= (String) pageContext.getAttribute("per") %>'/>
    Percent: <fmt:parseNumber value="1.5%" type="percent"/>
</tck:test>
