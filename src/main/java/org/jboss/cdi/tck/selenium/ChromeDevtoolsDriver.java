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

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.output.NullOutputStream;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverLogLevel;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumNetworkConditions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v108.network.Network;
import org.openqa.selenium.devtools.v108.network.model.Request;
import org.openqa.selenium.devtools.v108.network.model.RequestId;
import org.openqa.selenium.devtools.v108.network.model.ResponseReceived;
import org.openqa.selenium.devtools.v108.network.model.TimeSinceEpoch;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.Location;
import org.openqa.selenium.html5.SessionStorage;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.logging.EventType;
import org.openqa.selenium.mobile.NetworkConnection;
import org.openqa.selenium.print.PrintOptions;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.virtualauthenticator.VirtualAuthenticator;
import org.openqa.selenium.virtualauthenticator.VirtualAuthenticatorOptions;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Extended driver which we need for getting
 * the http response code
 * and the http response
 * without having to revert to proxy solutions
 * <p>
 * We need access top the response body and response
 * code from always the last access
 * <p>
 * We use the chrome dev tools to access the data but we isolate
 * the new functionality in an interface, so other drivers must apply something different
 * to get the results
 *
 * @see also https://medium.com/codex/selenium4-a-peek-into-chrome-devtools-92bca6de55e0
 */

class HttpCycleData {
    public RequestId requestId;
    public Request request;
    public ResponseReceived responseReceived;
}

@SuppressWarnings("unused")
public class ChromeDevtoolsDriver implements ExtendedWebDriver {

    ChromeDriver delegate;

    List<HttpCycleData> cycleData = new CopyOnWriteArrayList<>();

    String lastGet;

    public ChromeDevtoolsDriver(ChromeOptions options) {
        ChromeDriverService chromeDriverService = new ChromeDriverService.Builder().build();

        chromeDriverService.sendOutputTo(NullOutputStream.NULL_OUTPUT_STREAM);
        delegate = new ChromeDriver(chromeDriverService, options);
    }

    /**
     * initializes the extended functionality
     */
    public void postInit() {
        DevTools devTools = getDevTools();
        //we store always the last request for further reference
        initNetworkListeners(devTools);
        initDevTools(devTools);
        disableCache(devTools);
    }

    private static void disableCache(DevTools devTools) {
        devTools.send(Network.setCacheDisabled(true));
    }

    private static void initDevTools(DevTools devTools) {
        try {
            devTools.createSession();
            devTools.send(Network.clearBrowserCache());
        } catch (TimeoutException ex) {
            Logger log = Logger.getLogger(ChromeDevtoolsDriver.class.getName());
            log.warning("Init timeout error, can happen, " +
                    "if the driver already has been used, can be safely ignore");
        }
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
    }

    private void initNetworkListeners(DevTools devTools) {
        devTools.addListener(Network.requestWillBeSent(),
                entry -> {
                    // the jsf ajax only targets itself
                    // that way we can filter out resource requests early
                    if (!entry.getRequest().getUrl().contains(lastGet)) {
                        return;
                    }
                    HttpCycleData data = new HttpCycleData();
                    data.requestId = entry.getRequestId();
                    data.request = entry.getRequest();
                    cycleData.add(data);
                });
        devTools.addListener(Network.responseReceived(), entry -> {
            RequestId requestId = entry.getRequestId();
            //only in case of a match we add response to request
            //so we only cover our ajax cycle and the original get
            Optional<HttpCycleData> found = cycleData.stream().filter(item -> item.requestId.toJson().equals(requestId.toJson())).findFirst();
            found.ifPresent(httpCycleData -> httpCycleData.responseReceived = entry);
        });
    }

    public Capabilities getCapabilities() {
        return delegate.getCapabilities();
    }

    public void setFileDetector(FileDetector detector) {
        delegate.setFileDetector(detector);
    }

    public <X> void onLogEvent(EventType<X> kind) {
        delegate.onLogEvent(kind);
    }

    public void register(Predicate<URI> whenThisMatches, Supplier<Credentials> useTheseCredentials) {
        delegate.register(whenThisMatches, useTheseCredentials);
    }

    public LocalStorage getLocalStorage() {
        return delegate.getLocalStorage();
    }

    public SessionStorage getSessionStorage() {
        return delegate.getSessionStorage();
    }

    public Location location() {
        return delegate.location();
    }

    public void setLocation(Location location) {
        delegate.setLocation(location);
    }

    public NetworkConnection.ConnectionType getNetworkConnection() {
        return delegate.getNetworkConnection();
    }

    public NetworkConnection.ConnectionType setNetworkConnection(NetworkConnection.ConnectionType type) {
        return delegate.setNetworkConnection(type);
    }

    public void launchApp(String id) {
        delegate.launchApp(id);
    }

    public Map<String, Object> executeCdpCommand(String commandName, Map<String, Object> parameters) {
        return delegate.executeCdpCommand(commandName, parameters);
    }

