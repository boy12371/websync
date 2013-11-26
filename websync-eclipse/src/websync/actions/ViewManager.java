package websync.actions;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
public class ViewManager implements HttpHandler {

	@Override
	public void handle(HttpExchange t) throws IOException {
		// Empty response for a while 
		String response = "";

		// TODO: think about secure requests ??? SSL etc ...  
		String addr = t.getRemoteAddress().getHostName();
		com.sun.net.httpserver.Headers headers = t.getRequestHeaders();

		// Check that request if JSONP
		if (t.getRequestMethod().equals("GET")) {
			String uq  = t.getRequestURI().getQuery();
			String[] splited = uq.split("&");

			for (int i=0; i< splited.length; ++i) {
				String[] attrs =splited[i].split("=");
				if ((attrs.length == 2)
						&& (attrs[0].equalsIgnoreCase("callback"))) {
					response = attrs[1] + "(" + getViews() + ");"; 
				}
			}

		} else {
			// Handle the simple JSON        	
			// responseHeaders.set("Content-Type", "application/json");
		}

		com.sun.net.httpserver.Headers responseHeaders = t.getResponseHeaders();
		responseHeaders.set("Content-Type", "text/javascript");
		t.sendResponseHeaders(200, response.length());

		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}

	private String getViews() {
		return "[{'id':'cp', 'title': 'C/C++ Explorer', 'description':'View for C/C++ source code navigation and diagrams managment.', 'isdefault':true},"
				+"{'id':'java', 'title': 'Java Explorer', 'description':'View for JAVA source code navigation and diagrams managment.', 'isdefault':false},"
				+"{'id':'un', 'title': 'Projects Explorer', 'description':'View for source code navigation and diagrams managment.', 'isdefault':false}]";
	}

}
