package rcfviewer.clonepaircompare;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.IModificationDate;
import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.DiffNode;
import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.graphics.Image;

public class CompareInput extends CompareEditorInput {
	public CompareInput() {
		super(new CompareConfiguration());
	}

	@Override
	protected Object prepareInput(IProgressMonitor arg0) throws InvocationTargetException, InterruptedException {
		String contents="";
		for (int i=0;i<100;i++){
			contents += "contents\n";
		}
		String newContents="";
		for (int i=0;i<100;i++){
			newContents += "bew contents\n";
		}
		String oldContents="";
		for (int i=0;i<100;i++){
			oldContents += "old contents\n";
		}
		CompareItem ancestor = new CompareItem("Common", contents, 0);
		CompareItem left = new CompareItem("Left", newContents,0);
		CompareItem right = new CompareItem("Right", oldContents,0);
		return new DiffNode(null, Differencer.NO_CHANGE, ancestor, left, right);
	}
}
class CompareItem implements IStreamContentAccessor, ITypedElement, IModificationDate {
	private String contents, name;
	private long time;

	CompareItem(String name, String contents, long time) {
		this.name = name;
		this.contents = contents;
		this.time = time;
	}

	public InputStream getContents() throws CoreException {
		return new ByteArrayInputStream(contents.getBytes());
	}

	public Image getImage() {
		return null;
	}

	public long getModificationDate() {
		return time;
	}

	public String getName() {
		return name;
	}

	public String getString() {
		return contents;
	}

	public String getType() {
		return ITypedElement.TEXT_TYPE;
	}
}

