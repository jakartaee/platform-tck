/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/*
 * $Id: TestServlet.java 58962 2009-08-06 15:09:52Z cf126330 $
 */

package com.sun.ts.tests.ejb30.misc.datasource.twowars;

import java.io.IOException;
import java.sql.Connection;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.annotation.sql.DataSourceDefinitions;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.sun.ts.tests.ejb30.assembly.appres.common.AppResRemoteIF;
import com.sun.ts.tests.ejb30.assembly.appres.common.TestServletBase;
import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.lite.packaging.war.datasource.common.DataSourceTest;

@WebServlet(urlPatterns = "/TestServlet", loadOnStartup = 1)

@DataSourceDefinitions({
    @DataSourceDefinition(name="java:comp/env/compdsservlet",
            className="org.apache.derby.jdbc.ClientDataSource",
            portNumber=1527,
            serverName="localhost",
            databaseName="derbyDB",
            user="cts1",
            password="cts1",
            
            description="ds1",
            initialPoolSize=1,
            
            loginTimeout=300,
            maxIdleTime=1000,
            maxPoolSize=2,
            minPoolSize=1,
            transactional=false,
            properties={},
            url="jdbc:derby://${derby.server}:${derby.port}/${derby.dbName};create=true"
    ),
    @DataSourceDefinition(name="defaultdsservlet",
            className="org.apache.derby.jdbc.ClientDataSource",
            portNumber=1527,
            serverName="localhost",
            databaseName="derbyDB",
            user="cts1",
            password="cts1",
            properties={}),
            
    @DataSourceDefinition(name="java:module/env/moduledsservlet",
            className="org.apache.derby.jdbc.ClientDataSource",
            portNumber=1527,
            serverName="localhost",
            databaseName="derbyDB",
            user="cts1",
            password="cts1",
            properties={},
            isolationLevel=Connection.TRANSACTION_SERIALIZABLE),
            
    @DataSourceDefinition(name="java:app/env/servlet/appds",
            className="org.apache.derby.jdbc.ClientDataSource",
            portNumber=1527,
            serverName="localhost",
            databaseName="derbyDB",
            user="cts1",
            password="cts1",
            properties={}),
            
    @DataSourceDefinition(name="java:global/env/ts/datasource/servlet/globalds",
            className="org.apache.derby.jdbc.ClientDataSource",
            portNumber=1527,
            serverName="localhost",
            databaseName="derbyDB",
            user="cts1",
            password="cts1",
            properties={})
    })
public class TestServlet extends TestServletBase {

    @EJB(beanName="DataSourceBean")
    private AppResRemoteIF dataSourceBean;
    
    private void nonPostConstruct() {
        postConstructRecords = new StringBuilder();
        ServiceLocator.lookupShouldFail("java:app/env/servlet2/appds", getPostConstructRecords());
        Helper.getLogger().info(getPostConstructRecords().toString());
        
        DataSourceTest.verifyDataSource(postConstructRecords, false, 
            "java:comp/env/compdsservlet",
            "java:comp/env/defaultdsservlet",
            "java:module/env/moduledsservlet",
            "java:app/env/servlet/appds",
            "java:global/env/ts/datasource/servlet/globalds");
    }

    public void servletPostConstruct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        nonPostConstruct();
        verifyRecords(request, response, postConstructRecords);
    }


    public void ejbPostConstruct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        verifyRecords(request, response, dataSourceBean.getPostConstructRecords());
    }
}
