package websync.actions;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import websync.http.handlers.JSONPHandler;
import websync.http.interfaces.IHttpView;
import websync.http.interfaces.IHttpViewManager;
import websync.http.interfaces.uri.THttpCapability;
import websync.http.interfaces.uri.THttpGetKey;

import com.sun.net.httpserver.HttpServer;

/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be 
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class StartWebSession implements IWorkbenchWindowActionDelegate, IHttpViewManager {


	private List<IHttpView> views = new ArrayList<IHttpView>(); 
	private IWorkbenchWindow window;
	private THttpProjectExplorerView DefaultView;

	@SuppressWarnings("restriction")
	HttpServer server;
	private String host;
	private int port;
	private String secret;

	/**
	 * The constructor.
	 */
	public StartWebSession() {
	}

	@Override
	public void registerView(IHttpView view) {
		views.add(view);
	}

	@Override
	public String getDiagramFileExtension() {
		return ".umlsync";
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	@SuppressWarnings("restriction")
	public void run(IAction action) {
		WebsyncDialog d = new WebsyncDialog(window.getShell());
		d.create();
		
		int result = d.open();
		
		Boolean isChanged = (host != d.getHost() || port != d.getPort() || secret != d.getSecret());
		host = d.getHost();
		port = d.getPort();
		secret = d.getSecret();

		if (d.isClosePressed()) {
			if (server != null) {
			  server.stop(0);
			}
			return;	
		}

		if (result == org.eclipse.jface.window.Window.CANCEL) {
  		  // Do nothing, return
		  return;
		}

		// return if nothing changed !
		if (!isChanged) {
		  return;
		}

		if (server != null) {
		  server.stop(0);
		}

		try {
			server = HttpServer.create(new InetSocketAddress(host, port), 10);

			DefaultView = new THttpProjectExplorerView(this);
			// no Java or C++ view for a while
			//new THttpCdtProjectsViewer(this);
			//new THttpJavaProjectView(this);
			
			for (IHttpView v : views) {
				for (THttpCapability c : v.GetCapabilitis()) {
					server.createContext("/vm/" + v.getUid() +"/" + c.Uri, new JSONPHandler(v, c));
				}
			}
			
			// vm/ - view manager
			// vm/getviews       - return the list of registered views
			// vm/%viewid%/getcapabilities - view capabilities TBD
			// vm/cp/newfolder   - new folder creation
			server.createContext("/vm/getviews", new ViewManager());
// postponed capabilities of each view for a while
// Capabilities: indexing, open source code - file in the native editor, highlighted definition API etc(F3)
//               the same capabilities should be requested from each project (It could be possible that index not available etc)

//			server.createContext("/vm/cp/capabilities", new ViewManager()); // stub. TODO add some meaning
//			server.createContext("/vm/un/capabilities", new ViewManager()); // stub. TODO add some meaning
//			server.createContext("/vm/java/capabilities", new ViewManager()); // stub. TODO add some meaning
			//server.createContext("/vm/cp/getlist", new ProjectsHandler());
			//server.createContext("/vm/cp/save", new SaveHandler());
			//server.createContext("/vm/cp/open", new RestoreHandler());
			//server.createContext("/vm/cp/getdiagram", new RestoreHandler());
			
			// Based on TranslationUnit abstraction
			// vm/cp/db/class/methods {'md':s_key[1], 'attr':m1[1], 'ret':m1[2], 'args':m1[3]}
			// vm/cp/db/class/getbase
			// vm/cp/db/class/nested
			//server.createContext("/vm/cp/db/class/methods", new IndexHandler(IndexHandler.REQUEST_CLASS_INFO));			
			//server.createContext("/vm/cp/db/class/getbase", new IndexHandler(IndexHandler.REQUEST_CLASS_BASE));
			//server.createContext("/vm/cp/db/class/nested", new IndexHandler(IndexHandler.REQUEST_CLASS_NESTED));
			
			// Based on Indexer functionality
			// vm/cp/db/class/realization
			// server.createContext("/vm/cp/db/class/realization", new IndexDatabaseHandler());
			// Not implemented yet
			// vm/cp/db/class/friends
			// vm/cp/db/class/association
			// vm/cp/db/class/aggregation

			
	        server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	@Override
	public String save(List<THttpGetKey> keys) throws Exception {
		return DefaultView.SaveDiagram(keys);
	}

	@Override
	public String open(List<THttpGetKey> keys) throws Exception {
		return DefaultView.OpenDiagram(keys);
	}
	
	@Override
	public String file(List<THttpGetKey> keys) throws Exception {
		return DefaultView.OpenFile(keys);
	}

	
	class RRR implements Runnable {

		IWorkbenchPage Page;
		IFile File;
		RRR(IWorkbenchPage page, IFile file) {
			File = file;
			Page = page;
		}

		@Override
		public void run() {
			if (Page != null) {
				IEditorDescriptor desc = PlatformUI.getWorkbench().
						getEditorRegistry().getDefaultEditor(File.getName());
				try {
					Page.openEditor(new FileEditorInput(File), desc.getId());
				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Support for showing a Busy Cursor during a long running process.
	 *
	 * @see <a href="file:///C:/Users/aea301/Desktop/Diagrammer/trunk/diagrammer/index2.html">BusyIndicator snippets</a>
	 * @see <a href="file:///C:/Users/aea301/Desktop/Diagrammer/trunk/diagrammer/canvas.html">Sample code and further information</a>
	 */
	@Override
	public String open(IFile file) throws Exception {

		//IEditorDescriptor desc = PlatformUI.getWorkbench().
//				getEditorRegistry().getDefaultEditor(file.getName());

		IWorkbenchPage page2 = window.getActivePage();
/*		if (page2 != null)
			page2.openEditor(new FileEditorInput(file), desc.getId());
		
		if (awb != null) {
			IWorkbenchPage page = awb.getActivePage();
			if (page != null) {
				page.openEditor(new FileEditorInput(file), desc.getId());
			}
		}
*/

		window.getWorkbench().getDisplay().asyncExec(new RRR(page2, file));
		
		return "";
	}
	

	@Override
	public String remove(List<THttpGetKey> keys) throws Exception {
		return DefaultView.RemoveDiagram(keys);
	}

	@Override
	public String newfolder(List<THttpGetKey> keys) throws Exception {
		return DefaultView.NewFolder(keys);
	}
	


}