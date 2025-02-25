/*
 * Copyright (c) 2009, 2022 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
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
 * $Id: Client.java 58883 2009-07-30 03:09:37Z cf126330 $
 */
package com.sun.ts.tests.ejb30.lite.packaging.war.datasource.singleton;

import static com.sun.ts.tests.ejb30.lite.packaging.war.datasource.common.DataSourceTest.verifyDataSource;

import java.sql.Connection;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.annotation.sql.DataSourceDefinitions;
import jakarta.ejb.EJB;
import javax.sql.DataSource;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;
import com.sun.ts.tests.ejb30.lite.packaging.war.datasource.common.DataSourceIF;


// url is to be ignored
@DataSourceDefinitions({
@DataSourceDefinition(name="java:comp/env/compds",
        className="org.apache.derby.jdbc.ClientDataSource",
        portNumber=1527,
        serverName="localhost",
        databaseName="derbyDB",
        user="cts1",
        transactional=false,
        password="cts1",

        description="ds1",
        initialPoolSize=1,
        isolationLevel=Connection.TRANSACTION_READ_COMMITTED,
        loginTimeout=300,
        maxIdleTime=1000,
        maxPoolSize=2,
        minPoolSize=1,
        properties={},
        url="jdbc:derby://localhost:1527/derbyDB;create=true"
),
@DataSourceDefinition(name="java:comp/env/compds2",
        className="org.apache.derby.jdbc.ClientDataSource",
        portNumber=1527,
        serverName="localhost",
        databaseName="derbyDB",
        user="cts1",
        transactional=false,
        password="cts1")
})

public class Client extends EJBLiteClientBase {
    @EJB
    private DataSourceIF dataSourceBean;
    
    @Resource(lookup="java:comp/env/compds")
    private DataSource compds;
    
    @Resource(lookup="java:comp/env/compds2")
    private DataSource compds2;
    
    @Resource(lookup="java:comp/env/defaultds")
    private DataSource defaultds;
    
    @Resource(lookup="java:comp/env/defaultds2")
    private DataSource defaultds2;
    
    @SuppressWarnings("unused")
    @PostConstruct
    private void postConstruct() {
        boolean c = true;
        Helper.getLogger().info("In postConstruct of " + this);
        
        verifyDataSource(getReasonBuffer(), c, "java:comp/env/defaultds", "java:comp/env/defaultds2", 
                                               "java:comp/env/compds", "java:comp/env/compds2");
        verifyDataSource(getReasonBuffer(), c, defaultds, defaultds2, compds, compds2);

        verifyDataSource(getReasonBuffer(), c, compds, compds2);
    }
    
    /*
     * @testName: postConstructRecords
     * @test_Strategy: look up the data sources declared with annotation in this class, 
     * and verify the injected datasource inside PostConstruct method.
     */
    public void postConstructRecords() {
        appendReason("Test result verified inside postConstruct method.");
    }
    
    /*
     * @testName: postConstructRecordsEJB
     * @test_Strategy: look up the data sources declared with annotation in EJB, 
     * and verify the injected datasource inside its PostConstruct method.
     */
    public void postConstructRecordsEJB() {
        appendReason(dataSourceBean.getPostConstructRecords());
    }
    
    /*
     * @testName: getConnectionEJB
     * @test_Strategy: call getConnection() on the datasource
     */
    public void getConnectionEJB() {
        appendReason(dataSourceBean.getConnection());
    }
}

