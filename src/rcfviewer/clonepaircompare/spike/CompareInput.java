package rcfviewer.clonepaircompare.spike;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import de.uni_bremen.st.rcf.model.ClonePair;
import de.uni_bremen.st.rcf.model.Fragment;

public class CompareInput extends CompareEditorInput {
	private String rightContent = "";
	private String leftConent = "";

	public CompareInput() {
		super(new CompareConfiguration());
	}

	@Override
	protected Object prepareInput(IProgressMonitor arg0) throws InvocationTargetException, InterruptedException {
		CompareItem left = new CompareItem("Left", leftConent,0);
		CompareItem right = new CompareItem("Right", rightContent,0);
		DiffNode diffNode = new DiffNode(left, right);
		return diffNode;
	}

	public void loadClonePair(ClonePair clonePair) {
		try {
			rightContent=readContent(clonePair.getRight());
			leftConent = readContent(clonePair.getLeft());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String readContent(Fragment fragment) throws IOException {
		int currentLine = 0;
		StringBuffer s = new StringBuffer();
		
		String filename = fragment.getStart().getFile().getRelativePath();
		FileInputStream fis = new FileInputStream(new File(filename ));
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String line = null;
		while ((line = br.readLine()) != null) {
			currentLine++;
			if (currentLine>=fragment.getStart().getLine() &&
					currentLine<=fragment.getEnd().getLine()){
				s.append(line);
				s.append('\n');
			}
		}
	 
		br.close();
		return s.toString();
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

