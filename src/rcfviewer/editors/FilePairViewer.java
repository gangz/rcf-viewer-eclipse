package rcfviewer.editors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.part.EditorPart;

import de.uni_bremen.st.rcf.model.Fragment;
import de.uni_bremen.st.rcf.model.RCF;
import rcfviewer.clonepaircompare.ClonePairViewerInput;
import rcfviewer.handlers.FileLineLocator;
import rcfviewer.model.RcfDataReader;

public class FilePairViewer extends EditorPart {

	Fragment leftFragment;
	Fragment rightFragment;

	public FilePairViewer() {
		super();
	}

	public void dispose() {
		super.dispose();
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite shell = new Composite(parent, SWT.NONE);
		FormLayout layout = new FormLayout();
		shell.setLayout(layout);

		FormData leftFormPosition = new FormData();
		leftFormPosition.top = new FormAttachment(0, 5);
		leftFormPosition.bottom = new FormAttachment(100, -5);
		leftFormPosition.left = new FormAttachment(0, 5);
		leftFormPosition.right = new FormAttachment(50, -5);

		FormData rightFormPosition = new FormData();
		rightFormPosition.top = new FormAttachment(0, 5);
		rightFormPosition.bottom = new FormAttachment(100, -5);
		rightFormPosition.left = new FormAttachment(50, 5);
		rightFormPosition.right = new FormAttachment(100, -5);
		
		StyledText leftTextEditor = new StyledText(shell,SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		//Text leftTextEditor = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		leftTextEditor.setLayoutData(leftFormPosition);
		loadFragment(leftTextEditor, leftFragment);

		StyledText rightTextEditor = new StyledText(shell, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		loadFragment(rightTextEditor, rightFragment);
		rightTextEditor.setLayoutData(rightFormPosition);

	}

	private void loadFragment(StyledText editor, Fragment fragment) {
		if(fragment==null) {
			editor.setText("empty");
			return;
		}
		editor.setFont(new Font(editor.getDisplay(),
				new FontData( "Consolas", 10, SWT.NORMAL ) ));
		editor.setForeground(new Color(editor.getDisplay(),0,0,255));
		
		String filename = fragment.getStart().getFile().getAbsolutePath();
		String content;
		try {
			content = readFromFile(filename);
			editor.setText(content);
			Document doc = new Document(content);
			editor.setSelection(doc.getLineOffset(fragment.getStart().getLine()), 
					doc.getLineOffset(fragment.getEnd().getLine()));
			editor.setTopIndex(fragment.getStart().getLine());
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	private String readFromFile(String filename) throws Exception {
		StringBuffer s = new StringBuffer();
		FileInputStream fis = new FileInputStream(new File(filename));
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String line = null;
		while ((line = br.readLine()) != null) {
			s.append(line);
			s.append('\n');
		}
	 
		br.close();
		return s.toString();
	}

	@Override
	public void setFocus() {

	}

	@Override
	public void doSave(IProgressMonitor arg0) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);

		setPartName(input.getName());
		setTitleImage(input.getImageDescriptor().createImage());
		if (!(input instanceof ClonePairViewerInput)) {
			throw new PartInitException("");
		}
		ClonePairViewerInput clonePairViewerInput = (ClonePairViewerInput) input;
		clonePairViewerInput.setAssociatedEitorPart(this);
		if (clonePairViewerInput.getClonePair()==null) return;
		leftFragment = clonePairViewerInput.getClonePair().getLeft();
		rightFragment = clonePairViewerInput.getClonePair().getRight();
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
}
