package org.apache.streams.activitypub.webapp.test

import org.apache.catalina.Context
import org.apache.catalina.Loader
import org.apache.catalina.Server
import org.apache.catalina.loader.WebappClassLoaderBase
import org.apache.catalina.loader.WebappLoader
import org.apache.catalina.startup.Tomcat
import org.apache.http.client.utils.URIBuilder
import org.apache.http.impl.NoConnectionReuseStrategy
import org.apache.juneau.rest.client.RestClient
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.io.File
import java.net.URI
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files

class ActivityPubWebappTestSuiteExtension extends ParameterResolver with BeforeAllCallback with AfterAllCallback {

  private final val LOGGER: Logger = LoggerFactory.getLogger(classOf[ActivityPubWebappTestSuiteExtension])

  private val userDir: String = System.getProperty("user.dir")
  private val basePath: Path = Paths.get(userDir)
  private val basePathDir: String = basePath.toAbsolutePath.toString
  private val webappDir: String = basePath.getFileName.toString
  private val webappPath: Path = webappDir match {
    case "streams-activitypub" => Paths.get(basePathDir, "streams-activitypub-webapp/target/streams-activitypub-webapp")
    case "streams-activitypub-webapp" => Paths.get(basePathDir, "target/streams-activitypub-webapp")
  }
  private val webapp: String = webappPath.toAbsolutePath.toString
  
  private val tomcat: Tomcat = new Tomcat()
  tomcat.enableNaming()
  tomcat.setBaseDir(basePathDir)
  tomcat.setPort(18080)
  tomcat.setAddDefaultWebXmlToWebapp(false)
  tomcat.setSilent(false)
  tomcat.getHost.getAppBaseFile.mkdir

  private val appContext: Context = tomcat.addWebapp(tomcat.getHost, "", webapp)

  tomcat.start()

  def server: Server = tomcat.getServer
  def context: Context = appContext
  def host: String = tomcat.getHost.getName
  def port: Int = tomcat.getConnector.getPort

  val uriBuilder: URIBuilder = new URIBuilder()
    .setPath("/")
    .setScheme("http")
    .setHost(host)
    .setPort(port)

  var rootUri: URI = uriBuilder.build()

  def restClientBuilder: RestClient.Builder = RestClient.create()
    .connectionReuseStrategy(new NoConnectionReuseStrategy())
    .debug()
    .disableAutomaticRetries()
    .disableCookieManagement()
    .disableRedirectHandling()
    .maxConnTotal(10)
    .rootUrl(rootUri.toURL)

  def restClient: RestClient = restClientBuilder
    .pooled()
    .build();

  override def beforeAll(context : ExtensionContext) : Unit = {
    LOGGER.info("beforeAll()");
  }

  override def afterAll(extensionContext: ExtensionContext): Unit = {
    LOGGER.info("afterAll()")
    try {
      tomcat.stop();
    } catch {
      case e: Exception => LOGGER.error("Exception while stopping tomcat", e);
    } finally {
      try {
        tomcat.destroy();
      } catch {
        case e: Exception => LOGGER.error("Exception while destoying tomcat", e);
      }
    }
  }

  override def supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean = {
    false
  }

  override def resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): AnyRef = {
    null
  }
}
