# ratesapi-cucumber-tests

This is a demo project for testing [ratesapi.io](http://ratesapi.io). It is based on Cucumber JVM and Spring.

## Building project

Run the following command to build project and run unit tests (code coverage report is under `target/site/jacoco/index.html`):

`mvn clean install`

Run the following command to build project:

`mvn clean install -Dskip.unit.tests=true`

## Running tests

Run the following command inside project main directory to execute all tests:

`mvn clean verify -Pcucumber-test -Dcucumber.filter.tags="@Ratesapi" -Dthread.count=4`


## Reports

Test reports can be found under `target/cucumber` directory. There are two reports:
* HTML report - open `target/cucumber/cucumber-html-reports/overview-features.html` for the report main page
* timeline report - open `target/cucumber/timeline/index.html` for the report main page
