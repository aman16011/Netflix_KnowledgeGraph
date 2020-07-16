
import java.util.*;
import java.io.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import org.apache.jena.iri.impl.Main;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.FileManager;


public class Create_KG {
	
	//Defining all the required variables
	static String [] classes = {"Country","Genre","Movie","Show","Rating","CastRole","DirectorRole","Cast","Director"};
	static String [] object_properties = {"isCastRoleOf","isDirectorRoleOf","hasCasted","hasDirected","hasGenre","hasRated","hasCastRole",
			"hasDirectorRole","isCastedBy","isDirectedBy","isGenreOf","isRatingOf","isReleasedCountryOf","isReleasedinCountry"};
	static String [] dataproperties = {"hasDate","hasDescription","hasShowDuration","hasTimeDuration","hasMovieId","hasShowId","hasName"};
	static String NS = "http://www.semanticweb.org/amanroy/ontologies/2020/3/Movie#";
	
	
	// Getting the individual of movie or show.
	public static Individual create_individual(OntModel model, String name, String type)
	{
		OntClass cl = model.getOntClass(NS+type);
		return cl.createIndividual(NS+name);
	}
	
	// Creates triples of Movie_id and Movie_name
	public static void handle_movie_id(OntModel model, String id, String name, String type)
	{
		name = name.replace(' ', '_');
		Individual temp = create_individual(model,name,type);
		DatatypeProperty prop = model.getDatatypeProperty(NS+"hasMovieId");
		temp.addProperty(prop,id);
		return;
	}
	
	// Creates triples of Show_id and Movie_name
	public static void handle_show_id(OntModel model, String id, String name, String type)
	{
		name = name.replace(' ', '_');
		Individual temp = create_individual(model,name,type);
		DatatypeProperty prop = model.getDatatypeProperty(NS+"hasShowId");
		temp.addProperty(prop,id);
		return;
	}
	
	// Creates triples of Release_year and Movie_name
	public static void handle_release_year(OntModel model, String year, String name, String type)
	{
		name = name.replace(' ', '_');
		Individual movie_name = create_individual(model,name,type);
		DatatypeProperty prop = model.getDatatypeProperty(NS+"hasReleaseYear");
		movie_name.addProperty(prop, year);
		return;
	}
	
	// Creates triples of DirectorRole and Movie_name and Director_Role and Director
	public static void handle_director(OntModel model, String direc, String name, String type)
	{
		name = name.replace(' ','_');
		Individual movie_name = create_individual(model,name,type);
		Individual director_role = create_individual(model,"director_role_of_"+name,"DirectorRole");
		Individual director = create_individual(model,direc,"Director");
		ObjectProperty has_director_role = model.getObjectProperty(NS+"hasDirectorRole");
		ObjectProperty has_directed = model.getObjectProperty(NS+"hasDirected");
		director.addProperty(has_directed, director_role);
		movie_name.addProperty(has_director_role, director_role);
		return;
	}
	
	// Creates triples of CastRole and Movie_name and Cast_Role and Cast
	public static void handle_cast(OntModel model, String cas, String name, String type)
	{
		name = name.replace(' ','_');
		Individual movie_name = create_individual(model,name,type);
		Individual cast_role = create_individual(model,"cast_role_of_"+name,"CastRole");
		Individual cast = create_individual(model,cas,"Cast");
		ObjectProperty has_cast_role = model.getObjectProperty(NS+"hasCastRole");
		ObjectProperty has_cast = model.getObjectProperty(NS+"hasCasted");
		cast.addProperty(has_cast, cast_role);
		movie_name.addProperty(has_cast_role, cast_role);
		return;
	}
	
	// Creates triples of Country and Movie_name
	public static void handle_country(OntModel model, String cnt, String name, String type)
	{
		name = name.replace(' ','_');
		Individual movie_name = create_individual(model,name,type);
		Individual country_name = create_individual(model,cnt,"Country");
		ObjectProperty prop = model.getObjectProperty(NS+"isReleasedInCountry");
		movie_name.addProperty(prop,country_name);
		return;
	}
	
	// Creates triples of Genre and Movie_name
	public static void handle_genre(OntModel model, String cnt, String name, String type)
	{
		name = name.replace(' ','_');
		Individual movie_name = create_individual(model,name,type);
		Individual genre_name = create_individual(model,cnt,"Genre");
		ObjectProperty prop = model.getObjectProperty(NS+"hasGenre");
		movie_name.addProperty(prop,genre_name);
		return;
	}
	
	// Creates triples of Rating and Movie_name
	public static void handle_rating(OntModel model, String cnt, String name, String type)
	{
		name = name.replace(' ','_');
		Individual movie_name = create_individual(model,name,type);
		Individual rating_name = create_individual(model,cnt,"Rating");
		ObjectProperty prop = model.getObjectProperty(NS+"hasRated");
		movie_name.addProperty(prop,rating_name);
		return;
	}
	
	// Creates triples of Date and Movie_name
	public static void handle_date(OntModel model, String date, String name,String type)
	{
		name = name.replace(' ', '_');
		Individual movie_name = create_individual(model,name,type);
		DatatypeProperty prop = model.getDatatypeProperty(NS+"hasDate");
		movie_name.addProperty(prop,date);
		return;
	}
	
	// Creates triples of Description and Movie_name
	public static void handle_description(OntModel model, String description, String name,String type)
	{
		name = name.replace(' ', '_');
		Individual movie_name = create_individual(model,name,type);
		DatatypeProperty prop = model.getDatatypeProperty(NS+"hasDescription");
		movie_name.addProperty(prop,description);
		return;
	}
	
