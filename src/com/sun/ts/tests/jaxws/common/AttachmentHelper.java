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

package com.sun.ts.tests.jaxws.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.awt.*;
import java.util.*;
import java.io.*;
import java.net.URL;
import java.awt.image.*;
import javax.xml.soap.AttachmentPart;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.dom.DOMResult;
import javax.activation.DataHandler;
import javax.xml.transform.Source;

import org.w3c.dom.Element;

public class AttachmentHelper {
  public static Iterator getAttachments(Iterator iter) {
    while (iter.hasNext()) {
      Object obj = iter.next();
      if (!(obj instanceof AttachmentPart)) {
        return null;
      }
    }
    return iter;
  }

  public static AttachmentPart getAttachment(java.net.URI ref, Iterator iter) {
    if (iter == null || ref == null) {
      System.out.println("null Iterator for AttachmentPart");
      return null;
    }
    while (iter.hasNext()) {
      AttachmentPart tempAttachment = (AttachmentPart) iter.next();
      if (ref.isOpaque() && ref.getScheme().equals("cid")) {
        String refId = ref.getSchemeSpecificPart();
        String cId = tempAttachment.getContentId();
        if (cId.equals("<" + refId + ">") || cId.equals(refId)) {
          return tempAttachment;
        }
      }
    }
    return null;
  }

  public static boolean compareStreamSource(StreamSource src1,
      StreamSource src2, int length) throws Exception {
    if (src1 == null || src2 == null) {
      System.out.println("compareStreamSource - src1 or src2 null!");
      return false;
    }
    String in = getStringFromStreamSource(src1, length);
    String out = getStringFromStreamSource(src2, length);
    if (in == null) {
      System.out.println("src1 is null");
      return false;
    }
    if (out == null) {
      System.out.println("src2 is null");
      return false;
    }
    return in.equals(out);
  }

  private static String getStringFromStreamSource(StreamSource src, int length)
      throws Exception {
    byte buf[] = null;
    if (src == null)
      return null;
    InputStream is = src.getInputStream();
    if (is != null) {
      buf = new byte[length];
      int count = readTheData(is, buf, length);
      System.out.println("From inputstream: " + new String(buf, 0, count));
      return new String(buf, 0, count);
    } else {
      char buf1[] = new char[length];
      Reader r = src.getReader();
      if (r == null)
        return null;
      r.reset();
      int count = readTheData(r, buf1, length);
      System.out.println("From Reader: " + new String(buf1, 0, count));
      return new String(buf1, 0, count);
    }
  }

  public static boolean compareImages(Image image1, Image image2)
      throws IOException {
    if (image1 == null || image2 == null)
      return false;

    boolean matched = false;
    BufferedImage bi = convertToBufferedImage(image1);
    Rectangle rect = new Rectangle(0, 0, bi.getWidth(), bi.getHeight());
    Iterator iter1 = handlePixels(image1, rect);
    Iterator iter2 = handlePixels(image2, rect);

    while (iter1.hasNext() && iter2.hasNext()) {
      Pixel pixel = (Pixel) iter1.next();
      if (pixel.equals((Pixel) iter2.next())) {
        matched = true;
      } else {
        matched = false;
      }
    }
    if (matched)
      return true;
    return false;
  }

  public static boolean compareImages(Image image1, Image image2,
      Rectangle rect, String attach) {
    boolean matched = compareImages(image1, image2, rect);
    if (matched)
      TestUtil.logMsg(attach + " image content is equal in attachment");
    else
      TestUtil.logErr(attach + " image content is not equal in attachment");
    return matched;
  }

  public static boolean compareImages(Image image1, Image image2,
      Rectangle rect) {
    if (image1 == null || image2 == null)
      return false;

    boolean matched = false;

    Iterator iter1 = handlePixels(image1, rect);
    Iterator iter2 = handlePixels(image2, rect);

    while (iter1.hasNext() && iter2.hasNext()) {
      Pixel pixel = (Pixel) iter1.next();
      if (pixel.equals((Pixel) iter2.next())) {
        matched = true;
      } else {
        matched = false;
      }
    }
    if (matched)
      return true;
    return false;
  }

