<%--

    Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.

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

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

<html>
    <head>
        <title>encodetest</title>
        <style type="text/css">
            .text {
                background-color: blue;
            }
        </style>
    </head>
    
    <body>
        <f:view>
            <h:form id="form">
                <h:selectOneMenu id="menu1"> 
                    <f:selectItem id="menu1Item1"
                                  itemLabel="foo"
                                  itemValue="true" />
                    <f:selectItem id="menu1Item2"
                                  itemLabel="bar"
                                  itemValue="false" />
                </h:selectOneMenu> 
                <h:selectOneMenu id="menu2" 
                                 disabledClass="Color: red;"
                                 enabledClass="text">
                    <f:selectItem id="menu2Item1"
                                  itemLabel="foo"
                                  itemDisabled="true" />
                    <f:selectItem id="menu2Item2"
                                  itemLabel="bar"
                                  itemDisabled="false" />
                </h:selectOneMenu> 
                <h:selectOneMenu id="menu3" 
                                 styleClass="text">
                    <f:selectItem id="menu3Item1" 
                                  itemLabel="foo" />
                    <f:selectItem id="menu3Item2" 
                                  itemLabel="bar" />
                </h:selectOneMenu> 
                <h:selectOneMenu id="menu4"
                                 disabled="true">
                    <f:selectItem id="menu4Item1"
                                  itemLabel="foo" />
                    <f:selectItem id="menu4Item2"
                                  itemLabel="bar" />
                </h:selectOneMenu> 
                <h:selectOneMenu id="menu5"
                                 disabled="false">
                    <f:selectItem id="menu5Item1"
                                  itemLabel="foo" />
                    <f:selectItem id="menu5Item2"
                                  itemLabel="bar" />
                </h:selectOneMenu> 
                <h:selectOneMenu id="menu6"
                                 readonly="true">
                    <f:selectItem id="menu6Item1"
                                  itemLabel="foo" />
                    <f:selectItem id="menu6Item2"
                                  itemLabel="bar" />
                </h:selectOneMenu> 
                <h:selectOneMenu id="menu7"
                                 readonly="false">
                    <f:selectItem id="menu7Item1"
                                  itemLabel="foo" />
                    <f:selectItem id="menu7Item2"
                                  itemLabel="bar" />
                </h:selectOneMenu> 
                <!-- Test the binding attribute -->
                <h:selectOneMenu id="menu8" binding="#{Answer.yesNo}"/>
            </h:form>
        </f:view>
    </body>
</html>
