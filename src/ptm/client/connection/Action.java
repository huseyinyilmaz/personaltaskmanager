package ptm.client.connection;
import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Action implements Serializable{
	public enum ActionType {
		TODO_CREATE,TODO_EDIT,TODO_DELETE,TODO_MOVE,TODO_OPEN,TODO_CLOSE,
		NOTE_CREATE,NOTE_EDIT,NOTE_DELETE,NOTE_MOVE,NOTE_OPEN,NOTE_CLOSE,
		TASK_CREATE,TASK_EDIT,TASK_DELETE
	}
	private static long actionIdHolder = 1;
	private long actionId;
	private ActionType actionType;
	private long objectId;
	private int x;
	private int y;
	private String string;
	private Date date;
	private boolean isChecked;
	//private ToDoList toDoList;
	//private Task toDoListElement;
	private Serializable object;
	//Constructor
	public Action(){
		actionId = actionIdHolder;
		actionIdHolder++;
	}
	public Action(long actionId){
		this.actionId=actionId;
	}
	//equals and hascode
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (actionId ^ (actionId >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Action other = (Action) obj;
		if (actionId != other.actionId)
			return false;
		return true;
	}
	//Getters and setters
	public long getActionId() {
		return actionId;
	}
	/*public void setActionId(int actionId) {
		this.actionId = actionId;
	}*/
	public ActionType getActionType() {
		return actionType;
	}
	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	public Serializable getObject() {
		return object;
	}
	
	public void setObject(Serializable object) {
		this.object = object;
	}

	public long getObjectId() {
		return objectId;
	}
	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public String getString() {
		return string;
	}
	public void setString(String string) {
		this.string = string;
	}
	
}
