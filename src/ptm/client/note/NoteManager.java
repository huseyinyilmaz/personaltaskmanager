package ptm.client.note;

import java.util.ArrayList;

import ptm.client.connection.Action;
import ptm.client.datamodel.Note;
import ptm.client.datamodel.ObjectListElement;
import ptm.client.main.ApplicationManager;
import ptm.client.main.EventManager;


import com.google.gwt.user.client.Window;

/**
 * This is a Note Manager Class which is responsible everything about note structure.
 * note manager responsible for creating, editing, deleting ,opening ,holding 
 * note dialogs and every note dialog is responsible for holding the note object
 * it represents and edit note of this object. 
 * @author Huseyin
 */
public class NoteManager {

	//Manager of current session.
	private ApplicationManager applicationManager;
	
	//Handles onclick events of note dialogs
	private NoteClickHandler noteClickHandler = new NoteClickHandler(this);
	
	//List of openDialogs
	private ArrayList<NoteDialog> noteDialogList = new ArrayList<NoteDialog>();
	
	//Constructors
	
	/**
	 * Creates a new Note Manager which uses given application manager.
	 * @param applicationManager manager of current session
	 */
	public NoteManager(ApplicationManager applicationManager){
		this.applicationManager = applicationManager;
	}

	
	
	//ButtonActions
	
	/**
	 * this method is invoked when open button on tool-bar is pressed.
	 */
	public void openPressed (){
		Window.alert("openPressed");
	}

	/**
	 * this method is invoked when create button on tool-bar is pressed.
	 */
	public void createPressed (){
		long tempId = getApplicationManager().getNextTempOId();
		String name = "New Note";
		String content = "Press edit button to add content.";

		//Create Note object and Dialog for that object
		Note n = new Note();
		n.setTitle(name);
		n.setContent(content);
		n.setId(tempId);

		NoteDialog d = new NoteDialog(n, this);
		noteDialogList.add(d);
		
		
		//Create action
		Action action = new Action();
		action.setActionType(Action.ActionType.NOTE_CREATE);
		action.setObject(n);
		getApplicationManager().getConnectionManager().addAction(action);
		
		//Show Dialog on screen
		d.center();

		ObjectListElement e = new ObjectListElement();
		e.setId(tempId);
		e.setName(name);
		e.setX(d.getPopupLeft());
		e.setY(d.getPopupTop());
		int itemLocation = applicationManager.getSession().placeNote(e);
		applicationManager.getToolbarManager().getNoteListBox().insertItem(name,Long.toString(tempId),itemLocation);

	}

	/**
	 * Method is called automatically after server's create processes finished. it replaces temporary object id
	 * with new note id that is coming from server.
	 * @param note Note that just saved by server.This is the one that was stored by client. 
	 * Not the one that created by server.
	 * @param newObjectId Saved Note's new id.
	 */
	public void postCreate(Note note,long newObjectId){
		int index =applicationManager.getSession().getNoteIndex(note.getId());
		applicationManager.getSession().setNoteId(index, newObjectId);
		applicationManager.getToolbarManager().getNoteListBox().setValue(index, Long.toString(newObjectId));
		note.setId(newObjectId);
	}

	/**
	 * This method is invoked when delete button on tool-bar is pressed.
	 */
	public void deletePressed (){
		int index = applicationManager.getToolbarManager().getNoteListBox().getSelectedIndex();
		String name = applicationManager.getToolbarManager().getNoteListBox().getItemText(index);
		long id = Long.parseLong(applicationManager.getToolbarManager().getNoteListBox().getValue(index));
		if (Window.confirm("Are you sure that you want to delete Note [" + name + "]?")){
			//create new action
			Action action =new Action();
			action.setActionType(Action.ActionType.NOTE_DELETE);
			action.setObjectId(id);
			//action.setToDoList(new ToDoList(id,null,null));
			applicationManager.getConnectionManager().addAction(action);
			//delete from client
			applicationManager.getToolbarManager().getNoteListBox().removeItem(index);
			applicationManager.getSession().removeNote(id);
			//TODO if note is open on screen close dialog. Remove Item from dialoglist in application manager. Do this for to-do List too.
		}
	}

	
	//Getters and Setters
	public EventManager getEventManager(){
		return applicationManager.getEventManager();
	}
	
	public ArrayList<NoteDialog> getNoteDialogList() {
		return noteDialogList;
	}

	public ApplicationManager getApplicationManager() {
		return applicationManager;
	}
	
	public NoteDialog getDialog(long id){
		for(NoteDialog d : noteDialogList){
			if(d.getId() == id)
				return d;
		}
		return null;
	}

	public NoteClickHandler getNoteClickHandler() {
		return noteClickHandler;
	}

	
	
	
}