    public Optional<DevTools> maybeGetDevTools() {
        return delegate.maybeGetDevTools();
    }

    public List<Map<String, String>> getCastSinks() {
        return delegate.getCastSinks();
    }

    public String getCastIssueMessage() {
        return delegate.getCastIssueMessage();
    }

    public void selectCastSink(String deviceName) {
        delegate.selectCastSink(deviceName);
    }

    public void startDesktopMirroring(String deviceName) {
        delegate.startDesktopMirroring(deviceName);
    }

    public void startTabMirroring(String deviceName) {
        delegate.startTabMirroring(deviceName);
    }

    public void stopCasting(String deviceName) {
        delegate.stopCasting(deviceName);
    }

    public void setPermission(String name, String value) {
        delegate.setPermission(name, value);
    }

    public ChromiumNetworkConditions getNetworkConditions() {
        return delegate.getNetworkConditions();
    }

    public void setNetworkConditions(ChromiumNetworkConditions networkConditions) {
        delegate.setNetworkConditions(networkConditions);
    }

    public void deleteNetworkConditions() {
        delegate.deleteNetworkConditions();
    }


    public SessionId getSessionId() {
        return delegate.getSessionId();
    }

    public ErrorHandler getErrorHandler() {
        return delegate.getErrorHandler();
    }

    public void setErrorHandler(ErrorHandler handler) {
        delegate.setErrorHandler(handler);
    }

    public CommandExecutor getCommandExecutor() {
        return delegate.getCommandExecutor();
    }

    public void get(String url) {
        lastGet = url;
        delegate.get(url);
    }

    public String getTitle() {
        return delegate.getTitle();
    }

