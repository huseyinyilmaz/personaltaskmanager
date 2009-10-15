package ptm.client.main;

import ptm.client.datamodel.ObjectListElement;
import ptm.client.datamodel.Session;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ToolbarManager {
	// main Panels
	private VerticalPanel toolBarPanel = new VerticalPanel();
	private StackPanel stackPanel = new StackPanel();
	
	//	private Button logoutButton = new Button("log out");

	// ptm Panel
	private VerticalPanel todoPanel = new VerticalPanel();
	private ListBox todoListBox = new ListBox();
	private Button createTodoButton = new Button("Create");
	private Button deleteTodoButton = new Button("Delete");
	private Button editTodoButton = new Button("Edit");
	private Button openTodoButton = new Button("Open");
	//private Button showAllTodoButton = new Button("Show All");
	//private Button hideAllTodoButton = new Button("Hide All");
	
	//Note Panel
	private VerticalPanel notePanel = new VerticalPanel();
	private ListBox noteListBox = new ListBox();
	private Button createNoteButton = new Button("Create");
	private Button deleteNoteButton = new Button("Delete");
	private Button openNoteButton = new Button("Open");
	//private Button showAllNoteButton = new Button("Show All");
	//private Button hideAllNoteButton = new Button("Hide All");
	
	//Note Panel
	private VerticalPanel generalPanel = new VerticalPanel();
	//private Button generalShowAllButton = new Button("Show All");
	//private Button generalHideAllButton = new Button("Hide All");
	private Button generalSyncButton = new Button("Sync");
	private Button generalLogoutButton = new Button("Log out");
	
	
	
	//private Button createNoteButton = new Button("Create a new note");
	private ApplicationManager applicationManager;
	
	public ToolbarManager(ApplicationManager applicationManager){
		this.applicationManager = applicationManager;

		//initialize todoPanel
		todoListBox.setVisibleItemCount(8);
		todoListBox.setStyleName("toolbarTodoList");
		todoPanel.add(todoListBox);

		todoPanel.add(openTodoButton);
		todoPanel.add(createTodoButton);
		todoPanel.add(editTodoButton);
		todoPanel.add(deleteTodoButton);

		openTodoButton.addStyleName("toolbarTodoButton");
		createTodoButton.addStyleName("toolbarTodoButton");
		editTodoButton.addStyleName("toolbarTodoButton");
		deleteTodoButton.addStyleName("toolbarTodoButton");

		createTodoButton.addClickHandler(applicationManager.getEventManager());
		editTodoButton.addClickHandler(applicationManager.getEventManager());
		deleteTodoButton.addClickHandler(applicationManager.getEventManager());
		openTodoButton.addClickHandler(applicationManager.getEventManager());
		ChangeHandler handler = new ChangeHandler(){
			public void onChange(ChangeEvent event){
				evaluateToDoButtonStatus();
			}
		};
		todoListBox.addChangeHandler(handler);
		handler = new ChangeHandler(){
			public void onChange(ChangeEvent event){
				evaluateNoteButtonStatus();
			}
		};
		noteListBox.addChangeHandler(handler);
		
		
		//initialize notePanel
		noteListBox.setVisibleItemCount(8);
		noteListBox.setStyleName("toolbarNoteList");
		notePanel.add(noteListBox);

		notePanel.add(openNoteButton);
		notePanel.add(createNoteButton);
		notePanel.add(deleteNoteButton);

		openNoteButton.addClickHandler(applicationManager.getEventManager());
		createNoteButton.addClickHandler(applicationManager.getEventManager());
		deleteNoteButton.addClickHandler(applicationManager.getEventManager());
		
		openNoteButton.addStyleName("toolbarNoteButton");
		createNoteButton.addStyleName("toolbarNoteButton");
		deleteNoteButton.addStyleName("toolbarNoteButton");
		
		//initialize generalPanel

		generalPanel.add(generalSyncButton);
		generalPanel.add(generalLogoutButton);

		generalSyncButton.addStyleName("toolbarGeneralButton");
		generalLogoutButton.addStyleName("toolbarGeneralButton");
		generalLogoutButton.addClickHandler(applicationManager.getEventManager());
		generalSyncButton.addClickHandler(applicationManager.getEventManager());
		
		//initialize stackPanel
		stackPanel.add(todoPanel, "To-do List");
		stackPanel.add(notePanel, "Note List");
		stackPanel.addStyleName("stackPanel");
		
		//initialize toolbarPanel
		toolBarPanel.add(stackPanel);
		DecoratorPanel dp = new DecoratorPanel();
		dp.setWidget(generalPanel);
		dp.addStyleName("generalDecoratorPanel");
		toolBarPanel.add(dp);	
		toolBarPanel.addStyleName("toolbarPanel");
		
		
	}
	
	/**
	 * Starts session for tool bar. It fills list boxes.
	 */
	public void StartSession(){
		Session session = applicationManager.getSession();
		//Place to-do list names in list box
		for (ObjectListElement e: session.getAllToDoLists())
			getTodoListBox().addItem(e.getName(), Long.toString(e.getId()));
		//Place note names in list box
		for (ObjectListElement e: session.getAllNotes())
			getNoteListBox().addItem(e.getName(), Long.toString(e.getId()));
		evaluateToDoButtonStatus();
		evaluateNoteButtonStatus();

	}
	
	/**
	 * Enables or disables buttons in order the status of application.
	 */
	public void evaluateToDoButtonStatus(){
		int index = todoListBox.getSelectedIndex();
		boolean isOpenEnabled = false;
		boolean isEditEnabled = false;
		boolean isDeleteEnabled = false;
		if (index != -1){
			isEditEnabled = true;
			isDeleteEnabled = true;
			isOpenEnabled = true;
		}
		openTodoButton.setEnabled(isOpenEnabled);
		editTodoButton.setEnabled(isEditEnabled);
		deleteTodoButton.setEnabled(isDeleteEnabled);
	}

	/**
	 * Enables or disables buttons in order the status of application.
	 */
	public void evaluateNoteButtonStatus(){
		int index = noteListBox.getSelectedIndex();
		boolean isOpenEnabled = false;
		boolean isDeleteEnabled = false;
		if (index != -1){
			isDeleteEnabled = true;
			isOpenEnabled = true;
		}

		openNoteButton.setEnabled(isOpenEnabled);
		deleteNoteButton.setEnabled(isDeleteEnabled);
	}
	
	/**
	 * @param isSyncing 1
	 */
	public void setSyncStatus(boolean isSyncing){
		generalSyncButton.setEnabled(!isSyncing);
		if(isSyncing)
			generalSyncButton.setText("Syncing...");
		else
			generalSyncButton.setText("Sync");
		
	}
	//setters and getters
	public VerticalPanel getToolBar(){
		return toolBarPanel;
	}
	
	public Button getGeneralLogoutButton(){
		return generalLogoutButton;
	}
	
	public ListBox getTodoListBox() {
		return todoListBox;
	}

	public Button getCreateTodoButton() {
		return createTodoButton;
	}

	public Button getDeleteTodoButton() {
		return deleteTodoButton;
	}

	public Button getEditTodoButton() {
		return editTodoButton;
	}

	public Button getOpenTodoButton() {
		return openTodoButton;
	}

	public ListBox getNoteListBox() {
		return noteListBox;
	}

	public Button getCreateNoteButton() {
		return createNoteButton;
	}

	public Button getDeleteNoteButton() {
		return deleteNoteButton;
	}

	public Button getOpenNoteButton() {
		return openNoteButton;
	}
	
	public Button getGeneralSyncButton() {
		return generalSyncButton;
	}
	
	
}
