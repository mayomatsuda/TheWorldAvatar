package uk.ac.cam.cares.jps.agent.cea.utils.uri;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class OntologyURIHelper {
    public static final String ocgml = "ocgml";
    public static final String unitOntology = "unitOntology";
    public static final String ontoUBEMMP = "ontoUBEMMP";
    public static final String rdf = "rdf";
    public static final String owl = "owl";
    public static final String bot = "bot";
    public static final String ontobuiltenv = "ontobuiltenv";
    public static final String ontobuiltstructure = "ontobuiltstructure";
    public static final String ontotimeseries = "ontotimeseries";
    public static final String ontoems = "ontoems";
    public static final String geo = "geo";
    public static final String geof = "geof";
    public static final String epsg = "epsg";
    public static final String bldg = "bldg";
    public static final String grp = "grp";
    public static final String gml = "gml";

    public static final String geoliteral = "geoliteral";
    public static final String geoservice = "geoservice";
    public static Map<String, String> ontologyUriMap = new HashMap<>();

    /**
     * Constructs an OntologyURIHelper object with the given ontology URIs from the properties file
     * @param propertiesFileName name of the properties file
     */
    public OntologyURIHelper(String propertiesFileName) {
        ResourceBundle config = ResourceBundle.getBundle(propertiesFileName);
        ontologyUriMap.put(ocgml, config.getString("uri.ontology.ontocitygml"));
        ontologyUriMap.put(unitOntology, config.getString("uri.ontology.om"));
        ontologyUriMap.put(ontoUBEMMP, config.getString("uri.ontology.ontoubemmp"));
        ontologyUriMap.put(rdf, config.getString("uri.ontology.rdf"));
        ontologyUriMap.put(owl, config.getString("uri.ontology.owl"));
        ontologyUriMap.put(bot, config.getString("uri.ontology.bot"));
        ontologyUriMap.put(ontobuiltenv, config.getString("uri.ontology.ontobuiltenv"));
        ontologyUriMap.put(ontobuiltstructure, config.getString("uri.ontology.ontobuiltstructure"));
        ontologyUriMap.put(ontotimeseries, config.getString("uri.ontology.ontotimeseries"));
        ontologyUriMap.put(ontoems, config.getString("uri.ontology.ontoems"));
        ontologyUriMap.put(geo, config.getString("uri.ontology.geo"));
        ontologyUriMap.put(geof, config.getString("uri.ontology.geofunction"));
        ontologyUriMap.put(epsg, config.getString("uri.opengis.epsg"));
        ontologyUriMap.put(bldg, config.getString("uri.ontology.bldg"));
        ontologyUriMap.put(grp, config.getString("uri.ontology.grp"));
        ontologyUriMap.put(gml, config.getString("uri.ontology.gml"));
        ontologyUriMap.put(geoliteral, config.getString("uri.ontology.geoliteral"));
        ontologyUriMap.put(geoservice, config.getString("uri.service.geo"));
    }

    /**
     * Returns the ontology URI
     * @param ontologyName name of the ontology
     * @return ontology URI
     */
    public String getOntologyUri(String ontologyName) {
        return ontologyUriMap.get(ontologyName);
    }
}