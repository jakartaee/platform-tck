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
        <h:selectManyCheckbox id="checkbox1">
          <f:selectItem id="checkbox1Item1"
                        itemLabel="foo"
                        itemValue="true" />
          <f:selectItem id="checkbox1Item2"
                        itemLabel="bar"
                        itemValue="false" />
        </h:selectManyCheckbox>
        <h:selectManyCheckbox id="checkbox2"
                              disabledClass="Color: red;"
                              enabledClass="text">
          <f:selectItem id="checkbox2Item1"
                        itemLabel="foo"
                        itemDisabled="true" />
          <f:selectItem id="checkbox2Item2"
                        itemLabel="bar"
                        itemDisabled="false" />
        </h:selectManyCheckbox>
        <h:selectManyCheckbox id="checkbox3"
                              styleClass="text">
          <f:selectItem id="checkbox3Item1"
                        itemLabel="foo" />
          <f:selectItem id="checkbox3Item2"
                        itemLabel="bar" />
        </h:selectManyCheckbox>
        <h:selectManyCheckbox id="checkbox4"
                              disabled="true">
          <f:selectItem id="checkbox4Item1"
                        itemLabel="foo" />
          <f:selectItem id="checkbox4Item2"
                        itemLabel="bar" />
        </h:selectManyCheckbox>
        <h:selectManyCheckbox id="checkbox5"
                              disabled="false">
          <f:selectItem id="checkbox5Item1"
                        itemLabel="foo" />
          <f:selectItem id="checkbox5Item2"
                        itemLabel="bar" />
        </h:selectManyCheckbox>
        <h:selectManyCheckbox id="checkbox6"
                              readonly="true">
          <f:selectItem id="checkbox6Item1"
                        itemLabel="foo" />
          <f:selectItem id="checkbox6Item2"
                        itemLabel="bar" />
        </h:selectManyCheckbox>
        <h:selectManyCheckbox id="checkbox7"
                              readonly="false">
          <f:selectItem id="checkbox7Item1"
                        itemLabel="foo" />
          <f:selectItem id="checkbox7Item2"
                        itemLabel="bar" />
        </h:selectManyCheckbox>
        <h:selectManyCheckbox id="checkbox8">
          <f:selectItem id="checkbox8Item1"
                        itemLabel="foo" />
          <f:selectItem id="checkbox8Item2"
                        itemLabel="bar" />
        </h:selectManyCheckbox>
        <h:selectManyCheckbox id="checkbox9" layout="lineDirection">
          <f:selectItem id="checkbox9Item1"
                        itemLabel="foo" />
          <f:selectItem id="checkbox9Item2"
                        itemLabel="bar" />
        </h:selectManyCheckbox>
        <h:selectManyCheckbox id="checkbox10" layout="pageDirection">
          <f:selectItem id="checkbox10Item1"
                        itemLabel="foo" />
          <f:selectItem id="checkbox10Item2"
                        itemLabel="bar" />
        </h:selectManyCheckbox>
        <h:selectManyCheckbox id="checkbox11" border="11">
          <f:selectItem id="checkbox11Item1"
                        itemLabel="foo" />
          <f:selectItem id="checkbox11Item2"
                        itemLabel="bar" />
        </h:selectManyCheckbox>
        <%-- binding attribute test. --%>
        <h:selectManyCheckbox id="checkbox12" binding="#{Answer.yesNo}"/>
      </h:form>
    </f:view>
  </body>
</html>
