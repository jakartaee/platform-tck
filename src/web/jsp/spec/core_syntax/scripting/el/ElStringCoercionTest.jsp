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

<%@ page contentType="text/plain"%>
<%@ taglib uri="http://java.sun.com/tck/jsp/el" prefix="el"%>

<jsp:useBean id="type" scope="page"
	class="com.sun.ts.tests.jsp.spec.core_syntax.scripting.el.TypeBean" />

<el:checkCoercion name="String" control='<%= "strng" %>'
	string="${'strng'}" />
<el:checkCoercion name="BytePrim" control='<%= "0" %>'
	string="${type.bytePrim}" />
<el:checkCoercion name="CharPrim" control='<%= "1" %>'
	string="${type.charPrim}" />
<el:checkCoercion name="ShortPrim" control='<%= "2" %>'
	string="${type.shortPrim}" />
<el:checkCoercion name="IntPrim" control='<%= "3" %>'
	string="${type.intPrim}" />
<el:checkCoercion name="LongPrim" control='<%= "4" %>'
	string="${type.longPrim}" />
<el:checkCoercion name="FloatPrim" control='<%= "5.5" %>'
	string="${type.floatPrim}" />
<el:checkCoercion name="DoublePrim" control='<%= "6.5" %>'
	string="${type.doublePrim}" />
<el:checkCoercion name="BooleanPrim" control='<%= "false" %>'
	string="${type.booleanPrim}" />
<el:checkCoercion name="Byte" control='<%= "30" %>'
	string="${type.bite}" />
<el:checkCoercion name="Char"
	control='<%= new Character((char) 31).toString() %>'
	string="${type.chr}" />
<el:checkCoercion name="Short" control='<%= "32" %>'
	string="${type.shrt}" />
<el:checkCoercion name="Int" control='<%= "33" %>' string="${type.inti}" />
<el:checkCoercion name="Long" control='<%= "34" %>' string="${type.lng}" />
<el:checkCoercion name="Float" control='<%= "35.5" %>'
	string="${type.flote}" />
<el:checkCoercion name="Double" control='<%= "36.5" %>'
	string="${type.dble}" />
<el:checkCoercion name="Boolean" control='<%= "true" %>'
	string="${type.callable}" />
<el:checkCoercion name="Object" control='<%= "TypeBean;v1.0" %>'
	string="${type}" display="true" />