	// Creates triples of Duration and Movie_name
	public static void handle_duration(OntModel model, String duration, String name,String type)
	{
		name = name.replace(' ', '_');
		Individual movie_name = create_individual(model,name,type);
		DatatypeProperty prop;
		if (type.equals("Movie"))
		{
			prop = model.getDatatypeProperty(NS+"hasTimeDuration");
			duration = duration.trim().split("_")[0];
			System.out.println(duration);
		}
		else
		{
			prop = model.getDatatypeProperty(NS+"hasShowDuration");
		}
		movie_name.addProperty(prop,duration);
		return;
	}
	
	
	// Function that checks the type of triple and adds it to the model.
	public static void add_to_ontology(String param, OntModel model, String[] arguments)
	{
		
		if (param.equals("title"))
		{
			if (arguments[0].equals("Movie"))
			{
				handle_movie_id(model,arguments[1],arguments[2],arguments[0]);
			}
			else
			{
				handle_show_id(model,arguments[1],arguments[2],arguments[0]);
			}
			return;
			
		}
		else if (param.equals("director"))
		{
			handle_director(model,arguments[1],arguments[2],arguments[0]);
			return;
			
		}
		else if (param.equals("cast"))
		{
			handle_cast(model,arguments[1],arguments[2],arguments[0]);
			return;
			
		}
		else if (param.equals("country"))
		{
			handle_country(model,arguments[1],arguments[2],arguments[0]);
			return;
		}
		else if (param.equals("date_added"))
		{
			handle_date(model,arguments[1],arguments[2],arguments[0]);
			return;
		}
		else if (param.equals("release_year"))
		{
			handle_release_year(model,arguments[1],arguments[2],arguments[0]);
			return;
		}
		else if (param.equals("rating"))
		{
			handle_rating(model,arguments[1],arguments[2],arguments[0]);
			return;
		}
		else if (param.equals("duration"))
		{
			handle_duration(model,arguments[1],arguments[2],arguments[0]);
			return;
		}
		else if (param.equals("listed_in"))
		{
			handle_genre(model,arguments[1],arguments[2],arguments[0]);
			return;
		}
		else
		{
			handle_description(model,arguments[1],arguments[2],arguments[0]);
			return;
		}
		
	}
	
	public static void main(String [] args) throws IOException
	{
		// create the Property object
		Properties prop=new Properties();
		FileInputStream ip= new FileInputStream("/home/amanroy/eclipse-workspace/A4_Q1_part1/src/myproject/config.properties");
		prop.load(ip);
		
		// Create the base model.
		String SOURCE = prop.getProperty("ontsource");
		OntModel model = ModelFactory.createOntologyModel( OntModelSpec.OWL_DL_MEM );
		model.read(SOURCE);
		
		System.out.println("Ontology Loaded successfully");
		
		
	    // reading the file and accordingly calling add_to_ontology method
		try
	      {
	    	  CSVReader reader = new CSVReader(new FileReader(prop.getProperty("netflixpath")));
	    	  String[] header;
	    	  header = reader.readNext();
	    	  for (int i=0;i<header.length;i++)
	    	  {
	    		  System.out.println(header[i]);
	    	  }
	    	  System.out.println(Arrays.deepToString(header));
	    	  String [] nextline;
	    	  while((nextline=reader.readNext())!=null)
	    	  {
	    		  if(nextline != null)
	    		  {
	    			  System.out.println(nextline.length);
	    			  // Deciding between Movie and Show
	    			  if (nextline[1].equals("Movie"))
	    			  {
	    				  nextline[1] = "Movie";
	    				  
	    			  }
	    			  else
	    			  {
	    				  nextline[1] = "Show";
	    			  }
	    			  for(int i=0;i<3;i++)
	    			  {
	    				  nextline[i] = nextline[i].trim().replace(' ', '_');
	    			  }
	    			  String[] arguments = {nextline[1],nextline[0],nextline[2]};
	    			  // Creating triples from first three arguments of a row.
	    			  add_to_ontology(header[2],model,arguments);
	    			  for (int i=3;i<nextline.length;i++)
	    			  {
	    				  String[] temp;
	    				  
	    				  // Apart from 7th, 12th and 8th column every other column can be multi-attributed. 
	    				  if (i!=6 && i!=11 && i!=7)
	    				  {
	    					  temp = nextline[i].trim().split(",");
	    				  }
	    				  else
	    				  {
	    					  temp = new String [] {nextline[i].trim()};
	    				  }
	    					
//	    				  Looping on the multiple values
	    				  for(int j=0;j<temp.length;j++)
	    				  {
	    					  if (temp[j].length()!=0)
	    					  {
	    						  temp[j] = temp[j].trim().replace(" ", "_");
//	    						  System.out.println(header[i]+" "+nextline[1]+" "+temp[j]+" "+nextline[2]);
	    						  arguments = new String[]{nextline[1],temp[j],nextline[2]};
	    						  add_to_ontology(header[i],model,arguments);
	    					  }
	    				  }
	    			  }
	    		  }
	    	  }
	      }
		finally
	      {
	    	  System.out.println("Done");
	      }
//		handle_movie_id(model, "26435", "THOR","Movie");
//		handle_movie_id(model, "22564", "THOR","Show");
//		handle_release_year(model,"1992","THOR","Movie");
		Writer w = new FileWriter("ontology.ttl");
		model.write(w,"TURTLE");
		w.close();
		return;
		
	}

}
