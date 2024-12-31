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

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="c_rt" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<tck:test testName="positiveForTokensTest">
    <%
        String complexString = "a###b c# $d";
        String complexDelim = "# ";
        pageContext.setAttribute("complexStr", complexString);
        pageContext.setAttribute("complexDel", complexDelim);
    %>
    <!-- EL: Validation of forTokens using dynamic values -->
    <c:forTokens items="${complexStr}" delims="${complexDel}" var="eVar0">
        <c:out value="${eVar0}" default="forTokens Test FAILED"/><br>
    </c:forTokens>
    <!-- EL: Validation of forTokens using static values -->
    <c:forTokens items="1|2|3" delims="|" var="eVar1">
        <c:out value="${eVar1}" default="forTokens Test FAILED"/><br>
    </c:forTokens>
    
    <!-- RT: Validation of forTokens using dynamic values -->
    <c_rt:forTokens items='<%= complexString %>' delims='<%= complexDelim %>' var="rVar0">
        <c:out value="${rVar0}" default="forTokens Test FAILED"/><br>
    </c_rt:forTokens>
    <!-- RT: Validation of forTokens using static values -->
    <c_rt:forTokens items="a|b|c" delims="|"  var="rVar1">
        <c:out value="${rVar1}" default="forTokens Test FAILED"/><br>
    </c_rt:forTokens>

</tck:test>
