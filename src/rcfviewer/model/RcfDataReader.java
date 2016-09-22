package rcfviewer.model;

import java.io.File;
import java.io.FileNotFoundException;

import de.uni_bremen.st.rcf.model.*;
import de.uni_bremen.st.rcf.persistence.*;

public class RcfDataReader {
	private RCF rcf;
	public RcfDataReader(String rcfFilename) {
		File file = new java.io.File(rcfFilename);
		AbstractPersistenceManager apm;
		try {
			apm = PersistenceManagerFactory.getPersistenceManager(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		rcf = apm.load(file);
	}
	public RCF getRcf() {
		return rcf;
	}
}
