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

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * an extended web driver interface which takes the response into consideration
 * selenium does not have an official api but several webdrivers
 * allow access to the data via various means
 *
 * Another possibility would have been a proxy, but I could not find any properly
 * working proxy for selenium
 */
public interface ExtendedWebDriver extends WebDriver {

    /**
     * gets the response status of the last response (of the last triggered request against get)
     * @return
     */
    int getResponseStatus();

    /**
     * gets the last response as body
     * @return
     */
    String getResponseBody();


    /**
     * @return the request post data as string
     */
    public String getRequestData();

    /**
     * postinit call for tests
     */
    void postInit();

    /**
     * gets the internal webdriver delegate
     * @return
     */
    WebDriver getDelegate();

    /**
     * returns a reference to the Selenium JS Executor of this webdriver
     * @return
     */
    JavascriptExecutor getJSExecutor();

    /**
     * @return the innerText of the Page
     */
    String getPageText();

    /**
     * returns the innerText of the page in a blank reduced state (more than one blank is reduced to one
     * invisible blanks like nbsp are replaced by normal blanks)
     * @return
     */
    String getPageTextReduced();

    /**
     * debugging helper which allows to look into the processed response data
     */
    void printProcessedResponses();

    /**
     * quits the driver engine
     */
    void quit();

    /**
     * closes the current tab
     */
    void close();

    /**
     * resets the current page to its initial stage before the page load
     * without dropping the engine or closing it
     */
    void reset();
}
