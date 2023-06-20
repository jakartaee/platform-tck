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

<%--
    Validate:
         - The 'value' attributes acceptance of RT/EL expressions.
         - The ablity to provide values for the 'name' and 'value'
         - attributes via jsp:attribute actions
--%>

<jsp:include page="Resource.jsp">
    <jsp:param name="param1" value="${'value2'}" />
    <jsp:param name="param2" value='<%= "value2" %>' />
</jsp:include>

<jsp:include page="Resource.jsp">
    <jsp:param value="value2">
        <jsp:attribute name="name" trim="true">
            param1
        </jsp:attribute>
    </jsp:param>
    <jsp:param name="param2">
        <jsp:attribute name="value" trim="true">
            value2
        </jsp:attribute>
    </jsp:param>
</jsp:include>
