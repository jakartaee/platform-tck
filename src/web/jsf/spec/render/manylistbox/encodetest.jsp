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
                <h:selectManyListbox id="listbox1"> 
                    <f:selectItem id="box1Item1"
                    itemLabel="foo"
                    itemValue="true" />
                    <f:selectItem id="box1Item2"
                    itemLabel="bar"
                    itemValue="false" />
                </h:selectManyListbox> 
                <h:selectManyListbox id="listbox2" 
                    disabledClass="Color: red;"
                    enabledClass="text">
                    <f:selectItem id="box2Item1"
                    itemLabel="foo"
                    itemDisabled="true" />
                    <f:selectItem id="box2Item2"
                    itemLabel="bar"
                    itemDisabled="false" />
                </h:selectManyListbox> 
                <h:selectManyListbox id="listbox3" 
                    styleClass="text">
                    <f:selectItem id="box3Item1" 
                    itemLabel="foo" />
                    <f:selectItem id="box3Item2" 
                    itemLabel="bar" />
                </h:selectManyListbox> 
                <h:selectManyListbox id="listbox4"
                    disabled="true">
                    <f:selectItem id="box4Item1"
                    itemLabel="foo" />
                    <f:selectItem id="box4Item2"
                    itemLabel="bar" />
                </h:selectManyListbox> 
                <h:selectManyListbox id="listbox5"
                    disabled="false">
                    <f:selectItem id="box5Item1"
                    itemLabel="foo" />
                    <f:selectItem id="box5Item2"
                    itemLabel="bar" />
                </h:selectManyListbox> 
                <h:selectManyListbox id="listbox6"
                    readonly="true">
                    <f:selectItem id="box6Item1"
                    itemLabel="foo" />
                    <f:selectItem id="box6Item2"
                    itemLabel="bar" />
                </h:selectManyListbox> 
                <h:selectManyListbox id="listbox7"
                    readonly="false">
                    <f:selectItem id="box7Item1"
                    itemLabel="foo" />
                    <f:selectItem id="box7Item2"
                    itemLabel="bar" />
                </h:selectManyListbox> 
                <h:selectManyListbox id="listbox8"
                    size="5">
                    <f:selectItem id="box8Item1"
                    itemLabel="foo" />
                    <f:selectItem id="box8Item2"
                    itemLabel="bar" />
                    <f:selectItem id="box8Item3"
                    itemLabel="again" />
                    <f:selectItem id="box8Item4"
                    itemLabel="and" />
                    <f:selectItem id="box8Item5"
                    itemLabel="somemore" />
                </h:selectManyListbox> 
                <%--test binding attribute --%>
                <h:selectManyListbox id="listbox9" binding="#{Answer.yesNo}"/>
            </h:form>
        </f:view>
    </body>
</html>
