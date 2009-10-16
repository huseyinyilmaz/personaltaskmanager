package ptm.client.connection;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;

import ptm.client.datamodel.Note;
import ptm.client.datamodel.Session;
import ptm.client.datamodel.Task;
import ptm.client.datamodel.ToDoList;
import ptm.client.exception.NotLoggedInException;
import ptm.client.exception.UnmatchedUserException;
import ptm.client.main.ApplicationManager;
import ptm.client.todolist.ToDoListDialog;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * This class
 * @author huseyin
 *
 */
public class ConnectionManager {
	//private fields
	private ActionQueue actionQueue = new ActionQueue();
	private ConnectionServiceAsync connectionService = GWT.create(ConnectionService.class);
	private ApplicationManager applicationManager;
	private class SyncTimer extends Timer {
		private boolean isCounting=false;
		private final int second = 1000;
		private final int minute = 60;
		private final int interval = 5 * minute ;
		private int count = 0;
		public void start(){
			if (!isCounting){
				count = interval;
				this.scheduleRepeating(second);
				isCounting = true;
			}
		}
		
		public void cancel(){
			if(isCounting){
				isCounting = false;
				super.cancel();
			}
		}
		
		@Override
		public void run() {
			if (count>0){
				//applicationManager.getToolbarManager().getCounterLabel().setText(Integer.toString(count--));
				applicationManager.getToolbarManager().getGeneralSyncButton().setText("Syncing in "+ count-- + "sc.");
			}else{
				cancel();
				isCounting = false;
				sync();
				//applicationManager.getToolbarManager().getCounterLabel().setText("");
				//applicationManager.getToolbarManager().getGeneralSyncButton().setText("Sync");
			}
		}

	}
	SyncTimer timer = new SyncTimer();
	public ConnectionManager(ApplicationManager applicationManager){
		this.applicationManager = applicationManager;
	}

	/** 
	 * Adds given action to actionQueue.
	 * This function also processes other actions according to type of action;
	 * @param action Action to add actionQueue
	 */
	public void addAction(Action action){
		actionQueue.add(action);
		timer.start();
	}
	
	
	/** 
	 * Logouts current user and return to main window(login screen)
	 */
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
		connectionService.getLogoutUrl(callback);
	}
	
	
	/** 
	 * Gets session objects from server and hands it to application manager.
	 * Session Object is consist of open window locations, all ToDo and Note
	 * Lists (id's and names only) and current user.
	 */
	public void initializeSession(){
		AsyncCallback<Session> callback = new AsyncCallback<Session>() {
			public void onFailure(Throwable caught) {
				if (caught instanceof NotLoggedInException){
					Window.alert("You have to login in order to use this app.\nPlease press OK to redirect to login page");
					Window.open("/login.html", "_self", "");
				}else{
					Window.alert("Unknown Exception occured:Session initilization failed");
					Window.open("/login.html", "_self", "");
				}
			}
			public void onSuccess(Session result) {
				applicationManager.startSession(result);
			}
		};

		connectionService.getSession(callback);
	}
	
	
	/** 
	 * Sends action list to server and process result list.
	 */
	public void sync(){
		timer.cancel();
		//disable sync button
		applicationManager.getToolbarManager().setSyncStatus(true);
		
		// Set up the callback object.
		AsyncCallback<Queue<Result>> callback = new AsyncCallback<Queue<Result>>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Error caught!", caught);
				if(caught instanceof NotLoggedInException){
					Window.alert("You have to login in order to use this app.\nPlease press OK to redirect to login page");
					Window.open("/login.html", "_self", "");
				}else if (caught instanceof UnmatchedUserException){
					Window.alert("User on the server and client does not match.\nPlease login again.");
					logout();
				}else
					/*
					Window.alert("Error while trying to Sync your Changes.\n"+
							     "We will try again later.\n"+
							     "Exception :\n"+caught.getMessage());
					*/
				timer.start();
			}

			@Override
			public void onSuccess(Queue<Result> resultQueue) {
				Map<Long,Long> newIds = new LinkedHashMap<Long,Long>();
				long toDoId;
				for(Result result : resultQueue){
					Action action = toAction(result);
					
					if(!result.isSuccessfull()){
						GWT.log("Result for action["+action.getActionId()+"] is FAILED : type ="+ action.getActionType(),null);
						Window.alert("process is not ended successfully:result count=" + resultQueue.size());
					}else{
						actionQueue.remove(action);
						GWT.log("Result for action["+action.getActionId()+"] successful : type ="+ 
								action.getActionType(),null);
						switch (action.getActionType()){
						case TODO_CREATE:
							newIds.put(((ToDoList)action.getObject()).getId(), result.getToDo().getId());
							applicationManager.getTodoListManager().postCreate((ToDoList)action.getObject(),result.getToDo().getId());
							break;
						case TODO_DELETE:
							break;
						case TODO_OPEN:
							applicationManager.getTodoListManager().postOpen(result.getToDo());
							break;
						case TASK_CREATE:
							toDoId =action.getObjectId();
							if(toDoId<0)
								toDoId = newIds.get(toDoId);

							ToDoListDialog d =applicationManager.getTodoListManager().getDialog(toDoId);
							if(d != null)//dialog might be closed before results are back. 
								d.postCreate((Task)action.getObject(), result.getObjectId());
						case TASK_EDIT:
							break;
						case TASK_DELETE:
							break;
						case NOTE_CREATE:
							applicationManager.getNoteManager().postCreate((Note)action.getObject(),result.getNote().getId());
							break;
						case NOTE_OPEN:
							applicationManager.getNoteManager().postOpen(result.getNote());
							break;
						}//switch
					}//if
				}//for
				applicationManager.getToolbarManager().setSyncStatus(false);
				
			}
		};

		//do a remote procedure call
		connectionService.processActions(applicationManager.getSession().getUser(),actionQueue, callback);
	}
	
	private Action toAction(Result result){
		for (Action action:actionQueue)
			if (action.getActionId() == result.getActionId())
				return action;
		return null;
	}		
	
}
