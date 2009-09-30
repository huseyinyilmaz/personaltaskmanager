package ptm.client.login;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("connectionManager")
public interface ConnectionManager extends RemoteService {
	String getLoginUrl();
	String getLogoutUrl();
}
