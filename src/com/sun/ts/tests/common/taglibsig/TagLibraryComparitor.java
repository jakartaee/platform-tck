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
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.common.taglibsig;

import com.sun.ts.tests.common.taglibsig.validation.ValidationConfiguration;
import com.sun.ts.tests.common.taglibsig.validation.Validator;
import com.sun.ts.tests.common.taglibsig.validation.ValidatorFactory;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides the ability to compare two TagLibraryDescriptor objects and return
 * any differences found.
 */
public class TagLibraryComparitor {

  // The current configuration for this TagLibraryComparitory.
  ValidationConfiguration configuration;

  /**
   * <p>
   * Constructs a new TagLibraryComparitor that will use the provided
   * {@link ValidationConfiguration} to perform its comparison.
   * </p>
   * 
   * @param configuration
   *          - ValidationConfiguration
   */
  public TagLibraryComparitor(ValidationConfiguration configuration) {
    this.configuration = configuration;
  }

  /**
   * <p>
   * Sets a new {@link ValidationConfiguration} to use when performing
   * comparisons.
   * </p>
   * 
   * @param configuration
   *          - ValidationConfiguration
   */
  public void setConfiguration(ValidationConfiguration configuration) {
    this.configuration = configuration;
  }

  /**
   * <p>
   * Performs a comparison of two {@link TagLibraryDescriptor} objects using the
   * configured {@link ValidationConfiguration}.
   * 
   * @param control
   *          - the <code>control</code> TagLibraryDescriptor
   * @param underTest
   *          - the TagLibraryDescriptor that we are validating for correctness
   * @return an empty array if no differences are found
   * @throws IllegalStateException
   *           if the provided ValidationConfiguration is null, or has not be
   *           configured.
   */
  public String[] compare(TagLibraryDescriptor control,
      TagLibraryDescriptor underTest) {
    List messages = new ArrayList();

    if (configuration != null && configuration.hasBeenConfigured()) {
      for (Iterator i = configuration.getValidatorNames(); i.hasNext();) {
        Validator validator = ValidatorFactory
            .getValidator(configuration.getValidatorClass((String) i.next()));
        if (validator != null) {
          messages.addAll(validator.validate(control, underTest));
        }
      }
    } else {
      throw new IllegalStateException("No Configuration Available...");
    }
    return (String[]) messages.toArray(new String[messages.size()]);
  }
}
