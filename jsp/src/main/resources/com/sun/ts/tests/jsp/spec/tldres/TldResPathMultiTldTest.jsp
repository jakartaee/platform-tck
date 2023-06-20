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
    As of JSP 1.2 multiple TLD's can be packaged within a JAR file.
    Validate the container registers the taglibrary based on the URI
    specified in the <uri> element of the package TLDs.
--%>

<%@ taglib uri="http://java.sun.com/tck/jsp/multi1" prefix="m1" %>
<%@ taglib uri="http://java.sun.com/tck/jsp/multi2" prefix="m2" %>

<m1:multi1 />
<m2:multi2 />
