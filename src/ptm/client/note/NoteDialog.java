package ptm.client.note;

import ptm.client.datamodel.Note;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class NoteDialog extends DialogBox {
	
	/**
	 * Holds Note manager that handles this note. 
	 */
	private NoteManager noteManager;
	
	/**
	 * Holds note that this dialog represents 
	 */
	private Note note;
	
	private enum Panels{
		viewPanel,editPanel
	}

	private DeckPanel mainPanel = new DeckPanel();
	//view mode components
	DockPanel viewPanel = new DockPanel();
	HTML viewNote = new HTML("");
	Button editButton = new Button("Edit");
	Button closeButton = new Button("Close");
	
	
	DockPanel editPanel = new DockPanel();
	
	/**
	 * Creates new note dialog and shots it on screen
	 * @param note Note object that this dialog object represents.
	 * @param noteManager Note Manager Object that will handle this dialog
	 */
	public NoteDialog (Note note,NoteManager noteManager){
		super(false,false);
		this.noteManager = noteManager;
		this.note = note;
	
		setAnimationEnabled(true);
		setText(note.getTitle());

		//Create Button Panel
	    HorizontalPanel viewButtonPanel = new HorizontalPanel();
	    viewButtonPanel.setSpacing(10);
	    viewButtonPanel.add(editButton);

	    //Create view Panel
	    viewPanel.add(viewNote,DockPanel.CENTER);
	    viewPanel.add(viewButtonPanel,DockPanel.SOUTH);
	    viewPanel.setCellHeight(viewButtonPanel, "30");
	    viewPanel.setCellHorizontalAlignment(viewButtonPanel, DockPanel.ALIGN_RIGHT);
	    
	    //Create main Panel
	    mainPanel.add(viewPanel);
	    mainPanel.add(editPanel);
	    mainPanel.showWidget(Panels.viewPanel.ordinal());
	    
	    //setSize() has to be under showWidget() to work
	    viewPanel.setSize("400", "400");
	    
	    setWidget(mainPanel);
	    
	    //setContent
	    viewNote.setHTML(note.getContent());

	}
	
	/**
	 * returns Id of the dialog Which is also id of the Note object that this dialog represents.
	 * @return id of this dialog.
	 */
	public long getId(){
		return note.getId();
	}
	
	
}
