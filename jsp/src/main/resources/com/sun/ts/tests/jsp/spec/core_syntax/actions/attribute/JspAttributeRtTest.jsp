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
<%@ taglib uri="http://java.sun.com/tck/jsp/attribute" prefix="attr" %>

<attr:ctype>
    <jsp:attribute name="boolean">
        <%= new Boolean(true) %>
    </jsp:attribute>
    <jsp:attribute name="character">
        <%= new Character('a') %>
    </jsp:attribute>
    <jsp:attribute name="short">
        <%= new Short((short) 1) %>
    </jsp:attribute>
    <jsp:attribute name="integer">
        <%= new Integer(1) %>
    </jsp:attribute>
    <jsp:attribute name="long">
        <%= new Long(1l) %>
    </jsp:attribute>
    <jsp:attribute name="float">
        <%= new Float(1.0f) %>
    </jsp:attribute>
    <jsp:attribute name="double">
        <%= new Double(1.0d) %>
    </jsp:attribute>
    <jsp:attribute name="string">
        <%= "string" %>
    </jsp:attribute>
</attr:ctype>

<attr:ctype>
    <jsp:attribute name="boolean">
        <%= false %>
    </jsp:attribute>
    <jsp:attribute name="character">
        <%= 'b' %>
    </jsp:attribute>
    <jsp:attribute name="short">
        <%= (short) 2 %>
    </jsp:attribute>
    <jsp:attribute name="integer">
        <%= 2 %>
    </jsp:attribute>
    <jsp:attribute name="long">
        <%= 2l %>
    </jsp:attribute>
    <jsp:attribute name="float">
        <%= 2.0f %>
    </jsp:attribute>
    <jsp:attribute name="double">
        <%= 2.0d %>
    </jsp:attribute>
    <jsp:attribute name="string">
        <%= "string" %>
    </jsp:attribute>
</attr:ctype>

<attr:ctype>
    <jsp:attribute name="boolean">
        false
    </jsp:attribute>
    <jsp:attribute name="character">
        b
    </jsp:attribute>
    <jsp:attribute name="short">
        3
    </jsp:attribute>
    <jsp:attribute name="integer">
        3
    </jsp:attribute>
    <jsp:attribute name="long">
        3
    </jsp:attribute>
    <jsp:attribute name="float">
        3.0
    </jsp:attribute>
    <jsp:attribute name="double">
        3.0
    </jsp:attribute>
    <jsp:attribute name="string">
        <%= "string" %>
    </jsp:attribute>
</attr:ctype>

<attr:stype>
    <jsp:attribute name="boolean">
        <%= new Boolean(true) %>
    </jsp:attribute>
    <jsp:attribute name="character">
        <%= new Character('a') %>
    </jsp:attribute>
    <jsp:attribute name="short">
        <%= new Short((short) 1) %>
    </jsp:attribute>
    <jsp:attribute name="integer">
        <%= new Integer(1) %>
    </jsp:attribute>
    <jsp:attribute name="long">
        <%= new Long(1l) %>
    </jsp:attribute>
    <jsp:attribute name="float">
        <%= new Float(1.0f) %>
    </jsp:attribute>
    <jsp:attribute name="double">
        <%= new Double(1.0d) %>
    </jsp:attribute>
    <jsp:attribute name="string">
        <%= "string" %>
    </jsp:attribute>
</attr:stype>

<attr:stype>
    <jsp:attribute name="boolean">
        <%= false %>
    </jsp:attribute>
    <jsp:attribute name="character">
        <%= 'b' %>
    </jsp:attribute>
    <jsp:attribute name="short">
        <%= (short) 2 %>
    </jsp:attribute>
    <jsp:attribute name="integer">
        <%= 2 %>
    </jsp:attribute>
    <jsp:attribute name="long">
        <%= 2l %>
    </jsp:attribute>
    <jsp:attribute name="float">
        <%= 2.0f %>
    </jsp:attribute>
    <jsp:attribute name="double">
        <%= 2.0d %>
    </jsp:attribute>
    <jsp:attribute name="string">
        <%= "string" %>
    </jsp:attribute>
</attr:stype>

<attr:stype>
    <jsp:attribute name="boolean">
        false
    </jsp:attribute>
    <jsp:attribute name="character">
        b
    </jsp:attribute>
    <jsp:attribute name="short">
        3
    </jsp:attribute>
    <jsp:attribute name="integer">
        3
    </jsp:attribute>
    <jsp:attribute name="long">
        3
    </jsp:attribute>
    <jsp:attribute name="float">
        3.0
    </jsp:attribute>
    <jsp:attribute name="double">
        3.0
    </jsp:attribute>
    <jsp:attribute name="string">
        <%= "string" %>
    </jsp:attribute>
</attr:stype>
