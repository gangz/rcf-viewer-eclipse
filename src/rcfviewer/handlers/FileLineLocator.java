package rcfviewer.handlers;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.ITextEditor;

public class FileLineLocator extends AbstractHandler {
	private String filePath;
	private Integer lineNumer;

	public void setFileLocation(String fileName, Integer lineNumber) {
		this.filePath = fileName;
		this.lineNumer = lineNumber;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		this.locate();
		return null;
	}

	public void locate() {
		IFileStore fileStore = getFileStore();
		try {
			IEditorPart editor = IDE.openEditorOnFileStore(getActiveWorkbenchPage(), fileStore);
			selectLine(editor, lineNumer);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private void selectLine(IEditorPart editor, Integer lineNumer) throws BadLocationException {
		if (!(editor instanceof ITextEditor))
			return;
		ITextEditor textEditor = (ITextEditor) editor;
		IDocument document = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
		textEditor.selectAndReveal(document.getLineOffset(lineNumer - 1), document.getLineLength(lineNumer - 1));
	}

	private IFileStore getFileStore() {
		File fileToOpen = new File(filePath);
		if (!fileToOpen.exists())
			return null;
		if (!fileToOpen.isFile())
			return null;
		return EFS.getLocalFileSystem().getStore(fileToOpen.toURI());
	}

	private IWorkbenchPage getActiveWorkbenchPage() throws ExecutionException {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	}

}
