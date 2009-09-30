package ptm.client.datamodel;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Note implements Serializable {

	private long id;
	private String title;
	private String content;
	private Date creationDate;

	//Constructors
	
	/**
	 * This is do nothing constructor. It only creates the object.
	 */
	public Note(){}
	
	/**
	 * Creates a new Note Object with given id, name and content. 
	 * @param id id of the note.
	 * @param name title of the note.
	 * @param content content of the note.
	 */
	public Note(long id, String name, String content){
		this.id = id;
		this.title = name;
		this.content = content;
		creationDate = new Date(); 
	}
	
	//Getters and setters
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		Note other = (Note) obj;
		if (id != other.id)
			return false;
		return true;
	}

	
	
	//Setters and Getters
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String name) {
		this.title = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	
}
