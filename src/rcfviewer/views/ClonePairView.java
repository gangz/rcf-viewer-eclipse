package rcfviewer.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.*;

import de.uni_bremen.st.rcf.model.ClonePair;
import de.uni_bremen.st.rcf.model.Fragment;
import de.uni_bremen.st.rcf.model.RCF;
import de.uni_bremen.st.rcf.model.Version;
import rcfviewer.clonepaircompare.ClonePairViewerInput;
import rcfviewer.clonepaircompare.spike.CompareInput;
import rcfviewer.editors.FilePairViewer;
import rcfviewer.model.RcfDataReader;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.compare.CompareUI;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;

public class ClonePairView extends ViewPart {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "rcfviewer.views.ClonePairView";

	private TableViewer viewer;
	private Action doubleClickAction;

	List<ClonePair> clonePairs;

	private ClonePairViewerInput clonePairInput;
	String rcfFilePath = null;

	private IAction loadFileAction;

	private CompareInput compareInput;

	/**
	 * The constructor.
	 */
	public ClonePairView() {

	}

	public static final int COL_CLONEPAIR_ID = 0;
	public static final int COL_FRAGMENT_LEFT = 1;
	public static final int COL_FRAGMENT_RIGHT = 2;

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		Table table = viewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		final TableColumn[] newColumnTableColumns = new TableColumn[3];
		newColumnTableColumns[COL_CLONEPAIR_ID] = new TableColumn(table, SWT.NONE);
		newColumnTableColumns[COL_CLONEPAIR_ID].setWidth(10);
		newColumnTableColumns[COL_CLONEPAIR_ID].setText("id");

		newColumnTableColumns[COL_FRAGMENT_LEFT] = new TableColumn(table, SWT.NONE);
		newColumnTableColumns[COL_FRAGMENT_LEFT].setWidth(120);
		newColumnTableColumns[COL_FRAGMENT_LEFT].setText("Left");

		newColumnTableColumns[COL_FRAGMENT_RIGHT] = new TableColumn(table, SWT.NONE);
		newColumnTableColumns[COL_FRAGMENT_RIGHT].setWidth(120);
		newColumnTableColumns[COL_FRAGMENT_RIGHT].setText("Right");

		viewer.setContentProvider(new ContentProvider());
		viewer.setLabelProvider(new TableLabelProvider());
		viewer.setInput(clonePairs);
		getSite().setSelectionProvider(viewer);

		// Create the help context id for the viewer's control
		// PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(),
		// "test.viewer");
		makeActions(parent);
		hookDoubleClickAction();
		contributeToActionBars();
		loadClonePairView();

	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalToolBar(IToolBarManager toolBarManager) {
		toolBarManager.add(loadFileAction);
	}

	private void fillLocalPullDown(IMenuManager menuManager) {
		menuManager.add(loadFileAction);
	}

	private void loadRcfFile() {
		if (rcfFilePath == null)
			return;
		RcfDataReader r = new RcfDataReader(rcfFilePath);
		RCF rcf = r.getRcf();
		Version version = rcf.getVersions().getFirstEntry();
		clonePairs = version.getClonePairs();
		viewer.setInput(clonePairs);
		viewer.refresh();
	}

	private void loadClonePairView() {
		FilePairViewer editor = new FilePairViewer();
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		clonePairInput = new ClonePairViewerInput();
		try {
			page.openEditor(clonePairInput, "rcfviewer.editors.FilePairViewer");
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		if (this.compareInput==null){
			compareInput=new CompareInput();
		}
		CompareUI.openCompareEditor(this.compareInput);
	}

	private void refreshClonePairView(ClonePair clonePair) {
		FilePairViewer editor = new FilePairViewer();
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		page.closeEditor(clonePairInput.getAssociatedEitorPart(), false);
		clonePairInput = new ClonePairViewerInput();
		clonePairInput.setClonePair(clonePair);
		try {
			page.openEditor(clonePairInput, "rcfviewer.editors.FilePairViewer");
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		compareInput.loadClonePair(clonePair);
//		try {
//			compareInput.run(null);
//		} catch (InvocationTargetException | InterruptedException e) {
//			e.printStackTrace();
//		}
		CompareUI.openCompareEditor(compareInput,true);
	}

	private void makeActions(Composite parent) {
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				if (obj instanceof ClonePair) {
					ClonePair clonePair = (ClonePair) obj;
					refreshClonePairView(clonePair);
				} else
					showMessage("Double-click detected on " + obj.toString());
			}
		};

		loadFileAction = new Action() {
			public void run() {
				FileDialog fileDialog = new FileDialog(parent.getShell());

				rcfFilePath = fileDialog.open();
				if (rcfFilePath == null)
					return;
				loadRcfFile();
			}
		};
		loadFileAction.setText("Load RCF");
		loadFileAction.setToolTipText("Load RCF file");
		loadFileAction.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(), "Sample View", message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	class TableLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object element, int columnIndex) {
			if (!(element instanceof ClonePair))
				return null;
			ClonePair clonePair = (ClonePair) element;
			switch (columnIndex) {
			case COL_CLONEPAIR_ID:
				return "" + clonePair.getId();
			case COL_FRAGMENT_LEFT:
				return composeFragmentDesc(clonePair.getLeft());
			case COL_FRAGMENT_RIGHT:
				return composeFragmentDesc(clonePair.getRight());
			default:
				return null;
			}
		}

		private String composeFragmentDesc(Fragment fragment) {
			String filenameStart = fragment.getStart().getFile().getRelativePath();
			int startLine = fragment.getStart().getLine();
			int endLine = fragment.getEnd().getLine();
			String fileNameEnd = fragment.getEnd().getFile().getRelativePath();
			if (fileNameEnd.equals(filenameStart)) {
				return filenameStart + "(" + startLine + "," + endLine + ")";
			}
			return filenameStart + "(" + startLine + ")," + fileNameEnd + "(" + endLine + ")";
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
	}

	class ContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List) {
				return ((List) inputElement).toArray();
			} else {
				return new Object[0];
			}
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}
}
