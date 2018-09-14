/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $Id$
 */

/*
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.common.taglibsig;

import org.xml.sax.SAXException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.JarURLConnection;
import java.io.InputStream;
import java.io.IOException;
import java.io.CharArrayReader;

import com.sun.ts.lib.util.TestUtil;

/**
 * Represents a TLD from a JAR file allowing access to the tag and function and
 * validator entries as well as TLD level metatdata such as support container
 * version and version of the taglibrary itself.
 */
public class TagLibraryDescriptor {

  private static final String ONE_DOT_TWO_CONTAINER = "1.2";

  private static final String TWO_DOT_ZERO_CONTAINER = "2.0";

  private static final String DEFAULT_TAGLIB_VERSION = "1.0";

  private static final HashMap TAGLIB_CACHE = new HashMap();

  private static final TagEntry[] EMPTY_TAG_ENTRIES = {};

  private static final FunctionEntry[] EMPTY_FUNCTION_ENTRIES = {};

  private static final ValidatorEntry[] EMPTY_VALIDATOR_ENTRIES = {};

  private String _containerVersion = ONE_DOT_TWO_CONTAINER;

  private String _taglibVersion = DEFAULT_TAGLIB_VERSION;

  private String _uri;

  private List _tagEntries;

  private List _functionEntries;

  private List _validatorEntries;

  // Private
  private TagLibraryDescriptor() {
  }

  /**
   * Returns a TagLibraryDescriptor instance based on the URI the tag library is
   * known by (i.e. the <uri> element of the TLD). If the TagLibraryDescriptor
   * is not found in the cache, the this method will use the URL to the jar file
   * and scan the JAR file for TLDs. Any TLD's found will be processed and added
   * cache. If, after scanning the JAR file, no TLD's matching the specified
   * ttaglib uri can be found, this method will return null.
   *
   * @param jarUrl
   *          - The file URL of the JAR file to scan for TLDs if the cache
   *          doesn't contain the requested TagLibraryDescriptor instance
   * @param taglibUri
   *          - The uri of the tag library of interest
   * @return a TagLibraryDescriptor for the provided uri, or null if no tag
   *         library can be found
   */
  public static TagLibraryDescriptor getInstance(String jarUrl,
      String taglibUri) {
    TagLibraryDescriptor tld = (TagLibraryDescriptor) TAGLIB_CACHE
        .get(taglibUri);
    if (tld == null) {
      URL url = null;
      jarUrl = "jar:" + jarUrl + "!/";
      try {
        url = new URL(jarUrl);
      } catch (MalformedURLException e) {
        TestUtil.logErr("Malformed URL: " + jarUrl, e);
      }

      // Begin constructing the cache...
      TestUtil.logTrace("<<<<<<BUILDING CACHE>>>>>>");
      try {
        TldBuilder builder = new TldBuilder(new TldLocator(url));
        builder.build();
      } catch (BuildException be) {
        TestUtil.logErr(be.getMessage(), be);
      }

      tld = (TagLibraryDescriptor) TAGLIB_CACHE.get(taglibUri);
    }
    return tld;
  }

  /**
   * Returns the tags of this tag library.
   * 
   * @return the tags of this tag library
   */
  public TagEntry[] getTagEntries() {
    if (_tagEntries == null || _tagEntries.isEmpty()) {
      return EMPTY_TAG_ENTRIES;
    } else {
      return (TagEntry[]) _tagEntries.toArray(new TagEntry[_tagEntries.size()]);
    }
  }

  /**
   * Returns the functions of this tag library.
   * 
   * @return the functions of this tag library
   */
  public FunctionEntry[] getFunctionEntries() {
    if (_functionEntries == null || _functionEntries.isEmpty()) {
      return EMPTY_FUNCTION_ENTRIES;
    } else {
      return (FunctionEntry[]) _functionEntries
          .toArray(new FunctionEntry[_functionEntries.size()]);
    }
  }

