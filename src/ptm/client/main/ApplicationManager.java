package ptm.client.main;

import ptm.client.connection.Action;
import ptm.client.connection.ConnectionManager;
import ptm.client.datamodel.ObjectListElement;
import ptm.client.datamodel.Session;
import ptm.client.note.NoteManager;
import ptm.client.todolist.ToDoListManager;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ApplicationManager {
	private Session session;
	private DockPanel mainPanel = new DockPanel();
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
		mainPanel.setWidth("100%");
		mainPanel.add(toolbarManager.getToolBar(),DockPanel.WEST);
		
		DecoratorPanel dp = new DecoratorPanel();
		dp.setWidget(toolbarManager.getGeneralLogoutButton());
		dp.addStyleName("logoutGeneralButton");

		
		mainPanel.add(dp,DockPanel.EAST);
		mainPanel.setCellHorizontalAlignment(dp, DockPanel.ALIGN_RIGHT);

		// Associate the Main panel with the HTML host page.
	    RootPanel.get().add(mainPanel);
	    //get Session Info From server
	    getConnectionManager().initializeSession();
	    CloseHandler<Window> closeHandler = new CloseHandler<Window>(){
			@Override
			public void onClose(CloseEvent<Window> event) {
				getConnectionManager().sync();
			}
	    };
	    
	    Window.addCloseHandler(closeHandler);
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
