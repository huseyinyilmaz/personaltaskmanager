package ptm.client.datamodel;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ObjectListElement implements Serializable,Comparable<ObjectListElement>{
	
	//Object id
	private long id;
	
	//Name of the object (title)
	String name;
	
	//Location of the object
	int x,y;
	
	//is Object open on screen
	boolean isOpen;
	
	//Constructors
	public ObjectListElement(){
		//isOpen = true;
	}
	public ObjectListElement(long id, String name) {
		this.id = id;
		this.name = name;
		//isOpen = true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ObjectListElement o) {
		return this.getName().compareTo(o.getName());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObjectListElement other = (ObjectListElement) obj;
		if (id != other.id)
			return false;
		return true;
	}


	//Getters and Setters
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
