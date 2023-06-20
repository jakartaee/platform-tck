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

<% /** 	Name : ImplicitTld21.tag
	Description : Verify that the jsp version of an implicit 
                      tag library is configured by the implicit.tld
                      file.  Use of '#{' in an action should result
                      in a translation error when the jsp version 
                      is 2.1 or greater.
**/ %>

<%@ taglib tagdir="/WEB-INF/tags/reservedname21" prefix="tags" %>
<%@ tag deferredSyntaxAllowedAsLiteral="false" %>

<tags:DeferredSyntaxAsLiteral literal="#{expr}" />
<% out.println("Test FAILED:"); %>