  /**
   * The validators of this tag library.
   * 
   * @return the validators of this tag library
   */
  public ValidatorEntry[] getValidatorEntries() {
    if (_validatorEntries == null || _validatorEntries.isEmpty()) {
      return EMPTY_VALIDATOR_ENTRIES;
    } else {
      return (ValidatorEntry[]) _validatorEntries
          .toArray(new ValidatorEntry[_validatorEntries.size()]);
    }
  }

  /**
   * Returns the minimum container version required to use this tag libary.
   * 
   * @return the minimum container version required to use this tag library
   */
  public String getRequiredContainerVersion() {
    return this._containerVersion;
  }

  /**
   * Returns the version of this tag library.
   * 
   * @return the version of this tag library
   */
  public String getTaglibraryVersion() {
    return this._taglibVersion;
  }

  /**
   * Returns the URI that identifies this tag library
   * 
   * @return the URI that identifies this tag library
   */
  public String getURI() {
    return this._uri;
  }

  /**
   * Adds a TagEntry to the internal list of tags
   * 
   * @param entry
   *          - a TagEntry
   */
  private void addTagEntry(TagEntry entry) {
    if (_tagEntries == null)
      _tagEntries = new ArrayList();
    _tagEntries.add(entry);
  }

  /**
   * Adds a FunctionEntry to the internal list of functions
   * 
   * @param entry
   *          - a FunctionEntry
   */
  private void addFunctionEntry(FunctionEntry entry) {
    if (_functionEntries == null)
      _functionEntries = new ArrayList();
    _functionEntries.add(entry);
  }

  /**
   * Adds a ValidatorEntry to the internal list of validators.
   * 
   * @param entry
   *          - a ValidatorEntry
   */
  private void addValidatorEntry(ValidatorEntry entry) {
    if (_validatorEntries == null)
      _validatorEntries = new ArrayList();
    _validatorEntries.add(entry);
  }

  /**
   * Sets the required container version for this tag library.
   * 
   * @param version
   *          - required container version
   */
  private void setRequiredContainerVersion(String version) {
    _containerVersion = version;
  }

  /**
   * Sets the version of this tag library
   * 
   * @param version
   *          - tag library version
   */
  private void setTaglibraryVersion(String version) {
    _taglibVersion = version;
  }

  /**
   * Sets the URI of this tag library
   * 
   * @param uri
   *          - the URI of this tag library
   */
  private void setURI(String uri) {
    _uri = uri;
  }

  // ======================================== Inner Classes
  // =====================

  /**
   * Utility class to encapsulate all XML related functionality in creating
   * TagLibraryDescriptor objects.
   */
  private static class TldBuilder {

    /**
     * Elements that are of interest in a Tag Library Descriptor.
     */
    private static final String TLIB_VERSION_ELEMENT = "tlib-version";

    private static final String JSP_VERSION_ELEMENT = "jsp-version";

    private static final String VALIDATOR_ELEMENT = "validator";

    private static final String FUNCTION_ELEMENT = "function";

    private static final String TAG_ELEMENT = "tag";

    private static final String NAME_ELEMENT = "name";

    private static final String ATTRIBUTE_ELEMENT = "attribute";

    private static final String TYPE_ELEMENT = "type";

    private static final String VARIABLE_ELEMENT = "variable";

    private static final String BODY_ELEMENT = "body-content";

    private static final String URI_ELEMENT = "uri";

    private static final String RTEXPR_ELEMENT = "rtexprvalue";

    private static final String REQUIRED_ELEMENT = "required";

    private static final String NAME_GIVEN_ELEMENT = "name-given";

    private static final String SCOPE_ELEMENT = "scope";

    private static final String DECLARE_ELEMENT = "declare";

    private static final String VARIABLE_CLASS_ELEMENT = "variable-class";

    private static final String FUNCTION_SIGNATURE_ELEMENT = "function-signature";

    /**
     * The TldLocator used to obtain input streams to the TLD's.
     */
    TldLocator _locator;

