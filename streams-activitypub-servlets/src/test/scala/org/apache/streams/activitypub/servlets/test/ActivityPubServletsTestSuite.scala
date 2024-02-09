package org.apache.streams.activitypub.servlets.test

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.platform.suite.api.SelectClasses
import org.junit.platform.suite.api.Suite
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * ActivityPubServletsTestSuite
 */
@Suite
@SuiteDisplayName("ActivityPub Servlets Unit Tests")
@Order(Order.DEFAULT)
@SelectClasses(Array(
  classOf[org.apache.streams.activitypub.servlets.test.cases.RootServletTest]
))
class ActivityPubServletsTestSuite {

}
