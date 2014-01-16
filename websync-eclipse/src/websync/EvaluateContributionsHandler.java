package websync;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.e4.core.di.annotations.Execute;

import websync.http.interfaces.IHttpView;
import websync.http.interfaces.IHttpViewManager;

public class EvaluateContributionsHandler {
  private static final String IWEBSYNC_ID = 
      "org.websync.eclipse.cdt";
private IHttpViewManager ViewManager;
  @Execute
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

  private void executeExtension(final Object o, final Object vm) {
    ISafeRunnable runnable = new ISafeRunnable() {
      @Override
      public void handleException(Throwable e) {
        System.out.println("Exception in client");
      }

      @Override
      public void run() throws Exception {
        System.out.println(((IHttpView) o).getName());
      }
    };
    SafeRunner.run(runnable);
  }

} 