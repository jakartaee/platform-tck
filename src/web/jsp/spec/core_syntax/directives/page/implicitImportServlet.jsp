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
<body>
<% /**	Name: implicitImportServlet
		Description: Use jsp page directive with language="java" 
			    Do not specify the import attribute. The javax.servlet
			    package should be available implicitly.  Validate
                that a RequestDispatch object can be created.
			    
		Result:No error
**/ %>		

<!-- language=java and we check if implicit import works -->

<%@ page language="java"  %>

<%

try{

 RequestDispatcher rd = getServletConfig().getServletContext().getRequestDispatcher( "/implicit.jsp");
 rd.forward(request, response);

  }catch(Exception o){o.printStackTrace();}

%>
</body>
</html>	  
	  
