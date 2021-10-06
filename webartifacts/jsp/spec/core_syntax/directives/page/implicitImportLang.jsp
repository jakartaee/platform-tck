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
<title>implicitImportLang</title>
<body>
<% /**	Name: implicitImportLang
		Description: Use a jsp directive with language="java" 
			    Do not specify the import attribute.  The java.lang 
                package should be available implicitly.  Validate
                that a String and Integer object can be created.
			    
		Result:No error
**/ %>
<!-- language=java and check if implicit import works -->
<%@ page language="java" %>



<%  
    String str="sun";
     out.println(str);
    Integer i=new Integer(5);
    String x=i.toString();
    out.println(x);


 %>

</body>
</html>
