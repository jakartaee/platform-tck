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

<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fmt_rt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<tck:test testName="positiveBundleLocalizationScopeTest">
    <!-- EL: If the scope of the localization is limited to the 
             scope of the bundle actions, body content.  A message 
             action outside the body will return ???<KEY>??? as its
             not a part of the context. -->
    <fmt:bundle basename="com.sun.ts.tests.jstl.common.resources.Resources">
        Should be 'en message': <fmt:message key="mkey"/><br>
    </fmt:bundle>
    Should be ???mkey???: <fmt:message key="mkey"/><br>

    <!-- RT: If the scope of the localization is limited to the 
             scope of the bundle actions, body content.  A message 
             action outside the body will return ???<KEY>??? as its
             not a part of the context. -->
    <fmt_rt:bundle basename="com.sun.ts.tests.jstl.common.resources.Resources">
        Should be 'en message': <fmt:message key="mkey"/><br>
    </fmt_rt:bundle>
    Should be ???mkey???: <fmt:message key="mkey"/><br>
</tck:test>
