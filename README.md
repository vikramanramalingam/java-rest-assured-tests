# API Automation Framework - Rest Assured

## Overview
This project is an API automation framework developed using Rest Assured to test RESTful APIs efficiently.

## Prerequisites
- **Java 21 or higher**
- **Maven installed**
- **IDE:** Visual Studio Code

## Installing Java
1. Download and install Java from: [Oracle Java 21](https://www.oracle.com/in/java/technologies/downloads/#java21)
2. Set the Java home path in your `.zshrc` profile.

## Installing Maven
1. Install Maven using [Homebrew](https://brew.sh/).
2. After installing Homebrew, update your `.zshrc` profile to include the Homebrew path.
3. Install Maven using: [Maven Homebrew Formula](https://formulae.brew.sh/formula/maven#default)
4. Set the Maven home path in your `.zshrc` profile.

## Setting up Visual Studio Code
1. Download and install Visual Studio Code from: [VS Code Download](https://code.visualstudio.com/download)
2. Open VS Code and navigate to **Extensions**.
3. Install the following extensions:
    - [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)
    - [Code Runner](https://marketplace.visualstudio.com/items?itemName=formulahendry.code-runner)
4. Configure the settings:
    - Search for "Java" and set up the Java home path.
    - Search for "Maven" and set up the Maven home path.

---

## Project Structure
```
project-root/
│── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── globepoc/
│   │   │           ├── base/       # Base classes for API requests
│   │   │           ├── api/        # API-specific classes
│   │   │           ├── endpoints/  # contains api endpoints
│   │   │           ├── utils/      # Utility/helper classes
│   ├── resources/
│   ├── ├── allure.properties # properties for allure report
│   ├── ├── log4j2.xml        # config for log4j2
│   ├── test/
│   │   ├── java/
│   │   │       ├── dogApiTests/               # Tests for [Dog API](https://dog.ceo/dog-api/documentation/) endpoints
│   │   │       ├── petStoreApiTests/          # Tests for [Pet Store API](https://petstore.swagger.io/#/) endpoints
│   │   │       ├── exchangeRateApiTests/      # Tests for [Exchange Rates API](https://api.nbp.pl/en.html) endpoints
│   │   │       ├── utils/                     # Tests for [Exchange Rates API](https://api.nbp.pl/en.html) endpoints
│   │   │           ├── DataProviderUtil/      # Utility class for Data provider
│   │   ├── resources/
│   │   │       ├── payloads/                  # For storing json and xml payloads
│   │   │       ├── schemas/                   # For storing json and xml schemas
│   │   │       ├── testData/                  # Test data
│── testSuites/
│   ├── dogApiTests.xml             # Contains TestNG test suite for dog api endpoints
│   ├── perStoreApiTests.xml        # Contains TestNG test suite for petStore api endpoints
│   ├── exchangeRateApiTests.xml    # Contains TestNG test suite for exchange rate api endpoints
│── pom.xml           # Maven dependencies
│── .gitignore        # Git ignore file
│── README.md         # Project documentation
```
---

## Running Tests
### Run Dog API Tests
```sh
mvn clean test -Denv=dogapi -DsuiteXmlFile=testSuites/dogApiTests.xml
```

### Run Pet Store API Tests
```sh
mvn clean test -Denv=petstore -DsuiteXmlFile=testSuites/petStoreApiTests.xml
```

## To open Allure Report (without Allure Testops Integration)
### Run the below command
```sh
allure serve target/allure-results
```

## Allure Report Screenshot

![Overview Screenshot](/screenshots/allure_report_1.png)

![Details Screenshot](/screenshots/allure_report_2.png)