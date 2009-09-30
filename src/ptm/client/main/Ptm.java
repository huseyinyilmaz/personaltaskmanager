package ptm.client.main;


import com.google.gwt.core.client.EntryPoint;

/**
 * This is the entryPoint of this application. Only thing this class does is
 * to initialize applicationManager.
 * @author huseyin
 *
 */
public class Ptm implements EntryPoint {
	
	//Manager of the application. This application is controlled by this object
	private ApplicationManager applicationManager = new ApplicationManager();
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		applicationManager.initialize();
	}
	
	

}
