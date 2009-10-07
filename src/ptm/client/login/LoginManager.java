package ptm.client.login;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class LoginManager {
	private HorizontalPanel mainPanel = new HorizontalPanel();
	private Button loginButton = new Button("log in");
	private ConnectionManagerAsync loginConnectionManager = GWT.create(ConnectionManager.class);
	
	private EventManager eventManager;
	public void initialize(){
		eventManager = new EventManager(this);
		//create mainPanel
		mainPanel.add(loginButton);
		loginButton.addClickHandler(eventManager);
		// Associate the Main panel with the HTML host page.
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
