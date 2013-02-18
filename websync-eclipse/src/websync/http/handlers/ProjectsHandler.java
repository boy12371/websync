package websync.http.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ProjectsHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange t) throws IOException {
		// Empty response for a while 
		String response = "";

		// TODO: think about secure requests ??? SSL etc ...  
		String addr = t.getRemoteAddress().getHostName();

		// Check that request if JSONP
		if (t.getRequestMethod().equals("GET")) {
			String uq  = t.getRequestURI().getQuery();
			String[] splited = uq.split("&");
			String key = "";
			for (int i=0; i< splited.length; ++i) {
				String[] attrs =splited[i].split("=");
				if ((attrs.length == 2)
						&& (attrs[0].equalsIgnoreCase("key"))) {
					key = attrs[1];
				}
			}
			
			if (!key.isEmpty() && (key.charAt(0) == '/')) {
			  key = key.substring(1);
			}
			
			for (int i=0; i< splited.length; ++i) {
				String[] attrs =splited[i].split("=");
				if ((attrs.length == 2)
						&& (attrs[0].equalsIgnoreCase("callback"))) {
					response = attrs[1] + "(" + getProjects(key) + ");"; 
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

	public String getProjects(String path) {
		if (path.isEmpty()) {
			String result = "[";
			String comma = "";
			IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
			for (int i=0; i<projects.length; ++i) {
				result += comma + "{'isLazy': true, 'isFolder': true, 'title': '" +  projects[i].getName() + "'}";
				comma = ",";
			}
			result += "]";
			return result;
		}

		String result = "[";
		String comma = "";
		final String projectName = path.contains("/") ? path.split("/")[0] : path;
		String subpath = (path == projectName) ? "" : path.substring(projectName.length() + 1);
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		IResource res = project; 
		if (!subpath.isEmpty()) {
		  IResource folder = project.getFolder(subpath);
		  if (folder.exists())
			res = folder;
		
		IFile fff = project.getFile(subpath);
		if (fff.exists())
			res = fff;
		}
		final String searchPath = path;
		final List<IResource> resources = new ArrayList<IResource>();
		final List<String> ss = new ArrayList<String>();
		try {
			res.accept(new IResourceVisitor() {
				public boolean visit(IResource resource) throws CoreException {

					//exclude hidden resources
					if ((resource.getType() == IResource.HIDDEN)
					  || (resource.getName().indexOf('.') == 0))
						return false;
					
					String resourceName = resource.getFullPath().toString().substring(1);
					if (resourceName.equals(searchPath)) {
						return true;
					}


					if (resource instanceof IFolder) {
						resources.add(resource);
					}

					if (resource instanceof IFile) {
						resources.add(resource);
					}
					
					String rr = resource.getClass().getName();

					return false;				
				}
			});
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		for (IResource resource : resources) {
			String isFolder = "false";
			String isLazy = "false";
			String extraInfo = "";
			if (resource instanceof IFolder) {
				isFolder = "true";
				isLazy = "true";
			}

			if ((resource instanceof IFile)
					&& resource.getFileExtension() != null) {
				 
			   if (resource.getFileExtension().equals("umlsync")) {
  				  extraInfo = ", 'addClass' : 'diagramclass'";
			   } else {
				  isLazy = "true"; // File could provide more information about classes
			   }
			}
			result += comma + "{'isLazy': " + isLazy + ", 'isFolder' : " + isFolder + ", 'title': '" +  resource.getName() + "'"+extraInfo+"}";
			comma = ",";

		}
		if (!ss.isEmpty()) {
			result += ss.get(0);
		}
		result += "]";
		return result;
	}
}
