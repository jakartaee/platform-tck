/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.spec.render.common;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;

/**
 * $Id
 */
public class SelectManyValidator {

  public SelectManyValidator() {
    // Default CTOR
  }

  private String[] selectIds = { "array", "list", "set", "sortedset",
      "collection", "ilist", "iset", "isortedset", "icollection", "hintString",
      "hintClass", "object" };

  private String[][] initialSelections = { new String[0], new String[0],
      new String[0], new String[0], new String[0],
      new String[] { "Bilbo", "Pippin", "Merry" }, new String[] { "Frodo" },
      new String[] { "Pippin", "Frodo" }, new String[] { "Bilbo", "Merry" },
      new String[0], new String[0], new String[0] };

  private String[][] postBackSelections = { new String[] { "Bilbo" },
      new String[] { "Bilbo" }, new String[] { "Bilbo" },
      new String[] { "Bilbo" }, new String[] { "Bilbo" },
      new String[] { "Bilbo" }, new String[] { "Bilbo" },
      new String[] { "Bilbo" }, new String[] { "Bilbo" },
      new String[] { "Bilbo" }, new String[] { "Bilbo" },
      new String[] { "Bilbo" } };

  public boolean validate(HtmlPage page, Formatter formatter, HtmlForm form) {

    List<HtmlSelect> selects = new ArrayList<HtmlSelect>();
    for (DomElement e : form.getChildElements()) {
      if (e instanceof HtmlSelect) {
        selects.add((HtmlSelect) e);
      }
    }

    for (int i = 0; i < selectIds.length; i++) {
      String id = selectIds[i];
      System.out.println("Validating HtmlSelect with ID: " + id);
      String[] initialSelection = initialSelections[i];
      String[] newSelection = postBackSelections[i];

      HtmlSelect select = getHtmlSelectForId(selects, id);

      if (!validateExistence(id, "input", select, formatter)) {
        return false;
      }

      if (!(validateState(select, initialSelection, formatter))) {
        return false;
      }
      updateSelections(select, newSelection);
    }

    HtmlInput input = getInputIncludingId(page, "command");
    HtmlPage clickPage;
    try {
      clickPage = (HtmlPage) input.click();
    } catch (IOException ex) {
      formatter.format("Unexpected exception clicking button: %s%n", "command");
      return false;
    }

    // ensure no messages were queued by the post-back
    if (clickPage.asText().contains("Error:")) {
      formatter.format("post-back contains 'Error:'");
    }

    selects.clear();

    List<HtmlSelect> clrselects = new ArrayList<HtmlSelect>();
    for (DomElement e : form.getChildElements()) {
      if (e instanceof HtmlSelect) {
        clrselects.add((HtmlSelect) e);
      }
    }

    for (int i = 0; i < selectIds.length; i++) {
      String id = selectIds[i];
      String[] newSelection = postBackSelections[i];

      HtmlSelect select = getHtmlSelectForId(selects, id);

      if (!(validateState(select, newSelection, formatter))) {
        return false;
      }
    }

    // If everything Passes
    return true;
  }

