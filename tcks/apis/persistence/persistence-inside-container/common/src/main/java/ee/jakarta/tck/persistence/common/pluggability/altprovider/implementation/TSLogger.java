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

/**
 *
 * @author Raja Perumal
 */

package ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TSLogger is the custom Logger which extends java.util.Logger
 */
public class TSLogger extends Logger {
	public static final String MESSAGE_PREFIX = "JPA_ALTERNATE_PROVIDER : ";

	public static final String LOG_NAME = "JPALog.xml";
	// System property set to true if the log file location is defaulted
	public static final String LOG_NAME_DEFAULT_PROPERTY = "log.file.location.defaulted";

	public static final DateFormat df = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss.SSS");

	/**
	 * @serial The logging context Id
	 */
	private String contextId;

	private int levelValue = Level.INFO.intValue();

	private int offValue = Level.OFF.intValue();

	private String name;
	private String logFile;

	protected TSLogger(String name, String logFile) {
		super(name, null);
		this.name = name;
		levelValue = Level.INFO.intValue();
		this.logFile = logFile;
	}

	/**
	 * Used for testing purposes only
	 */
	public static synchronized void clearLogger() {
		if(logger != null) {
			Handler handler = logger.getHandlers()[0];
			logger.removeHandler(handler);
			handler.close();
		}
		logger = null;
	}

	/**
	 * Get the log file path
	 *
	 * @return the full path to the log file
	 */
	public String getLogFile() {
		return logFile;
	}
	private void setLogFile(String logFileLocation) {
		this.logFile = logFileLocation;
	}

	/**
	 * Log a message, with no arguments.
	 * <p/>
	 * If the logger is currently enabled for the given message level then the given
	 * message is forwarded to all the registered output Handler objects at the
	 * Level.INFO level
	 * <p/>
	 *
	 * @param msg The string message (or a key in the message catalog)
	 */
	public void log(String msg) {
		// assign default context (JPA) to all messages ???
		log(Level.INFO, msg);
	}

	/**
	 * Log a message, with no arguments.
	 * <p/>
	 * If the logger is currently enabled for the given message level then the given
	 * message is forwarded to all the registered output Handler objects.
	 * <p/>
	 *
	 * @param level One of the message level identifiers, e.g. SEVERE
	 * @param msg   The string message (or a key in the message catalog)
	 */
	public void log(Level level, String msg) {
		// assign default context (JPA) to all messages ???

		log(level, createDateTime(), MESSAGE_PREFIX + msg, "JPA");
	}

	/**
	 * Log a message, with no arguments.
	 * <p/>
	 * If the logger is currently enabled for the given message level then the given
	 * message is forwarded to all the registered output Handler objects.
	 * <p/>
	 *
	 * @param level    One of the message level identifiers, e.g. SEVERE
	 * @param dateTime The dateTime stamp of the message
	 * @param msg      The string message (or a key in the message catalog)
	 */
	public void log(Level level, String dateTime, String msg) {
		// assign default context (JPA) to all messages ???

		log(level, dateTime, MESSAGE_PREFIX + msg, "JPA");
	}

	/**
	 * Log a message, with no arguments.
	 * <p/>
	 * If the logger is currently enabled for the given message level then the given
	 * message is forwarded to all the registered output Handler objects.
	 * <p/>
	 *
	 * @param level     One of the message level identifiers, e.g. SEVERE
	 * @param msg       The string message (or a key in the message catalog)
	 * @param contextId the logging context Id
	 */
	public void log(Level level, String dateTime, String msg, String contextId) {
		if (level.intValue() < levelValue || levelValue == offValue) {
			return;
		}
		TSLogRecord lr = new TSLogRecord(level, dateTime, msg, contextId);
		log(lr);
	}

	/**
	 * Log a TSLogRecord.
	 *
	 * @param record the TSLogRecord to be published
	 */
	public void log(TSLogRecord record) {
		Handler targets[] = logger.getHandlers();
		targets[0].publish(record);
	}

	// pulled from TSJavaLog:
	private static TSLogger logger;

	public static TSLogger getInstance() {
		if (logger == null) {
			try {
				String logFileLocation = validateOrCreateLogFileLocation();
				System.out.println("JPA_ALTERNATE_PROVIDER log:" + logFileLocation);

				logger = new TSLogger("JPA", logFileLocation);
				logger.setUseParentHandlers(false);
				// create a new file handler
				FileHandler fileHandler = new FileHandler(logFileLocation, false);
				fileHandler.setFormatter(new TSXMLFormatter());
				logger.addHandler(fileHandler);

			} catch (Exception e) {
				throw new RuntimeException("TSLogger Initialization failed", e);
			}
		}
		return logger;
	}

	/**
	 * Look to log.file.location system property for the directory to write the JPALog.xml file.
	 * @return the full path to the log file
	 */
	private static String validateOrCreateLogFileLocation() throws IOException {

		String logFileLocation = System.getProperty("log.file.location");
		if (logFileLocation == null) {
			System.out.println("JPA_ALTERNATE_PROVIDER log.file.location not set, will create tmp location.\n"+
					"The log.file.location needs to be set as system property on the test container.");
			Path tmpPath = Files.createTempFile("JPALog.", ".xml");
			tmpPath.toFile().deleteOnExit();
			logFileLocation = tmpPath.toAbsolutePath().toString();
			System.setProperty(LOG_NAME_DEFAULT_PROPERTY, logFileLocation);
			System.out.println("JPA_ALTERNATE_PROVIDER tmp log.file.location:" + logFileLocation);
			return logFileLocation;
		}
		if(logFileLocation.endsWith("/")) {
			logFileLocation = logFileLocation.substring(0, logFileLocation.length()-1);
		}
		System.out.println("JPA_ALTERNATE_PROVIDER log.file.location:" + logFileLocation);

		File dir = new File(logFileLocation);
		if (!dir.exists()) {
			System.out.println("Log directory does not exist, creating it.");
			if(!dir.mkdirs()) {
				throw new IOException("Failed to create log directory: " + logFileLocation);
			}
		}
		if(!dir.isDirectory()) {
			throw new IOException("Log directory is not a directory: " + logFileLocation);
		}

		String[] chld = dir.list();
		System.out.println("Searching for previous log files to delete");
        for (String fileName : chld) {
            if (fileName.contains(LOG_NAME)) {
                // System.out.println("Found File:"+fileName);
                File file = new File(logFileLocation + "/" + fileName);
                if (file.exists()) {
                    System.out.println("Deleting JPA logfile:" + file.getName());
                    file.delete();
                }
            }
        }
		return logFileLocation+"/"+LOG_NAME;
	}

	public String createDateTime() {
		return df.format(new Date());
	}
}
