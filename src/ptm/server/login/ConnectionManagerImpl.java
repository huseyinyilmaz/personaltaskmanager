package ptm.server.login;

import ptm.client.login.ConnectionManager;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ConnectionManagerImpl extends RemoteServiceServlet implements ConnectionManager {
	UserService userService = UserServiceFactory.getUserService();

	@Override
	public String getLoginUrl() {
		return userService.createLoginURL("/ptm.html");
	}

	@Override
	public String getLogoutUrl() {
		return userService.createLogoutURL("/login.html");
	}

}
