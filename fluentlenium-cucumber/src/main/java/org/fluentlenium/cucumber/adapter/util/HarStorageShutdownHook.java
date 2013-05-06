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
package org.fluentlenium.cucumber.adapter.util;


import org.browsermob.core.har.Har;
import org.browsermob.proxy.ProxyServer;
import org.fluentlenium.cucumber.adapter.harstorage.HarStorage;
import org.openqa.selenium.Capabilities;

import java.io.*;

public class HarStorageShutdownHook extends Thread {
    private final ProxyServer harStorageServer;
    private final Capabilities capabilities;

    public HarStorageShutdownHook(final String s, final ProxyServer harStorageServer, final Capabilities capabilities) {
        super(s);
        this.harStorageServer = harStorageServer;
        this.capabilities = capabilities;
    }

    @Override
    public synchronized void start() {

        try {
            String harServerPort = (String) capabilities.getCapability("harstorage.port");
            String harServerHost = (String) capabilities.getCapability("harstorage.host");

            Har har = harStorageServer.getHar();

            String strFilePath = "target/selenium_report.har";
            File file = new File(strFilePath);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            har.writeTo(fos);
            harStorageServer.stop();

            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file));
            LineNumberReader lineNumberReader = new LineNumberReader(inputStreamReader);
            String ligne;
            String res = "";
            while ((ligne = lineNumberReader.readLine()) != null) {
                res += ligne;
            }

            // Send results to HAR Storage
            HarStorage hs = new HarStorage(harServerHost, harServerPort);

            hs.save(res);
        } catch (Exception e) {
            e.printStackTrace();
            //TODO
        }
    }
}
