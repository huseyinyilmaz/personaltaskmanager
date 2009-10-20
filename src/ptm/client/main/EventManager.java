package ptm.client.main;

import ptm.client.todolist.ToDoListDialog;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable;

/**
 * This class is used by every component in application.
 * When an event occur, this class takes it and calls responsible object.
 * @author huseyin
 *
 */
public class EventManager implements ClickHandler {
	
	
	/**
	 * Manager of the application.
	 */
	private ApplicationManager applicationManager;

	
	/**
	 * Creates a new event manager.
	 * @param applicationManager Manager of the application which is also creator of the EventManager object
	 */
	public EventManager(ApplicationManager applicationManager){
		this.applicationManager = applicationManager;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
	 */
	@Override
	public void onClick(ClickEvent event) {
		//Toolbar buttons
		if ((Widget)event.getSource() == applicationManager.getToolbarManager().getGeneralLogoutButton()){
			if (Window.confirm("Are you sure you want to logout from ptm?"))
				applicationManager.getConnectionManager().logout();
		}else if ((Widget)event.getSource() == applicationManager.getToolbarManager().getGeneralSyncButton()){
			applicationManager.getConnectionManager().sync();
			return;
		}
		//TodoListButtons
		else if ((Widget)event.getSource() == applicationManager.getToolbarManager().getCreateTodoButton()){
			applicationManager.getTodoListManager().createPressed();
			return;
		}else if ((Widget)event.getSource() == applicationManager.getToolbarManager().getEditTodoButton()){
			applicationManager.getTodoListManager().editPressed();
			return;
		}else if ((Widget)event.getSource() == applicationManager.getToolbarManager().getDeleteTodoButton()){
			applicationManager.getTodoListManager().deletePressed();
			return;
		}else if ((Widget)event.getSource() == applicationManager.getToolbarManager().getOpenTodoButton()){
			applicationManager.getTodoListManager().openPressed();
			return;
		}
		//Note Buttons
		else if ((Widget)event.getSource() == applicationManager.getToolbarManager().getCreateNoteButton()){
			applicationManager.getNoteManager().createPressed();
			return;
		}else if ((Widget)event.getSource() == applicationManager.getToolbarManager().getDeleteNoteButton()){
			applicationManager.getNoteManager().deletePressed();
			return;
		}else if ((Widget)event.getSource() == applicationManager.getToolbarManager().getOpenNoteButton()){
			applicationManager.getNoteManager().openPressed();
			return;
		}

		//check if it is a todolistbutton
		for(ToDoListDialog d:applicationManager.getTodoListManager().getToDoListDialogs()){
			if ((Widget)event.getSource() == d.getCloseButton()){
				d.closePressed();
				return;
			}else if ((Widget)event.getSource() == d.getNewButton()){
				d.createPressed();
				return;
			}else if ((Widget)event.getSource() == d.getEditButton()){
				d.editPressed();
				return;
			}else if ((Widget)event.getSource() == d.getDeleteButton()){
				d.deletePressed();
				return;
			}else if ((Widget)event.getSource() == d.getOkButton()){
				d.okPressed();
				return;
			}else if ((Widget)event.getSource() == d.getCancelButton()){
				d.cancelPressed();
				return;
			}

			else if ((Widget)event.getSource() == d.getTaskTable()){
				d.setCurrentRow(((FlexTable)(event.getSource())).getCellForEvent(event).getRowIndex());
				d.evaluateButtonStatus();
				return;
			}

		}//for
		
	}

}
