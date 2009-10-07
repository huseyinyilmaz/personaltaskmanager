/**
 * 
 */
package ptm.client.note;

import ptm.client.todolist.ToDoListDialog;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * Handles Buttons for Note dialogs
 * @author Huseyin
 *
 */
public class NoteClickHandler implements ClickHandler {
	
	private NoteManager noteManager;
	
	public NoteClickHandler(NoteManager noteManager){
		this.noteManager = noteManager; 
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
	 */
	@Override
	public void onClick(ClickEvent event) {
		
		
		//check if it is a todolistbutton
		for(NoteDialog d:noteManager.getNoteDialogList()){
			if ((Widget)event.getSource() == d.getCloseButton()){
				d.closePressed();
				return;
			}else if ((Widget)event.getSource() == d.getEditButton()){
				d.editPressed();
				return;
			}else if ((Widget)event.getSource() == d.getOkButton()){
				d.okPressed();
				return;
			}else if ((Widget)event.getSource() == d.getCancelButton()){
				d.cancelPressed();
				return;
			}
		}//for

		
	}

}
