/**
 * Keepts a Todo list
 */
package ptm.server.datamodel;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;


/**
 * JDO class that holds to-do list information.
 * @author huseyin
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ToDoList{
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key id;

    @Persistent
    private User owner;

    @Persistent
    private LinkedList<Task> list;
    
    @Persistent
    private String name;

    @Persistent
    private Date creationDate;

    @Persistent
    private int x,y;
    
    @Persistent
    private boolean isOpen;
   
    
	//Constructors
	public ToDoList(String name, User owner) {
		this.name = name;
		this.owner = owner;
		this.creationDate = new Date();
		this.list = new LinkedList<Task>();
	}
	
	public ToDoList(ptm.client.datamodel.ToDoList clientList,User owner){
		this.name = clientList.getName();
		this.owner = owner;
		this.creationDate = new Date();
		
		for (ptm.client.datamodel.Task e : clientList.getList()){
			add(e.getContent(),e.getDueDate(),e.getIsDone());
		}
		
		this.list = new LinkedList<Task>();
	}
	
	public boolean add(String content,Date dueDate,boolean isDone){
		return list.add(new Task(content,dueDate,isDone));
	}
	public boolean add(Task task){
		return list.add(task);
	}
	
	public ptm.client.datamodel.ToDoList toClientObject(){
		ptm.client.datamodel.ToDoList clientObject = new ptm.client.datamodel.ToDoList();
		clientObject.setId(getId());
		clientObject.setName(getName());
		clientObject.setCreationDate(getCreationDate());
		
		for (Task e : getList() ){
			clientObject.addElement(e.toClientObject());
		}
		return clientObject;
	}

	public Task getTask(long taskId){
		for(Task task:list){
			if (task.getKey().getId() == taskId)
				return task;
		}
		return null;
	}
	
	public boolean deleteTask(long taskId){
		Iterator<Task> i = list.iterator();
		while(i.hasNext()){
			Task t = i.next();
			if(t.getKey().getId() == taskId){
				i.remove();
				return true;
			}
		}
		
		return false;
	}
	
	//Getters And Setters
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getId() {
		return id.getId();
	}
	public List<Task> getList() {
		return list;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
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

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	
	
}
