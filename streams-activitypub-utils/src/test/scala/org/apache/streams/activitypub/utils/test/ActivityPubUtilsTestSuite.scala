package org.apache.streams.activitypub.utils.test

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.platform.suite.api.SelectClasses
import org.junit.platform.suite.api.Suite
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * ActivityPubServletsTestSuite
 */

object ActivityPubUtilsTestSuite {

}

@Suite
@SuiteDisplayName("ActivityPub Utils Unit Tests")
@Order(Order.DEFAULT)
@SelectClasses(Array(
  classOf[org.apache.streams.activitypub.utils.test.cases.AcctPrefixResourceURISwapTest],
  classOf[org.apache.streams.activitypub.utils.test.cases.JsonLdHelperTest]
))
class ActivityPubUtilsTestSuite {

}
