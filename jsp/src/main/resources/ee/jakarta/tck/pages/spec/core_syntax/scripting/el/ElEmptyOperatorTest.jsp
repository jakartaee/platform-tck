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

<%@ page import="java.util.List,
                 java.util.ArrayList,
                 java.util.Map,
                 java.util.HashMap,
                 java.util.Collection"%>

<%@ page contentType="text/plain" %>
<%@ taglib uri="http://java.sun.com/tck/jsp/el" prefix="el" %>

<%
    List eList = new ArrayList();
    List neList = new ArrayList();
    neList.add("string");
    Collection eCollection = eList;
    Collection neCollection = neList;
    Map eMap = new HashMap();
    Map neMap = new HashMap();
    neMap.put("key", "value");
    String[] eArray = new String[] { };
    String[] neArray = new String[] { "string" };
    pageContext.setAttribute("emptyList", eList);
    pageContext.setAttribute("nonEmptyList", neList);
    pageContext.setAttribute("emptyCollection", eCollection);
    pageContext.setAttribute("nonEmptyCollection", neCollection);
    pageContext.setAttribute("emptyMap", eMap);
    pageContext.setAttribute("nonEmptyMap", neMap);
    pageContext.setAttribute("emptyArray", eArray);
    pageContext.setAttribute("nonEmptyArray", neArray);
    pageContext.setAttribute("emptyString", "");
    pageContext.setAttribute("nonEmptyString", "string");
    Boolean False = new Boolean(false);
    Boolean True = new Boolean(true);
%>

<%-- Validate the EL 'empty' operator --%>
<el:checkOperator name="Empty List" control='<%= True %>' object="${empty emptyList}">
 <el:checkOperator name="Non-Empty List" control='<%= False %>' object="${empty nonEmptyList}">
  <el:checkOperator name="Empty Map" control='<%= True %>' object="${empty emptyMap}">
   <el:checkOperator name="Non-Empty Map" control='<%= False %>' object="${empty nonEmptyMap}">
    <el:checkOperator name="Empty Array" control='<%= True %>' object="${empty emptyArray}">
     <el:checkOperator name="Non-Empty Array" control='<%= False %>' object="${empty nonEmptyArray}">
      <el:checkOperator name="Empty String" control='<%= True %>' object="${empty emptyString}">
       <el:checkOperator name="Non-Empty String" control='<%= False %>' object="${empty nonEmptyString}">
        <el:checkOperator name="Empty Collection" control='<%= True %>' object="${empty emptyCollection}">
         <el:checkOperator name="Non-Empty Collection" control='<%= False %>' object="${empty nonEmptyCollection}">
          <el:checkOperator name="Null" control='<%= True %>' object="${empty pageScope.nillValue}"
                          display="true" />
         </el:checkOperator>
        </el:checkOperator>
       </el:checkOperator>
      </el:checkOperator>
     </el:checkOperator>
    </el:checkOperator>
   </el:checkOperator>
  </el:checkOperator>
 </el:checkOperator>
</el:checkOperator>

