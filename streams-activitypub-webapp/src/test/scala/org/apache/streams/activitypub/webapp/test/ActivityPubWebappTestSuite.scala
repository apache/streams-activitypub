package org.apache.streams.activitypub.webapp.test

import org.apache.streams.activitypub.graph.test.ActivityPubGraphTestSuiteExtension
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.platform.suite.api.SelectClasses
import org.junit.platform.suite.api.Suite
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * ActivityPubServletsTestSuite
 */

object ActivityPubWebappTestSuite {

  @RegisterExtension
  given webapp: ActivityPubWebappTestSuiteExtension = new ActivityPubWebappTestSuiteExtension()

  @RegisterExtension
  given graph: ActivityPubGraphTestSuiteExtension = new ActivityPubGraphTestSuiteExtension()

}

@Suite
@SuiteDisplayName("ActivityPub Webapp Integration Tests")
@SelectClasses(Array(
  classOf[org.apache.streams.activitypub.graph.test.cases.GraphDatabaseServerAvailableTest],
  classOf[org.apache.streams.activitypub.webapp.test.cases.WebappServerAvailableTest],
  classOf[org.apache.streams.activitypub.webapp.test.cases.ServletRegistrationTest],
  classOf[org.apache.streams.activitypub.webapp.test.cases.RootServletTest],
  classOf[org.apache.streams.activitypub.webapp.test.cases.NodeinfoServletTest],
  classOf[org.apache.streams.activitypub.webapp.test.cases.WebfingerServletTest]
))
class ActivityPubWebappTestSuite {

}
