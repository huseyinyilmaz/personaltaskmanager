/**
 * 
 */
package ptm.client.todolist;

import java.util.Collections;

import ptm.client.connection.Action;
import ptm.client.datamodel.Task;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;

/**
 * This Class Handles TaskCheckboxes
 * @author Huseyin
 *
 */
public class TaskClickHandler implements ClickHandler {
	//Dialog that this handler belongs to.
	private ToDoListDialog toDoListDialog;
	
	
	/**
	 * Creates new click handler which is used by given to-do list.
	 * @param toDoListDialog
	 */
	public TaskClickHandler(ToDoListDialog toDoListDialog){
		super();
		this.toDoListDialog = toDoListDialog;
	}
	/* (non-Javadoc)
	 * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
	 */
	@Override
	public void onClick(ClickEvent event) {
		for(int i=0;i<toDoListDialog.getToDolist().getList().size();i++){
			if (toDoListDialog.getTaskTable().getWidget(i, 0) == (CheckBox)event.getSource()){
				
				Task task = toDoListDialog.getToDolist().getList().get(i);
				//set Task value
				task.setIsDone(((CheckBox)event.getSource()).getValue());
				Collections.sort(toDoListDialog.getToDolist().getList());
				int newLoc = toDoListDialog.getToDolist().getList().indexOf(task);
				if ( newLoc!=i ){
					toDoListDialog.setCurrentRow(-1);
					toDoListDialog.getTaskTable().removeRow(i);
					toDoListDialog.addTaskTableRow(newLoc, task);
				}
				
				Action action = new Action();
				action.setActionType(Action.ActionType.TASK_EDIT);
				action.setObjectId(toDoListDialog.getId());
				action.setObject(task);
				
				toDoListDialog.getToDoListManager().getApplicationManager().getConnectionManager().addAction(action);
				break;//for
			}
		}
	}

}
