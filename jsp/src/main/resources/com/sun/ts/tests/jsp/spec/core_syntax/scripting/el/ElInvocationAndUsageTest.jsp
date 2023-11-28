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
<%
    pageContext.setAttribute("eval", "Evaluated");
%>
<%--
     EL is invoked via the construct ${expr}
     and must be interpreted by the container
     when provided as body content of a custom
     action, an attribute of a custom attribute
     that accepts rtexpr values or in template
     text outside of any action.  Expressions will
     not be evaluated when provided as body content
     to custom actions have a body type of 'tagdependent'.
 --%>

Expression in template text: ${pageScope.eval}
These should not be evaluated: {pageScope.eval}, pageScope.eval}, {pageScope.eval

<el:echo echo="${pageScope.eval}" />
<el:echo>
    Expression in body of action: ${pageScope.eval}
</el:echo>

<el:dependent>
    Expression in body of tagdependent body content tag: ${pageScope.eval}
</el:dependent>


