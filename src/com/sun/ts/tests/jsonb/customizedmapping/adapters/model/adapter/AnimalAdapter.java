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

package com.sun.ts.tests.jsonb.customizedmapping.adapters.model.adapter;

import com.sun.ts.tests.jsonb.customizedmapping.adapters.model.Animal;
import com.sun.ts.tests.jsonb.customizedmapping.adapters.model.Cat;
import com.sun.ts.tests.jsonb.customizedmapping.adapters.model.Dog;

import javax.json.bind.adapter.JsonbAdapter;

import static com.sun.ts.tests.jsonb.customizedmapping.adapters.model.adapter.AnimalJson.TYPE.CAT;
import static com.sun.ts.tests.jsonb.customizedmapping.adapters.model.adapter.AnimalJson.TYPE.DOG;
import static com.sun.ts.tests.jsonb.customizedmapping.adapters.model.adapter.AnimalJson.TYPE.GENERIC;

public class AnimalAdapter implements JsonbAdapter<Animal, AnimalJson> {
  @Override
  public AnimalJson adaptToJson(Animal animal) throws Exception {
    AnimalJson adapted = new AnimalJson();
    if (Cat.class.isAssignableFrom(animal.getClass())) {
      adapted.setType(CAT);
      adapted.setCuddly(((Cat) animal).isCuddly());
    } else if (Dog.class.isAssignableFrom(animal.getClass())) {
      adapted.setType(DOG);
      adapted.setBarking(((Dog) animal).isBarking());
    } else {
      adapted.setType(GENERIC);
    }
    adapted.setName(animal.getName());
    adapted.setAge(animal.getAge());
    adapted.setFurry(animal.isFurry());
    adapted.setWeight(animal.getWeight());
    return adapted;
  }

  @Override
  public Animal adaptFromJson(AnimalJson adapted) throws Exception {
    Animal animal;
    switch (adapted.getType()) {
    case CAT:
      animal = new Cat();
      ((Cat) animal).setCuddly(adapted.isCuddly());
      break;
    case DOG:
      animal = new Dog();
      ((Dog) animal).setBarking(adapted.isBarking());
      break;
    default:
      animal = new Animal();
    }
    animal.setName(adapted.getName());
    animal.setAge(adapted.getAge());
    animal.setFurry(adapted.isFurry());
    animal.setWeight(adapted.getWeight());
    return animal;
  }
}
