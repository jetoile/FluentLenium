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
package org.fluentlenium.cucumber;

import cucumber.api.junit.Cucumber;
import org.fluentlenium.cucumber.adapter.FluentCucumberAdapter;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

/**
 * @author : Mathilde Lemee
 */
@RunWith(Cucumber.class)
@Cucumber.Options(features = "classpath:org/fluentlenium/cucumber/multibrowser", format = {"pretty", "html:target/cucumber-multi", "json:target/cucumber_multi.json"})
public class MultiRunner {

    @BeforeClass
    public static void setup() {
        FluentCucumberAdapter.sharedDriver = null;
    }
}