    /**
     * Creates a new TldBuilder instance.
     * 
     * @param locator
     *          - the TldLocator to get InputStreams from
     */
    public TldBuilder(TldLocator locator) {
      _locator = locator;
    }

    /**
     * Builds TagLibraryDescriptor objects based off all TLD's that contain URI
     * elements.
     *
     * @throws BuildException
     *           if an error occurs during processing.
     */
    public void build() throws BuildException {
      try {
        processTlds(_locator.getTldsAsStreams());
      } catch (Throwable t) {
        throw new BuildException(
            "Unexpected Exception building TLDs: " + t.toString());
      }
    }

    /**
     * Utility method to setup and return a DocumentBuilder for XML parsing.
     * 
     * @return - DocumentBuilder instance
     */
    private DocumentBuilder getDocumentBuilder() {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setExpandEntityReferences(false);
      factory.setNamespaceAware(false);
      factory.setValidating(false);
      DocumentBuilder builder = null;
      try {
        builder = factory.newDocumentBuilder();
      } catch (ParserConfigurationException e) {
        TestUtil.logErr(e.getMessage(), e);
      }
      return builder;
    }

    /**
     * Parses the provided Document object (created from a TLD), and constructs
     * logical TagLibraryDescriptor instances from the information contained
     * within the Document.
     * 
     * @param doc
     *          - Document object representing a TLD
     */
    private void processDocument(Document doc) {
      TagLibraryDescriptor tld = new TagLibraryDescriptor();
      Element taglib = doc.getDocumentElement();

      processTopLevelElements(taglib, tld);

      processTagEntries(taglib.getElementsByTagName(TAG_ELEMENT), tld);
      processFunctionEntries(taglib.getElementsByTagName(FUNCTION_ELEMENT),
          tld);
    }

    private void processTopLevelElements(Element taglib,
        TagLibraryDescriptor des) {

      des.setTaglibraryVersion(getNodeText(taglib, TLIB_VERSION_ELEMENT));
      des.setRequiredContainerVersion(getNodeText(taglib, JSP_VERSION_ELEMENT));
      String uri = getNodeText(taglib, URI_ELEMENT);
      des.setURI(uri);
      addCacheEntry(uri, des);
    }

    private void processFunctionEntries(NodeList functionNodes,
        TagLibraryDescriptor des) {
      for (int i = 0, size = functionNodes.getLength(); i < size; i++) {
        Element functionElement = (Element) functionNodes.item(i);
        FunctionEntry funcEntry = new FunctionEntry();
        funcEntry.setName(getNodeText(functionElement, NAME_ELEMENT));
        funcEntry.setFunctionSignature(
            getNodeText(functionElement, FUNCTION_SIGNATURE_ELEMENT));
      }
    }

    private void processTagEntries(NodeList tagNodes,
        TagLibraryDescriptor des) {
      for (int i = 0, size = tagNodes.getLength(); i < size; i++) {
        Element tagElement = (Element) tagNodes.item(i);
        TagEntry tagEntry = new TagEntry();
        tagEntry.setName(getNodeText(tagElement, NAME_ELEMENT));
        tagEntry.setBody(getNodeText(tagElement, BODY_ELEMENT));

        processTagAttributes(tagElement.getElementsByTagName(ATTRIBUTE_ELEMENT),
            tagEntry);

        processTagVariables(tagElement.getElementsByTagName(VARIABLE_ELEMENT),
            tagEntry);

        des.addTagEntry(tagEntry);
      }

    }

    private void processTagAttributes(NodeList attrNodes, TagEntry tag) {
      for (int i = 0, size = attrNodes.getLength(); i < size; i++) {
        Element attrElement = (Element) attrNodes.item(i);
        AttributeEntry attrEntry = new AttributeEntry();
        attrEntry.setName(getNodeText(attrElement, NAME_ELEMENT));
        attrEntry.setType(getNodeText(attrElement, TYPE_ELEMENT));
        attrEntry.setRtexpr(getNodeText(attrElement, RTEXPR_ELEMENT));
        attrEntry.setRequired(getNodeText(attrElement, REQUIRED_ELEMENT));
        tag.addAttribute(attrEntry);
      }
    }

