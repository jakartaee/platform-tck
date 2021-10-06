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

<jsp:doBody>
    <jsp:attribute name="var">pageVar</jsp:attribute>
    <jsp:attribute name="scope">page</jsp:attribute>
</jsp:doBody>

<jsp:doBody>
    <jsp:attribute name="varReader">pageVarReader</jsp:attribute>
    <jsp:attribute name="scope">page</jsp:attribute>
</jsp:doBody>

<%
    Object obj1 = jspContext.getAttribute("pageVar");
    Object obj2 = jspContext.getAttribute("pageVarReader");

    if (obj1 != null) {
        if (obj1 instanceof String) {
            if (((String) obj1).trim().equals("Body Content")) {
                if (obj2 != null) {
                    if (obj2 instanceof java.io.Reader) {
                        StringBuffer sb = new StringBuffer();
                        java.io.Reader reader = (java.io.Reader) obj2;
                        for (int read = reader.read(); read != -1; read = reader.read()) {
                            sb.append((char) read);
                        }
                        obj2 = sb.toString().trim();
                        if (obj2.equals("Body Content")) {
                            out.println("Test PASSED");
                        } else {
                            out.println("Test FAILED.  Expected content of reader" +
                                " to be 'Body Content'.  Received: " + obj2);
                        }
                    } else {
                        out.println("Test FAILED.  a java.io.Reader object was" +
                            " not exported when varReader was specified.  Actual" +
                            " class type: " + obj2.getClass().getName());
                    }
                } else {
                    out.println("Test FAILED.  No object exported when varReader" +
                        " was specified.");
                }
            } else {
                out.println("Test FAILED.  Expected exported result to be " +
                    "'Body Content'.  Received: " + ((String) obj1).trim());
            }
        } else {
            out.println("Test FAILED.  Expected exported object for var to be" +
                " of type java.lang.String.  Received: " + obj1.getClass().getName());
        }
    } else {
        out.println("Test FAILED.  No object exported when 'var' attribute was" +
            " specified.");
    }
%>
