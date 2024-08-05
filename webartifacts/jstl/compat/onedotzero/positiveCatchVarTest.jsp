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
<tck:test testName="positiveCatchVarTest">
    <!-- EL: Validate that if var is specified, and an exception is
             thrown, that the exception is stored in var. -->
    <c:catch var="ex">
        <tck:throwit/>
    </c:catch>
    <tck:isInstance varName="ex" type="java.lang.IllegalArgumentException"/>

    <!-- RT: Validate that if var is specified, and an exception is
             thrown, that the exception is stored in var. -->
    <c_rt:catch var="ex">
        <tck:throwit/>
    </c_rt:catch>
    <tck:isInstance varName="ex" type="java.lang.IllegalArgumentException"/>
</tck:test>
