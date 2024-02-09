package org.apache.streams.activitypub.webapp.test.cases

import org.apache.streams.activitypub.webapp.test.ActivityPubWebappTestSuite
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory;

class ServletRegistrationTest {

    private final val LOGGER = LoggerFactory.getLogger(classOf[ServletRegistrationTest]);

    @Test
    @DisplayName("All Servlets are registered")
    @throws(classOf[Exception])
    def testAllServletsRegistered() : Unit = {
        val servletRegistations = ActivityPubWebappTestSuite.helper.context.getServletContext.getServletRegistrations()
        Assertions.assertNotNull(servletRegistations)
        Assertions.assertEquals(2, servletRegistations.size())
        Assertions.assertTrue(servletRegistations.containsKey("root"))
        Assertions.assertTrue(servletRegistations.containsKey("webfinger"))
    }
}
