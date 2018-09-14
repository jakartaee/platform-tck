/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.adapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.json.bind.adapter.JsonbAdapter;

import com.sun.ts.tests.jsonb.customizedmapping.adapters.model.Animal;
import com.sun.ts.tests.jsonb.customizedmapping.adapters.model.adapter.AnimalJson;

public class InjectedListAdapter
    implements JsonbAdapter<List<Animal>, List<AnimalJson>> {
  @Inject
  private InjectedAdapter animalAdapter;

  @Override
  public List<AnimalJson> adaptToJson(List<Animal> animals) throws Exception {
    List<AnimalJson> adapted = new ArrayList<>();
    for (Animal animal : animals) {
      adapted.add(animalAdapter.adaptToJson(animal));
    }
    return adapted;
  }

  @Override
  public List<Animal> adaptFromJson(List<AnimalJson> adapted) throws Exception {
    List<Animal> animals = new ArrayList<>();
    for (AnimalJson animal : adapted) {
      animals.add(animalAdapter.adaptFromJson(animal));
    }
    return animals;
  }
}
