package com.sun.ts.tests.saaj.common;

import java.io.IOException;
import java.io.InputStream;
import java.lang.System.Logger;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.lib.porting.TSURL;

@ExtendWith(ArquillianExtension.class)
public class Client {

	protected static final String PROTOCOL = "http";

	protected static final String HOSTNAME = "localhost";

	protected static final int PORTNUM = 8000;

	public static final String WEBSERVERHOSTPROP = "webServerHost";

	public static final String WEBSERVERPORTPROP = "webServerPort";

	protected TSURL tsurl = new TSURL();

	protected URL url = null;

	protected URLConnection urlConn = null;

	protected Properties props = new Properties();

	protected String hostname = HOSTNAME;

	protected int portnum = PORTNUM;

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public static void addFilesToArchive(String contentRoot, String[] fileNames, WebArchive archive)
			throws IOException {
		for (String fileName : fileNames) {
			InputStream inStream = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(contentRoot + fileName);
			ByteArrayAsset attach = new ByteArrayAsset(inStream);
			archive.add(attach, fileName);
		}
	}

	/*
	 * @class.setup_props: webServerHost; webServerPort;
	 */
	@BeforeEach
	public void setup() throws Exception {
		boolean pass = true;

		try {
			hostname = System.getProperty(WEBSERVERHOSTPROP);
			if (hostname == null)
				pass = false;
			else if (hostname.equals(""))
				pass = false;
			try {
				portnum = Integer.parseInt(System.getProperty(WEBSERVERPORTPROP));
			} catch (Exception e) {
				pass = false;
			}
		} catch (Exception e) {
			throw new Exception("setup failed:", e);
		}
		props.put(WEBSERVERHOSTPROP, hostname);
		props.put(WEBSERVERPORTPROP, String.valueOf(portnum));
		if (!pass) {
			logger.log(Logger.Level.ERROR, "Please specify host & port of web server " + "in config properties: "
					+ WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
			throw new Exception("setup failed:");
		}
		logger.log(Logger.Level.INFO, "setup ok");
	}

	@AfterEach
	public void cleanup() throws Exception {
		logger.log(Logger.Level.INFO, "cleanup ok");
	}

}