    public String getCurrentUrl() {
        return delegate.getCurrentUrl();
    }

    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        return delegate.getScreenshotAs(outputType);
    }

    public Pdf print(PrintOptions printOptions) throws WebDriverException {
        return delegate.print(printOptions);
    }

    public WebElement findElement(By locator) {
        return delegate.findElement(locator);
    }

    public List<WebElement> findElements(By locator) {
        return delegate.findElements(locator);
    }

    public List<WebElement> findElements(SearchContext context, BiFunction<String, Object, CommandPayload> findCommand, By locator) {
        return delegate.findElements(context, findCommand, locator);
    }

    public String getPageSource() {
        return delegate.getPageSource();
    }

    public String getPageText() {
        String head = delegate.findElement(By.tagName("head")).getAttribute("innerText").replaceAll("[\\s\\n ]", " ");
        String body = delegate.findElement(By.tagName("body")).getAttribute("innerText").replaceAll("[\\s\\n ]", " ");
        return head + " " + body;
    }

    public String getPageTextReduced() {
        String head = delegate.findElement(By.tagName("head")).getAttribute("innerText");
        String body = delegate.findElement(By.tagName("body")).getAttribute("innerText");
        //handle blanks and nbsps
        return (head + " " + body).replaceAll("[\\s\\u00A0]+", " ");
    }

    /**
     * resets the current tab and gives it a clean slate
     * that way we do not have to build up the entire tab again
     */
    public void reset() {
        DevTools devTools = delegate.getDevTools();
        devTools.clearListeners();
        devTools.send(Network.clearBrowserCookies()); // just to be sure w clear out all cookies
        devTools.disconnectSession(); //This kills off the existing session

        cycleData.clear();
        lastGet = null;
    }

    /**
     * closes the current tab for good, this has been problematic
     * when the last or only tab was closes for recycling
     * apparently we run into dev tools timeouts then
     * hence reset now is the preferred way to recycle tabs instead
     * of using close
     */
    public void close() {
        reset();
        delegate.close();
    }

    /**
     * quits the driver entirely
     */
    public void quit() {
        delegate.quit();
    }

    public Set<String> getWindowHandles() {
        return delegate.getWindowHandles();
    }

    public String getWindowHandle() {
        return delegate.getWindowHandle();
    }

    public Object executeScript(String script, Object... args) {
        return delegate.executeScript(script, args);
    }

    public Object executeAsyncScript(String script, Object... args) {
        return delegate.executeAsyncScript(script, args);
    }

    public TargetLocator switchTo() {
        return delegate.switchTo();
    }

    public Navigation navigate() {
        return delegate.navigate();
    }

    public Options manage() {
        return delegate.manage();
    }

    public void setLogLevel(Level level) {
        delegate.setLogLevel(level);
    }

    public void perform(Collection<Sequence> actions) {
        delegate.perform(actions);
    }

    public void resetInputState() {
        delegate.resetInputState();
    }

    public VirtualAuthenticator addVirtualAuthenticator(VirtualAuthenticatorOptions options) {
        return delegate.addVirtualAuthenticator(options);
    }

    public void removeVirtualAuthenticator(VirtualAuthenticator authenticator) {
        delegate.removeVirtualAuthenticator(authenticator);
    }

    public FileDetector getFileDetector() {
        return delegate.getFileDetector();
    }

    public ScriptKey pin(String script) {
        return delegate.pin(script);
    }

    public void unpin(ScriptKey key) {
        delegate.unpin(key);
    }

    public Set<ScriptKey> getPinnedScripts() {
        return delegate.getPinnedScripts();
    }

    public Object executeScript(ScriptKey key, Object... args) {
        return delegate.executeScript(key, args);
    }

    public void register(Supplier<Credentials> alwaysUseTheseCredentials) {
        delegate.register(alwaysUseTheseCredentials);
    }

    public DevTools getDevTools() {
        return delegate.getDevTools();
    }


    @Override
    public int getResponseStatus() {
        try {
            HttpCycleData data = getLastGetData();
            if (data == null) {
                return -1;
            }
            if (data.responseReceived == null) {
                return -1;
            }
            return data.responseReceived.getResponse().getStatus();
        } catch (java.util.NoSuchElementException ex) {
            return -1;
        }
    }

    @Override
    public String getResponseBody() {
        HttpCycleData data = getLastGetData();
        return delegate.getDevTools().send(Network.getResponseBody(data.requestId)).getBody();
    }

    public String getRequestData() {
        HttpCycleData data = getLastGetData();
        return data.request.getPostData().orElse("");
    }

    public String[] getRequestDataAsArray() {
        String requestData = getRequestData();
        String[] splitData = requestData.split("&");
        return Stream.of(splitData)
                .map((String keyVal) -> URLDecoder.decode(keyVal, StandardCharsets.UTF_8))
                .toArray(String[]::new);
    }

    private HttpCycleData getLastGetData() {
        sortResponses();

        return cycleData.stream().filter(item -> item.request.getUrl().contains(lastGet))
                // missing last api
                .reduce((item1, item2) -> item2).orElse(null);
    }

    private void sortResponses() {
        //we sort by response timestamps, with items with no response being first
        //last response always being the one at the bottom
        cycleData.sort((o1, o2) -> {
            if (o1.responseReceived == null) {
                return -1;
            }
            if (o2.responseReceived == null) {
                return 1;
            }
            return Long.compare(o1.responseReceived.getTimestamp().toJson().longValue(), o2.responseReceived.getTimestamp().toJson().longValue());
        });
    }

    public void printProcessedResponses() {
        sortResponses();

        cycleData.stream().filter(item -> item.responseReceived != null)
                // missing last api
                .forEach(item -> {
                    System.out.println("Url: " + item.request.getUrl());
                    System.out.println("RequestId: " + item.requestId.toJson());
                    Optional<TimeSinceEpoch> responseTime = item.responseReceived.getResponse().getResponseTime();
                    System.out.println("ResponseTime: " + responseTime.orElse(new TimeSinceEpoch(-1)));
                    System.out.println("ResponseStatus: " + item.responseReceived.getResponse().getStatus());
                });
    }

    public ChromeDriver getDelegate() {
        return delegate;
    }

    @Override
    public JavascriptExecutor getJSExecutor() {
        return delegate;
    }

    /*
     * we only want the cdp version warning once, now matter how
     * often the driver is called
     * if not wanted at all the selenium webdriver version must match the browser version
     */
    static AtomicBoolean firstLog = new AtomicBoolean(Boolean.TRUE);


    public static ExtendedWebDriver stdInit() {
        Locale.setDefault(new Locale("en", "US"));
        WebDriverManager.chromedriver().setup();
        initCDPVersionMessageFilter();

        ChromeOptions options = new ChromeOptions();

        // we can turn on a visual browser by
        // adding chromedriver.headless = false to our properties
        // default is headless = true
        if(System.getProperty("chromedriver.headless") == null  ||
        "true".equals(System.getProperty("chromedriver.headless"))) {
            options.addArguments("--headless");
        }
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-insecure-localhost");
        options.addArguments("--ignore-urlfetcher-cert-requests");
        options.addArguments("--auto-open-devtools-for-tabs");
        options.addArguments("--disable-gpu");
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("intl.accept_languages", "en");
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--lang=en");

        options.setLogLevel(ChromeDriverLogLevel.OFF);

        ExtendedWebDriver driver = new ChromeDevtoolsDriver(options);
        driver.manage().timeouts().implicitlyWait(WebPage.STD_TIMEOUT);
        driver.manage().timeouts().pageLoadTimeout(WebPage.STD_TIMEOUT);
        driver.manage().timeouts().scriptTimeout(WebPage.STD_TIMEOUT);

        return driver;
    }

    private static void initCDPVersionMessageFilter() {
        Logger logger = Logger.getLogger("org.openqa.selenium.devtools.CdpVersionFinder");

        logger.setFilter(record -> {
            //report the match warning only once, this suffices
            boolean isMatchWarning = record.getMessage().contains("Unable to find an exact match for CDP version");
            if(isMatchWarning && !firstLog.get()) {
                return false;
            }
            if(isMatchWarning) {
                firstLog.set(false);
            }
            return true;
        });
    }

}
