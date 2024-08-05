<%--

    Copyright (c) 2003, 2022 Oracle and/or its affiliates and others.
    All rights reserved.

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

<%--
     Validate the body of jsp:params can be provided through the use
     of jsp:body
--%>
<jsp:plugin type="bean" code="foo.class" codebase="/" >
    <jsp:params>
        <jsp:body>
            <jsp:param name="param1" value="value1" />
        </jsp:body>
    </jsp:params>
</jsp:plugin>
expected_text