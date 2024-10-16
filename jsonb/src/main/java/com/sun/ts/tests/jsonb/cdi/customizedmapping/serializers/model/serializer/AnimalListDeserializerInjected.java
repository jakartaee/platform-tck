/*
 * Copyright (c) 2017, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Animal;

import jakarta.inject.Inject;
import jakarta.json.bind.serializer.DeserializationContext;
import jakarta.json.bind.serializer.JsonbDeserializer;
import jakarta.json.stream.JsonParser;

public class AnimalListDeserializerInjected
    implements JsonbDeserializer<List<Animal>> {
  @Inject
  private AnimalDeserializer animalDeserializer;

  public List<Animal> deserialize(JsonParser jsonParser,
      DeserializationContext deserializationContext, Type type) {
    // start array
    List<Animal> animals = new ArrayList<>();
    while (jsonParser.next() == JsonParser.Event.START_OBJECT) {
      animals.add(animalDeserializer.deserialize(jsonParser,
          deserializationContext, type));
    }

    return animals;
  }
}
