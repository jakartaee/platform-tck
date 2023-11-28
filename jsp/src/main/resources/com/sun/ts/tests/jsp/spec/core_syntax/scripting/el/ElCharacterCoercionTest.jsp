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
<%@ taglib uri="http://java.sun.com/tck/jsp/el" prefix="el" %>

<jsp:useBean id="type" scope="page"
             class="com.sun.ts.tests.jsp.spec.core_syntax.scripting.el.TypeBean" />

<%-- Validate that if a Boolean or boolean is provided to
     an action that accepts a Character, an error occurs --%>
<% try { %>
<el:checkCoercion control='<%= new Boolean(false) %>' char="${type.booleanPrim}" />
Test FAILED.  Coercion from boolean to Char didn't result in an error.
<% } catch (Throwable t) { } %>

<% try { %>
<el:checkCoercion control='<%= new Boolean(false) %>' char="${type.booleanPrim}" />
Test FAILED.  Coercion from Boolean to Char didn't result in an error.
<% } catch (Throwable t) { } %>

<%-- Validate that if any uncoercable type is provided, an error is raised --%>
<% try { %>
<el:checkCoercion control='<%= new Boolean(false) %>' char="${type}" />
<% } catch (Throwable t) { } %>

<%-- Validate coercion to Character --%>

 <el:checkCoercion name="EmptyStringToChar" control='<%= new Character((char) 0) %>'
                   char="${''}">
  <el:checkCoercion name="CharToChar" control="<%= new Character((char) 31) %>"
                    char="${type.chr}">
   <el:checkCoercion name="ByteToChar" control="<%= new Character((char) 30) %>"
                     char="${type.bite}">
    <el:checkCoercion name="ShortToChar" control="<%= new Character((char) 32) %>"
                    char="${type.shrt}">
     <el:checkCoercion name="IntToChar" control="<%= new Character((char) 33) %>"
                    char="${type.inti}">
      <el:checkCoercion name="LongToChar" control="<%= new Character((char) 34) %>"
                     char="${type.lng}">
       <el:checkCoercion name="FloatToChar" control="<%= new Character((char) 35) %>"
                     char="${type.flote}">
        <el:checkCoercion name="DoubleToChar" control="<%= new Character((char) 36) %>"
                     char="${type.dble}">
         <el:checkCoercion name="StringToChar" control="<%= new Character('s') %>"
                           char="${'strng'}" display="true" />
        </el:checkCoercion>
       </el:checkCoercion>
      </el:checkCoercion>
     </el:checkCoercion>
    </el:checkCoercion>
   </el:checkCoercion>
  </el:checkCoercion>
 </el:checkCoercion>
