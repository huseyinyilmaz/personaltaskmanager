/**
 * Keeps ptm list tasks.
 */
package ptm.server.datamodel;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Task {
    //fields
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private Text content;

    @Persistent
    private Date creationDate;
    
    @Persistent
    private Date checkedDate;

    @Persistent
    private Date dueDate;
    
    @Persistent
    private Boolean isDone;

    
	public ptm.client.datamodel.Task toClientObject(){
		ptm.client.datamodel.Task clientObject = new ptm.client.datamodel.Task();
		
		clientObject.setContent(this.getContent());
		clientObject.setIsDone(this.getIsDone());
		clientObject.setId(getKey().getId());
		clientObject.setCheckedDate(this.getCheckedDate());
		clientObject.setCreationDate(this.getCreationDate());
		clientObject.setDueDate(this.getDueDate());
		return clientObject;
	}

    
    //Constructors
	public Task(String content,Date dueDate,boolean isDone) {
		setContent(content);
		setDueDate(dueDate);
		setIsDone(isDone);
		this.creationDate = new Date();
	}

	public Task(ptm.client.datamodel.Task clientObject){
		setValues(clientObject);
		this.creationDate = new Date();
	}
	
	public void setValues(ptm.client.datamodel.Task clientObject){
		setContent(clientObject.getContent());
		setIsDone(clientObject.getIsDone());
		setDueDate(clientObject.getDueDate());
	}
	
	//Getter and setters
	public String getContent() {
		return content.getValue();
	}
	public void setContent(String content) {
		this.content = new Text(content);
	}
	public Boolean getIsDone() {
		return isDone;
	}
	public void setIsDone(Boolean isDone) {
		this.isDone = isDone;
		if(isDone)
			this.checkedDate = new Date();
		else
			this.checkedDate = null;
	}
	public Key getKey() {
		return key;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public Date getCheckedDate() {
		return checkedDate;
	}
	public void setContent(Text content) {
		this.content = content;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

}