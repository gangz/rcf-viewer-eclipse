package rcfviewer.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.uni_bremen.st.rcf.model.RCF;
import rcfviewer.model.RcfDataReader;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class RcfFileLoader extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String rcfFilePath = "/home/gangz/00quick/clone/bellon_benchmark/bellon_benchmark/netbeans-javadoc.rcf";
		RcfDataReader r = new RcfDataReader(rcfFilePath);
		RCF rcf = r.getRcf();
		String filePath = rcf.getVersions().getFirstEntry().getFiles().get(0).getAbsolutePath();
		FileLineLocator locator = new FileLineLocator();
		locator.setFileLocation(filePath,20);
		locator.locate();
		return null;
	}
}
