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
<%@ taglib uri="http://java.sun.com/tck/jsp/el" prefix="el" %>

<jsp:useBean id="type" scope="page"
             class="com.sun.ts.tests.jsp.spec.core_syntax.scripting.el.TypeBean" />
<jsp:useBean id="typeChild" scope="page"
             class="com.sun.ts.tests.jsp.spec.core_syntax.scripting.el.TypeBeanChild" />

<%-- If the target type has no PropertyEditor, fail --%>
<% try { %>
<el:checkCoercion control='<%= "TypeBean;v,1.0" %>' typeBean="${type}" />
<% } catch (Throwable t) { } %>

<el:checkCoercion name="Assignable" control='<%= "TypeBean;v,1.0" %>' typeBean="${typeChild}">
 <el:checkCoercion name="Null" control='<%= null %>' typeBean="${null}">
  <el:checkCoercion name="EmptyString" control='<%= null %>' typeBean="${''}" display="true"/>
 </el:checkCoercion>
</el:checkCoercion>
