

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lemurproject.galago.tupleflow.Parameters;

import ciir.umass.edu.sum.TermExtractor;


/**
 * Servlet implementation class adobeDemo
 */

//@WebServlet("/adobeDemo")

public class adobeDemo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TermExtractor te;
    /**
     * Default constructor. 
     * @throws Exception 
     */
    public adobeDemo() throws Exception {
    	
    	        te= new TermExtractor("/usr/dan/users4/ashishjain/apache-tomcat-7.0.53/bin/indexing.json");
    	        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		Parameters param = new Parameters();
		String entity= request.getParameter("entity");
		String query= request.getParameter("query");
		String aspects=  request.getParameter("aspects");
		System.out.println(aspects);
		if (entity!=null) {
		        entity = entity.replaceAll("[#,\\!,\\$,\\^,\\*,&,\\`,[0-9],@,%,(,),\\[,\\],\\?,\\.,\\,\\|,>,<]", "");
		        if (entity.length() > 1) {
		            //System.out.println(entity);
		            if(aspects.equals("tag")) {
		                List<String>L;
		                try {
							L=te.getResults(entity, true);
			                if(L.size()==0)
			                {
			                	
			                }
			                else{

			                	param.set("tags", new Parameters());
			                	Parameters tags = (Parameters) param.get("tags");
			                	for(String str:L) {
			                		
			                		String count="0";
			                		count = te.getPhraseCount(str);

			                		tags.set(str, count);
			                	} 
			                }
			                
						} catch (Exception e) {
						
							e.printStackTrace();
						}
		            	}
		                else {
		                    List<String> L = null;
		                	param.set("aspects", new Parameters());
		                	Parameters phrases = (Parameters) param.get("aspects");
		                	
		                    try {
								L=te.getResults(entity, false);
								
								if(L.size()==0)
			                    {
			                    }
			                    else{
			                    for(String str:L) {
			                        String count="0";
			                        count=te.getPhraseCount(str);
			                        phrases.set(str, count);
			                    }
			                    }
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		                    }
		                }
		        }
		        if(query!=null) {
		        		int counter = 1 ;
		                List<String> Q = null;
		                param.set("documents", new Parameters());
						try {
							Q = te.getDocuments(query, 50, "tweet");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Parameters documents = (Parameters) param.get("documents");
		                for(String doc:Q) {
		                	documents.set(counter+"", doc);
		                	counter++;
		                }
		                
		        }


		        writer.println(param.toString());
		        
	}

}
