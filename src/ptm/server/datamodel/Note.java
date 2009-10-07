package ptm.server.datamodel;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.users.User;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Note {
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key id;

    @Persistent
    private User owner;
    
    @Persistent
    private String title;

    @Persistent
    private Text content;

    @Persistent
    private Date creationDate;

    @Persistent
    private Date modifyDate;
    
    @Persistent
    private boolean isOpen;

    @Persistent
    private int x,y;
 
 	//Constructors
	/**
	 * @param content
	 * @param title
	 * @param owner
	 */
	public Note(Text content, String title, User owner) {
		this.content = content;
		this.title = title;
		this.owner = owner;
		this.creationDate = new Date();
		this.modifyDate = this.creationDate;
	}

	/**
	 * Creates a new Client side note object from current server side Note Object
	 * @return client side note object that represents current server side note object.
	 */
	public ptm.client.datamodel.Note toClientObject(){
		ptm.client.datamodel.Note clientObj = new ptm.client.datamodel.Note();
		
		clientObj.setId(getId());
		clientObj.setCreationDate(getCreationDate());
		clientObj.setTitle(getTitle());
		clientObj.setContent(getContent().getValue());
		
		return clientObj;
	}
	
	//Getters and Setters
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Text getContent() {
		return content;
	}

	public void setContent(Text content) {
		this.content = content;
		this.modifyDate = new Date();
	}

	public long getId() {
		return id.getId();
	}

	public User getOwner() {
		return owner;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}
	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
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
	
    
}