  public boolean validate(HtmlPage page, Formatter formatter, HtmlForm form,
      boolean isCheckbox) {

    if (!isCheckbox) {
      List<HtmlSelect> selects = new ArrayList<HtmlSelect>();
      for (DomElement e : form.getChildElements()) {
        if (e instanceof HtmlSelect) {
          selects.add((HtmlSelect) e);
        }
      }
      for (int i = 0; i < selectIds.length; i++) {
        String id = selectIds[i];
        System.out.println("Validating HtmlSelect with ID: " + id);
        String[] initialSelection = initialSelections[i];
        String[] newSelection = postBackSelections[i];

        HtmlSelect select = getHtmlSelectForId(selects, id);

        if (!validateExistence(id, "input", select, formatter)) {
          return false;
        }

        if (!(validateState(select, initialSelection, formatter))) {
          return false;
        }
        updateSelections(select, newSelection);
      }

      HtmlInput input = getInputIncludingId(page, "command");
      try {
        page = (HtmlPage) input.click();
      } catch (IOException ex) {
        formatter.format("Unexpected exception clicking button: %s%n",
            "command");
        return false;
      }

      // ensure no messages were queued by the post-back
      if (page.asText().contains("Error:")) {
        formatter.format("post-back contains 'Error:'");
      }

      selects.clear();

      List<HtmlSelect> clrselects = new ArrayList<HtmlSelect>();
      for (DomElement e : form.getChildElements()) {
        if (e instanceof HtmlSelect) {
          clrselects.add((HtmlSelect) e);
        }
      }

      for (int i = 0; i < selectIds.length; i++) {
        String id = selectIds[i];
        String[] newSelection = postBackSelections[i];
        HtmlSelect select = getHtmlSelectForId(selects, id);

        if (!(validateState(select, newSelection, formatter))) {
          return false;
        }
      }

    } else {
      List<HtmlTable> tables = new ArrayList<HtmlTable>();

      for (HtmlElement e : form.getHtmlElementDescendants()) {
        if (e instanceof HtmlTable) {
          tables.add((HtmlTable) e);
        }
      }

      for (int i = 0; i < selectIds.length; i++) {
        String id = selectIds[i];
        System.out.println("Loading HtmlTable with ID: " + id);
        String[] initialSelection = initialSelections[i];
        String[] newSelection = postBackSelections[i];
        List<HtmlInput> inputElements = new ArrayList<HtmlInput>();

        for (HtmlElement hi : tables.get(i).getHtmlElementDescendants()) {
          if (hi instanceof HtmlInput) {
            // Validate and add the HtmlInputs.
            String hiId = ((HtmlInput) hi).getId();
            if (!validateExistence(hiId, "input", (HtmlInput) hi, formatter)) {
              return false;
            } else {
              System.out.println("Validating HtmlInput with " + "ID: " + hiId);
              inputElements.add((HtmlInput) hi);
            }
          }
        }
        if (!(validateState(inputElements, initialSelection, formatter))) {
          return false;
        }

        updateSelections(inputElements, newSelection);
      }

      HtmlInput input = getInputIncludingId(page, "command");
      try {
        page = (HtmlPage) input.click();
      } catch (IOException ex) {
        formatter.format("Unexpected exception clicking button: %s%n",
            "command");
        return false;
      }

      // ensure no messages were queued by the post-back
      if (page.asText().contains("Error:")) {
        formatter.format("post-back contains 'Error:'");
        return false;
      }

      tables.clear();

      List<HtmlTable> clrtables = new ArrayList<HtmlTable>();
      for (HtmlElement e : form.getHtmlElementDescendants()) {
        if (e instanceof HtmlTable) {
          clrtables.add((HtmlTable) e);
        }
      }
      for (int i = 0; i < selectIds.length; i++) {
        String[] newSelection = postBackSelections[i];
        List<HtmlInput> newinputElements = new ArrayList<HtmlInput>();

        for (HtmlElement hi : clrtables.get(i).getHtmlElementDescendants()) {
          if (hi instanceof HtmlInput) {
            // Validate and add the HtmlInputs.
            String hiId = ((HtmlInput) hi).getId();
            if (!validateExistence(hiId, "input", (HtmlInput) hi, formatter)) {
              return false;
            } else {
              newinputElements.add((HtmlInput) hi);
            }
          }
        }
        if (!(validateState(newinputElements, newSelection, formatter))) {
          return false;
        }

      }
    }
    // If everything Passes
    return true;

  }

