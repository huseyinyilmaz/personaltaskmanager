package ptm.client.connection;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import ptm.client.datamodel.Note;
import ptm.client.datamodel.Task;
import ptm.client.datamodel.ToDoList;


import com.google.gwt.core.client.GWT;

@SuppressWarnings("serial")
public class ActionQueue extends LinkedList<Action> implements Serializable {

	//constructors
	public ActionQueue() {
		super();
	}

	public ActionQueue(Collection<? extends Action> arg0) {
		super(arg0);
	}

	@Override
	public boolean add(Action action) {
		boolean result = false;
		boolean needNewAction;
		//process action according to action type
		switch(action.getActionType()){

		case TODO_DELETE:
			//remove other actions on this todolist.
			for (Iterator<Action> itr = iterator();itr.hasNext();){
				Action a= itr.next();
				if(	(a.getObjectId() == action.getObjectId()) ||
					(a.getActionType() == Action.ActionType.TODO_CREATE &&//Create todo sends object to server
					 action.getObjectId() == ((ToDoList) a.getObject()).getId()) ){
					itr.remove();
				}
				
			}

			//if there is no server object for that to-do list we don't have to delete it
			if(action.getObjectId()>=0)
				result = super.add(action);
			break;
			
		case TODO_EDIT:
			if(action.getObjectId()<0){//object still has temporary id.
				for(Iterator<Action> itr = iterator();itr.hasNext();){
					Action a=itr.next();
					// action is temporary so we need to find create action and change string value
					// we do not need to add a new action in this case.
					if( a.getActionType() == Action.ActionType.TODO_CREATE &&
						((ToDoList)a.getObject()).getId() == action.getObjectId()){
						
						((ToDoList)a.getObject()).setName(action.getString());
						break;//breaks for statement
					}
				}//for
			}else{
				needNewAction = true;
				for(Iterator<Action> itr = iterator();itr.hasNext();){
					Action a=itr.next();
					if( a.getObjectId() == action.getObjectId() && 
						a.getActionType() == action.getActionType()){
						
						a.setString(action.getString());
						needNewAction = false;
						break;//breaks for statement
					}
				}//for
				
				if(needNewAction)
					result = super.add(action);
			}
			break;
		
		case TODO_MOVE:
			needNewAction = true;
			for(Iterator<Action> itr = iterator();itr.hasNext();){
				Action a=itr.next();
				if( a.getActionType() == action.getActionType() &&
					a.getObject() == action.getObject()){
					//we already have a reference to this object.
					needNewAction = false;
					break;//breaks for statement
				}
			}//for
			
			if(needNewAction)
				result = super.add(action);
		break;
		case TASK_DELETE:
			//remove other actions on this task.
			for (Iterator<Action> itr = iterator();itr.hasNext();){
				Action a= itr.next();
				if(	(a.getActionType() == Action.ActionType.TASK_CREATE ||
					 a.getActionType() == Action.ActionType.TASK_EDIT )
				&&	(Task)a.getObject() == (Task)action.getObject() 
				){
					
					itr.remove();
				}
			}
			//if there is no server object for that task we don't have to delete it
			if(((Task)action.getObject()).getId()>=0)
				result = super.add(action);
			break;

		case TASK_EDIT:	
			if(((Task)action.getObject()).getId()<0){//object still has temporary id.
				for(Iterator<Action> itr = iterator();itr.hasNext();){
					Action a=itr.next();
					// action is temporary so we need to find create action and change string value
					// we do not need to add a new action in this case.
					if( a.getActionType() == Action.ActionType.TASK_CREATE &&
						(Task)a.getObject() == (Task)action.getObject()){
						
						((Task)a.getObject()).setContent( ((Task)action.getObject()).getContent() );
						((Task)a.getObject()).setDueDate(((Task)action.getObject()).getDueDate() );
						((Task)a.getObject()).setIsDone(((Task)action.getObject()).getIsDone() );
						
						break;//breaks for statement
					}
				}//for
			}else{//object has real id
				needNewAction = true;
				for(Iterator<Action> itr = iterator();itr.hasNext();){
					Action a=itr.next();
					if( a.getObject() == action.getObject() && 
						a.getActionType() == action.getActionType()){
						
						((Task)a.getObject()).setContent( ((Task)action.getObject()).getContent() );
						((Task)a.getObject()).setDueDate(((Task)action.getObject()).getDueDate() );
						((Task)a.getObject()).setIsDone(((Task)action.getObject()).getIsDone() );

						needNewAction = false;
						break;//breaks for statement
					}
				}//while
				
				if(needNewAction)
					result = super.add(action);
			}
			break;
		case NOTE_DELETE:
			//remove other actions on this note.
			for (Iterator<Action> itr = iterator();itr.hasNext();){
				Action a= itr.next();
				if(	(a.getActionType() == Action.ActionType.NOTE_CREATE ||
					 a.getActionType() == Action.ActionType.NOTE_EDIT )
				&&	((Note)a.getObject()).getId() == action.getObjectId()
				){
					itr.remove();
				}
			}
			
			//if there is no server object for that note we don't have to delete it
			if(action.getObjectId()>=0)
				result = super.add(action);
			break;
		case NOTE_EDIT:
			/* Object still has temporary id. Do not need to add it again.
			 * Create note action already has a reference to this object.
			 * when we sand create action we will send latest version of this
			 * object.
			 * */
			if( ((Note) action.getObject()).getId()>0){
				needNewAction = true;
				for(Iterator<Action> itr = iterator();itr.hasNext();){
					Action a=itr.next();
					if( a.getObject() == action.getObject() && 
						a.getActionType() == action.getActionType()){
						needNewAction = false;
						break;//breaks for statement
					}
				}//for
				
				if(needNewAction)
					result = super.add(action);
			}
			break;
			
		case NOTE_MOVE:
			needNewAction = true;
			for(Iterator<Action> itr = iterator();itr.hasNext();){
				Action a=itr.next();
				if( a.getActionType() == action.getActionType() &&
					a.getObject() == action.getObject()){
					//we already have a reference to this object.
					needNewAction = false;
					break;//breaks for statement
				}
			}//for
			
			if(needNewAction)
				result = super.add(action);
		break;

			
		default:
			//for the rest of the actions just add new action.
			result = super.add(action);	
			break;
		}//switch
		
		GWT.log("Action["+ action.getActionId() +"] was added.Type = "+action.getActionType() + " added.", null);
		GWT.log("Action Queue size =" + (size()),null);
		return result;
	}
	
	

}
