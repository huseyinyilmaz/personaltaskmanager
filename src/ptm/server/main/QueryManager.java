package ptm.server.main;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import ptm.client.exception.PersistanceManagerException;
import ptm.server.datamodel.Note;
import ptm.server.datamodel.PMF;
import ptm.server.datamodel.Task;
import ptm.server.datamodel.ToDoList;


import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;

/**
 * This Class has static query methods to manipulate database.
 * @author Huseyin
 *
 */
public class QueryManager {
	//private static final Logger log = Logger.getLogger(QueryManager.class.getName());

	
	/**
	 * Gets session from database.
	 * @param user current user
	 * @return client side session object of current user.
	 */
	@SuppressWarnings("unchecked")
	public static ptm.client.datamodel.Session getSession(User user){
		//create return object
		ptm.client.datamodel.Session session = new ptm.client.datamodel.Session(user.getEmail());
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		//get ToDoList Objects from server
		Query query = pm.newQuery(ToDoList.class);
		query.setFilter("owner == currentUser");
		query.declareParameters("com.google.appengine.api.users.User currentUser");
		List<ToDoList> toDoList = (List<ToDoList>) query.execute(user);
		for(ToDoList list: toDoList){
			ptm.client.datamodel.ObjectListElement e = new ptm.client.datamodel.ObjectListElement();
			e.setId(list.getId());
			e.setName(list.getName());
			e.setX(list.getX());
			e.setY(list.getY());
			e.setOpen(list.isOpen());
			session.addToDo(e);
		}
		
		//get Note Objects from server
		query = pm.newQuery(Note.class);
		query.setFilter("owner == currentUser");
		query.declareParameters("com.google.appengine.api.users.User currentUser");
		List<Note> notes = (List<Note>) query.execute(user);
		for(Note note: notes){
			ptm.client.datamodel.ObjectListElement e = new ptm.client.datamodel.ObjectListElement();
			e.setId(note.getId());
			e.setName(note.getTitle());
			e.setX(note.getX());
			e.setY(note.getY());
			e.setOpen(note.isOpen());
			session.addNote(e);
		}

		
		return session;
		
	}
	
	public static ToDoList createToDo( ptm.client.datamodel.ToDoList clientList, User owner) throws PersistanceManagerException{
		PersistenceManager pm = PMF.get().getPersistenceManager();

        ToDoList list = new ToDoList(clientList.getName(),owner);
        list.setOpen(true);
        try {
            pm.makePersistent(list);
        }catch(Exception e){
        	PersistanceManagerException newE = new PersistanceManagerException("ToDoList [" + clientList.getName() + "] could not be saved",e);
        	throw newE;
        } finally {
            pm.close();
        }
        return list;
	}
	
	public static void deleteToDo(long id) throws PersistanceManagerException{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ToDoList object = pm.getObjectById(ToDoList.class,id);
        try {
        	pm.deletePersistent(object);
        }catch(Exception e){
        	PersistanceManagerException newE = new PersistanceManagerException("ToDoList [" + object.getName() + "] could not be Deleted",e);
        	throw newE;
        } finally {
            pm.close();
        }
	}

	
	public static void editToDo(long id,String newName) throws PersistanceManagerException{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ToDoList object = pm.getObjectById(ToDoList.class,id);
		object.setName(newName);
        try {
        	pm.makePersistent(object);
        }catch(Exception e){
        	PersistanceManagerException newE = new PersistanceManagerException("ToDoList [" + object.getName() + "] could not be Deleted",e);
        	throw newE;
        } finally {
            pm.close();
        }
	}

	public static ToDoList getToDo(long id){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ToDoList object = pm.getObjectById(ToDoList.class,id);
		return object;
		
	}

	public static ToDoList openToDo(long id) throws PersistanceManagerException{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ToDoList object = pm.getObjectById(ToDoList.class,id);
		object.setOpen(true);
        try {
        	pm.makePersistent(object);
        }catch(Exception e){
        	PersistanceManagerException newE = new PersistanceManagerException("ToDoList [" + object.getName() + "] could not be Deleted",e);
        	throw newE;
        } finally {
            pm.close();
        }
        //object = pm.getObjectById(ToDoList.class,id);
        return getToDo(id);
	}
	
