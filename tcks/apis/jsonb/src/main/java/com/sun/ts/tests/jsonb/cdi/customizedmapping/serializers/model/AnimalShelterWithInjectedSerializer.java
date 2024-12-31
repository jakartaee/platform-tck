/*
 * Copyright (c) 2017, 2022 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model;

import java.util.ArrayList;
import java.util.List;

import com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalListDeserializerInjected;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalListSerializer;

import jakarta.json.bind.annotation.JsonbTypeDeserializer;
import jakarta.json.bind.annotation.JsonbTypeSerializer;

public class AnimalShelterWithInjectedSerializer {
  @JsonbTypeSerializer(AnimalListSerializer.class)
  @JsonbTypeDeserializer(AnimalListDeserializerInjected.class)
  private List<Animal> animals = new ArrayList<>();

  public List<Animal> getAnimals() {
    return animals;
  }

  public void setAnimals(List<Animal> animals) {
    this.animals = animals;
  }

  public boolean addAnimal(Animal animal) {
    return animals.add(animal);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof AnimalShelterWithInjectedSerializer))
      return false;

    AnimalShelterWithInjectedSerializer that = (AnimalShelterWithInjectedSerializer) o;

    return animals != null ? animals.equals(that.animals)
        : that.animals == null;
  }

  @Override
  public int hashCode() {
    return animals != null ? animals.hashCode() : 0;
  }
}
