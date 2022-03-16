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

<%
    try {
%>
<el:checkCoercion control='<%= new Boolean(true) %>' boolean="${type}" />
Test FAILED. Failure didn't occur when attempting to coerce 'TypeBean'
to a Boolean.
<%
    } catch (Throwable t) {
    }
%>

<%-- Validate coercions to Boolean --%>
<el:checkCoercion name="BooleanToBoolean"
	control='<%= new Boolean(true) %>' boolean="${type.booln}">
	<el:checkCoercion name="StringToBoolean"
		control='<%= new Boolean(false) %>' boolean="${'false'}">

		<el:checkCoercion name="EmptyStringToBoolean"
			control='<%= new Boolean(false) %>' boolean="${''}" display="true" />
	</el:checkCoercion>
</el:checkCoercion>
