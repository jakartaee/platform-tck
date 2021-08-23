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
<jsp:plugin>
    <jsp:attribute name="type">bean</jsp:attribute>
    <jsp:attribute name="code">foo.class</jsp:attribute>
    <jsp:attribute name="codebase">/</jsp:attribute>
    <jsp:attribute name="align">middle</jsp:attribute>
    <jsp:attribute name="archive">test.jar</jsp:attribute>
    <jsp:attribute name="height">10</jsp:attribute>
    <jsp:attribute name="vspace">1</jsp:attribute>
    <jsp:attribute name="width">10</jsp:attribute>
    <jsp:attribute name="hspace">1</jsp:attribute>
    <jsp:attribute name="jreversion">1.3.1</jsp:attribute>
    <jsp:attribute name="name">test</jsp:attribute>
    <jsp:attribute name="nspluginurl">
        http://www.nowaythiswebsitecouldpossiblyexist.com
    </jsp:attribute>
    <jsp:attribute name="iepluginurl">
        http://www.nowaythis websitecouldpossibleexist.com
    </jsp:attribute>
    <jsp:body>
        <jsp:params>
            <jsp:param name="test" value="testvalue"/>
        </jsp:params>
        <jsp:fallback>fallback_text</jsp:fallback>
    </jsp:body>
</jsp:plugin>
