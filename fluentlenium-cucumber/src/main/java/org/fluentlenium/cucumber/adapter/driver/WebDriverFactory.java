/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
package org.fluentlenium.cucumber.adapter.driver;

import org.browsermob.proxy.ProxyServer;
import org.fluentlenium.cucumber.adapter.FluentCucumberAdapter;
import org.fluentlenium.cucumber.adapter.exception.UnsupportedDriverException;
import org.fluentlenium.cucumber.adapter.util.HarStorageShutdownHook;
import org.fluentlenium.cucumber.adapter.util.SharedDriverHelper;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class WebDriverFactory {
    private static Map<SupportedWebDriver, WebDriver> webDriverInstances = new HashMap<SupportedWebDriver, WebDriver>();
    
    private static ProxyServer harStorageServer;

    public static synchronized WebDriver newWebdriverInstance(FluentCucumberAdapter adapter, SupportedWebDriver driverType, DesiredCapabilities capabilities) throws UnsupportedDriverException {
        String proxyApiPort = (String) capabilities.getCapability("harstorage.api.proxy.port");
        String recordingName = (String) capabilities.getCapability("harstorage.recording.name");

        if (adapter.isHarStorageDecorated()) {
            if (harStorageServer == null) {
                harStorageServer = new ProxyServer(Integer.valueOf(proxyApiPort));
				try {
				harStorageServer.start();

					Proxy proxy = harStorageServer.seleniumProxy();
					capabilities.setCapability(CapabilityType.PROXY, proxy);
				} catch (Exception e) {
					e.printStackTrace();
					//TODO
				}
			}

            //force driver name to firefox
            capabilities.setBrowserName(SupportedWebDriver.FIREFOX.getName());

            if (driverType != SupportedWebDriver.FIREFOX && driverType != SupportedWebDriver.REMOTE) {
                //force to local firefoxDriver
                driverType = SupportedWebDriver.FIREFOX;
            }

            harStorageServer.newHar(recordingName);
            Runtime.getRuntime().addShutdownHook(new HarStorageShutdownHook("fluentLenium-harStorage", harStorageServer, capabilities));
        }

        if (SharedDriverHelper.isSharedDriverPerFeature(adapter.getClass())) {
            if (webDriverInstances.get(driverType) == null) {
                webDriverInstances.put(driverType, getWebDriver(driverType, capabilities));
            }
            return webDriverInstances.get(driverType);
        } else {
            return getWebDriver(driverType, capabilities);
        }
    }

    private static WebDriver getWebDriver(SupportedWebDriver driverType, DesiredCapabilities capabilities) throws UnsupportedDriverException {
        try {
            switch (driverType) {
                case FIREFOX:
                    return firefoxDriver(capabilities);
                case CHROME:
                    return chromeDriver(capabilities);
                case HTMLUNIT:
                    return htmlUnitDriver();
                case PHANTOMJS:
                    return phantomjsDriver(capabilities);
                case REMOTE:
                    return remoteDriver(capabilities);
            }
        } catch (Exception cause) {
            throw new UnsupportedDriverException("Could not instantiate " + driverType, cause);
        }
        return null;
    }

    private static WebDriver remoteDriver(DesiredCapabilities capabilities) throws UnsupportedDriverException {
        capabilities.setBrowserName((String) capabilities.getCapability("browser.name"));

        String osName =  (String) capabilities.getCapability("os.name");

        capabilities.setPlatform(Platform.extractFromSysProperty(osName, ""));
        capabilities.setVersion((String) capabilities.getCapability("os.version"));

        String url =  (String) capabilities.getCapability("webdriver.remote.url");
        try {
            return new RemoteWebDriver(new URL(url), capabilities);
        } catch (MalformedURLException e) {
            throw new UnsupportedDriverException("wrong parameters for remote webDriver: " + capabilities, e);
        }
    }

    private static WebDriver phantomjsDriver(DesiredCapabilities capabilities) {
        return new PhantomJSDriver(capabilities);
    }

    private static WebDriver htmlUnitDriver() {
        return new HtmlUnitDriver();
    }

    private static WebDriver chromeDriver(DesiredCapabilities capabilities) {
        System.setProperty("webdriver.chrome.driver", (String) capabilities.getCapability("webdriver.chrome.driver"));
        return new ChromeDriver();
    }

    private static WebDriver firefoxDriver(DesiredCapabilities capabilities) {
        return new FirefoxDriver(capabilities);
    }


}