	public static void closeToDo(long id) throws PersistanceManagerException{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ToDoList object = pm.getObjectById(ToDoList.class,id);
		object.setOpen(false);
        try {
        	pm.makePersistent(object);
        }catch(Exception e){
        	PersistanceManagerException newE = new PersistanceManagerException("ToDoList [" + object.getName() + "] could not be Deleted",e);
        	throw newE;
        } finally {
            pm.close();
        }
	}
	
	
	public static long createTask(long toDoListId,ptm.client.datamodel.Task clientTask)throws PersistanceManagerException{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ToDoList toDoList = pm.getObjectById(ToDoList.class,toDoListId);
		Task element = new Task(clientTask);//new Task(content,dueDate,isDone);
		toDoList.add(element);
		try {
        	pm.makePersistent(toDoList);
        }catch(Exception e){
        	PersistanceManagerException newE = new PersistanceManagerException("New Task could not be created on to-doList [" + toDoList.getName() + "]",e);
        	throw newE;
        } finally {
            pm.close();
        }
        return element.getKey().getId(); 
	}
	
	public static boolean deleteTask(long toDoListId,ptm.client.datamodel.Task task)throws PersistanceManagerException{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ToDoList toDoList = pm.getObjectById(ToDoList.class,toDoListId);
		Task t = toDoList.getTask(task.getId());
		boolean result = toDoList.deleteTask(t.getKey().getId());
		//ArrayList<Task> list = toDoList.getList();
		//boolean result = list.remove(t);
		try {
        	pm.makePersistent(toDoList);
        }catch(Exception e){
        	PersistanceManagerException newE = new PersistanceManagerException("Task could not be deleted ("+task.getContent()+")",e);
        	throw newE;
        } finally {
            pm.close();
        }
        return result;
	}

	public static void editTask(long toDoListId,ptm.client.datamodel.Task task)throws PersistanceManagerException{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ToDoList toDoList = pm.getObjectById(ToDoList.class,toDoListId);
		toDoList.getTask(task.getId()).setValues(task);
		try {
        	pm.makePersistent(toDoList);
        }catch(Exception e){
        	PersistanceManagerException newE = new PersistanceManagerException("Task could not be deleted ("+task.getContent()+")",e);
        	throw newE;
        } finally {
            pm.close();
        }

	}

	public static void moveToDo(ptm.client.datamodel.ObjectListElement element) throws PersistanceManagerException{

		PersistenceManager pm = PMF.get().getPersistenceManager();
		ToDoList toDoList = pm.getObjectById(ToDoList.class,element.getId());
		toDoList.setX(element.getX());
		toDoList.setY(element.getY());

		try {
        	pm.makePersistent(toDoList);
        }catch(Exception e){
        	PersistanceManagerException newE = new PersistanceManagerException("Error while saving to-do list [" + element.getName() + "]location data",e);
        	throw newE;
        } finally {
            pm.close();
        }
	
	}

	//Note Methods
	
	/**
	 * Creates a new server side note object and saves it to data store.
	 * @param clientObject client side note object(ptm.client.datamodel.Note) to save.
	 * New server side note object is created using the data this object has.
	 * @param owner owner of this object which is also current user.
	 * @return returns new server side object that created and saved by this methdo.
	 * @throws PersistanceManagerException if there is an error while saving object we throw this exception. this exception goes to
	 * client and shown to user.
	 */
	public static Note createNote( ptm.client.datamodel.Note clientObject, User owner) throws PersistanceManagerException{
		PersistenceManager pm = PMF.get().getPersistenceManager();

        Note note = new Note(new Text(clientObject.getContent()),clientObject.getTitle(),owner);
        note.setOpen(true);
        try {
            pm.makePersistent(note);
        }catch(Exception e){
        	PersistanceManagerException newE = new PersistanceManagerException("Note [" + clientObject.getTitle() + "] could not be saved",e);
        	throw newE;
        } finally {
            pm.close();
        }
        return note;
	}
	
	
	public static void deleteNote(long id) throws PersistanceManagerException{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Note object = pm.getObjectById(Note.class,id);
        try {
        	pm.deletePersistent(object);
        }catch(Exception e){
        	PersistanceManagerException newE = new PersistanceManagerException("Note [" + object.getTitle() + "] could not be deleted",e);
        	throw newE;
        } finally {
            pm.close();
        }
	}


}