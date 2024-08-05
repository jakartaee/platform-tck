<%--

    Copyright (c) 2003, 2020 Oracle and/or its affiliates. All rights reserved.

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

<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fmt_rt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<tck:test testName="positiveDefaultEncodingTest">
    <c:remove var="jakarta.servlet.jsp.jstl.fmt.request.charset"/>
    <!-- EL: No content-type and no scoped variable, so the default
             of iso-8859-1 will be used -->
    <fmt:requestEncoding/>
    <%= request.getCharacterEncoding().toLowerCase() %>

   <!-- RT: No content-type and no scoped variable, so the default
             of iso-8859-1 will be used -->
    <fmt_rt:requestEncoding/>
    <%= request.getCharacterEncoding().toLowerCase() %>
    
</tck:test>
