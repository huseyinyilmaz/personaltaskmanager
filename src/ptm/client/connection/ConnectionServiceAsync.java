package ptm.client.connection;

import java.util.Queue;

import ptm.client.datamodel.Session;


import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ConnectionServiceAsync {

	void getLoginUrl(AsyncCallback<String> callback);

	void getLogoutUrl(AsyncCallback<String> callback);

	void getSession(AsyncCallback<Session> callback);

	void processActions(String user, Queue<Action> actionQueue,
			AsyncCallback<Queue<Result>> callback);

}
