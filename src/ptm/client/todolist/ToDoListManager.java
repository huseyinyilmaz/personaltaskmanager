package ptm.client.todolist;

import java.util.ArrayList;

import ptm.client.connection.Action;
import ptm.client.datamodel.ObjectListElement;
import ptm.client.datamodel.ToDoList;
import ptm.client.main.ApplicationManager;
import ptm.client.main.EventManager;


import com.google.gwt.user.client.Window;

/**
 * This is a ToDoList Manager Object which is responsible everything about to - do List structure.
 * To-to list manager responsible for creating, editing, deleting ,opening ,holding 
 * todo list dialogs and every to-do list dialog is responsible for holding the to-doList object
 * it represents and ,create,edit,delete tasks on this object. 
 * @author Huseyin
 */
public class ToDoListManager {
	
	//Manager of current session.
	private ApplicationManager applicationManager;
	
	//List of openDialogs
	private ArrayList<ToDoListDialog> toDoListDialogs = new ArrayList<ToDoListDialog>();
	
	//Constructors
	
	/**
	 * Creates a new ToDoList Manager which uses given application manager.
	 * @param applicationManager manager of current session
	 */
	public ToDoListManager(ApplicationManager applicationManager){
		this.applicationManager = applicationManager;
	}

	
	/**
	 * Searches registers dialogs on this manager. And returns the one that has given objectid
	 * @param toDoListId Id of the to-do list that contained by the dialog we are looking for. 
	 * @return Dialog that has a to-do list with given id
	 */
	public ToDoListDialog getToDoListDialog(long toDoListId){
		for (ToDoListDialog d: toDoListDialogs){
			if (d.getId()==toDoListId)
				return d;
		}
		return null;
	}
	
	/**
	 * Creates a new TodoList.This method asks user for a new to-do list name and 
	 * creates a new todoList with this name.
	 */
	public void createPressed(){
		String name = Window.prompt("Please enter a name for To-do list", "");
		if (name != null){
			long tempId = applicationManager.getNextTempOId();
	
			
			ToDoList newList = new ToDoList(tempId,name,null); 
			ToDoListDialog dialog = new ToDoListDialog(newList, this);
			
			//create new action
			Action action =new Action();
			action.setActionType(Action.ActionType.TODO_CREATE);
			action.setObject(newList);
			
			applicationManager.getConnectionManager().addAction(action);

			//add new item to client
			toDoListDialogs.add(dialog);
			dialog.center();

			ObjectListElement e = new ObjectListElement();
			e.setId(tempId);
			e.setName(name);
			e.setX(dialog.getPopupLeft());
			e.setY(dialog.getPopupTop());
			e.setOpen(true);
			int itemLocation = applicationManager.getSession().placeToDo(e);
			applicationManager.getToolbarManager().getTodoListBox().insertItem(name,Long.toString(tempId),itemLocation);
			
			action = new Action();
			action.setActionType(Action.ActionType.TODO_MOVE);
			action.setObject(e);
			
			applicationManager.getConnectionManager().addAction(action);
			
		}
	}

	/**
	 * Method is called automatically after server's create processes finished. it replace temprory object id
	 * with new to-do list id that is coming from server.
	 * @param toDoList ToDoList that just saved by server. This is the one that was stored by client. 
	 * Not the one that created by server.
	 * @param newObjectId Saved ToDoList's new id.
	 */
	public void postCreate(ToDoList toDoList,long newObjectId){
		int index =applicationManager.getSession().getToDoIndex(toDoList.getId());
		applicationManager.getSession().setToDoId(index, newObjectId);
		applicationManager.getToolbarManager().getTodoListBox().setValue(index, Long.toString(newObjectId));
		toDoList.setId(newObjectId);
	}

