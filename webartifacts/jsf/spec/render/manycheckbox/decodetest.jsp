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
    <title>decodetest</title>
  </head>

  <body>
    <f:view>
      <h:form id="form1">
        <h:selectManyCheckbox id="checkbox1">
          <f:selectItem id="checkbox1Item1"
                        itemLabel="foo"
                        itemValue="red" />
          <f:selectItem id="checkbox1Item2"
                        itemLabel="bar"
                        itemValue="green" />
        </h:selectManyCheckbox>
        <h:commandButton id="button1" value="Submit"/>
      </h:form>
      
      <h:form id="selectmany01">
        <p>
          Array Values
        </p>
        <h:selectManyCheckbox id="array" value="#{select01.arrayValues}">
          <f:selectItems value="#{select01.possibleValues}"/>
        </h:selectManyCheckbox>

        <p>
          Initially Null List Values
        </p>
        <h:selectManyCheckbox id="list" value="#{select01.listValues}">
          <f:selectItems value="#{select01.possibleValues}"/>
        </h:selectManyCheckbox>

        <p>
          Initially Null Set Values
        </p>
        <h:selectManyCheckbox id="set" value="#{select01.setValues}">
          <f:selectItems value="#{select01.possibleValues}"/>
        </h:selectManyCheckbox>

        <p>
          Initially Null Sorted Set Values
        </p>
        <h:selectManyCheckbox id="sortedset"
                              value="#{select01.sortedSetValues}">
          <f:selectItems value="#{select01.possibleValues}"/>
        </h:selectManyCheckbox>

        <p>
          Initially Null Collection Values
        </p>
        <h:selectManyCheckbox id="collection"
                              value="#{select01.collectionValues}">
          <f:selectItems value="#{select01.possibleValues}"/>
        </h:selectManyCheckbox>

        <!-- ///////////////////////////////////////////////////////////// -->

        <p>
          Initially Non-Null List Values
        </p>
        <h:selectManyCheckbox id="ilist" value="#{select01.initialListValues}">
          <f:selectItems value="#{select01.possibleValues}"/>
        </h:selectManyCheckbox>

        <p>
          Initially Non-Null Set Values
        </p>
        <h:selectManyCheckbox id="iset" value="#{select01.initialSetValues}">
          <f:selectItems value="#{select01.possibleValues}"/>
        </h:selectManyCheckbox>

        <p>
          Initially Non-Null Sorted Set Values
        </p>
        <h:selectManyCheckbox id="isortedset"
                              value="#{select01.initialSortedSetValues}">
          <f:selectItems value="#{select01.possibleValues}"/>
        </h:selectManyCheckbox>

        <p>
          Initially Non-Null Collection Values
        </p>
        <h:selectManyCheckbox id="icollection"
                              value="#{select01.initialCollectionValues}">
          <f:selectItems value="#{select01.possibleValues}"/>
        </h:selectManyCheckbox>

        <!-- ///////////////////////////////////////////////////////////// -->

        <p>
          Initially Null Collection Values using f:attribute hint (java.lang.String)
        </p>
        <h:selectManyCheckbox id="hintString"
                              value="#{select01.collectionFromHintValues}">
          <f:attribute name="collectionType" value="java.util.LinkedList"/>
          <f:selectItems value="#{select01.possibleValues}"/>
        </h:selectManyCheckbox>

        <p>
          Initially Null Collection Values using f:attribute hint (java.lang.String)
        </p>
        <h:selectManyCheckbox id="hintClass"
                              value="#{select01.collectionFromHintValues2}">
          <f:attribute name="collectionType"
                       value="#{select01.collectionType}"/>
          <f:selectItems value="#{select01.possibleValues}"/>
        </h:selectManyCheckbox>

        <!-- ///////////////////////////////////////////////////////////// -->

        <p>
          Initially Null Object type field
        </p>
        <h:selectManyCheckbox id="object" value="#{select01.someValues}">
          <f:selectItems value="#{select01.possibleValues}"/>
        </h:selectManyCheckbox>

        <p>
          <h:commandButton id="command" value="Submit" />
        </p>

      </h:form>
      <h:message id="messages" for="selectmany01"/>
    </f:view>
  </body>
</html>
