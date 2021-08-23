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

<%@ page language="java" %>
<%@ page import="javax.naming.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.sun.ts.tests.ejb.ee.bb.localaccess.webaccesstest.*" %>
<%@ page import="com.sun.ts.lib.util.*" %>
<%@ page import="com.sun.ts.lib.porting.*" %>
 
<%
    java.util.Properties harnessProps = null;
    java.util.Enumeration enum1 = request.getParameterNames();
    if (enum1.hasMoreElements())
        harnessProps = new java.util.Properties();
    while (enum1.hasMoreElements()) {
        String name = (String) enum1.nextElement();
        String value = request.getParameter(name);
        harnessProps.setProperty(name, value);
    }


    Properties p = new Properties();
    ALocal aLocalRef = null;
    String s;
    try {
            TSNamingContext nctx = new TSNamingContext();
            TestUtil.logMsg("Lookup local home of Entity Bean (CMP) and do create");
            ALocalHome  aLocalHome =
		(ALocalHome)nctx.lookup("java:comp/env/ejb/AEJB");
            aLocalRef = (ALocal) aLocalHome.createA(1, "a1", 1);
        try {
            TestUtil.logMsg("Call business method on local EJB");
            s = aLocalRef.whoAmI();
	    p.setProperty("whoAmI", s);
	    TestUtil.logMsg("whoAmI: " +s);
        } catch (Exception e) {
            TestUtil.logErr("Caught exception: " + e.getMessage());
            TestUtil.printStackTrace(e);
        } finally {
            try {
       		TestUtil.logMsg("Removing local EJB");
                aLocalRef.remove();
            }
            catch (Exception e) {
                TestUtil.printStackTrace(e);
            }
        }

    } catch(Exception e) {
        e.printStackTrace();
        out.println(e.toString());
    }

   StringBuffer lep = new StringBuffer();
    java.util.Enumeration key = p.keys();
    String name;
    while (key.hasMoreElements())
    {
     name = (String)key.nextElement();
     lep.append(name+"="+p.getProperty(name)+"\n");
    }

%>


<%= lep.toString() %>

