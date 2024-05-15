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
<tck:test testName="positiveParamNameValueTest">
    <!-- EL: Validate the acceptance of EL and static values -->
    <c:set var="pname" value="testparm"/>
    <c:set var="pvalue" value="parmvalue"/>
    <c:import url="import.jsp">
        <c:param name="${pname}" value="${pvalue}"/>
        <c:param name="testparm2" value="parmvalue2"/>
    </c:import>

    <!-- RT: Validate the acceptance of RT and static values -->
    <c_rt:import url="import.jsp">
        <c_rt:param name='<%= (String) pageContext.getAttribute("pname") %>'
                    value='<%= (String) pageContext.getAttribute("pvalue") %>'/>
        <c_rt:param name="testparm2" value="parmvalue2"/>
    </c_rt:import>
</tck:test>
