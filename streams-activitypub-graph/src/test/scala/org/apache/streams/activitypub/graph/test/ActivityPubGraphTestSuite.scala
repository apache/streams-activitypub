package org.apache.streams.activitypub.graph.test

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.platform.suite.api.SelectClasses
import org.junit.platform.suite.api.Suite
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * ActivityPubServletsTestSuite
 */

object ActivityPubGraphTestSuite {

  @RegisterExtension
  given fuseki: ActivityPubGraphTestSuiteExtension = new ActivityPubGraphTestSuiteExtension()

}

@Suite
@SuiteDisplayName("ActivityPub Graph Integration Tests")
@Order(Order.DEFAULT)
@SelectClasses(Array(
  classOf[org.apache.streams.activitypub.graph.test.cases.GraphDatabaseServerAvailableTest],
  classOf[org.apache.streams.activitypub.graph.test.cases.WebfingerGraphImplTest]
))
class ActivityPubGraphTestSuite {

}
