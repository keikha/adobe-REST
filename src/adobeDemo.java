

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lemurproject.galago.tupleflow.Parameters;

import ciir.umass.edu.sum.TermExtractor;


/**
 * Servlet implementation class adobeDemo
 */

@WebServlet("/adobeDemo")

public class adobeDemo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TermExtractor te;
	/**
	 * Default constructor. 
	 * @throws Exception 
	 */
	public adobeDemo() throws Exception {
	}
	public void init() throws ServletException {
		try {
			te = new TermExtractor(getServletContext().getRealPath("/parameters/aspect.json"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
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
		List<String> L = new ArrayList<String>();
		try {
			if (entity != null) {
				entity = entity.replaceAll("[#,\\!,\\$,\\^,\\*,&,\\`,[0-9],@,%,(,),\\[,\\],\\?,\\.,\\,\\|,>,<]", "");
				if (entity.length() > 1) {
					if (aspects.equals("tag")) {
						param.set("tags", new Parameters());
						Parameters tags = (Parameters) param.get("tags");
						L = te.getResults(entity, true);
						if (L.size() > 0) {
							for(String str:L) {
								String count = te.getPhraseCount(str);
								tags.set(str, count);
							} 
						}
					} else {
						param.set("aspects", new Parameters());
						Parameters phrases = (Parameters) param.get("aspects");
						L = te.getResults(entity, false);
						if (L.size() > 0) {
							for (String str:L) {
								String count = te.getPhraseCount(str);
								phrases.set(str, count);
							}
						}
					}
				}
			}

			if (query != null) {
				int counter = 1 ;
				List<String> Q = null;
				param.set("documents", new Parameters());
				Q = te.getDocuments(query, 50, "tweet");
				Parameters documents = (Parameters) param.get("documents");
				for(String doc:Q) {
					documents.set(counter+"", doc);
					counter++;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		writer.println(param.toString());
		writer.close();
	}

}
