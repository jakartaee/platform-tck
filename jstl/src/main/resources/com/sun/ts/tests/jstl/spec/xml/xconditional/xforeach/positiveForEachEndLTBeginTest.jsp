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
<%@ taglib uri="jakarta.tags.xml" prefix="x" %>
 <x:parse var="doc">
        <a>
            <b>btext1</b>
            <b>btext2</b>
            <b>btext3</b>
            <b>btext4</b>
            <b>btext5</b>
            <b>btext6</b>
            <b>btext7</b>
        </a>
    </x:parse>
<x:forEach select="$doc//b" begin="2" end="1">
    Test FAILED
</x:forEach>
