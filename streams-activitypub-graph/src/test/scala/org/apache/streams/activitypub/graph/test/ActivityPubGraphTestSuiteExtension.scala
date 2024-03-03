package org.apache.streams.activitypub.graph.test

import org.apache.http.client.utils.URIBuilder
import org.apache.http.impl.NoConnectionReuseStrategy
import org.apache.jena.fuseki.main.FusekiServer
import org.apache.jena.query.Dataset
import org.apache.jena.query.DatasetFactory
import org.apache.jena.riot.RDFDataMgr
import org.apache.juneau.rest.client.RestClient
import org.apache.streams.activitypub.graph.impl.BaseGraphImpl
import org.apache.streams.activitypub.graph.test.config.ActivityPubGraphTestSuiteExtensionConfig
import org.apache.streams.config.ComponentConfigurator
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.net.URL

object ActivityPubGraphTestSuiteExtension {
  private final val configurator: ComponentConfigurator[ActivityPubGraphTestSuiteExtensionConfig] = new ComponentConfigurator(classOf[ActivityPubGraphTestSuiteExtensionConfig])
  final val config: ActivityPubGraphTestSuiteExtensionConfig = configurator.detectConfiguration()
}

class ActivityPubGraphTestSuiteExtension extends ParameterResolver with BeforeAllCallback with AfterAllCallback {

  import ActivityPubGraphTestSuiteExtension.config as testConfig
  import BaseGraphImpl.config as graphConfig

  private final val LOGGER: Logger = LoggerFactory.getLogger(classOf[ActivityPubGraphTestSuiteExtension])

  private val dataset: Dataset = DatasetFactory.create()

  private final val server: FusekiServer = {
    val builder: FusekiServer.Builder = FusekiServer.create()
      .add(graphConfig.getDefaultDataset, dataset)
      .enablePing(true)
      .loopback(true)
      .port(testConfig.getFusekiEndpointURI.getPort)
      .verbose(true)
    graphConfig.getDatasetResources.forEach { resource =>
      RDFDataMgr.read(builder.getDataset(graphConfig.getDefaultDataset).getDefaultGraph, resource.toString)
    }
    testConfig.getTestDatasetResources.forEach { resource =>
      RDFDataMgr.read(builder.getDataset(graphConfig.getDefaultDataset).getDefaultGraph, resource.toString)
    }
    builder.build()
  }.start()

  val serverUrlBuilder = new URIBuilder(server.serverURL())
  val datasetUrlBuilder = new URIBuilder(server.datasetURL(graphConfig.getDefaultDataset))

  val serverUrl: URL = serverUrlBuilder.build().toURL
  val datasetUrl: URL = datasetUrlBuilder.build().toURL

  def restClientBuilder: RestClient.Builder = RestClient.create()
    .connectionReuseStrategy(new NoConnectionReuseStrategy())
    .debug()
    .disableAutomaticRetries()
    .disableCookieManagement()
    .disableRedirectHandling()
    .maxConnTotal(10)
    .rootUrl(serverUrl)

  def restClient: RestClient = restClientBuilder
    .pooled()
    .build();

  override def beforeAll(context : ExtensionContext) : Unit = {
    LOGGER.info("beforeAll()");
  }

  override def afterAll(extensionContext: ExtensionContext): Unit = {
    LOGGER.info("afterAll()")
  }

  override def supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean = {
    parameterContext.getParameter.getType.equals(classOf[ActivityPubGraphTestSuiteExtension])
  }

  override def resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): AnyRef = {
    this
  }
}
