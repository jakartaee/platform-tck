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
 * $Id$
 */

package com.sun.xml.messaging.saaj.soap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.transform.stream.StreamSource;

import com.sun.xml.messaging.saaj.soap.*;
import com.sun.xml.messaging.saaj.util.*;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.internet.ContentType;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.ParameterList;
import jakarta.xml.soap.AttachmentPart;
import jakarta.xml.soap.MimeHeaders;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class MySOAPMessageImpl extends SOAPMessage {
  private MimeHeaders headers;

  private SOAPPartImpl soapPart;

  private Vector attachments;

  private boolean saved = false;

  private byte[] messageBytes;

  private int messageByteCount;

  /**
   * Construct a new message. This will be invoked before message sends.
   */
  public MySOAPMessageImpl() {
    headers = new MimeHeaders();
  }

  /**
   * Shallow copy.
   */
  public MySOAPMessageImpl(SOAPMessage msg) {
    if (!(msg instanceof MySOAPMessageImpl)) {
      // don't know how to handle this.
    }
    MySOAPMessageImpl src = (MySOAPMessageImpl) msg;
    this.headers = src.headers;
    this.soapPart = src.soapPart;
    this.attachments = src.attachments;
    this.saved = src.saved;
    this.messageBytes = src.messageBytes;
    this.messageByteCount = src.messageByteCount;
  }

  /**
   * Construct a message from an input stream. When messages are received,
   * there's two parts -- the transport headers and the message content in a
   * transport specific stream.
   */
  public MySOAPMessageImpl(MimeHeaders headers, final InputStream in)
      throws IOException, SOAPException {
    this.headers = headers;
    final String ct = getContentType();
    if (ct == null)
      throw new SOAPException("Absent Content-Type");

    try {
      ContentType contentType = new ContentType(ct);

      int v = verify(contentType);

      if (v == 1) {
        getSOAPPart().setContent(new StreamSource(in));
      } else {
        DataSource ds = new DataSource() {
          public InputStream getInputStream() {
            return in;
          }

          public OutputStream getOutputStream() {
            return null;
          }

          public String getContentType() {
            return ct;
          }

          public String getName() {
            return "";
          }
        };

        MimeMultipart mp = new MimeMultipart(ds);

        MimeBodyPart mbp = (MimeBodyPart) mp.getBodyPart(0);

        getSOAPPart().setContent(new StreamSource(mbp.getInputStream()));

        for (int i = 1; i < mp.getCount(); i++) {
          MimeBodyPart bp = (MimeBodyPart) mp.getBodyPart(i);
          AttachmentPartImpl ap = new AttachmentPartImpl();

          DataHandler dh = bp.getDataHandler();
          ap.setDataHandler(dh);

          AttachmentPartImpl.copyMimeHeaders(bp, ap);
          addAttachmentPart(ap);
        }
      }
    } catch (Throwable ex) {
      ex.printStackTrace();
      throw new SOAPException("Unable to internalize message", ex);
    }
    needsSave();
  }

  /**
   * Verify a contentType.
   *
   * @return 1 for plain XML and 2 for MIME multipart.
   */
  private int verify(ContentType contentType) throws SOAPException {
    // TBD
    // Is there anything else we need to verify here?

    String primary = contentType.getPrimaryType();
    String sub = contentType.getSubType();

    if (!primary.equalsIgnoreCase("multipart")) {
      if (primary.equalsIgnoreCase("text") && sub.equalsIgnoreCase("xml"))
        return 1;
      else
        throw new SOAPException("Invalid Content-Type:" + primary + "/" + sub);
    } else if (sub.equalsIgnoreCase("related")) {
      String type = contentType.getParameter("type").toLowerCase();
      if (type.startsWith("text/xml"))
        return 2;
      else
        throw new SOAPException("Content-Type needs to be Multipart/Related "
            + "and with \"type=text/xml\"");
    } else
      throw new SOAPException("Invalid Content-Type: " + primary + "/" + sub);
  }

  public MimeHeaders getMimeHeaders() {
    return this.headers;
  }

  final String getContentType() {
    String[] values = headers.getHeader("Content-Type");
    if (values == null)
      return null;
    else
      return values[0];
  }

  /**
   * All write methods (i.e setters) should call this method in order to make
   * sure that a save is necessary since the state has been modified.
   */
  private final void needsSave() {
    saved = false;
  }

  public synchronized boolean saveRequired() {
    return saved != true;
  }

  public String getContentDescription() {
    String[] values = headers.getHeader("Content-Description");
    if (values.length > 0)
      return values[0];
    return null;
  }

  public void setContentDescription(String description) {
    headers.setHeader("Content-Description", description);
    needsSave();
  }

  public SOAPPart getSOAPPart() {
    // if (soapPart == null)
    // soapPart = new SOAPPartImpl();

    return soapPart;
  }

  public void removeAllAttachments() {
    if (attachments != null) {
      attachments.removeAllElements();
      needsSave();
    }
  }

  public int countAttachments() {
    if (attachments != null)
      return attachments.size();
    return 0;
  }

  public void addAttachmentPart(AttachmentPart attachment) {
    if (attachments == null)
      attachments = new Vector();

    attachments.addElement(attachment);

    needsSave();
  }

  static private final Iterator nullIter = new Iterator() {
    public boolean hasNext() {
      return false;
    }

    public Object next() {
      return null;
    }

    public void remove() {
      throw new IllegalStateException();
    }
  };

  public Iterator getAttachments() {
    if (attachments == null)
      return nullIter;
    return attachments.iterator();
  }

  private class MimeMatchingIterator implements Iterator {
    public MimeMatchingIterator(MimeHeaders headers) {
      this.headers = headers;
      this.iter = attachments.iterator();
    }

    private Iterator iter;

    private MimeHeaders headers;

    private Object nextAttachment;

    public boolean hasNext() {
      if (nextAttachment == null)
        nextAttachment = nextMatch();
      return nextAttachment != null;
    }

    public Object next() {
      if (nextAttachment != null) {
        Object ret = nextAttachment;
        nextAttachment = null;
        return ret;
      }

      if (hasNext())
        return nextAttachment;

      return null;
    }

    Object nextMatch() {
      while (iter.hasNext()) {
        AttachmentPartImpl ap = (AttachmentPartImpl) iter.next();
        if (ap.hasAllHeaders(headers))
          return ap;
      }
      return null;
    }

    public void remove() {
      iter.remove();
    }
  }

  public Iterator getAttachments(MimeHeaders headers) {
    if (attachments == null)
      return nullIter;

    return new MimeMatchingIterator(headers);
  }

  public AttachmentPart createAttachmentPart() {
    return new AttachmentPartImpl();
  }

  private final ByteInputStream getHeaderBytes()
      throws SOAPException, IOException {
    SOAPPartImpl sp = (SOAPPartImpl) getSOAPPart();
    return sp.getContentAsStream();
  }

  private MimeMultipart getMimeMessage() throws SOAPException {
    try {
      MimeMultipart headerAndBody = new MimeMultipart();

      SOAPPartImpl sp = (SOAPPartImpl) getSOAPPart();
      headerAndBody.addBodyPart(sp.getMimePart());

      for (Iterator i = getAttachments(); i.hasNext();)
        headerAndBody
            .addBodyPart(((AttachmentPartImpl) i.next()).getMimePart());

      ContentType contentType = new ContentType(headerAndBody.getContentType());

      ParameterList l = new ParameterList();
      l.set("type", "text/xml");
      l.set("boundary", contentType.getParameter("boundary"));

      ContentType nct = new ContentType("multipart", "related", l);

      headers.setHeader("Content-Type", nct.toString());

      // TBD
      // Set content length MIME header here.

      return headerAndBody;
    } catch (SOAPException ex) {
      throw ex;
    } catch (Throwable ex) {
      throw new SOAPException(
          "Unable to convert SOAP message into " + "a MimeMultipart object",
          ex);
    }
  }

  public synchronized void saveChanges() throws SOAPException {

    // suck in all the data from the attachments and have it
    // ready for writing/sending etc.

    if (countAttachments() == 0) {
      ByteInputStream in = null;

      try {
        in = getHeaderBytes();
      } catch (IOException ex) {
        throw new SOAPException(
            "Unable to get header stream in saveChanges: " + ex.getMessage());
      }

      messageBytes = in.getBytes();
      messageByteCount = in.getCount();

      headers.setHeader("Content-Type", "text/xml; charset=\"utf-8\"");
      headers.setHeader("Content-Length", Integer.toString(messageByteCount));
    } else {
      ByteOutputStream out = new ByteOutputStream();

      try {
        getMimeMessage().writeTo(out);
      } catch (Throwable ex) {
        ex.printStackTrace();
        throw new SOAPException("Error during saving a multipart message", ex);
      }

      messageBytes = out.getBytes();
      messageByteCount = out.getCount();

      headers.setHeader("Content-Length", Integer.toString(messageByteCount));
    }

    String[] soapAction = headers.getHeader("SOAPAction");

    if (soapAction == null || soapAction.length == 0)
      headers.setHeader("SOAPAction", "\"\"");

    saved = true;
  }

  public void writeTo(OutputStream out) throws SOAPException, IOException {
    if (saveRequired())
      saveChanges();

    out.write(messageBytes, 0, messageByteCount);
    messageBytes = null;
    needsSave();
  }
}
