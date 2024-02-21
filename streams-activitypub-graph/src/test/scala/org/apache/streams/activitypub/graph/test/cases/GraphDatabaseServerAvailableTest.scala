package org.apache.streams.activitypub.graph.test.cases

import org.apache.http.HttpStatus
import org.apache.streams.activitypub.graph.test.ActivityPubGraphTestSuiteExtension
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory

import scala.concurrent.duration.MINUTES
import scala.concurrent.duration.SECONDS
import scala.util.Try;

/**
 * ActivityPubServletsTestSuite
 */
@ExtendWith(Array(classOf[ActivityPubGraphTestSuiteExtension]))
class GraphDatabaseServerAvailableTest {

  private final val LOGGER = LoggerFactory.getLogger(classOf[GraphDatabaseServerAvailableTest]);

  @Test
  @DisplayName("Verify Fuseki Online")
  @Order(0)
  def testGraphDatabaseServerOnline(using helper : ActivityPubGraphTestSuiteExtension): Unit = {
    val testAttempt = Try {
      await atMost(1, MINUTES) pollInterval(1, SECONDS) until {
        () => Try {
          val request = helper.restClient.get("/$/ping")
          val response = request.run()
          response.getStatusCode == HttpStatus.SC_OK
        }.isSuccess
      }
    }
    Assertions.assertTrue(testAttempt.isSuccess, "Graph Database Server is available")
  }
}
