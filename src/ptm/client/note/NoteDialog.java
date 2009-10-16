package ptm.client.note;

import ptm.client.connection.Action;
import ptm.client.datamodel.Note;
import ptm.client.datamodel.ObjectListElement;

import com.google.gwt.event.dom.client.MouseUpEvent;
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
	
		Action action = new Action();
		action.setActionType(Action.ActionType.NOTE_CLOSE);
		action.setObjectId(getId());
		
		noteManager.getApplicationManager().getConnectionManager().addAction(action);
		
		setVisible(false);
		noteManager.getNoteDialogList().remove(this);
		//update session
		noteManager.getApplicationManager().getSession().getNote(note.getId()).setOpen(false);
	}
	
	/**
	 * Invoked when OK button on note dialog was pressed.
	 * This button is on edit panel.
	 */
	public void okPressed(){
		String newTitle = titleTextBox.getText();
		//update view mode
		viewNote.setHTML(noteTextArea.getHTML());
		//update Session
		//if title of note was changed update toolbar and session.
		if (note.getTitle() != newTitle ){
			int oldNoteIndex = noteManager.getApplicationManager().getSession().getNoteIndex(note.getId());
			ObjectListElement noteElement = noteManager.getApplicationManager().getSession().getNote(note.getId());
			noteElement.setName(newTitle);
			noteManager.getApplicationManager().getSession().sortNote();
			int newNoteIndex = noteManager.getApplicationManager().getSession().getNoteIndex(note.getId());
			//if index of note was changed delete refresh toolbar
			if( oldNoteIndex==newNoteIndex)
				noteManager.getApplicationManager().getToolbarManager().getNoteListBox().setItemText(oldNoteIndex, newTitle);
			else{
				noteManager.getApplicationManager().getToolbarManager().getNoteListBox().removeItem(oldNoteIndex);
				noteManager.getApplicationManager().getToolbarManager().getNoteListBox().insertItem(newTitle, Long.toString(note.getId()), newNoteIndex);
				noteManager.getApplicationManager().getToolbarManager().getNoteListBox().setSelectedIndex(newNoteIndex);
			}
			
		}
		
		note.setTitle(newTitle);
		note.setContent(noteTextArea.getHTML());
		setText(note.getTitle());	
		//create action
		Action action = new Action();
		action.setActionType(Action.ActionType.NOTE_EDIT);
		action.setObject(note);
		noteManager.getApplicationManager().getConnectionManager().addAction(action);
		
		mainPanel.showWidget(Panels.viewPanel.ordinal());
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.DialogBox#endDragging(com.google.gwt.event.dom.client.MouseUpEvent)
	 */
	@Override
	protected void endDragging(MouseUpEvent event) {
		super.endDragging(event);
		ObjectListElement e = noteManager.getApplicationManager().getSession().getAllNotes().get(noteManager.getApplicationManager().getSession().getNoteIndex(note.getId()));
		if (e.getX() != getAbsoluteLeft() || e.getY() != getAbsoluteTop()){
			e.setX(getPopupLeft());
			e.setY(getPopupTop());
			
			Action action = new Action();
			action.setActionType(Action.ActionType.NOTE_MOVE);
			action.setObject(e);
			
			noteManager.getApplicationManager().getConnectionManager().addAction(action);
		}
		
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
