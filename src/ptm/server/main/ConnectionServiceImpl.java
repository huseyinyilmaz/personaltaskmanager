package ptm.server.main;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import ptm.client.connection.Action;
import ptm.client.connection.ConnectionService;
import ptm.client.connection.Result;
import ptm.client.datamodel.ObjectListElement;
import ptm.client.datamodel.Session;
import ptm.client.exception.NotLoggedInException;
import ptm.client.exception.ScratchpadException;
import ptm.client.exception.UnmatchedUserException;


import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ConnectionServiceImpl extends RemoteServiceServlet implements ConnectionService {
	UserService userService = UserServiceFactory.getUserService();

	@Override
	public String getLoginUrl() {
		return userService.createLoginURL("/ptm.html");
	}

	@Override
	public String getLogoutUrl(){
		return userService.createLogoutURL("/login.html");
	}

	@Override
	public Session getSession() throws NotLoggedInException {
		//Check if there is a user logged in
		if(!userService.isUserLoggedIn())
			throw new NotLoggedInException();
		User currentUser = userService.getCurrentUser();
		//Session session = new Session(currentUser.getEmail());
		return QueryManager.getSession(currentUser);
	}

	@Override
	public Queue<Result> processActions(String user,Queue<Action> actionQueue)throws ScratchpadException {
		Queue<Result> queue = new LinkedList<Result>();
		Map<Long,Long> newIds = new LinkedHashMap<Long,Long>();
		long oid;
		//Get current user
		if(!userService.isUserLoggedIn())
			throw new NotLoggedInException();
		User currentUser = userService.getCurrentUser();
		if (!currentUser.getEmail().equals(user))
			throw new UnmatchedUserException();		

		//process events
		for(Action a : actionQueue){
			Result result = new Result();
			result.setActionId(a.getActionId());
			try{
				switch (a.getActionType()){
				//TODO actions
				case TODO_CREATE:
					result.setToDo(QueryManager.createToDo((ptm.client.datamodel.ToDoList)a.getObject(),currentUser).toClientObject());
					newIds.put(((ptm.client.datamodel.ToDoList)a.getObject()).getId(), result.getToDo().getId());
					break;
				case TODO_DELETE:
					/*
					 * we do not need to get any value from newIds collection
					 * because if we delete new todo lists they don't even come
					 * to server. we delete them on client side.
					 */
					QueryManager.deleteToDo(a.getObjectId());
					break;
				case TODO_EDIT:
					QueryManager.editToDo(a.getObjectId(),a.getString());
					break;
				case TODO_OPEN:
					oid =a.getObjectId();
					if(oid<0)
						oid = newIds.get(oid);
					result.setToDo(QueryManager.openToDo(oid).toClientObject());
					break;
				case TODO_GET:	
					oid =a.getObjectId();
					if(oid<0)
						oid = newIds.get(oid);
					result.setToDo(QueryManager.getToDo(oid).toClientObject());
					break;
				case TODO_CLOSE:
					oid =a.getObjectId();
					if(oid<0)
						oid = newIds.get(oid);
					QueryManager.closeToDo(oid);
					break;
				case TODO_MOVE:
					oid =((ObjectListElement)a.getObject()).getId();
					if(oid<0)
						((ObjectListElement)a.getObject()).setId(newIds.get(oid));
					QueryManager.moveToDo((ObjectListElement)a.getObject());
					break;
				//TASK ACTIONS	
				case TASK_CREATE:
					//if TodoListId is negative get list created list id from new Ids map.
					oid =a.getObjectId();
					if(oid<0)
						oid = newIds.get(oid);
					result.setObjectId(QueryManager.createTask(oid,(ptm.client.datamodel.Task)a.getObject()));
					break;
				case TASK_DELETE:
					//if TodoListId is negative get list created list id from new Ids map.
					oid =a.getObjectId();
					if(oid<0)
						oid = newIds.get(oid);
					QueryManager.deleteTask(oid,(ptm.client.datamodel.Task)a.getObject());
					break;
				case TASK_EDIT:
					//if TodoListId is negative get list created list id from new Ids map.
					oid =a.getObjectId();
					if(oid<0)
						oid = newIds.get(oid);
					QueryManager.editTask(oid,(ptm.client.datamodel.Task)a.getObject());
					break;
				case NOTE_CREATE:
					result.setNote(QueryManager.createNote((ptm.client.datamodel.Note)a.getObject(),currentUser).toClientObject());
					newIds.put(((ptm.client.datamodel.Note)a.getObject()).getId(), result.getNote().getId());
					break;
				case NOTE_DELETE:
					QueryManager.deleteNote(((ptm.client.datamodel.Note)a.getObject()).getId());
					break;
				case NOTE_CLOSE:
					QueryManager.closeNote(a.getObjectId());
					break;
				case NOTE_OPEN:
					oid =a.getObjectId();
					if(oid<0)
						oid = newIds.get(oid);
					result.setNote(QueryManager.openNote(oid).toClientObject());
					break;

				}//switch
				result.setSuccessfull(true);
			}catch(ScratchpadException e){
				result.setException(e);
				result.setSuccessfull(false);
			}finally{
				queue.add(result);
			}
		}//for
		return queue;
	}



}
