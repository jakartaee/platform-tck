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
     Validate a taglibrary can be detected when packaged
     as specified in the JSP 1.1 specification.  No
     translation error should occur if properly found
     by the container and the tag should work as expected.
--%>

<%@ taglib uri="/WEB-INF/lib/jsp11taglib.jar" prefix="taglib" %>
<taglib:tld11 />

