/*
 * Copyright 2012 International Business Machines Corp.
 * 
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License, 
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibm.jbatch.tck.utils;

import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Named;

/**
 * This class is not needed at all by the executor of the TCK.   It
 * is used to generate the META-INF/batch.xml associated with the
 * set of batch artifacts used in the TCK
 * 
 * It was convenient to keep it in the same project with the TCK itself but it
 * can be ignored by someone simply running/executing the TCK.
 */
public class BatchXMLGenerator {

	private final static Logger logger = Logger.getLogger(BatchXMLGenerator.class.getName());

	private static final String SLASH = System.getProperty("file.separator");

	List<BeanDefinition> beanDefinitions = new ArrayList<BeanDefinition>();

	private final static String BATCHXML = "META-INF/batch.xml";

	private void writeBatchXML(File dir) {


		try {

			File batchXMLFile = new File (dir, "batch.xml");

			logger.info("Writing batch.xml: " + batchXMLFile);

			BufferedWriter writer = new BufferedWriter(new FileWriter(batchXMLFile));

			writer.write("<batch-artifacts xmlns=\"http://xmlns.jcp.org/xml/ns/javaee\">\n");

			for (BeanDefinition beanDef : this.beanDefinitions) {
				writer.write("    " + beanDef.getXMLString() + "\n");
			}
			writer.write("</batch-artifacts>\n");

			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}



	/*
	 * Implements default per:
	 * http://docs.jboss.org/cdi/spec/1.0/html/implementation.html Sec. 3.1.5
	 * Default name for a managed bean
	 */
	private String generateId(String qualifiedClassName) {
		String retVal = null;

		int index = qualifiedClassName.lastIndexOf(".");

		//We don't check for the default package
		String simpleName = qualifiedClassName.substring(index+1);

		String simpleNameFirst = simpleName.substring(0, 1).toLowerCase();
		String simpleNameRest = simpleName.substring(1);
		retVal = simpleNameFirst + simpleNameRest; // Works on 1-char
		// boundary condition
		// where "rest" is empty
		// string

		return retVal;
	}

	private void processClass(String qualifiedClassName) {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Processing class: " + qualifiedClassName);
		}

		String namedAnnotationValue = null;
		Class<?> artifactClass = null;
		try {
			artifactClass = Class.forName(qualifiedClassName);
			Named namedAnnotation = artifactClass.getAnnotation(Named.class);
			if (namedAnnotation != null) {
				namedAnnotationValue = namedAnnotation.value();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// Continue and use classname-based defaulting
		}

		String beanID = null;
		// If we see a @Named with empty value (default), then use the classname-based calculation,
		// which is purposely designed to mirror the CDI default.
		if (namedAnnotationValue != null && !namedAnnotationValue.trim().isEmpty()) {
			beanID = namedAnnotationValue;
		} else {
			beanID = generateId(qualifiedClassName);
		}

		BeanDefinition beanXML = new BeanDefinition(beanID, qualifiedClassName);

		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Adding bean to batch.xml: beanId=" + beanID + " className=" + qualifiedClassName);
		}

		this.beanDefinitions.add(beanXML);
	}




	private static List<String> findClasses(String dir) {
		File directory = new File(dir);
		if (!directory.exists()) {
			throw new IllegalArgumentException("This directory does not exist: " + directory.toString());
		} else if (!directory.isDirectory()) {
			throw new IllegalArgumentException("This is not a directory: " + directory.toString());
		}


		List<String> classList = new ArrayList<String>();

		findClasses(directory, "" , classList);

		return classList;

	}

	private static void findClasses(File directory, String path, List<String> classList) {

		File[] files = directory.listFiles();

		for (File file : files) {
			if (file.isDirectory()){
				findClasses(file, path + file.getName() + SLASH , classList);

			}

			String filename = file.getName();
			if (filename.endsWith(".class")) {

				String classname = filename.substring(0, filename.lastIndexOf("."));

				classList.add(path.replace(SLASH, ".") + classname);

			}

		}

	}

	public static void main(String[] args){
		logger.info("Starting BatchXMLGenerator");

		BatchXMLGenerator bxg = new BatchXMLGenerator();

		//FIXME Need more input validation here

		List<String> classList = bxg.findClasses(args[0]);

		for (String className : classList) {
			bxg.processClass(className);
		}

		File batchXMLDir = new File(args[1]);

		if (!batchXMLDir.exists()) {
			throw new IllegalArgumentException("This directory does not exist: " + args[1]);
		}

		if (!batchXMLDir.isDirectory()) {
			throw new IllegalArgumentException("This is not a directory: " + args[1]);

		}

		bxg.writeBatchXML(batchXMLDir);

		logger.info("BatchXMLGenerator completed successfully.");

	}
}
