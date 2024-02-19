package org.apache.streams.activitypub.webapp.test.cases

import org.apache.streams.activitypub.webapp.test.ActivityPubWebappTestSuite
import org.apache.streams.activitypub.webapp.test.ActivityPubWebappTestSuiteExtension
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory

@ExtendWith(Array(classOf[ActivityPubWebappTestSuiteExtension]))
class RootServletTest {

    private final val LOGGER = LoggerFactory.getLogger(classOf[RootServletTest]);

    /**
     * Test that the root resource is available
     */
    @DisplayName("Root Resource Available")
    @Order(2)
    @Test
    def testRootResourceAvailable(using helper : ActivityPubWebappTestSuiteExtension) : Unit = {
        val response = helper.restClient.get().json().run();
        assert(response.getStatusCode == 200);
    }
}
