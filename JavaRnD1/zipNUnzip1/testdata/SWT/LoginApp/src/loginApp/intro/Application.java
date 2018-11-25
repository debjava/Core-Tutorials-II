package loginApp.intro;

import loginApp.LoginDialog;

import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IPlatformRunnable {

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IPlatformRunnable#run(java.lang.Object)
	 */
	public Object run(Object args) throws Exception {
		Display display = PlatformUI.createDisplay();

		try {
			if (authenticate(display)) {
				int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
				return (returnCode == PlatformUI.RETURN_RESTART) 
						? IPlatformRunnable.EXIT_RESTART
						: IPlatformRunnable.EXIT_OK;

			}
			Platform.endSplash();
			return IPlatformRunnable.EXIT_OK;
		} finally {
			display.dispose();
		}
	}
	
	private boolean authenticate(Display display) {
		LoginDialog loginDialog = new LoginDialog(display);
		loginDialog.createContents();
		return true;
	}
}
