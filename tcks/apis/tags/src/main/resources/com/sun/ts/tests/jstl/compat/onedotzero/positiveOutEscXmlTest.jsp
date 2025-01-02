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

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jstl/xml" %>
<%@ taglib prefix="x_rt" uri="http://java.sun.com/jstl/xml_rt" %>
<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<tck:test testName="positiveOutEscXmlTest">
    <x:parse var="doc">
        <a>
          <![CDATA[< > & ' "]]>
        </a>
    </x:parse>
    <!-- EL: If escapeXml is true, the following characters will
             be converted to their corresponding entity codes:
             < -> &lt;
             > -> &gt;
             & -? &amp;
             ' -> &#039;
             " -> &#034
             If false, no escaping will occur.
             If escapeXml is not specified, escaping will occur. -->
     escapeXml == true: <x:out select="$doc//a" escapeXml="${true}"/><br>
     escapeXml == false: <x:out select="$doc//a" escapeXml="false"/><br>
     escapeXml == not specified: <x:out select="$doc//a"/><br>

     <!-- RT: If escapeXml is true, the following characters will
             be converted to their corresponding entity codes:
             < -> &lt;
             > -> &gt;
             & -? &amp;
             ' -> &#039;
             " -> &#034
             If false, no escaping will occur.
             If escapeXml is not specified, escaping will occur. -->
     escapeXml == true: <x_rt:out select="$doc//a" 
                                  escapeXml='<%= true %>'/>
     escapeXml == false: <x_rt:out select="$doc//a" escapeXml="false"/><br>
     escapeXml == not specified: <x_rt:out select="$doc//a"/><br>
</tck:test>
