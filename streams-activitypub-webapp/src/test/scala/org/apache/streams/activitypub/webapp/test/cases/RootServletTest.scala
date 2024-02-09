package org.apache.streams.activitypub.webapp.test.cases

import org.apache.streams.activitypub.webapp.test.ActivityPubWebappTestSuite
import org.apache.streams.activitypub.webapp.test.ActivityPubWebappTestSuiteExtension
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory;

class RootServletTest {

    private final val LOGGER = LoggerFactory.getLogger(classOf[RootServletTest]);

    @Test
    @DisplayName("Root Resource Available")
    @throws(classOf[Exception])
    def testRootResourceAvailable() : Unit = {
        val response = ActivityPubWebappTestSuite.helper.restClient.get().json().run();
        assert(response.getStatusCode() == 200);
    }
}
