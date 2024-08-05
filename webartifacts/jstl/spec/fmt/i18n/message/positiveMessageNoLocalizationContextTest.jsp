<%--

    Copyright (c) 2003, 2020 Oracle and/or its affiliates. All rights reserved.

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

<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<%@ page import="jakarta.servlet.jsp.jstl.fmt.LocalizationContext" %>
<tck:test testName="positiveMessageNoLocalizationContextTest">
    <fmt:setBundle basename="nosuchbundle" var="bundle"/>

    <!-- If the I18N localization context that this action
             determines, does not have any resource bundle,
             an error message of teh form ???<key>??? is produced. -->

    Null LocalizationContext with null ResourceBundle provided:<br>
    <fmt:message bundle='<%= (LocalizationContext) pageContext.getAttribute("bundle") %>' key="mkey"/><br>

    Message wrapped by bundle action with a non existing ResourceBundle:<br>
    <fmt:bundle basename="nosuchbundle">
        <fmt:message key="mkey"/><br>
    </fmt:bundle>

    
    No ResourceBundle found based based on localizationContext variable.<br>
    <fmt:setBundle basename="nosuchbundle"/>
    <fmt:message key="mkey"/><br>
    <tck:config op="remove" configVar="localectx"/>

</tck:test>
