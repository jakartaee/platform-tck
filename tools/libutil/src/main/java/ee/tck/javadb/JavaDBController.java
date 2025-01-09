/*
 * Copyright (c) 2022 Eclipse Foundation and/or its affiliates. All rights reserved.
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
package ee.tck.javadb;

import org.apache.derby.drda.NetworkServerControl;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * JavaDBController is a utility class to start and stop the JavaDB server. It provides the same functionality as the
 * EE10 ant start.javadb and stop.javadb targets.
 */
public class JavaDBController {
    String dbName = "derbyDB";
    String server = "localhost";
    int port = 1527;
    String dmlFile = "derby/derby.dml.sql";
    String user = "cts1";
    String passwd = "cts1";
    int startupDelay = 5;

    /**
     * Use the default values for the JavaDBController
     */
    public JavaDBController() {
    }

    /**
     * Use the provided values for the JavaDBController
     * 
     * @param dbName
     * @param server
     * @param port
     * @param user
     * @param passwd
     */
    public JavaDBController(String dbName, String server, int port, String user, String passwd) {
        this.dbName = dbName;
        this.server = server;
        this.port = port;
        this.user = user;
        this.passwd = passwd;
    }

    /**
     * Load the JavaDBController settings from the tsJte file
     * 
     * @param tsJte
     * @throws IOException
     */
    public JavaDBController(Path tsJte) throws IOException {
        Properties props = new Properties();
        props.load(Files.newInputStream(tsJte));
        this.dbName = props.getProperty("derby.db.name");
        this.server = server;
        this.port = port;
        this.user = user;
        this.passwd = passwd;
    }

    public void startJavaDB(Path derbyHome) throws Exception {
        System.setProperty("derby.system.home", derbyHome.toString());
        InetAddress inetAddress = InetAddress.getByName(server);
        NetworkServerControl server = new NetworkServerControl(inetAddress, port);
        server.start(null);
    }

    public void stopJavaDB() throws Exception {
        InetAddress inetAddress = InetAddress.getByName(server);
        NetworkServerControl server = new NetworkServerControl(inetAddress, port);
        server.shutdown();
    }

    /**
     * Check if the JavaDB server is running
     * 
     * @return true if the server is running and , false otherwise
     */
    public boolean isJavaDBRunning() {
        java.util.Properties props = new java.util.Properties();
        String dbURL = "jdbc:derby://" + server + ":" + port + "/" + dbName + ";user=" + user + ";password=" + passwd + ";create=true";
        props.setProperty("user", user);
        props.setProperty("password", passwd);
        boolean isRunning = false;
        try (Connection conn = DriverManager.getConnection(dbURL, props)) {
            /* interact with Derby */
            isRunning = conn.isValid(0);
            try (Statement s = conn.createStatement()) {
                try (ResultSet rs = s.executeQuery("values user")) {
                } catch (Exception e) {
                    isRunning = false;
                    // e.printStackTrace();
                }
            } catch (Exception e) {
                isRunning = false;
                // e.printStackTrace();
            }
        } catch (Exception e) {
            isRunning = false;
            // e.printStackTrace();
        }
        return isRunning;
    }
}