  public static Iterator handlePixels(Image img, Rectangle rect) {
    int x = rect.x;
    int y = rect.y;
    int w = rect.width;
    int h = rect.height;

    int[] pixels = new int[w * h];
    PixelGrabber pg = new PixelGrabber(img, x, y, w, h, pixels, 0, w);
    try {
      pg.grabPixels();
    } catch (InterruptedException e) {
      System.err.println("interrupted waiting for pixels!");
      return null;
    }
    if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
      System.err.println("image fetch aborted or errored");
      return null;
    }
    // System.out.println("handlePixels: x="+x+", y="+y+", width="+w+",
    // height="+h);
    ArrayList tmpList = new ArrayList();
    for (int j = 0; j < h; j++) {
      for (int i = 0; i < w; i++) {
        tmpList.add(handleSinglePixel(x + i, y + j, pixels[j * w + i]));
      }
    }
    return tmpList.iterator();
  }

  private static Pixel handleSinglePixel(int x, int y, int pixel) {
    int alpha = (pixel >> 24) & 0xff;
    int red = (pixel >> 16) & 0xff;
    int green = (pixel >> 8) & 0xff;
    int blue = (pixel) & 0xff;
    return new Pixel(alpha, red, green, blue);
  }

  private static class Pixel {
    private int a;

    private int r;

    private int g;

    private int b;

    Pixel(int a, int r, int g, int b) {
      this.a = a;
      this.r = r;
      this.g = g;
      this.b = b;
    }

    protected boolean equals(Pixel p) {
      if (p.a == a && p.r == r && p.g == g && p.b == b)
        return true;
      return false;
    }
  }

  private static BufferedImage convertToBufferedImage(Image image)
      throws IOException {
    if (image instanceof BufferedImage) {
      return (BufferedImage) image;

    } else {
      MediaTracker tracker = new MediaTracker(
          null/* not sure how this is used */);
      tracker.addImage(image, 0);
      try {
        tracker.waitForAll();
      } catch (InterruptedException e) {
        throw new IOException(e.getMessage());
      }
      BufferedImage bufImage = new BufferedImage(image.getWidth(null),
          image.getHeight(null), BufferedImage.TYPE_INT_RGB);

      Graphics g = bufImage.createGraphics();
      g.drawImage(image, 0, 0, null);
      return bufImage;
    }
  }

  public static Image getImageDoc(URL url) throws Exception {
    System.out.println("getImageDoc: url=" + url);
    Image image = null;
    try {
      System.out.println("Attempting to load image via URL ...");
      image = javax.imageio.ImageIO.read(url);
      System.out.println("Complete loading image via URL ...");
    } catch (Exception e) {
      System.out.println(
          "Failed loading image via URL with exception: " + e.getMessage());
      System.out.println(
          "Attempting to load image as a resource via ClassLoader ...");
      String file = url.getFile();
      file = file.substring(file.lastIndexOf("/") + 1);
      System.out.println("file=" + file);
      InputStream is = Thread.currentThread().getContextClassLoader()
          .getResourceAsStream(file);
      System.out.println("is=" + is);
      image = javax.imageio.ImageIO.read(is);
      is.close();
    }
    return image;
  }

  public static String getStringDoc(URL url) throws Exception {
    System.out.println("getStringDoc: url=" + url);
    String string = null;
    try {
      System.out.println("Attempting to load URL as String object ...");
      DataHandler dh = new DataHandler(url);
      InputStream is = dh.getInputStream();
      ByteArrayOutputStream baos = readTheData(is);
      string = new String(baos.toByteArray());
      baos.close();
      System.out.println("Complete loading URL as a String object ...");
    } catch (Exception e) {
      System.out
          .println("Failed loading URL as a String object with exception: "
              + e.getMessage());
      System.out.println(
          "Attempting to load resource via ClassLoader as String object ...");
      String file = url.getFile();
      file = file.substring(file.lastIndexOf("/") + 1);
      System.out.println("file=" + file);
      InputStream is = Thread.currentThread().getContextClassLoader()
          .getResourceAsStream(file);
      System.out.println("is=" + is);
      ByteArrayOutputStream baos = readTheData(is);
      string = new String(baos.toByteArray());
      is.close();
      baos.close();
    }
    return string;
  }

  public static StreamSource getSourceDoc(URL url) throws Exception {
    System.out.println("getSourceDoc: url=" + url);
    StreamSource streamSource = null;
    try {
      System.out.println("Attempting to load URL as StreamSource object ...");
      DataHandler dh = new DataHandler(url);
      streamSource = new StreamSource(dh.getInputStream());
      System.out.println("Complete loading URL as a StreamSource object ...");
    } catch (Exception e) {
      System.out.println(
          "Failed loading URL as a StreamSource object with exception: "
              + e.getMessage());
      System.out.println(
          "Attempting to load resource via ClassLoader as StreamSource object ...");
      String file = url.getFile();
      file = file.substring(file.lastIndexOf("/") + 1);
      System.out.println("file=" + file);
      InputStream is = Thread.currentThread().getContextClassLoader()
          .getResourceAsStream(file);
      System.out.println("is=" + is);
      streamSource = new StreamSource(is);
    }
    return streamSource;
  }

  public static DataHandler getDataHandlerDoc(URL url) throws Exception {
    System.out.println("getDataHandlerDoc: url=" + url);
    DataHandler dh = null;
    try {
      System.out.println("Attempting to load URL as DataHandler object ...");
      dh = new DataHandler(url);
      System.out.println("Complete loading URL as a DataHandler object ...");
    } catch (Exception e) {
      System.out
          .println("Failed loading URL as a DataHandler object with exception: "
              + e.getMessage());
      System.out.println(
          "Attempting to load resource via ClassLoader as DataHandler object ...");
      String file = url.getFile();
      file = file.substring(file.lastIndexOf("/") + 1);
      System.out.println("file=" + file);
      dh = new DataHandler(
          Thread.currentThread().getContextClassLoader().getResource(file));
      System.out.println("is=" + dh.getInputStream());
    }
    return dh;
  }

  public static String validateAttachmentData(String expectedDoc,
      String actualDoc, String whichAttachment) {
    String result = null;
    TestUtil.logMsg(
        "Verifying data of attachment matches: [" + whichAttachment + "]");
    if (actualDoc != null) {
      if (!actualDoc.equals(expectedDoc)) {
        TestUtil.logMsg("Verification failed");
        result = "FAILURE: " + whichAttachment
            + " String documents did not compare correctly";
        System.out.println("========================");
        System.out.println(whichAttachment + " comparison failed");
        System.out.println("expected:");
        System.out.println("------------------------");
        System.out.println(expectedDoc);
        System.out.println("------------------------");
        System.out.println("actual:");
        System.out.println("------------------------");
        System.out.println(actualDoc);
        System.out.println("------------------------");
      }
    } else {
      TestUtil.logMsg("Verification failed");
      result = "FAILURE: " + whichAttachment
          + " actual String document was null";
    }
    if (result == null)
      TestUtil.logMsg("Verification passed");
    if (result != null)
      result += "\n";
    return result;
  }

  public static String validateAttachmentData(DataHandler expectedDoc,
      DataHandler actualDoc, String whichAttachment) throws Exception {
    InputStream is = expectedDoc.getInputStream();
    ByteArrayOutputStream baos = readTheData(is);
    String strExpected = new String(baos.toByteArray());
    baos.close();
    is = actualDoc.getInputStream();
    baos = readTheData(is);
    String strActual = new String(baos.toByteArray());
    baos.close();
    String result = validateAttachmentData(strExpected, strActual,
        whichAttachment);
    return result;
  }

  public static String validateAttachmentData(Source expectedDoc,
      Source actualDoc, String whichAttachment) throws Exception {
    StringBuffer result = new StringBuffer();
    boolean debug = false;
    ArrayList alExpected = null;
    ArrayList alActual = null;
    TestUtil.logMsg(
        "Verifying data of attachment matches: [" + whichAttachment + "]");
    if (actualDoc != null) {
      try {
        TestUtil.logMsg("Transform actual XML document to a DOMResult");
        DOMResult dr = JAXWS_Util.getSourceAsDOMResult(actualDoc);
        XMLUtils.startCapturedResults();
        XMLUtils.xmlDumpDOMNodes(dr.getNode());
        alActual = XMLUtils.getCapturedResults();
        XMLUtils.stopCapturedResults();
      } catch (Exception e) {
        TestUtil.logMsg("Transform failed with exception: " + e.getMessage());
        result.append("FAILURE: " + whichAttachment
            + " actual XML document was malformed (unexpected)");
      }
    } else {
      result.append(
          "FAILURE: " + whichAttachment + " actual XML document was null");
    }
    if (expectedDoc != null) {
      if (result.length() == 0) {
        try {
          TestUtil.logMsg("Transform expected XML document to a DOMResult");
          DOMResult dr = JAXWS_Util.getSourceAsDOMResult(expectedDoc);
          XMLUtils.startCapturedResults();
          XMLUtils.xmlDumpDOMNodes(dr.getNode());
          alExpected = XMLUtils.getCapturedResults();
          XMLUtils.stopCapturedResults();
        } catch (Exception e) {
          TestUtil.logMsg("Transform failed with exception: " + e.getMessage());
          result.append("FAILURE: " + whichAttachment
              + " expected XML document was malformed (unexpected)");
        }
      }
    } else {
      result.append(
          "FAILURE: " + whichAttachment + " expected XML document was null");
    }
    if ((alActual != null) && (alExpected != null)) {
      TestUtil
          .logMsg("Verifying that all expected xml items are in the actual");

      for (int i = 0; i < alExpected.size(); i++) {
        int index = alActual.indexOf(alExpected.get(i));
        if (index == -1) {
          result.append("Did not find expected value:" + alExpected.get(i)
              + " in actual result ");
        }
      }
      TestUtil
          .logMsg("Verifying that all actual xml items are in the expected");
      for (int i = 0; i < alActual.size(); i++) {
        int index = alExpected.indexOf(alActual.get(i));
        if (index == -1) {
          result.append("Did not find actual value:" + alActual.get(i)
              + " in expected result ");
        }
      }
    } else {
      if (result.length() == 0)
        result
            .append("Either the Actual or Expected results returned was null");
    }
    if (result.length() > 0) {
      TestUtil.logMsg("Verification failed");
      TestUtil.logMsg(result.toString());
      result.append("\n");
      return result.toString();
    } else
      TestUtil.logMsg("Verification passed");
    return null;
  }

  public static boolean validateAttachmentData(int count1, byte[] data1,
      int count2, byte[] data2, String whichAttachment) {
    int max = 0;
    boolean debug = false;
    TestUtil.logMsg(
        "Verifying data of attachment matches: [" + whichAttachment + "]");
    if (debug) {
      String str1 = new String(data1, 0, count1);
      String str2 = new String(data2, 0, count2);
      TestUtil.logMsg("validateAttachmentData: data1=" + str1);
      TestUtil.logMsg("validateAttachmentData: data2=" + str2);
    }
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    if (count2 > count1) {
      TestUtil.logMsg(
          "Data counts are different so check for and remove any trailing CR's");
      TestUtil.logMsg("Data count1=" + count1 + ", Data count2=" + count2);
      for (int i = count1; i < count2; i++) {
        if ((char) data2[i] != '\r')
          break;
      }
      TestUtil
          .logMsg("Removed " + (count2 - count1) + " trailing CR's from data2");
      count2 = count1;
    }
    if (count1 != count2) {
      TestUtil.logErr(
          whichAttachment + " data count is not equal in request and response");
      TestUtil.logErr("Request data count = " + count1);
      TestUtil.logErr("Response data count = " + count2);
      if (count2 > count1)
        max = count1;
      else
        max = count2;
      ps.printf("data1[%d]=0x%x  data2[%d]=0x%x", max - 1, data1[max - 1],
          max - 1, data2[max - 1]);
      TestUtil.logErr(baos.toString());
      baos.reset();
      if (count2 > count1) {
        for (int i = count1; i < count2; i++) {
          ps.printf("Extra data was: data2[%d]=0x%x|0%o", i, data2[i],
              data2[i]);
          TestUtil.logErr(baos.toString());
          baos.reset();
        }
      } else {
        for (int i = count2; i < count1; i++) {
          ps.printf("Extra data was: data1[%d]=0x%x|0%o", i, data1[i],
              data1[i]);
          TestUtil.logErr(baos.toString());
          baos.reset();
        }
      }
      TestUtil.logMsg("Verification failed");
      return false;
    }
    for (int i = 0; i < count1; i++) {
      if (data1[i] != data2[i]) {
        TestUtil.logErr(
            whichAttachment + " data content is not equal in attachment");
        TestUtil.logErr("Failed at byte " + i + ", data1[" + i + "]=" + data1[i]
            + ", data2[" + i + "]=" + data2[i]);
        return false;
      }
    }
    TestUtil.logMsg("Verification passed");
    TestUtil.logMsg(whichAttachment + " data count [" + count1
        + "] and content is equal in attachment");
    return true;
  }

  public static String validateAttachmentData(Image image1, Image image2,
      String whichAttachment) throws IOException {
    String result = null;
    TestUtil.logMsg(
        "Verifying data of attachment matches: [" + whichAttachment + "]");
    if (image2 != null) {
      if (!compareImages(image1, image2, new Rectangle(0, 0, 100, 120))) {
        System.out.println(whichAttachment + " comparison failed");
        result = "FAILURE: " + whichAttachment
            + " Image documents did not compare correctly";
      }
    } else {
      result = "FAILURE: " + whichAttachment
          + " actual Image document was null";
    }
    if (result != null) {
      TestUtil.logMsg("Verification failed");
      TestUtil.logMsg(result);
      result += "\n";
    } else
      TestUtil.logMsg("Verification passed");
    return result;
  }

  public static void dumpByteArrays(byte[] b1, int c1, byte[] b2, int c2,
      String whichAttachment) {
    System.out.println("in dumpByteArrays");
    boolean error_found = false;
    StringBuffer s1 = new StringBuffer();
    StringBuffer s2 = new StringBuffer();
    int nexti = 0;
    for (int i = 0; i < c1; i++) {
      if (i > c1 || i > c2) {
        nexti = i;
        break;
      }
      s1.append((char) b1[i]);
      s2.append((char) b2[i]);
      if (b1[i] != b2[i]) {
        if (!error_found) {
          System.out.println("FAILURE begins at (index=" + i + ")");
          System.out.printf(
              "byte1[%d]=0x%x|0%o|%d,  byte2[%d]=0x%x|0%o|%d,  count1=%d,  count2=%d\n",
              i, b1[i], b1[i], b1[i], i, b2[i], b2[i], b2[i], c1, c2);
          error_found = true;
        }
        nexti = i + 1;
        break;
      }
    }
    int i = nexti;
    int count = 0;
    while (i < c1 && count < 10) {
      s1.append((char) b1[i++]);
      count++;
    }
    i = nexti;
    count = 0;
    while (i < c2 && count < 10) {
      s2.append((char) b2[i++]);
      count++;
    }
    System.out.println("--------------------------------------------");
    System.out.println("doc1 upto point of error (Expected " + whichAttachment
        + " document)\n" + s1.toString());
    System.out.println("--------------------------------------------");
    System.out.println("doc2 upto point of error (Actual " + whichAttachment
        + " document)\n" + s2.toString());
    System.out.println(
        "========================================================================");
  }

  public static ByteArrayOutputStream readTheData(InputStream is)
      throws Exception {
    byte[] buf = new byte[1024];
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    int count = 0;
    try {
      while ((count = is.read(buf, 0, buf.length)) > 0) {
        baos.write(buf, 0, count);
      }
    } finally {
      is.close();
      baos.close();
    }
    return baos;
  }

  public static int readTheData(InputStream is, byte[] data, int length)
      throws Exception {
    int count = 0;
    int tmpcount = 0;
    try {
      while (tmpcount != -1) {
        tmpcount = is.read(data, count, length - count);
        if (tmpcount != -1)
          count += tmpcount;
        if (tmpcount == 0)
          break;
      }
    } finally {
      is.close();
    }
    return count;
  }

  public static int readTheData(Reader r, char[] data, int length)
      throws Exception {
    int count = 0;
    int tmpcount = 0;
    try {
      while (tmpcount != -1) {
        tmpcount = r.read(data, count, length - count);
        if (tmpcount != -1)
          count += tmpcount;
        if (tmpcount == 0)
          break;
      }
    } finally {
      r.close();
    }
    return count;
  }
}
