/**
 * Copyright Werner Punz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// Original code stemming 100% from me, hence relicense from EPL
package org.jboss.cdi.tck.selenium;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * a helper class providing pool management for our drivers
 * Note, the pool itself is thread safe (and must be), the drivers are not!
 */
@SuppressWarnings("unused")
public class DriverPool {

    ConcurrentLinkedQueue<ExtendedWebDriver> allDrivers = new ConcurrentLinkedQueue<>();
    ConcurrentLinkedQueue<ExtendedWebDriver> availableDrivers = new ConcurrentLinkedQueue<>();


    /**
     * creates or activates a new driver instance
     * @return a new or recycled driver instance
     */
    public synchronized ExtendedWebDriver getOrNewInstance() {
        //synchronized to avoid get race conditions.... there is a non synchonzed part between the check and remove
        //to make this easy we simply synchronize the get to fix it
        ExtendedWebDriver webDriver = (availableDrivers.size() > 0) ? availableDrivers.remove() : null;
        if(webDriver == null) {
            webDriver = ChromeDevtoolsDriver.stdInit();
            allDrivers.add(webDriver);
        }

        webDriver.postInit();
        return webDriver;
    }

    /**
     * resets a driver and keeps it in the pool for recycling
     * @param driver
     */
    public void returnInstance(ExtendedWebDriver driver) {
        driver.reset();
        availableDrivers.add(driver);
    }

    /**
     * closes a driver but keeps it in the pool
     * @param driver
     */
    public void returnAndCloseInstance(ExtendedWebDriver driver) {
        driver.close();
        availableDrivers.add(driver);
    }

    /**
     * quits a driver and removes it from the pool
     * @param driver
     */
    public void quitInstance(ExtendedWebDriver driver) {
        driver.quit();
        allDrivers.remove(driver);
    }

    public void removeInstance(ExtendedWebDriver driver) {
        allDrivers.remove(driver);
    }

    /**
     * cleans up the pool
     */
    public void quitAll() {
        allDrivers.stream().forEach(webDriver -> webDriver.quit());
        allDrivers.clear();
        availableDrivers.clear();
    }
}
