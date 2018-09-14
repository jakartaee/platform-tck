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
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>

<%--
    For each attribute declared and specified, a page-scoped variable
    must be created in the page scope of the JSP Context Wrapper.
    The name of the variable must be the same as the declared attribute name.
    The value of the variable must be the value of the attribute passed in
    during invocation.
--%>

<tags:DeclaredAttributesTag attr1="attrValue" attr2="attr2Value" />


