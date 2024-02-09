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

class ServerAvailableTest extends AwaitilitySupport {

    private final val LOGGER = LoggerFactory.getLogger(classOf[ServerAvailableTest]);

    @Test
    @DisplayName("Test server is available")
    @throws(classOf[Exception])
    def testServerAvailable() : Unit = {
        val testAttempt = Try {
            await atMost(5, MINUTES) untilAsserted {
                ActivityPubWebappTestSuite.helper.server.getStateName.matches("STARTED")
            }
        }
        Assertions.assertTrue(testAttempt.isSuccess)
    }
}
