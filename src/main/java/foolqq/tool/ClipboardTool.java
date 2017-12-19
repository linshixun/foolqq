package foolqq.tool;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException; 
import java.io.IOException;
 
public class ClipboardTool {
 
	public static void setClipboardImage(final Image image) {
		Transferable trans = new Transferable() {
			@Override
			public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
				if (isDataFlavorSupported(flavor)) {
					return image;
				}
				throw new UnsupportedFlavorException(flavor);
			}

			@Override
			public DataFlavor[] getTransferDataFlavors() {
				return new DataFlavor[] { DataFlavor.imageFlavor };
			}

			@Override
			public boolean isDataFlavorSupported(DataFlavor flavor) {
				return DataFlavor.imageFlavor.equals(flavor);
			}
		};

		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(trans, null);
	}

	public static String getSystemClipboard() {
		Clipboard sysClb = null;
		sysClb = Toolkit.getDefaultToolkit().getSystemClipboard();
		try {
			Transferable t = sysClb.getContents(null);
			if (null != t && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				String text = (String) t.getTransferData(DataFlavor.stringFlavor);
				return text;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void setSysClipboardText(String msg) {
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable tText = new StringSelection(msg);
		clip.setContents(tText, null);
	}
}
