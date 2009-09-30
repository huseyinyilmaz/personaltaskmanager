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
	public boolean add(Action arg0) {
		boolean result = false;
		boolean needNewAction;
		//process action according to action type
		switch(arg0.getActionType()){

		case TODO_DELETE:
			//remove other actions on this todolist.
			for (Iterator<Action> itr = iterator();itr.hasNext();){
				Action a= itr.next();
				if(	(a.getObjectId() == arg0.getObjectId()) ||
					(a.getActionType() == Action.ActionType.TODO_CREATE &&//Create todo sends object to server
					 arg0.getObjectId() == ((ToDoList) a.getObject()).getId()) ){
					itr.remove();
				}
				
			}

			//if there is no server object for that to-do list we don't have to delete it
			if(arg0.getObjectId()>=0)
				result = super.add(arg0);
			break;
			
		case TODO_EDIT:
			if(arg0.getObjectId()<0){//object still has temporary id.
				for(Iterator<Action> itr = iterator();itr.hasNext();){
					Action a=itr.next();
					// action is temporary so we need to find create action and change string value
					// we do not need to add a new action in this case.
					if( a.getActionType() == Action.ActionType.TODO_CREATE &&
						((ToDoList)a.getObject()).getId() == arg0.getObjectId()){
						
						((ToDoList)a.getObject()).setName(arg0.getString());
						break;//breaks for statement
					}
				}//for
			}else{
				needNewAction = true;
				for(Iterator<Action> itr = iterator();itr.hasNext();){
					Action a=itr.next();
					if( a.getObjectId() == arg0.getObjectId() && 
						a.getActionType() == arg0.getActionType()){
						
						a.setString(arg0.getString());
						needNewAction = false;
						break;//breaks for statement
					}
				}//for
				
				if(needNewAction)
					result = super.add(arg0);
			}
			break;
		
		case TODO_MOVE:
			needNewAction = true;
			for(Iterator<Action> itr = iterator();itr.hasNext();){
				Action a=itr.next();
				if( a.getActionType() == arg0.getActionType() &&
					a.getObject() == arg0.getObject()){
					//we already have a reference to this object.
					needNewAction = false;
					break;//breaks for statement
				}
			}//for
			
			if(needNewAction)
				result = super.add(arg0);
		break;
		case TASK_DELETE:
			//remove other actions on this task.
			for (Iterator<Action> itr = iterator();itr.hasNext();){
				Action a= itr.next();
				if(	(a.getActionType() == Action.ActionType.TASK_CREATE ||
					 a.getActionType() == Action.ActionType.TASK_EDIT )
				&&	(Task)a.getObject() == (Task)arg0.getObject() 
				){
					
					itr.remove();
				}
			}
			//if there is no server object for that task we don't have to delete it
			if(((Task)arg0.getObject()).getId()>=0)
				result = super.add(arg0);
			break;

		case TASK_EDIT:	
			if(((Task)arg0.getObject()).getId()<0){//object still has temporary id.
				for(Iterator<Action> itr = iterator();itr.hasNext();){
					Action a=itr.next();
					// action is temporary so we need to find create action and change string value
					// we do not need to add a new action in this case.
					if( a.getActionType() == Action.ActionType.TASK_CREATE &&
						(Task)a.getObject() == (Task)arg0.getObject()){
						
						((Task)a.getObject()).setContent( ((Task)arg0.getObject()).getContent() );
						((Task)a.getObject()).setDueDate(((Task)arg0.getObject()).getDueDate() );
						((Task)a.getObject()).setIsDone(((Task)arg0.getObject()).getIsDone() );
						
						break;//breaks for statement
					}
				}//for
			}else{//object has real id
				needNewAction = true;
				for(Iterator<Action> itr = iterator();itr.hasNext();){
					Action a=itr.next();
					if( a.getObject() == arg0.getObject() && 
						a.getActionType() == arg0.getActionType()){
						
						((Task)a.getObject()).setContent( ((Task)arg0.getObject()).getContent() );
						((Task)a.getObject()).setDueDate(((Task)arg0.getObject()).getDueDate() );
						((Task)a.getObject()).setIsDone(((Task)arg0.getObject()).getIsDone() );

						needNewAction = false;
						break;//breaks for statement
					}
				}//while
				
				if(needNewAction)
					result = super.add(arg0);
			}
			break;
		case NOTE_DELETE:
			//remove other actions on this note.
			for (Iterator<Action> itr = iterator();itr.hasNext();){
				Action a= itr.next();
				if(	(a.getActionType() == Action.ActionType.NOTE_CREATE ||
					 a.getActionType() == Action.ActionType.NOTE_EDIT )
				&&	((Note)a.getObject()).getId() == arg0.getObjectId()
				){
					itr.remove();
				}
			}
			
			//if there is no server object for that note we don't have to delete it
			if(arg0.getObjectId()>=0)
				result = super.add(arg0);
			break;

			
		default:
			//for the rest of the actions just add new action.
			result = super.add(arg0);	
			break;
		}//switch
		
		GWT.log("Action["+ arg0.getActionId() +"] was added.Type = "+arg0.getActionType() + " added.", null);
		GWT.log("Action Queue size =" + (size()),null);
		return result;
	}
	
	

}
