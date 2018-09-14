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

<%@ page import="javax.faces.application.ViewHandler" %>
<%@ page import="javax.faces.context.ExternalContext" %>
<%@ page import="javax.faces.context.FacesContext" %>

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

<html>
    <head>
        <title>encodetest</title>
        <style type="text/css">
            img.newBorder {
                border: darkblue;
            }
        </style>
    </head>
    
    <body>
        <f:view>
            <h:graphicImage id="img1" value="#{resource['pnglogo.png']}"/>
            <h:graphicImage id="img2" 
                            value="#{resource['pnglogo.png']}"
                            styleClass="newBorder"/>
            <h:graphicImage id="img3"
                            value="#{resource['pnglogo.png']}"
                            ismap="true"/>
            <h:graphicImage id="img4"
                            value="#{resource['pnglogo.png']}"
                            ismap="false"/>
            <h:graphicImage id="img5"
                            binding="#{pictures.img1}"/>
            <h:graphicImage id="img6" 
                            title="Resource_Image"
                            value="#{resource['duke-boxer.gif']}"/>
        </f:view>
        
    </body>
</html>
