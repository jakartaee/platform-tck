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
        <h:selectOneRadio id="radio1">
          <f:selectItem id="radio1Item1"
                        itemLabel="foo"
                        itemValue="true" />
          <f:selectItem id="radio1Item2"
                        itemLabel="bar"
                        itemValue="false" />
        </h:selectOneRadio>
        <h:selectOneRadio id="radio2"
                          disabledClass="Color: red;"
                          enabledClass="text">
          <f:selectItem id="radio2Item1"
                        itemLabel="foo"
                        itemDisabled="true" />
          <f:selectItem id="radio2Item2"
                        itemLabel="bar"
                        itemDisabled="false" />
        </h:selectOneRadio>
        <h:selectOneRadio id="radio3"
                          styleClass="text">
          <f:selectItem id="radio3Item1"
                        itemLabel="foo" />
          <f:selectItem id="radio3Item2"
                        itemLabel="bar" />
        </h:selectOneRadio>
        <h:selectOneRadio id="radio4"
                          disabled="true">
          <f:selectItem id="radio4Item1"
                        itemLabel="foo" />
          <f:selectItem id="radio4Item2"
                        itemLabel="bar" />
        </h:selectOneRadio>
        <h:selectOneRadio id="radio5"
                          disabled="false">
          <f:selectItem id="radio5Item1"
                        itemLabel="foo" />
          <f:selectItem id="radio5Item2"
                        itemLabel="bar" />
        </h:selectOneRadio>
        <h:selectOneRadio id="radio6"
                          readonly="true">
          <f:selectItem id="radio6Item1"
                        itemLabel="foo" />
          <f:selectItem id="radio6Item2"
                        itemLabel="bar" />
        </h:selectOneRadio>
        <h:selectOneRadio id="radio7"
                          readonly="false">
          <f:selectItem id="radio7Item1"
                        itemLabel="foo" />
          <f:selectItem id="radio7Item2"
                        itemLabel="bar" />
        </h:selectOneRadio>
        <h:selectOneRadio id="radio8">
          <f:selectItem id="radio8Item1"
                        itemLabel="foo" />
          <f:selectItem id="radio8Item2"
                        itemLabel="bar" />
        </h:selectOneRadio>
        <h:selectOneRadio id="radio9" layout="lineDirection">
          <f:selectItem id="radio9Item1"
                        itemLabel="foo" />
          <f:selectItem id="radio9Item2"
                        itemLabel="bar" />
        </h:selectOneRadio>
        <h:selectOneRadio id="radio10" layout="pageDirection">
          <f:selectItem id="radio10Item1"
                        itemLabel="foo" />
          <f:selectItem id="radio10Item2"
                        itemLabel="bar" />
        </h:selectOneRadio>
        <h:selectOneRadio id="radio11" border="11">
          <f:selectItem id="radio11Item1"
                        itemLabel="&foo" escape="false"/>
          <f:selectItem id="radio11Item2"
                        itemLabel="&bar" />
        </h:selectOneRadio>
        <!-- Test the binding attribute -->
        <h:selectOneRadio id="radio12" binding="#{Answer.yesNo}"/>
      </h:form>
    </f:view>
  </body>
</html>