  private boolean validateState(HtmlSelect select, String[] selectedOptions,
      Formatter formatter) {
    if (select != null) {
      List<HtmlOption> options = select.getOptions();
      if (options.size() == 4) {
        if (selectedOptions == null || selectedOptions.length == 0) {
          for (HtmlOption option : options) {
            if (option.isSelected()) {
              formatter.format(
                  option.getId() + " is selected and should not have been!");
              return false;
            }
          }
        } else {
          for (String text : selectedOptions) {
            for (HtmlOption option : options) {
              if (text.equals(option.asText())) {
                if (!option.isSelected()) {
                  formatter.format(option.getId()
                      + " is not selected and should have" + " been!");
                  return false;
                }
              }
            }
          }
        }
      }
    }
    return true;
  }

  private boolean validateState(List<HtmlInput> inputs,
      String[] selectedOptions, Formatter formatter) {

    if ((inputs != null) && (inputs.size() == 4)) {

      for (HtmlInput input : inputs) {
        Collection<DomAttr> options = input.getAttributesMap().values();

        if ((selectedOptions == null || selectedOptions.length == 0)) {
          for (DomAttr option : options) {
            if (("checked".equals(option.getName())
                && ("checked".equals(option.getValue())))) {
              formatter.format(option.getName()
                  + " is marked as checked and should not " + "have been!");
              return false;
            }
          }
        } else {
          for (String text : selectedOptions) {
            for (DomAttr option : options) {
              if (text.equals(option.getName())) {
                if ("checked".equals(option.getValue())) {
                  formatter.format(option.getName()
                      + " is not selected and should have" + " been!");
                  return false;
                }
              }
            }
          }
        }
      }
    } else {
      formatter.format("List of Inputs is either 'null' or less " + "then 4");
      return false;
    }

    return true;
  }

  private void updateSelections(HtmlSelect select, String[] selectedOptions) {
    if (select != null) {
      List<HtmlOption> options = select.getOptions();
      if (options.size() == 4) {
        for (String s : selectedOptions) {
          for (HtmlOption option : options) {
            option.setSelected(s.equals(option.asText()));
          }
        }
      }
    }
  }

  private void updateSelections(List<HtmlInput> inputs,
      String[] selectedOptions) {
    if (inputs != null) {
      for (HtmlInput input : inputs) {
        for (String s : selectedOptions) {
          if (input.getValueAttribute().equals(s)) {
            input.setChecked(true);
          }
        }
      }
    }
  }

  private HtmlSelect getHtmlSelectForId(List<HtmlSelect> selects, String id) {

    for (HtmlSelect select : selects) {
      if (select.getId().contains(id)) {
        return select;
      }

    }

    return null;
  }

  private boolean validateExistence(String id, String elementName,
      HtmlElement element, Formatter formatter) {

    if (element == null) {
      formatter.format(
          "Unable to find rendered '%s' element containing " + "the ID '%s'%n",
          elementName, id);
      return false;
    }

    return true;
  }

  private HtmlInput getInputIncludingId(HtmlPage root, String id) {

    HtmlInput result = null;

    List list = getAllElementsOfGivenClass(root, null, HtmlInput.class);
    for (int i = 0; i < list.size(); i++) {
      result = (HtmlInput) list.get(i);
      if (-1 != result.getId().indexOf(id)) {
        break;
      }
      result = null;
    }
    return result;
  }

  private List getAllElementsOfGivenClass(HtmlElement root,
      List<HtmlElement> list, Class matchClass) {

    if (null == root) {
      return list;
    }
    if (null == list) {
      list = new ArrayList<HtmlElement>();
    }

    for (Iterator<HtmlElement> i = root.getHtmlElementDescendants()
        .iterator(); i.hasNext();) {
      getAllElementsOfGivenClass(i.next(), list, matchClass);
    }

    if (matchClass.isInstance(root)) {
      if (!list.contains(root)) {
        list.add((HtmlElement) root);
      }
    }
    return list;

  }

  private List getAllElementsOfGivenClass(HtmlPage root, List<HtmlElement> list,
      Class matchClass) {

    return getAllElementsOfGivenClass(root.getDocumentElement(), list,
        matchClass);

  }
}
