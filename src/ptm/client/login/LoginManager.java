package ptm.client.login;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class LoginManager {
	private DockPanel mainPanel = new DockPanel();
	private HorizontalPanel loginButtonPanel = new HorizontalPanel();
	private Button loginButton = new Button("log in");
	private ConnectionManagerAsync loginConnectionManager = GWT.create(ConnectionManager.class);
	
	private EventManager eventManager = new EventManager(this);
	
	public void initialize(){
		String mainHTML = 
			"<p>"+
			"This application holds your to-do Lists and notes." +
			" It is still in development,but we are stable right now." +
			" So you can depend on it." +
			" Thank you for trying." +
			"If you want to report a bug or check source code, visit <a target=\"_blank\" href=\"http://code.google.com/p/personaltaskmanager/\">project page</a>" +
			"</p>"+
			"<p>"+
			"This application uses Google user service to keep track of users. So you have to be signed in with your google account to use this app." +
			"Please click login button on top-right corner of this page to start application."+
			"</p>"
			
			
			;
			
		mainPanel.setWidth("100%");
		mainPanel.addStyleName("mainPanel");
		//create loginButtonPanel
		loginButtonPanel.add(loginButton);
		loginButton.addClickHandler(eventManager);
		// Associate the Main panel with the HTML host page.
	    
		DecoratorPanel tempDecoratorPanel = new DecoratorPanel();
		tempDecoratorPanel.add(loginButton);
		tempDecoratorPanel.addStyleName("loginButton");
		
	    DockPanel tempNorthPanel = new DockPanel();
	    tempNorthPanel.setWidth("100%");
	    tempNorthPanel.addStyleName("northPanel");
	    tempNorthPanel.add(tempDecoratorPanel, DockPanel.EAST);
	    HTML tempTitleHTML = new HTML("Personal Task Manager");
	    tempTitleHTML.addStyleName("mainTitle");
	    tempNorthPanel.add(tempTitleHTML, DockPanel.CENTER);
	    tempNorthPanel.setCellHorizontalAlignment(tempTitleHTML, DockPanel.ALIGN_CENTER);
	    mainPanel.add(tempNorthPanel, DockPanel.NORTH);
	    HTML tempMainBodyHTML = new HTML(mainHTML);
	    tempMainBodyHTML.addStyleName("mainBody");
	    mainPanel.add(tempMainBodyHTML,DockPanel.CENTER);
	    mainPanel.setCellHorizontalAlignment(tempDecoratorPanel, DockPanel.ALIGN_RIGHT);
		RootPanel.get().add(mainPanel);		
	}
	
	public Button getLoginButton(){
		return loginButton;
	}
	
	public void login(){

		// Set up the callback object.
		AsyncCallback<String> callback = new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				Window.alert("Error while trying to get login url from server. Please try again later");
			}

			public void onSuccess(String result) {
				Window.open(result, "_self", "");

			}
		};

		//do a remote procedure call
		loginConnectionManager.getLoginUrl(callback);
	}

	public void logout(){

		// Set up the callback object.
		AsyncCallback<String> callback = new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				Window.alert("Error while trying to get logout url from server. Please try again later");
			}

			public void onSuccess(String result) {
				Window.open(result, "_self", "");

			}
		};

		//do a remote procedure call
		loginConnectionManager.getLogoutUrl(callback);
	}


}
