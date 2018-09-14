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

package com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters;

import java.util.Properties;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.AnimalShelterInjectedAdapter;
import com.sun.ts.tests.jsonb.customizedmapping.adapters.model.Animal;
import com.sun.ts.tests.jsonb.customizedmapping.adapters.model.Cat;
import com.sun.ts.tests.jsonb.customizedmapping.adapters.model.Dog;

/**
 * @test
 * @sources AdaptersCustomizationTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.adapters.AdaptersCustomizationTest
 **/
/*
 * @class.setup_props: webServerHost; webServerPort; ts_home;
 */
public class AdaptersCustomizationCDITest extends ServiceEETest {

  private static final long serialVersionUID = 10L;

  private final Jsonb jsonb = JsonbBuilder.create();

  public static void main(String[] args) {
    EETest t = new AdaptersCustomizationCDITest();
    Status s = t.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: testCDISupport
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.7.1-3
   *
   * @test_Strategy: Assert that CDI injection is supported in adapters
   */
  public void testCDISupport() throws Fault {
    AnimalShelterInjectedAdapter animalShelter = new AnimalShelterInjectedAdapter();
    animalShelter.addAnimal(new Cat(5, "Garfield", 10.5f, true, true));
    animalShelter.addAnimal(new Dog(3, "Milo", 5.5f, false, true));
    animalShelter.addAnimal(new Animal(6, "Tweety", 0.5f, false));

    String jsonString = jsonb.toJson(animalShelter);
    if (!jsonString.matches("\\{\\s*\"animals\"\\s*:\\s*\\[\\s*"
        + "\\{\\s*\"age\"\\s*:\\s*5\\s*,\\s*\"cuddly\"\\s*:\\s*true\\s*,\\s*\"furry\"\\s*:\\s*true\\s*,\\s*\"name\"\\s*:\\s*\"Garfield\"\\s*,\\s*\"type\"\\s*:\\s*\"CAT\"\\s*,\\s*\"weight\"\\s*:\\s*10.5\\s*}\\s*,\\s*"
        + "\\{\\s*\"age\"\\s*:\\s*3\\s*,\\s*\"barking\"\\s*:\\s*true\\s*,\\s*\"furry\"\\s*:\\s*false\\s*,\\s*\"name\"\\s*:\\s*\"Milo\"\\s*,\\s*\"type\"\\s*:\\s*\"DOG\"\\s*,\\s*\"weight\"\\s*:\\s*5.5\\s*}\\s*,\\s*"
        + "\\{\\s*\"age\"\\s*:\\s*6\\s*,\\s*\"furry\"\\s*:\\s*false\\s*,\\s*\"name\"\\s*:\\s*\"Tweety\"\\s*,\\s*\"type\"\\s*:\\s*\"GENERIC\"\\s*,\\s*\"weight\"\\s*:\\s*0.5\\s*}\\s*"
        + "]\\s*}")) {
      throw new Fault(
          "Failed to correctly marshall complex type hierarchy using an adapter with a CDI managed field configured using JsonbTypeAdapter annotation to a simpler class.");
    }

    AnimalShelterInjectedAdapter unmarshalledObject = jsonb
        .fromJson("{ \"animals\" : [ "
            + "{ \"age\" : 5, \"cuddly\" : true, \"furry\" : true, \"name\" : \"Garfield\" , \"type\" : \"CAT\", \"weight\" : 10.5}, "
            + "{ \"age\" : 3, \"barking\" : true, \"furry\" : false, \"name\" : \"Milo\", \"type\" : \"DOG\", \"weight\" : 5.5}, "
            + "{ \"age\" : 6, \"furry\" : false, \"name\" : \"Tweety\", \"type\" : \"GENERIC\", \"weight\" : 0.5}"
            + " ] }", AnimalShelterInjectedAdapter.class);
    if (!animalShelter.equals(unmarshalledObject)) {
      throw new Fault(
          "Failed to correctly unmarshall complex type hierarchy using an adapter with a CDI managed field configured using JsonbTypeAdapter annotation to a simpler class.");
    }
  }
}
