package ptm.client.note;

import ptm.client.connection.Action;
import ptm.client.datamodel.Note;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextBox;

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

	//view panel components
	DockPanel viewPanel = new DockPanel();
	HTML viewNote = new HTML("");
	Button editButton = new Button("Edit");
	Button closeButton = new Button("Close");
	
	//edit panel components
	private DockPanel editPanel = new DockPanel();
	private TextBox titleTextBox = new TextBox();
	private RichTextArea noteTextArea = new RichTextArea();
	private Button okButton = new Button("OK");
	private Button cancelButton = new Button("Cancel");
	
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

		//Create view Button Panel
	    HorizontalPanel buttonPanel = new HorizontalPanel();
	    buttonPanel.setSpacing(10);
	    buttonPanel.add(editButton);
	    buttonPanel.add(closeButton);

	    //Create view Panel
	    viewPanel.add(viewNote,DockPanel.CENTER);
	    viewPanel.add(buttonPanel,DockPanel.SOUTH);
	    viewPanel.setCellHeight(buttonPanel, "30");
	    viewPanel.setCellHorizontalAlignment(buttonPanel, DockPanel.ALIGN_RIGHT);

	    //Create edit Button Panel
	    buttonPanel = new HorizontalPanel();
	    buttonPanel.setSpacing(10);
	    buttonPanel.add(okButton);
	    buttonPanel.add(cancelButton);
	    
	    //Create edit Panel
	    HorizontalPanel tempPanel = new HorizontalPanel();
	    tempPanel.setSpacing(10);
	    tempPanel.add(new Label("Title "));
	    tempPanel.add(titleTextBox);
	    titleTextBox.setSize("350","23");
	    editPanel.add(tempPanel,DockPanel.NORTH);
	    editPanel.add(noteTextArea,DockPanel.CENTER);
	    editPanel.add(buttonPanel,DockPanel.SOUTH);
	    editPanel.setCellHeight(buttonPanel, "30px");
	    editPanel.setCellHorizontalAlignment(buttonPanel, DockPanel.ALIGN_CENTER);
	    noteTextArea.setSize("400", "320");
	    //Create main Panel
	    mainPanel.add(viewPanel);
	    mainPanel.add(editPanel);
	    mainPanel.showWidget(Panels.viewPanel.ordinal());
	    
	    editButton.addClickHandler(noteManager.getNoteClickHandler());
	    closeButton.addClickHandler(noteManager.getNoteClickHandler());
	    okButton.addClickHandler(noteManager.getNoteClickHandler());
	    cancelButton.addClickHandler(noteManager.getNoteClickHandler());

	    //setSize() has to be under showWidget() to work
	    viewPanel.setSize("400", "400");
	    editPanel.setSize("400", "400");
	    setWidget(mainPanel);
	    
	    //setContent
	    viewNote.setHTML(note.getContent());

	}
	
	/**
	 * Invoked when edit button on note dialog was pressed.
	 * This button is on view panel.
	 */
	public void editPressed(){
		titleTextBox.setText(note.getTitle());
		noteTextArea.setHTML(note.getContent());
		mainPanel.showWidget(Panels.editPanel.ordinal());
	}
	
	/**
	 * Invoked when cancel button on note dialog was pressed.
	 * This button is on edit panel.
	 */
	public void cancelPressed(){
		mainPanel.showWidget(Panels.viewPanel.ordinal());
	}
	
	/**
	 * Invoked when close button on note dialog was pressed.
	 * This button is on view panel.
	 */
	public void closePressed(){
		// XXX close actionini yaz
	}
	
	/**
	 * Invoked when OK button on note dialog was pressed.
	 * This button is on edit panel.
	 */
	public void okPressed(){
		note.setTitle(titleTextBox.getText());
		note.setContent(noteTextArea.getHTML());
		
		//update view mode
		viewNote.setHTML(note.getContent());
		setText(note.getTitle());
		
		//create action
		Action action = new Action();
		action.setActionType(Action.ActionType.NOTE_EDIT);
		action.setObject(note);
		noteManager.getApplicationManager().getConnectionManager().addAction(action);
		
		mainPanel.showWidget(Panels.viewPanel.ordinal());
	}
	
	
	
	/**
	 * returns Id of the dialog Which is also id of the Note object that this dialog represents.
	 * @return id of this dialog.
	 */
	public long getId(){
		return note.getId();
	}
	
	//getters and setters
	public Button getEditButton() {
		return editButton;
	}

	public Button getCloseButton() {
		return closeButton;
	}

	public Button getOkButton() {
		return okButton;
	}

	public Button getCancelButton() {
		return cancelButton;
	}

	
}
