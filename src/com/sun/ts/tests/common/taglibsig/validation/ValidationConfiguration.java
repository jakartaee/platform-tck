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

package com.sun.ts.tests.common.taglibsig.validation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This configuration class defines the {@link Validator} names that will be
 * used to perform the validation of the TaglibraryDescriptor objects. This
 * class is used exclusively by the {@link ValidatorFactory}, which will take
 * the names contained within and create {@link Validator} instances.
 */

public class ValidationConfiguration {

  /*
   * All standard Validators will be listed here.
   */
  public static final String URI_VALIDATOR = "URIValidator";

  /*
   * Package name for standard validator implementations.
   */
  private static final String VALIDATOR_PACKAGE = "com.sun.ts.tests.common.taglibsig.validation.";

  /*
   * All standard validators will be stored in here as well. This is used by the
   * initValidators() method.
   */
  private static final String[] VALIDATORS = { URI_VALIDATOR };

  /*
   * Map of all Validator's and their implementing class.
   */
  private static final Map KNOWN_VALIDATORS = new HashMap();

  static {
    // init the standard validator map
    initValidators();
  }

  /*
   * Map containing the Validators the end-user is interested in.
   */
  private Map configuredValidatorMap;

  /**
   * Constructs a new <code>ValidationConfiguation</code> instance.
   */
  public ValidationConfiguration() {
    configuredValidatorMap = new HashMap();
  }

  /**
   * <p>
   * Adds the name of a {@link Validator} implementation to this configuration.
   * The name must be a known name (i.e. be a constant name defined by this
   * class), or a {@link Validator} will not be added. If a non-standard
   * validator is required, use <code>addValidator(String, String)</code>
   * instead.
   * </p>
   *
   * @param validatorName
   *          - Validator name
   */
  public void addValidator(String validatorName) {
    String className = (String) KNOWN_VALIDATORS.get(validatorName);
    if (className != null)
      configuredValidatorMap.put(validatorName, className);
  }

  /**
   * <p>
   * Adds a custom {@link Validator} name to the current configuration.
   * </p>
   * 
   * @param validatorName
   *          - Validator name
   * @param validatorClass
   *          - The class name of this {@link Validator}
   */
  public void addValidator(String validatorName, String validatorClass) {
    configuredValidatorMap.put(validatorName, validatorClass);
  }

  /**
   * <p>
   * Removes the specified {@link Validator} name from the current
   * configuration.
   * </p>
   * 
   * @param validatorName
   *          - Validator name
   */
  public void removeValidator(String validatorName) {
    configuredValidatorMap.remove(validatorName);
  }

  /**
   * <p>
   * Returns an Iterator of the {@link Validator} names in the current
   * configuration.
   * </p>
   * 
   * @return Iterator of this configuration's {@link Validator} names
   */
  public Iterator getValidatorNames() {
    return configuredValidatorMap.keySet().iterator();
  }

  /**
   * <p>
   * Returns the name of the {@link Validator} implementation class.
   * </p>
   * 
   * @param validatorName
   *          - Validator name
   * @return The name of the {@link Validator} implementation class.
   */
  public String getValidatorClass(String validatorName) {
    return (String) configuredValidatorMap.get(validatorName);
  }

  /**
   * <p>
   * True if {@link Validator} names have been added to the current
   * configuration, otherwise false.
   * </p>
   * 
   * @return True if {@link Validator} names have been added to the current
   *         configuration, otherwise false.
   */
  public boolean hasBeenConfigured() {
    if (configuredValidatorMap.size() > 0)
      return true;
    return false;
  }

  // Initialize all standard validators.
  private static void initValidators() {
    for (int i = 0; i < VALIDATORS.length; i++) {
      KNOWN_VALIDATORS.put(VALIDATORS[i], VALIDATOR_PACKAGE + VALIDATORS[i]);
    }
  }
}
