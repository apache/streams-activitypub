package org.apache.streams.activitypub.webapp.test.cases

import org.apache.streams.activitypub.webapp.test.ActivityPubWebappTestSuiteExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory


@ExtendWith(Array(classOf[ActivityPubWebappTestSuiteExtension]))
class ServletRegistrationTest {

    private final val LOGGER = LoggerFactory.getLogger(classOf[ServletRegistrationTest]);

    /**
     * Tests that all servlets are registered
     */
    @DisplayName("All Servlets are registered")
    @Order(3)
    @Test
    def testAllServletsRegistered(using helper: ActivityPubWebappTestSuiteExtension) : Unit = {
        val servletRegistrations = helper.context.getServletContext.getServletRegistrations()
        Assertions.assertNotNull(servletRegistrations)
        Assertions.assertEquals(2, servletRegistrations.size())
        Assertions.assertTrue(servletRegistrations.containsKey("root"))
        Assertions.assertTrue(servletRegistrations.containsKey("webfinger"))
    }
}
