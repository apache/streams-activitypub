package org.apache.streams.activitypub.webapp.test.cases

import org.apache.streams.activitypub.webapp.test.ActivityPubWebappTestSuiteExtension
import org.awaitility.Awaitility.await
import org.awaitility.scala.AwaitilitySupport
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory

import scala.concurrent.duration.MINUTES
import scala.util.Try;

@ExtendWith(Array(classOf[ActivityPubWebappTestSuiteExtension]))
class WebappServerAvailableTest extends AwaitilitySupport {

    private final val LOGGER = LoggerFactory.getLogger(classOf[WebappServerAvailableTest]);

    /**
     * Test that the server is available
     */
    @DisplayName("Test server is available")
    @Order(0)
    @Test
    def testWebappServerAvailable(using helper: ActivityPubWebappTestSuiteExtension) : Unit = {
        val testAttempt = Try {
            await atMost(5, MINUTES) untilAsserted {
                helper.server.getStateName.matches("STARTED")
            }
        }
        Assertions.assertTrue(testAttempt.isSuccess)
    }
}