	/**
	 * Edits selected todoList
	 */
	public void editPressed(){
		int index = applicationManager.getToolbarManager().getTodoListBox().getSelectedIndex();
		String orgName= applicationManager.getToolbarManager().getTodoListBox().getItemText(index);
		long id = Long.parseLong(applicationManager.getToolbarManager().getTodoListBox().getValue(index));
		String newName = Window.prompt("Please enter a name for to-do list", orgName);
		if (newName != null){
			//create new action
			Action action =new Action();
			action.setActionType(Action.ActionType.TODO_EDIT);
			//action.setToDoList(new ToDoList(id,newName,null));
			action.setObjectId(id);
			action.setString(newName);
			applicationManager.getConnectionManager().addAction(action);
			//edit client list.
			
			//set new name in the session variable
			int oldItemLocation = applicationManager.getSession().getToDoIndex(id);
			ObjectListElement e = applicationManager.getSession().getAllToDoLists().get(oldItemLocation);
			e.setName(newName);
			applicationManager.getSession().setToDo(oldItemLocation, e);
			applicationManager.getSession().sortToDo();
			int newItemLocation = applicationManager.getSession().getToDoIndex(id);
			// if place of ptm list is changed move element on screen too;
			if( oldItemLocation==newItemLocation)
				applicationManager.getToolbarManager().getTodoListBox().setItemText(oldItemLocation, newName);
			else{
				applicationManager.getToolbarManager().getTodoListBox().removeItem(oldItemLocation);
				applicationManager.getToolbarManager().getTodoListBox().insertItem(newName, Long.toString(id), newItemLocation);
				applicationManager.getToolbarManager().getTodoListBox().setSelectedIndex(newItemLocation);
			}
			// if list is open refresh todo list dialog too
			ToDoListDialog toDoListDialog = getDialog(id);
			if (toDoListDialog != null){
				toDoListDialog.setName(newName);
			}
		}
	}//edit()

	/**
	 * Deletes selected to-do list from client and add an action to actionQueue
	 */
	public void deletePressed(){
		int index = applicationManager.getToolbarManager().getTodoListBox().getSelectedIndex();
		//TODO if there is no list selected do nothing.You could do it in event manager. you could even disable buttons
		String name = applicationManager.getToolbarManager().getTodoListBox().getItemText(index);
		long id = Long.parseLong(applicationManager.getToolbarManager().getTodoListBox().getValue(index));
		if (Window.confirm("Are you sure that you want to delete Todo List [" + name + "]?")){
			//create new action
			Action action =new Action();
			action.setActionType(Action.ActionType.TODO_DELETE);
			action.setObjectId(id);
			//action.setToDoList(new ToDoList(id,null,null));
			applicationManager.getConnectionManager().addAction(action);
			//delete from client
			applicationManager.getToolbarManager().getTodoListBox().removeItem(index);
			ToDoListDialog toDoListDialog =getDialog(id);
			ObjectListElement todoElement = getApplicationManager().getSession().getToDo(toDoListDialog.getId());
			//if it is open remove it.
			if (todoElement.isOpen()){
				toDoListDialog.hide();
				getToDoListDialogs().remove(toDoListDialog);
			}
			applicationManager.getSession().removeToDo(id);
		}
	}

	/**
	 * Opens Selected to-do list. This is the only action that forces client to sync.
	 */
	public void openPressed(){
		int index = applicationManager.getToolbarManager().getTodoListBox().getSelectedIndex();
		long id = Long.parseLong(applicationManager.getToolbarManager().getTodoListBox().getValue(index));
	
		Action action = new Action();
		action.setActionType(Action.ActionType.TODO_OPEN);
		//action.setToDoList(new ToDoList(id,null,null));
		action.setObjectId(id);
		applicationManager.getConnectionManager().addAction(action);
		//we force system to sync action to get our list as soon as possible
		applicationManager.getConnectionManager().sync();
	}
	
	
	/**
	 * This method is called after open_todolist respond comes back from server.
	 * it creates a new dialog with to-do list that came from server and shows it
	 * on the page.
	 * @param toDoList to-do list that came back from server.
	 */
	public void postOpen(ToDoList toDoList){
		ToDoListDialog dialog = new ToDoListDialog(toDoList, this);
		ObjectListElement e = getApplicationManager().getSession().getToDo(toDoList.getId());
		dialog.setPopupPosition(e.getX(), e.getY());
		toDoListDialogs.add(dialog);
		dialog.show();
	}
	
	
	//Getters and Setters
	public EventManager getEventManager(){
		return applicationManager.getEventManager();
	}
	public ArrayList<ToDoListDialog> getToDoListDialogs() {
		return toDoListDialogs;
	}

	public ApplicationManager getApplicationManager() {
		return applicationManager;
	}
	
	public ToDoListDialog getDialog(long id){
		for(ToDoListDialog d : toDoListDialogs){
			if(d.getId() == id)
				return d;
		}
		return null;
	}
	
	
	

	
	
}
