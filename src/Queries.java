
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;

public class Queries {
	
	public static void main(String [] args) throws IOException
	{
		// Creating the property Object
		Properties prop=new Properties();
		FileInputStream ip= new FileInputStream("/home/amanroy/eclipse-workspace/A4_Q1_part1/src/myproject/config.properties");
		prop.load(ip);
		// Setting up the RDF Connection.
		RDFConnection conn = RDFConnectionFactory.connect(prop.getProperty("fuscekipath"));
		
		// Finding all movies before 2016, adding it to oldmoviesgraph and then deleting these triples from base graph.
		conn.update("prefix base: <http://www.semanticweb.org/amanroy/ontologies/2020/3/Movie#>\n" + 
				"INSERT \n" + 
				"{\n" + 
				"  GRAPH <http://iiitd.ac.in/sweb/2016011/oldmoviesgraph> \n" + 
				"  { ?Movie ?prop ?ReleaseYear }\n" + 
				"}\n" + 
				"WHERE\n" + 
				"{\n" + 
				"  {	?Movie base:hasReleaseYear ?ReleaseYear.\n" + 
				"    FILTER (str(?ReleaseYear) < \"2016\" )\n" + 
				"    ?Movie ?prop ?ReleaseYear\n" + 
				"  }\n" + 
				"};\n" + 
				"\n" + 
				"DELETE\n" + 
				"{ ?Movie ?prop ?ReleaseYear}\n" + 
				"WHERE\n" + 
				"{ \n" + 
				"  { ?Movie base:hasReleaseYear ?ReleaseYear.\n" + 
				"    FILTER (str(?ReleaseYear) < \"2016\" )\n" + 
				"    ?Movie ?prop ?ReleaseYear\n" + 
				"  }\n" + 
				"}");
		
		// Finding all movies in or after 2016, adding it to newmoviesgraph and then deleting these triples from base graph.
		conn.update("prefix base: <http://www.semanticweb.org/amanroy/ontologies/2020/3/Movie#>\n" + 
				"INSERT \n" + 
				"{\n" + 
				"  GRAPH <http://iiitd.ac.in/sweb/2016011/newmoviesgraph> \n" + 
				"  { ?Movie ?prop ?ReleaseYear }\n" + 
				"}\n" + 
				"WHERE\n" + 
				"{\n" + 
				"  {	?Movie base:hasReleaseYear ?ReleaseYear.\n" + 
				"    FILTER (str(?ReleaseYear) >= \"2016\" )\n" + 
				"    ?Movie ?prop ?ReleaseYear\n" + 
				"  }\n" + 
				"};\n" + 
				"\n" + 
				"DELETE\n" + 
				"{ ?Movie ?prop ?ReleaseYear}\n" + 
				"WHERE\n" + 
				"{ \n" + 
				"  { ?Movie base:hasReleaseYear ?ReleaseYear.\n" + 
				"    FILTER (str(?ReleaseYear) >= \"2016\" )\n" + 
				"    ?Movie ?prop ?ReleaseYear\n" + 
				"  }\n" + 
				"}\n" + 
				"");
		
		
		
		// Adding rest of the triples to remaining graph.
		conn.update("prefix base: <http://www.semanticweb.org/amanroy/ontologies/2020/3/Movie#>\n" + 
				"INSERT \n" + 
				"{\n" + 
				"  GRAPH <http://iiitd.ac.in/sweb/2016011/remainingmoviesgraph> \n" + 
				"  { ?Movie ?prop ?ReleaseYear }\n" + 
				"}\n" + 
				"WHERE\n" + 
				"{\n" + 
				"    ?Movie ?prop ?ReleaseYear\n" + 
				"};\n" + 
				"\n" + 
				"DELETE\n" + 
				"{ ?Movie ?prop ?ReleaseYear}\n" + 
				"WHERE\n" + 
				"{ \n" + 
				"  {\n" + 
				"    ?Movie ?prop ?ReleaseYear\n" + 
				"  }\n" + 
				"}");
		
		System.out.println("Done");
		conn.close();
	}

}
