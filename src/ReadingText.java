
import java.util.*;
import java.io.*;
import com.opencsv.CSVReader;

import org.apache.jena.iri.impl.Main;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.FileManager;

class Triples
{
	public String sub,pred,obj;
	public boolean isliteral;
	public Triples(String s, String p,String o, boolean literal)
	{
		sub = s;
		pred = p;
		obj = o;
		isliteral = literal;
	}
}

public class ReadingText {
	public static void main(String args[]) throws IOException {
		
		ArrayList <Triples> ar = new ArrayList<Triples>();
		HashSet categories = new HashSet();
		categories.add("director");
		categories.add("cast");
		categories.add("country");
		categories.add("listed_in");
		
		
		HashSet constant = new HashSet();
		
		constant.add("date_added");
		constant.add("release_year");
		constant.add("rating");
		constant.add("duration");
		constant.add("description");
		
		String base = "http://example.com/";
		
		String prefix_id = "http://example.com/show_id#";
		String prefix_type = "http://example.com/type#";
		String prefix_title = "http://example.com/title#";
		String prefix_director = "http://example.com/director#";
		String prefix_cast = "http://example.com/cast#";
		String prefix_country = "http://example.com/country#";
		String prefix_listed_in = "http://example.com/listed_in#";
		
		String prefix_relation = "http://example.com/relation#";
		Model model = ModelFactory.createDefaultModel();
		
		HashMap <String,Property> propertyname = new HashMap<String,Property>();
	    
		try
	      {
	    	  CSVReader reader = new CSVReader(new FileReader("/home/amanroy/Documents/semantic_Web/Assignment1/NetflixList.csv"));
	    	  String[] header;
	    	  header = reader.readNext();
	    	  for (int i=0;i<header.length;i++)
	    	  {
	    		  System.out.println(prefix_relation+header[i]);
	    		  propertyname.put(prefix_relation+header[i], model.getProperty(prefix_relation,header[i]));
	    	  }
	    	  System.out.println(Arrays.deepToString(header));
	    	  String [] nextline;
	    	  while((nextline=reader.readNext())!=null)
	    	  {
	    		  if(nextline != null)
	    		  {
	    			  int count = 1;
	    			  ar.add(new Triples(base+header[0]+"#"+nextline[0],base+"relation#"+header[0],nextline[0],true));
//	    			  System.out.println(Arrays.toString(nextline));
	    			  for (int i=1;i<nextline.length;i++)
	    			  {
//	    				  System.out.println(categories.contains(header[count])+" "+header[count]);
	    				  if (!categories.contains(header[count]))
	    				  {
	  
	    					  if (constant.contains(header[count]))
	    					  {
	    						  System.out.println(header[count]+" 2");
	    						  ar.add(new Triples(base+header[0]+"#"+nextline[0],base+"relation#"+header[i],nextline[i],true));
	    					  }
	    					  else
	    					  {
	    						  System.out.println(header[count]+" 1");
	    						  String obj = base+header[i]+"#"+nextline[i];
	    						  obj = obj.replace(" ","_");
	    						  ar.add(new Triples(base+header[0]+"#"+nextline[0],base+"relation#"+header[i],obj,false));
	    					  }
	    				  }
	    				  else
	    				  {
	    					  String [] temp = nextline[i].trim().split(", ");
	    					  for (int j=0;j<temp.length;j++)
	    					  {
		    					  if (constant.contains(header[count]))
		    					  {
		    						  System.out.println(header[count]+" 2");
		    						  ar.add(new Triples(base+header[0]+"#"+nextline[0],base+"relation#"+header[i],temp[j],true));
		    					  }
		    					  else
		    					  {
		    						  System.out.println(header[count]+" 1");
		    						  String obj = base+header[i]+"#"+temp[j];
		    						  obj = obj.replace(" ","_");
		    						  ar.add(new Triples(base+header[0]+"#"+nextline[0],base+"relation#"+header[i],obj,false));
		    					  }  
	    					  }
	    				  }
	    				  count += 1;
	    			  }
	    		  }
	    		  System.out.println("Next Line");
	    	  }
	      }catch(Exception e)
	      {
	    	  System.out.println(e);
	      }

		model.setNsPrefix("id",prefix_id);
		model.setNsPrefix("type",prefix_type);
		model.setNsPrefix("title",prefix_title);
		model.setNsPrefix("director",prefix_director);
		model.setNsPrefix("cast",prefix_cast);
		model.setNsPrefix("country",prefix_country);
		model.setNsPrefix("listed",prefix_listed_in);
		model.setNsPrefix("rel",prefix_relation);
		
		
		for(int i=0;i<ar.size();i++)
		{
			Triples temp = ar.get(i);
			System.out.println(temp.sub+";"+temp.pred+";"+temp.obj);
			if (temp.isliteral)
			{
				model.createResource(temp.sub).addProperty(propertyname.get(temp.pred), temp.obj,"en");
			}
			else
			{
				System.out.println("Hey");
				model.createResource(temp.sub).addProperty(propertyname.get(temp.pred), model.createResource(temp.obj));
			}
			
		}
//	      
//
////		// some definitions
////		final String personURI   = "http://example.com/John Smith";
////		final String fullname = "John Smith";
////		// create an empty Model
////		Model model = ModelFactory.createDefaultModel();
////
////		// create the resource
////		model.setNsPrefix("rel","http://example.com/owl/csvtupler/map2sparql#");
////		model.setNsPrefix("foaf","http://example.com/" );
////		Property nameProperty = model.getProperty("http://example.com/owl/csvtupler/map2sparql#","hasname");
////		Resource johnSmith = model.createResource(personURI).addProperty(nameProperty, model.createResource(fullname));
////		johnSmith.addProperty(nameProperty, "John","en");
		StmtIterator iter = model.listStatements();
		while (iter.hasNext()) {
		    Statement stmt      = iter.nextStatement();  // get next statement
		    String  subject   = stmt.getSubject().getNameSpace();     // get the subject
		    Property  predicate = stmt.getPredicate();   // get the predicate
		    RDFNode   object    = stmt.getObject();      // get the object
		    
		    System.out.print(subject.toString());
		    System.out.print(" " + predicate.toString() + " ");
		    if (object instanceof Resource) {
		       System.out.print(object.toString());
		    } else {
		        // object is a literal
		    	System.out.print(" Hey");
		        System.out.print(" \"" + object.toString() + "\"");
		    }

		    System.out.println(" .");
		}
		FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
		String fileName = "/home/amanroy/Documents/semantic_Web/Assignment1/model.ttl";
		FileWriter out = new FileWriter(fileName);
		model.write(out,"TURTLE");
		
}
}
