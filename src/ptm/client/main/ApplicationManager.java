package ptm.client.main;

import ptm.client.connection.Action;
import ptm.client.connection.ConnectionManager;
import ptm.client.datamodel.ObjectListElement;
import ptm.client.datamodel.Session;
import ptm.client.note.NoteManager;
import ptm.client.todolist.ToDoListManager;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ApplicationManager {
	private Session session;
	private VerticalPanel mainPanel = new VerticalPanel();
	private EventManager eventManager= new EventManager(this);
	//initialize manager variables
	private ToolbarManager toolbarManager=new ToolbarManager(this);
	private ToDoListManager toDoListManager = new ToDoListManager(this);
	private NoteManager noteManager = new NoteManager(this);
	//private Queue<Action> actionQueue = new LinkedList<Action>(); 
	private static long temproryObjectIdHolder=-1;
	private ConnectionManager connectionManager = new ConnectionManager(this);
	
	
	
	
	//public constructor
	//public ApplicationManager(){}


	
	//initialize Application.
	public void initialize(){
		//construct main panel
		mainPanel.add(toolbarManager.getToolBar());
		// Associate the Main panel with the HTML host page.
	    RootPanel.get().add(mainPanel);
	    //get Session Info From server
	    getConnectionManager().initializeSession();
	    // TODO make sure that everything is disabled before we get initilization data.
	}

	public long getNextTempOId(){
		return temproryObjectIdHolder--;
	}

	/**
	 * this object is called after session was received
	 */
	public void startSession(Session session){
		//session do not sort objects on server side anymore
		session.sortToDo();
		session.sortNote();
		
		setSession(session);
		
		//fills list boxes.
		getToolbarManager().StartSession();
		
		//Retrieve opened to-do lists
		Action action;
		for (ObjectListElement e : session.getAllToDoLists()){
			if(e.isOpen()){
				action = new Action();
				action.setActionType(Action.ActionType.TODO_OPEN);
				action.setObjectId(e.getId());
				getConnectionManager().addAction(action);
			}
		}
		//Retrieve opened note
		for ( ObjectListElement e : session.getAllNotes() ){
			if(e.isOpen()){
				action = new Action();
				action.setActionType(Action.ActionType.NOTE_OPEN);
				action.setObjectId(e.getId());
				getConnectionManager().addAction(action);
			}
		}
		
		getConnectionManager().sync();
		
	}
	
	//Getters
	public ToolbarManager getToolbarManager(){
		return toolbarManager;
	}
	
	public EventManager getEventManager(){
		return eventManager;
	}
	
	public ConnectionManager getConnectionManager(){
		return this.connectionManager;
	}


	public ToDoListManager getTodoListManager() {
		return toDoListManager;
	}
	
	public NoteManager getNoteManager() {
		return noteManager;
	}

	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}

}
