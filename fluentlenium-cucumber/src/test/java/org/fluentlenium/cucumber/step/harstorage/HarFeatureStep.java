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
package org.fluentlenium.cucumber.step.harstorage;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.fluentlenium.core.annotation.Page;
import org.fluentlenium.cucumber.adapter.FluentCucumberAdapter;
import org.fluentlenium.cucumber.page.HarPage;
import org.fluentlenium.cucumber.page.LocalPage;

import static org.fest.assertions.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.withName;

public class HarFeatureStep extends FluentCucumberAdapter {

    @Page
    HarPage harPage;

    private HarFeatureInitialStep initialStep;

    public HarFeatureStep(HarFeatureInitialStep initialStep) {
        this.initialStep = initialStep;
    }

    @When("^I go on harPage$")
    public void go_on_harPage() {
        this.initFluentWithWebDriver(initialStep);
        this.initTest();

        goTo(harPage);
    }

    @When("^I am on harPage$")
    public void i_am_on_harPage() {
        this.initFluentWithWebDriver(initialStep);
        this.initTest();

        harPage.isAt();
    }

    @After
    public void after() {
		this.quit();
    }
}
