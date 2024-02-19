package org.apache.streams.activitypub.webapp.test.cases

import org.awaitility.Awaitility.await
import org.awaitility.Awaitility.waitAtMost
import org.awaitility.core.ConditionTimeoutException
import org.apache.streams.activitypub.webapp.test.ActivityPubWebappTestSuite
import org.awaitility.scala.AwaitilitySupport
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

import scala.concurrent.duration.MINUTES
import scala.concurrent.duration.SECONDS
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
            await atMost(1, MINUTES) pollInterval(1, SECONDS) untilAsserted {
                helper.server.getStateName.matches("STARTED")
            }
        }
        Assertions.assertTrue(testAttempt.isSuccess)
    }
}
