package org.apache.streams.activitypub.webapp.test

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.platform.suite.api.SelectClasses
import org.junit.platform.suite.api.Suite
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * ActivityPubServletsTestSuite
 */

object ActivityPubWebappTestSuite {

  @RegisterExtension
  val helper: ActivityPubWebappTestSuiteExtension = new ActivityPubWebappTestSuiteExtension()

}

@Suite
@SuiteDisplayName("ActivityPub Webapp Integration Tests")
@Order(Order.DEFAULT)
@SelectClasses(Array(
  classOf[org.apache.streams.activitypub.webapp.test.cases.ServerAvailableTest],
  classOf[org.apache.streams.activitypub.webapp.test.cases.ServletRegistrationTest],
  classOf[org.apache.streams.activitypub.webapp.test.cases.RootServletTest],
  classOf[org.apache.streams.activitypub.webapp.test.cases.WebfingerServletTest]
))
class ActivityPubWebappTestSuite {

}
