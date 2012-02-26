package com.mailsigner.model;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.util.ArrayList;

public class RtfSelection implements Transferable, ClipboardOwner {
	
	private static ArrayList<DataFlavor> supportedFlavors = new ArrayList<DataFlavor>();
	private String rtf;
	
	static {
		try {
			supportedFlavors.add(new DataFlavor("application/rtf; class=java.io.InputStream"));
			supportedFlavors.add(new DataFlavor("text/rtf;class=java.lang.String"));
			supportedFlavors.add(new DataFlavor("text/rtf;class=java.io.Reader"));
			supportedFlavors.add(new DataFlavor("text/rtf;charset=unicode;class=java.io.InputStream"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public RtfSelection(String rtf) {
		this.rtf = rtf;
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		if(String.class.equals(flavor.getRepresentationClass())) {
			return rtf;
		} else if (Reader.class.equals(flavor.getRepresentationClass())) {
			return new StringReader(rtf);
		} else if (InputStream.class.equals(flavor.getRepresentationClass())) {
			return new StringBufferInputStream(rtf);
		}
		throw new UnsupportedFlavorException(flavor);
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		DataFlavor[] dataFlavorArray = new DataFlavor[supportedFlavors.size()];
		return supportedFlavors.toArray(dataFlavorArray);
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return supportedFlavors.contains(flavor);
	}

	@Override
	public void lostOwnership(Clipboard arg0, Transferable arg1) {
		System.out.println("Lost ownership");
		
	}

}
