package rcfviewer.clonepaircompare;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;

import de.uni_bremen.st.rcf.model.ClonePair;
import rcfviewer.editors.FilePairViewer;

public class ClonePairViewerInput implements IEditorInput {

	private ClonePair clonePair;
	private IEditorPart associatedEitorPart;

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return WorkbenchImages.getImageDescriptor(IWorkbenchGraphicConstants.IMG_DTOOL_NEW_FASTVIEW);
	}

	@Override
	public String getName() {
		return "Clone Pair";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "tooltip text";
	}

	@Override
	public <T> T getAdapter(Class<T> arg0) {
		return null;
	}

	public void setClonePair(ClonePair clonePair) {
		this.clonePair = clonePair;
	}

	public IEditorPart getAssociatedEitorPart() {
		return associatedEitorPart;
	}

	public void setAssociatedEitorPart(IEditorPart associatedEitorPart) {
		this.associatedEitorPart = associatedEitorPart;
	}

	public ClonePair getClonePair() {
		return clonePair;
	}


}