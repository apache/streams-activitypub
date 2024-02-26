package org.apache.streams.activitypub.graph.impl

import org.apache.http.client.utils.URIBuilder
import org.apache.jena.query.ParameterizedSparqlString
import org.apache.jena.rdf.model.Model
import org.apache.jena.riot.system.PrefixMapStd
import org.apache.jena.sparql.exec.http.QueryExecutionHTTP
import org.apache.jena.sparql.exec.http.QueryExecutionHTTPBuilder
import org.apache.streams.activitypub.api.WebfingerApi
import org.apache.streams.activitypub.api.pojo.webfinger.WebfingerQueryRequest
import org.apache.streams.activitypub.api.pojo.webfinger.WebfingerQueryResponse
import org.apache.streams.activitypub.graph.config.WebfingerGraphImplConfig
import org.apache.streams.activitypub.utils.AcctPrefixResourceToResourceURISwap
import org.apache.streams.config.ComponentConfigurator

import java.net.URI
import scala.io.Source;

object WebfingerGraphImpl {

  private final val configurator: ComponentConfigurator[WebfingerGraphImplConfig] = new ComponentConfigurator(classOf[WebfingerGraphImplConfig])
  final val config: WebfingerGraphImplConfig = configurator.detectConfiguration()
  final val DEFAULT: WebfingerGraphImpl = new WebfingerGraphImpl(config)

}

class WebfingerGraphImpl(config: WebfingerGraphImplConfig) extends WebfingerApi {

  private def serverUri = new URIBuilder(config.getFusekiEndpointURI).build()

  private def datasetQueryUri = new URIBuilder(serverUri).setPath(s"${config.getDefaultDatasetId}/query").build()

  final val sparqlBuilder: QueryExecutionHTTPBuilder = QueryExecutionHTTP.service(datasetQueryUri.toString).postQuery()

  val prefixMap = new PrefixMapStd()

  /**
   * Query the dataset for the requested resource.
   * @param request
   * @return
   */
  override def webfingerQuery(request: WebfingerQueryRequest): WebfingerQueryResponse = {

    val resourceParamURI: URI = request.getResource match {
      case s"acct:$x" => AcctPrefixResourceToResourceURISwap.doUnswap(x)
      case _ => new URI(request.getResource)
    }

    val askQueryBody: String = Source.fromResource("queries/webfingerAskByResource.sparql").getLines.mkString
    val askQuery: ParameterizedSparqlString = new ParameterizedSparqlString(askQueryBody)
    askQuery.setIri("resourceParam", resourceParamURI.toString)
    val askExecution: QueryExecutionHTTP = sparqlBuilder.query(askQuery.asQuery()).build()
    val askResult = askExecution.execAsk()
    if( !askResult ) throw new Exception("Requested resource not found in dataset.")

    val constructQueryBody: String = Source.fromResource("queries/webfingerGetByResource.sparql").getLines.mkString
    val constructQuery: ParameterizedSparqlString = new ParameterizedSparqlString(constructQueryBody)
    constructQuery.setIri("resourceParam", resourceParamURI.toString)
    val constructExecution: QueryExecutionHTTP = sparqlBuilder.query(constructQuery.asQuery()).build()
    val model: Model = constructExecution.execConstruct()

    //    val result = writer.write(System.out, constructed, prefixMap, RDFFormat.JSONLD_FRAME_PRETTY, context);
    if( checkModelContainsSubject( resourceParamURI, model ) ) {
      generateResponse(request, model, resourceParamURI)
    } else throw new Exception("Error generating response.")
  }

  /**
   * Check if the model returned from the query contains the requested resource
   * and necessary details to generate a valid response.
   * @param request
   * @param model
   * @return
   */
  def checkModelContainsSubject(resourceParamURI: URI, model: Model): Boolean = {
    model.containsResource(model.getResource(resourceParamURI.toString))
  }

  /**
   * Translate the model into a webfinger response object.
   * @param request
   * @param model
   * @return
   */
  def generateResponse(request: WebfingerQueryRequest, model: Model, resourceParamURI: URI): WebfingerQueryResponse = {
    val subject = model.getResource(resourceParamURI.toString)
    val result = new WebfingerQueryResponse()
      .withSubject(subject.getURI)
    result
  }

}
