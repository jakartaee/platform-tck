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
<%@ taglib uri="http://java.sun.com/tck/jsp/el" prefix="el"  %>

 <el:checkLiteral name="Floating Point" control='<%= new Float(8.1F) %>'
                 object="${8.1}">
    <el:checkLiteral name="Floating Point" control='<%= new Float(-70.2F) %>'
                     object="${-70.2}">
        <el:checkLiteral name="Floating Point" control='<%= new Float(8.1e4F) %>'
                         object="${8.1e4}">
          <el:checkLiteral name="Floating Point" control='<%= new Float(8.1E6F) %>'
                           object="${8.1E6}" >
            <el:checkLiteral name="Floating Point" control='<%= new Float(8.1e-9F) %>'
                             object="${8.1e-9}" >
                <el:checkLiteral name="Floating Point" control='<%= new Float(8.1E+3F) %>'
                                 object="${8.1E+3}">
                   <el:checkLiteral name="Floating Point" control='<%= new Float(-.72F) %>'
                                    object="${-.72}">
                       <el:checkLiteral name="Floating Point" control='<%= new Float(.999F) %>'
                                        object="${.999}">
                          <el:checkLiteral name="Floating Point" control='<%= new Float(-.1e1F) %>'
                                           object="${-.1e1}">
                             <el:checkLiteral name="Floating Point" control='<%= new Float(.234E22F) %>'
                                              object="${.234E22}">
                                <el:checkLiteral name="Floating Point" control='<%= new Float(-.3444e-2F) %>'
                                                  object="${-.3444e-2}">
                                     <el:checkLiteral name="Floating Point" control='<%= new Float(.5E+7F) %>'
                                                      object="${.5E+7}">
                                         <el:checkLiteral name="Floating Point" control='<%= new Float(-1e1F) %>'
                                                          object="${-1e1}">
                                              <el:checkLiteral name="Floating Point" control='<%= new Float(234E2F) %>'
                                                               object="${234E2}">
                                                 <el:checkLiteral name="Floating Point" control='<%= new Float(-3444e-2F) %>'
                                                                  object="${-3444e-2}">
                                                      <el:checkLiteral name="Floating Point" control='<%= new Float(5E+7F) %>'
                                                                      object="${5E+7}" display="true"/>
                                                 </el:checkLiteral>
                                              </el:checkLiteral>
                                          </el:checkLiteral>
                                      </el:checkLiteral>
                                  </el:checkLiteral>
                              </el:checkLiteral>
                          </el:checkLiteral>
                      </el:checkLiteral>
                  </el:checkLiteral>
              </el:checkLiteral>
          </el:checkLiteral>
        </el:checkLiteral>
    </el:checkLiteral>
  </el:checkLiteral>
</el:checkLiteral>