    private void processTagVariables(NodeList varNodes, TagEntry tag) {
      for (int i = 0, size = varNodes.getLength(); i < size; i++) {
        Element varElement = (Element) varNodes.item(i);
        VariableEntry varEntry = new VariableEntry();
        varEntry.setNameGiven(getNodeText(varElement, NAME_GIVEN_ELEMENT));
        varEntry
            .setVariableClass(getNodeText(varElement, VARIABLE_CLASS_ELEMENT));
        varEntry.setScope(getNodeText(varElement, SCOPE_ELEMENT));
        varEntry.setDeclare(getNodeText(varElement, DECLARE_ELEMENT));
        tag.addVariable(varEntry);
      }
    }

    private String getNodeText(Element parent, String nodeName) {
      String nodeText = null;
      NodeList list = parent.getElementsByTagName(nodeName);

      for (int i = 0, size = list.getLength(); i < size; i++) {
        Node node = list.item(0).getFirstChild();
        if (node.getNodeType() == Node.TEXT_NODE) {
          nodeText = node.getNodeValue();
          break;
        }
      }

      return nodeText;
    }

    private void addCacheEntry(String key, Object value) {
      TAGLIB_CACHE.put(key, value);
    }

    /**
     * Sequentially processes the array of InputStreams, where each input stream
     * represents a TLD.
     * 
     * @param inStreams
     *          - array of input streams representing one or more TLDs
     * @throws SAXException
     *           - if an unexpected parsing error occurs
     * @throws IOException
     *           - if an unexpected IO error occurs
     */
    private void processTlds(InputStream[] inStreams)
        throws SAXException, IOException {
      DocumentBuilder builder = getDocumentBuilder();
      builder.setEntityResolver(new EntityResolver() {
        public InputSource resolveEntity(String publicId, String systemId)
            throws SAXException, IOException {
          return new InputSource(new CharArrayReader(new char[0]));
        }
      });

      for (int i = 0; i < inStreams.length; i++) {
        Document doc = builder.parse(inStreams[i]);
        processDocument(doc);
      }
    }

  }

  /**
   * Processes the JAR file as identified by the provided URL. Create new
   * TagLibraryDescriptor instances based on the any TLD's found.
   */
  private static class TldLocator {

    /**
     * The META-INF directory of the target JAR URL.
     */
    private static final String META_INF = "META-INF/";

    /**
     * The file extension of Tag Library Descriptor files.
     */
    private static final String TLD_EXTENSION = ".tld";

    /**
     * The URL of the JAR file.
     */
    private URL _url;

    /**
     * Creates a new TldLocator instance.
     * 
     * @param url
     *          - the JAR url to use in scanning for TLDs
     */
    public TldLocator(URL url) {
      _url = url;
    }

    /**
     * Scans the JAR file idetified by the provided URL. For each TLD found an
     * InputStream will be created for processing.
     * 
     * @return - array of InputStreams representing TLDs found in the JAR
     * @throws IOException
     *           - if an unexpected I/O error occurs
     */
    public InputStream[] getTldsAsStreams() throws IOException {
      JarURLConnection jarCon = (JarURLConnection) _url.openConnection();
      JarFile jar = jarCon.getJarFile();
      List inputStreams = new ArrayList();
      for (Enumeration e = jar.entries(); e.hasMoreElements();) {
        JarEntry entry = (JarEntry) e.nextElement();
        String name = entry.getName();
        if (!name.startsWith(META_INF) || !name.endsWith(TLD_EXTENSION)) {
          continue;
        }
        inputStreams.add(jar.getInputStream(entry));
      }
      return (InputStream[]) inputStreams
          .toArray(new InputStream[inputStreams.size()]);
    }
  }
}
