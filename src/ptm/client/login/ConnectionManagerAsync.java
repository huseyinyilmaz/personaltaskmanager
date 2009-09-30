package ptm.client.login;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ConnectionManagerAsync {

	void getLoginUrl(AsyncCallback<String> callback);

	void getLogoutUrl(AsyncCallback<String> callback);

}
