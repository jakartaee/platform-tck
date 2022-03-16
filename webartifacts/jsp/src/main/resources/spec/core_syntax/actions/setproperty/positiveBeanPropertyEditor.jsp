<%--

    Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.

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

<html>
<title>positiveBeanPropertyEditor</title>
<body>
<% /** 	Name : positiveBeanPropertyEditor
	Description : Using a bean that has been configured with a 
                  PropertyEditor, validate that the editors
                  are in fact used.
	Result : Output to the client (see comments below)
**/ %>
<jsp:useBean id="propertyBean" class="com.sun.ts.tests.jsp.spec.core_syntax.actions.setproperty.PropertyBean" />
<jsp:setProperty name="propertyBean" property="PString" value="Validated" />
<jsp:setProperty name="propertyBean" property="PBoolean" value="false" />
<jsp:setProperty name="propertyBean" property="PInteger" value="218" />

<!-- getProperty PString should return "PString Validated" -->
<jsp:getProperty name="propertyBean" property="PString" />

<!-- getProperty PBoolean should return "true" -->
<jsp:getProperty name="propertyBean" property="PBoolean" />

<!-- getProperty PInteger should return "218314" -->
<jsp:getProperty name="propertyBean" property="PInteger" />

</body>
</html>
