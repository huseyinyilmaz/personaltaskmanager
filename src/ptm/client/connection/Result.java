package ptm.client.connection;

import java.io.Serializable;

import ptm.client.datamodel.Note;
import ptm.client.datamodel.ToDoList;
import ptm.client.exception.ScratchpadException;



@SuppressWarnings("serial")
public class Result implements Serializable {
	private boolean successfull;
	private long actionId;
	private long objectId;
	private ToDoList toDoList;
	private Note note;
	private ScratchpadException exception;
	
	//Getters and Setters
	public ScratchpadException getException() {
		return exception;
	}
	public void setException(ScratchpadException exception) {
		this.exception = exception;
	}
	public boolean isSuccessfull() {
		return successfull;
	}
	public void setSuccessfull(boolean successfull) {
		this.successfull = successfull;
	}
	public long getActionId() {
		return actionId;
	}
	public void setActionId(long actionId) {
		this.actionId = actionId;
	}
	public ToDoList getToDo() {
		return toDoList;
	}
	public void setToDo(ToDoList toDo) {
		this.toDoList = toDo;
	}
	public Note getNote() {
		return note;
	}
	public void setNote(Note note) {
		this.note = note;
	}
	public long getObjectId() {
		return objectId;
	}
	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}
	
	
}
