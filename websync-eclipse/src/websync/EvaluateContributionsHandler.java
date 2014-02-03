package websync;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;

import websync.http.interfaces.IHttpView;
import websync.http.interfaces.IHttpViewManager;

public class EvaluateContributionsHandler {
  private static final String IWEBSYNC_ID = 
      "org.websync.eclipse.cdt";
private IHttpViewManager ViewManager;

  public void execute(IExtensionRegistry extensionRegistry,
			IHttpViewManager vm) {
	ViewManager = vm;
	evaluate(extensionRegistry);
  }
  
  private void evaluate(IExtensionRegistry registry) {
    IConfigurationElement[] config =
        registry.getConfigurationElementsFor(IWEBSYNC_ID);
    try {
      for (IConfigurationElement e : config) {
        System.out.println("Evaluating extension");
        final Object o =
            e.createExecutableExtension("class");
        if (o instanceof IHttpView) {
          ((IHttpView) o).Init(ViewManager);
        }
      }
    } catch (CoreException ex) {
      System.out.println(ex.getMessage());
    }
  }
} 