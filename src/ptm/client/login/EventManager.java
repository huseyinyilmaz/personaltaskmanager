package ptm.client.login;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;

public class EventManager implements ClickHandler {
	private LoginManager loginManager;
	
	public EventManager(LoginManager loginManager){
		this.loginManager = loginManager;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		if ((Widget)event.getSource() == loginManager.getLoginButton()){
			loginManager.login();
		}
		
	}
	
	
	
}
