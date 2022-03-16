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

<%@ page contentType="text/plain" %>

<jsp:useBean id="type" scope="page"
              class="com.sun.ts.tests.jsp.spec.core_syntax.scripting.el.TypeBean" />

<%-- Validate Boxed types can be properly converted to primitive types
     and primitives types can be converted to boxed types.  If the
     test passes, no visible output will be displayed.  If the test
     fails, it will be manifest itself as a translation error. --%>

<%-- boxed to primitive --%>
<jsp:setProperty name="type" property="booleanPrim" value="${type.booln}" />
<jsp:setProperty name="type" property="bytePrim" value="${type.bite}" />
<jsp:setProperty name="type" property="charPrim" value="${type.chr}" />
<jsp:setProperty name="type" property="shortPrim" value="${type.shrt}" />
<jsp:setProperty name="type" property="intPrim" value="${type.inti}" />
<jsp:setProperty name="type" property="longPrim" value="${type.lng}" />
<jsp:setProperty name="type" property="floatPrim" value="${type.flote}" />
<jsp:setProperty name="type" property="doublePrim" value="${type.dble}" />

<%-- primitive to boxed --%>
<jsp:setProperty name="type" property="booln" value="${type.booleanPrim}" />
<jsp:setProperty name="type" property="bite" value="${type.bytePrim}" />
<jsp:setProperty name="type" property="chr" value="${type.charPrim}" />
<jsp:setProperty name="type" property="shrt" value="${type.shortPrim}" />
<jsp:setProperty name="type" property="inti" value="${type.intPrim}" />
<jsp:setProperty name="type" property="lng" value="${type.longPrim}" />
<jsp:setProperty name="type" property="flote" value="${type.floatPrim}" />
<jsp:setProperty name="type" property="dble" value="${type.doublePrim}" />
