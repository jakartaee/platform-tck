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

<%@ page contentType="text/plain" import="java.io.Reader" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>

<tags:DoBodyVarReaderTag>
    Body Content
</tags:DoBodyVarReaderTag>

<%
    Object export = pageContext.getAttribute("reqVar", PageContext.REQUEST_SCOPE);
    if (export != null) {
        if (export instanceof Reader) {
            StringBuffer sb = new StringBuffer();
            Reader reader = (Reader) export;
            for (int read = reader.read(); read != -1; read = reader.read()) {
                sb.append((char) read);
            }
            String result = sb.toString().trim();
            if (result.equals("Body Content")) {
                reader.reset();
                sb = new StringBuffer();
                for (int read = reader.read(); read != -1; read = reader.read()) {
                    sb.append((char) read);
                }
                result = sb.toString().trim();
                if (result.equals("Body Content")) {
                    out.println("Test PASSED");
                } else {
                    out.println("Test FAILED.  It seems the exported reader is" +
                        " not resettable.  Expected content of reader to be" +
                        " 'Body Content' after the reset.  Received: " +
                        result);
                }
            } else {
                out.println("Test FAILED.  Expected result of body evaluation to" +
                    " be 'Body Content'.  Received: " + result);
            }
        } else {
            out.println("Test FAILED.  The type of the exported variable was" +
                " not java.lang.String.  Received: " + export.getClass().getName());
        }
    } else {
        out.println("Test FAILED.  Variable 'reqVar' was not exported to the" +
            " request scope by the jsp:doBody action.");
    }
%>
