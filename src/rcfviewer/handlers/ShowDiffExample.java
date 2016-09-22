package rcfviewer.handlers;

import org.eclipse.compare.CompareUI;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import rcfviewer.clonepaircompare.CompareInput;

public class ShowDiffExample extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		CompareUI.openCompareEditor(new CompareInput());
		return null;
	}
}
