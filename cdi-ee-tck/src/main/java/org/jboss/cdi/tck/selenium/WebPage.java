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

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * Mimics the html unit webpage
 */
public class WebPage {

    public static final Duration STD_TIMEOUT = Duration.ofMillis(8000);
    public static final Duration LONG_TIMEOUT = Duration.ofMillis(16000);

    protected ExtendedWebDriver webDriver;

    public WebPage(ExtendedWebDriver webDriver) {
        this.webDriver = webDriver;
    }

    
    public ExtendedWebDriver getWebDriver() {
        return webDriver;
    }

    
    public void setWebDriver(ExtendedWebDriver webDriver) {
        this.webDriver = webDriver;
    }

    /**
     * implemented helper to wait for the background javascript
     *
     * @param timeout the standard timeout to wait in case the condition is not executed
     */
    public void waitForBackgroundJavascript(Duration timeout) {
        this.waitForBackgroundJavascript(timeout, Duration.ZERO);
    }

    /**
     * This method fixes following issue: While we wait for javascripts
     * we cannot be entirely sure, that the execution has fully terminated.
     * The problem is asynchronous code, which opens execution windows.
     * HTML Unit does not have the problem, because it never executes the code
     * in a separate process and has full track of the execution "windows"
     *
     * There is no way to fix this, given the asynchronous nature of the Selenium drivers.
     * The best bet is simply to give the possibility of another wait delay,
     * after the supposed execution end, and also use waitForCondition with a dom
     * check wherever possible (aka dom changes happen)
     *
     * @param timeout the timeout until the wait is terminated max
     * @param delayAfterExcecution this introduces a second delay after the determined
     *                             end of exeuction point.
     */
    public void waitForBackgroundJavascript(Duration timeout, Duration delayAfterExcecution) {
        synchronized (webDriver) {
            WebDriverWait wait = new WebDriverWait(webDriver, timeout);
            double rand = Math.random();
            @SuppressWarnings("UnnecessaryLabelJS") final String identifier = "__insert__:" + rand;
            webDriver.manage().timeouts().scriptTimeout(timeout);
            // We use a trick here, javascript  is cooperative multitasking
            // we defer into a time when the script is executed
            // and then execute a small invisible script adding an element
            // At the time the element gets added, we are either at an end of execution
            // phase or in an execution pause (timeouts maybe pending etc...)
            try {
                webDriver.getJSExecutor().executeAsyncScript("let [resolve] = arguments; setTimeout(function() { var insert__ = document.createElement('div');" +
                        "insert__.id = '" + identifier + "';" +
                        "insert__.innerHTML = 'done';" +
                        "document.body.append(insert__); resolve()}, 50);");


                wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.id(identifier), 0));
                // problem is here, if we are in a code pause phase (aka timeout is pending but not executed)
                // we are still not at the end of the code. For those cases a simple wait on top might fix the issue
                // we cannot determine the end of the execution here anymore.
                if (!delayAfterExcecution.isZero()) {
                    wait(delayAfterExcecution);
                }
            } finally {
                webDriver.getJSExecutor().executeScript("document.body.removeChild(document.getElementById('" + identifier + "'));");
            }
        }
    }

    /**
     * waits for a certain condition is met, until a timeout is hit.
     * In case of exceeding the condition, a runtime exception is thrown!
     * @param isTrue the condition lambda to check
     * @param timeout timeout duration
     */
    public <V> void waitForCondition(Function<? super WebDriver, V> isTrue, Duration timeout) {
        synchronized (webDriver) {
            WebDriverWait wait = new WebDriverWait(webDriver, timeout);
            wait.until(isTrue);
        }
    }

    /**
     * The same as before, but with the long default timeout of LONG_TIMEOUT (16000ms)
     * @param isTrue condition lambda
     */
    public <V> void waitForCondition(Function<? super WebDriver, V> isTrue) {
        synchronized (webDriver) {
            WebDriverWait wait = new WebDriverWait(webDriver, LONG_TIMEOUT);
            wait.until(isTrue);
        }
    }


    /**
     * Wait for a certain period of time
     * @param timeout the timeout to wait (note due to the asynchronous nature
     *                of the web drivers, any code running on the browser itself
     *                will proceed (aka javascript)
     *                only the client operations are stalled.
     */
    public void wait(Duration timeout) {
        synchronized (webDriver) {
            try {
                webDriver.wait(timeout.toMillis());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * waits until no more running requests are present
     * in case of exceeding our STD_TIMEOUT, a runtime exception is thrown
     */
    public void waitForCurrentRequestEnd() {
        waitForCondition(webDriver1 -> webDriver.getResponseStatus() != -1, STD_TIMEOUT);
    }

    /**
     * wait for the current request to be ended, with a timeout of "timeout"
     * @param timeout the timeout for the waiting, if it is exceeded a timeout exception is thrown
     */
    public void waitForCurrentRequestEnd(Duration timeout) {
        waitForCondition(webDriver1 -> webDriver.getResponseStatus() != -1, timeout);
    }

    /**
     * wait until the current Ajax request targeting the same page
     * has stopped and then tests for blocking running scripts
     * still running.
     * A small initial delay cannot hurt either
     */
    public void waitReqJs() {
        this.waitReqJs(STD_TIMEOUT);
    }

    /**
     * same as before but with a dedicated timeout
     * @param timeout the timeout duration after that the wait is cancelled
     *                and an exception is thrown
     */
    public void waitReqJs(Duration timeout) {
        // We stall the connection between browser and client for 200ms to make sure everything
        // is done (usually a request takes between 6 and 20ms), but
        // Note, if you have long-running request, I recommend to wait for a condition instead
        this.wait(Duration.ofMillis(200));

        // just in case the request takes longer, we also check the request queue for the current request to end
        waitForCurrentRequestEnd(timeout);

        // we stall the tests at another 100ms simply to make sure everything has been properly executed
        // this reduces the chance to fall into an execution window significantly, but does not eliminate it entirely
        waitForBackgroundJavascript(timeout, Duration.ofMillis(100));
    }

    /**
     * waits for backgrounds processes on the browser to complete
     *
     * @param timeOut the timeout duration until the wait can proceed before being interupopted
     */
    public void waitForPageToLoad(Duration timeOut) {
        ExpectedCondition<Boolean> expectation = driver -> webDriver.getJSExecutor()
                .executeScript("return document.readyState").equals("complete");
        synchronized (webDriver) {
            WebDriverWait wait = new WebDriverWait(webDriver, timeOut);
            wait.until(expectation);
        }
    }

    /**
     * wait until the page load is finished
     */
    public void waitForPageToLoad() {
        waitForPageToLoad(STD_TIMEOUT);
    }

    /**
     * conditional waiter and checker which checks whether the page text is present
     * we add our own waiter internally, because pageSource always delivers
     * @param text to check
     * @return true in case of found false in case of found after our standard timeout is reached
     */
    public boolean isInPageText(String text) {
        try {
            //values are not returned by getPageText
            String values = getInputValues();

            waitForCondition(webDriver1 -> (webDriver.getPageText() + values).contains(text), STD_TIMEOUT);
            return true;
        } catch (TimeoutException ex) {
            //timeout is wanted in this case and should result in a false
            return false;
        }
    }

    public boolean isInPageTextReduced(String text) {
        try {
            String values = getInputValues();
            waitForCondition(webDriver1 -> (webDriver.getPageTextReduced() + values.replaceAll("\\s+", " ")).contains(text), STD_TIMEOUT);
            return true;
        } catch (TimeoutException ex) {
            //timeout is wanted in this case and should result in a false
            return false;
        }
    }

    public boolean matchesPageText(String regexp) {
        try {
            waitForCondition(webDriver1 -> webDriver.getPageText().matches(regexp), STD_TIMEOUT);
            return true;
        } catch (TimeoutException ex) {
            //timeout is wanted in this case and should result in a false
            return false;
        }
    }

    /**
     * adds the reduced page text functionality to the regexp match
     *
     * @param regexp
     * @return
     */
    public boolean matchesPageTextReduced(String regexp) {
        try {
            waitForCondition(webDriver1 -> webDriver.getPageTextReduced().matches(regexp), STD_TIMEOUT);
            return true;
        } catch (TimeoutException ex) {
            //timeout is wanted in this case and should result in a false
            return false;
        }
    }
    /**
     * conditional waiter and checker which checks whether a text is not in the page
     * we add our own waiter internally, because pageSource always delivers
     * @param text to check
     * @return true in case of found false in case of found after our standard timeout is reached
     */
    public boolean isNotInPageText(String text) {
        try {
            String values = getInputValues();
            waitForCondition(webDriver1 -> !(webDriver.getPageText() + values).contains(text), STD_TIMEOUT);
            return true;
        } catch (TimeoutException ex) {
            //timeout is wanted in this case and should result in a false
            return false;
        }
    }

    /**
     * conditional waiter and checker which checks whether a text is  in the page
     * we add our own waiter internally, because pageSource always delivers
     * this version of isInPage checks explicitly the full markup not only the text
     * @param text to check
     * @return true in case of found false in case of found after our standard timeout is reached
     */
    public boolean isInPage(String text) {
        try {
            String values = getInputValues();
            waitForCondition(webDriver1 -> (webDriver.getPageSource() + values).contains(text), STD_TIMEOUT);
            return true;
        } catch (TimeoutException ex) {
            //timeout is wanted in this case and should result in a false
            return false;
        }
    }
    /**
     * conditional waiter and checker which checks whether a text is not in the page
     * we add our own waiter internally, because pageSource always delivers
     * we need to add two different condition checkers herte because
     * a timeout automatically throws internally an error which is mapped to false
     * We therefore cannot simply wait for the condition either being met or timeout
     * with one method
     * @param text to check
     * @return true in case of found false in case of found after our standard timeout is reached
     */
    public boolean isNotInPage(String text) {
        try {
            String values = getInputValues();
            waitForCondition(webDriver1 -> !(webDriver.getPageSource() + values).contains(text), STD_TIMEOUT);
            return true;
        } catch (TimeoutException ex) {
            //timeout is wanted in this case and should result in a false
            return false;
        }
    }

    /**
     * conditional waiter and checker which checks whether a text is in the page
     * we add our own waiter internally, because pageSource always delivers
     * we need to add two different condition checkers herte because
     * a timeout automatically throws internally an error which is mapped to false
     * We therefore cannot simply wait for the condition either being met or timeout
     * with one method
     * @param text to check
     * @return true in case of found false in case of found after our standard timeout is reached
     */
    public boolean isInPage(String text, boolean allowExceptions) {
        try {
            String values = getInputValues();
            waitForCondition(webDriver1 -> (webDriver.getPageSource() + values).contains(text), STD_TIMEOUT);
            return true;
        } catch (TimeoutException exception) {
            if(allowExceptions) {
                throw exception;
            }
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * is condition reached or not reached after until a STD_TIMEOUT is reached
     * if the timeout is exceeded the condition is not met
     * @param isTrue the isTrue condition lambda
     * @return true if it is met until STD_TIMEOUT, otherwise false
     */
    public <V> boolean isCondition(Function<? super WebDriver, V> isTrue) {
        try {
            waitForCondition(isTrue, STD_TIMEOUT);
            return true;
        } catch (TimeoutException ex) {
            //timeout is wanted in this case and should result in a false
            return false;
        }
    }

    public WebElement findElement(By by) {
        return webDriver.findElement(by);
    }
    public List<WebElement> findElements(By by) {
        return webDriver.findElements(by);
    }

    public int getResponseStatus() {
        return webDriver.getResponseStatus();
    }

    public String getResponseBody() {
        return webDriver.getResponseBody();
    }

    public String getRequestData() {
        return webDriver.getRequestData();
    }

    public void postInit() {
        webDriver.postInit();
    }

    public JavascriptExecutor getJSExecutor() {
        return webDriver.getJSExecutor();
    }

    public void printProcessedResponses() {
        webDriver.printProcessedResponses();
    }

    public void get(String url) {
        webDriver.get(url);
    }

    public String getCurrentUrl() {
        return webDriver.getCurrentUrl();
    }

    public String getTitle() {
        return webDriver.getTitle();
    }

    public String getPageSource() {
        return webDriver.getPageSource();
    }

    public void close() {
        webDriver.close();
    }

    public void quit() {
        webDriver.quit();
    }

    public Set<String> getWindowHandles() {
        return webDriver.getWindowHandles();
    }

    public String getWindowHandle() {
        return webDriver.getWindowHandle();
    }

    public WebDriver.TargetLocator switchTo() {
        return webDriver.switchTo();
    }

    public WebDriver.Navigation navigate() {
        return webDriver.navigate();
    }

    public WebDriver.Options manage() {
        return webDriver.manage();
    }

    /**
     * Convenience method to get all anchor elmements
     * @return a list of a hrefs as WebElements
     */
    public List<WebElement> getAnchors() {
        return webDriver.findElements(By.cssSelector("a[href]"));
    }

    private String getInputValues() {
        return webDriver.findElements(By.cssSelector("input, textarea, select"))
                .stream()
                .map(webElement -> webElement.getAttribute("value"))
                .reduce("", (str1, str2) -> str1 + " " + str2);
    }
}
