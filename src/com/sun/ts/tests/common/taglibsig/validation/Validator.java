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

import com.sun.ts.tests.common.taglibsig.TagLibraryDescriptor;

import java.util.List;

/**
 * Performs validation of either all or part of a {@link TagLibraryDescriptor}.
 */
public interface Validator {

  /**
   * <p>
   * Performs validations (specific to the particular implementation) of two
   * {@link TagLibraryDescriptor} instances.
   * </p>
   * 
   * @param control
   *          - the <code>control</code> TagLibraryDescriptor
   * @param underTest
   *          - the TagLibraryDescriptor that we are validating for correctness
   * @return
   */
  public List validate(TagLibraryDescriptor control,
      TagLibraryDescriptor underTest);

}
