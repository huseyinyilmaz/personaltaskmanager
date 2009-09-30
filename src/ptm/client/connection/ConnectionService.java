package ptm.client.connection;

import java.util.Queue;

import ptm.client.datamodel.Session;
import ptm.client.exception.NotLoggedInException;
import ptm.client.exception.ScratchpadException;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("connectionService")
public interface ConnectionService extends RemoteService {
	String getLoginUrl();
	String getLogoutUrl();
	Session getSession()throws NotLoggedInException;
	Queue<Result> processActions(String user,Queue<Action> actionQueue)throws ScratchpadException;
}