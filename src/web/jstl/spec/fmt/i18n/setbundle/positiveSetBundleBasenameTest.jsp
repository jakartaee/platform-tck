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
<tck:test testName="positiveSetBundleBasenameTest">
    <c:set var="base" value="com.sun.ts.tests.jstl.common.resources.Resources"/>

    <!-- The basename attribute provides the base resource bundle
             to be used when providing localized messages.  In this case,
             since var is not specified, the configuration variable 
             javax.servlet.jsp.jstl.fmt.localizationContext will be set
             with LocalizationContext that was initialized by the 
             ResourceBundle lookup algorithm. -->
    <fmt:setBundle basename="com.sun.ts.tests.jstl.common.resources.Resources"/>
    <tck:config op="get" var="ctx" configVar="localectx"/>
    <c:if test="${ctx != null}">
        Property LocalizationContext properly set.<br>
        Message: <fmt:message key="mkey"/><br>
    </c:if>
    <tck:config op="remove" configVar="localectx"/>

    <fmt:setBundle basename='<%= (String) pageContext.getAttribute("base") %>'/>
    <tck:config op="get" var="ctx" configVar="localectx"/>
    <c:if test="${ctx != null}">
        Property LocalizationContext properly set.<br>
        Message: <fmt:message key="mkey"/><br>
    </c:if>
    <tck:config op="remove" configVar="localectx"/>
</tck:test>
