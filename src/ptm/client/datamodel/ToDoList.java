package ptm.client.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@SuppressWarnings("serial")
public class ToDoList implements Serializable {
	private ArrayList<Task> list = new ArrayList<Task>();
	private long id;
	private String name;
	private Date creationDate;
	

	public boolean addElement(long id,String content){
		return addElement(new Task(id,content));
	}
	
	public boolean addElement(Task e){
		return list.add(e);
	}
	
	public boolean removeElement(Task e){
		return list.remove(e);
	}
	
	public boolean setElementId(long oldId,long newId){
		for(Task e : list){
			if (e.getId()== oldId){
				e.setId(newId);
				return true;
			}
		}
		return false;
	}
	/*Constructors*/
	public ToDoList(){}
	public ToDoList(long id, String name, ArrayList<Task> list) {
		if (list == null)
			list = new ArrayList<Task>();
		this.id = id;
		this.name = name;
		this.list = list;
	}
	
	/*Setter - Getter*/
	public void setList(ArrayList<Task> list) {
		this.list = list;
	}
	public ArrayList<Task> getList() {
		return list;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getId() {
		return id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
}
