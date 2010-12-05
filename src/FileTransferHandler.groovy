import java.awt.datatransfer.DataFlavor;

import javax.swing.SwingUtilities;
import javax.swing.TransferHandler
import javax.swing.TransferHandler.TransferSupport


class FileTransferHandler extends TransferHandler {
	MainController controller
	
	@Override
	boolean canImport(TransferSupport support) {
   		return support.transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)
	}
	
	@Override
	public boolean importData(TransferSupport support) {
		List<File> files = support.transferable.getTransferData(DataFlavor.javaFileListFlavor)
		SwingUtilities.invokeLater({
			controller.startComparison(files)
		} as Runnable)
	}
	
}
