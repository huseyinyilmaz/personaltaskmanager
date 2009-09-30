package ptm.client.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Session object holds information about current session.
 * It holds user that opend current session , array of todo lists that current user has. 
 * Array of notes that current user has. It does not hold actual todolist nor note data.
 * It only holds names and ids.
 * @author Huseyin
 *
 */
@SuppressWarnings("serial")
public class Session implements Serializable {
	
	/**
	 * Current users email address
	 */
	private String user;
	
	/**
	 * List of Todolists that current user has. This array only holds
	 * names,oids and location if they are opened.
	 */
	private ArrayList<ObjectListElement> allToDoLists;

	/**
	 * List of notes that current user has. This array only holds
	 * names,oids and location if they are opened.
	 */
	private ArrayList<ObjectListElement> allNotes;
	

	//Constructors

	/**
	 * Create a new session for given user.
	 * @param user
	 */
	public Session(String user){
		this.user = user;
		//toDoList = new ArrayList<ToDoList>();
		allToDoLists = new ArrayList<ObjectListElement>();
		allNotes =  new ArrayList<ObjectListElement>();
		//toDoListLocation = new HashMap<Long,ObjectLocation>();
	}
	
	/**
	 * Do nothing constructor. This is necessary for serializable interface.
	 */
	public Session(){
		allToDoLists = new ArrayList<ObjectListElement>();
		allNotes =  new ArrayList<ObjectListElement>();
	}

	
	//To-do list methods.
	
	/**
	 * Places to-do list alphabetically
	 * @param e to-do list information to place on array
	 * @return new index that given to new to-do list.
	 */
	public int placeToDo(ObjectListElement e){
		//ObjectListElement e = new ObjectListElement(id, name);
		allToDoLists.add(e);
		sortToDo();
		return allToDoLists.indexOf(e);
	}

	/**
	 * Add todolist to end of array
	 * @param e new to-to list to add
	 * @return new location of to-do list.
	 */
	public int addToDo(ObjectListElement e){
		//ObjectListElement e = new ObjectListElement(id, name);
		allToDoLists.add(e);
		return allToDoLists.indexOf(e);
	}
	
	/**
	 * Sorts to-do list array
	 */
	public void sortToDo(){
		Collections.sort(allToDoLists);
	}
	
	
	/**
	 * Removes to-do list from array.
	 * @param id id of to-do list to remove.
	 * @return true if process ended successfully.
	 */
	public boolean removeToDo(long id){
		return allToDoLists.remove(new ObjectListElement(id,""));
		//toDoListLocation.remove(new ObjectLocation(id, -1, -1));
	}
	
	/**
	 * Change given to-do list's id. this is used to change temp oids
	 * @param index
	 * @param newId
	 */
	public void setToDoId(int index,long newId){
		//int index = getToDoIndex(oldId);
		if (index < 0)
			throw new IllegalArgumentException("Index of an ObjectListElement object cannot be a negative number");
		
		ObjectListElement e= allToDoLists.get(index);
		e.setId(newId);
	}
	
	/**
	 * Replace spesific to-do list with another one.
	 * @param index index that old to-do list has.
	 * @param e new to-do list that will have given index.
	 * @return given index.
	 */
	public int setToDo(int index,ObjectListElement e){
		
		if (index < 0)
			throw new IllegalArgumentException("Index of an ObjectListElement object cannot be a negative number");
		ObjectListElement orgElement= allToDoLists.get(index);
		if (orgElement!=e)
			throw new IllegalArgumentException("New ObjectListElements holds different object.");

		allToDoLists.set(index, e);
		return index;
	}
	
	/**
	 * Get spesific to-do list information.
	 * @param id id of to-do list 
	 * @return to-do list that has given id. if there is no such element return null;
	 */
	public ObjectListElement getToDo(long id){
		ObjectListElement e;
		for(int i = 0 ; i<allToDoLists.size();i++){
			e = allToDoLists.get(i);
			if(e.getId()==id)
				return e;
		}
		return null;
	}
	
	/**
	 * returns index of specific to-do list.
	 * @param id id of to-do list we are looking for.
	 * @return index of to-do list if to-do list could not be found, it returns -1
	 */
	public int getToDoIndex(long id){
		int result = -1;
		ObjectListElement e;
		for(int i = 0 ; i<allToDoLists.size();i++){
			e = allToDoLists.get(i);
			if(e.getId()==id){
				result = i;
				break;
			}
		}
		return result;
	}


	
	
	
	//Note methods.
	
	/**
	 * Places note alphabetically
	 * @param e note information to place on array
	 * @return new index that given to new note.
	 */
	public int placeNote(ObjectListElement e){
		allNotes.add(e);
		sortNote();
		return allNotes.indexOf(e);
	}

	/**
	 * Add note to end of array
	 * @param e new note to add
	 * @return location of note on array.
	 */
	public int addNote(ObjectListElement e){
		allNotes.add(e);
		return allNotes.indexOf(e);
	}
	
	/**
	 * Sorts notes array
	 */
	public void sortNote(){
		Collections.sort(allNotes);
	}
	
	
	/**
	 * Removes given note from array.
	 * @param id id of note to remove.
	 * @return true if process ended successfully.
	 */
	public boolean removeNote(long id){
		return allNotes.remove(new ObjectListElement(id,""));
	}
	
	/**
	 * Change given note's id. This is used to change temp ids
	 * @param index index of Note that will get new id.
	 * @param newId new id to set.
	 */
	public void setNoteId(int index,long newId){
		if (index < 0)
			throw new IllegalArgumentException("Index of an ObjectListElement object cannot be a negative number("+index+")");
		
		ObjectListElement e= allNotes.get(index);
		e.setId(newId);
	}
	
	/**
	 * Replace specific note with another one.
	 * @param index index that old note has.
	 * @param e new to-do list that will have given index.
	 * @return given index.
	 */
	public int setNote(int index,ObjectListElement e){
		
		if (index < 0)
			throw new IllegalArgumentException("Index of an ObjectListElement object cannot be a negative number");
		ObjectListElement orgElement= allNotes.get(index);
		if (orgElement!=e)
			throw new IllegalArgumentException("New ObjectListElements holds different object.");

		allNotes.set(index, e);
		return index;
	}
	
	/**
	 * Get specific note information.
	 * @param id id of note. 
	 * @return note that has given id. if there is no such element return null;
	 */
	public ObjectListElement getNote(long id){
		ObjectListElement e;
		for(int i = 0 ; i<allNotes.size();i++){
			e = allNotes.get(i);
			if(e.getId()==id)
				return e;
		}
		return null;
	}
	
	/**
	 * returns index of specific note.
	 * @param id id of note we are looking for.
	 * @return index of note if note could not be found, it returns -1
	 */
	public int getNoteIndex(long id){
		int result = -1;
		ObjectListElement e;
		for(int i = 0 ; i<allNotes.size();i++){
			e = allNotes.get(i);
			if(e.getId()==id){
				result = i;
				break;
			}
		}
		return result;
	}
	
	//Getters and Setters
	public String getUser() {
		return user;
	}

	public ArrayList<ObjectListElement> getAllToDoLists(){
		return allToDoLists;
	}

	public ArrayList<ObjectListElement> getAllNotes() {
		return allNotes;
	}
	
	
}
