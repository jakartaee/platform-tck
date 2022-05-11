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


import jakarta.json.bind.serializer.JsonbSerializer;
import jakarta.json.bind.serializer.SerializationContext;
import jakarta.json.stream.JsonGenerator;

import com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Animal;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Cat;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Dog;

public class AnimalSerializer implements JsonbSerializer<Animal> {
  public void serialize(Animal animal, JsonGenerator jsonGenerator,
      SerializationContext serializationContext) {
    if (animal != null) {
      jsonGenerator.writeStartObject();
      if (Cat.class.isAssignableFrom(animal.getClass())) {
        jsonGenerator.write("type", "cat");
        jsonGenerator.write("cuddly", ((Cat) animal).isCuddly());
      } else if (Dog.class.isAssignableFrom(animal.getClass())) {
        jsonGenerator.write("type", "dog");
        jsonGenerator.write("barking", ((Dog) animal).isBarking());
      } else {
        jsonGenerator.write("type", "animal");
      }
      jsonGenerator.write("age", animal.getAge());
      jsonGenerator.write("furry", animal.isFurry());
      jsonGenerator.write("name", animal.getName());
      jsonGenerator.write("weight", animal.getWeight());
      jsonGenerator.writeEnd();
    } else {
      serializationContext.serialize(null, jsonGenerator);
    }
  }
}
