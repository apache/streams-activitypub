package org.apache.streams.activitypub.graph.impl

import org.apache.http.client.utils.URIBuilder
import org.apache.jena.query.ResultSet
import org.apache.jena.rdf.model.Model
import org.apache.jena.riot.system.PrefixMap
import org.apache.jena.riot.system.PrefixMapStd
import org.apache.jena.shared.PrefixMapping
import org.apache.jena.sparql.exec.http.QueryExecutionHTTP
import org.apache.jena.sparql.exec.http.QueryExecutionHTTPBuilder
import org.apache.juneau.json.JsonParser
import org.apache.juneau.json.JsonSerializer
import org.apache.streams.activitypub.graph.config.BaseGraphImplConfig
import org.apache.streams.config.ComponentConfigurator

import java.net.URI
import scala.io.Source;

/**
 * Base class for jena/fuseki api implementations with common
 * configuration, member variables, and member methods.
 */
object BaseGraphImpl {
  private final val configurator: ComponentConfigurator[BaseGraphImplConfig] = new ComponentConfigurator(classOf[BaseGraphImplConfig])
  final val config: BaseGraphImplConfig = configurator.detectConfiguration()
  final val jsonParser: JsonParser = JsonParser.DEFAULT.copy()
    .debug()
    .ignoreUnknownBeanProperties()
    .ignoreUnknownEnumValues()
    .trimStrings()
    .build()
  final val jsonSerializer: JsonSerializer = JsonSerializer.DEFAULT_READABLE.copy()
    .debug()
    .ignoreUnknownBeanProperties()
    .ignoreUnknownEnumValues()
    .trimStrings()
    .trimEmptyMaps()
    .trimEmptyCollections()
    .build()
}

abstract class BaseGraphImpl(config: BaseGraphImplConfig = BaseGraphImpl.config) {

  private def serverUri = new URIBuilder(config.getFusekiEndpointURI).build()

  private def datasetQueryUri = new URIBuilder(serverUri).setPath(s"${config.getDefaultDataset}/query").build()

  final val sparqlBuilder: QueryExecutionHTTPBuilder = QueryExecutionHTTP.service(datasetQueryUri.toString).postQuery()

  /**
   * Check if the model returned from the query contains the requested resource
   * and necessary details to generate a valid response.
   *
   * @param request
   * @param model
   * @return
   */
  def checkModelContainsSubject(resourceParamURI: URI, model: Model): Boolean = {
    model.containsResource(model.getResource(resourceParamURI.toString))
  }
}
