# Development

## Setup

To be able to run tests, create a file `castle_sdk.properties` in the folder `src/test/resources`. The content should look something like this

	app_id=test_app_id
	api_secret=test_api_secret
	white_list=TestWhite,User-Agent,Accept-Language,Accept-Encoding,Accept-Charset,Accept,Accept-Datetime,X-Forwarded-For,Forwarded,X-Forwarded,X-Real-IP,REMOTE_ADDR
	black_list=TestBlack,Cookie
	timeout=100
	backend_provider=OKHTTP
	failover_strategy=CHALLENGE
	base_url=https://testing.api.dev.castle/v1/

## Installing dependencies

To install maven dependencies run:
	
	mvn install

## Running tests

To check test coverage run:

    mvn clean test
    
## Bumping dependencies

The versions maven plugin (http://www.mojohaus.org/versions-maven-plugin) is used to update dependencies. To bump dependencies run:

	mvn versions:use-latest-releases

## Documentation

To generate the javadoc documentation run:

    mvn clean compile javadoc:javadoc

The javadoc will be located inside the `target/site` directory.

To check test coverage run:

    mvn clean test jacoco:report

The coverage report will be on the page `target/jacoco-ut/index.html`