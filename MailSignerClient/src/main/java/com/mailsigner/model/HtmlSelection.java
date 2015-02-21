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

public class HtmlSelection implements Transferable, ClipboardOwner {
	
	private static ArrayList<DataFlavor> supportedFlavors = new ArrayList<DataFlavor>();
	private String html;
	
	static {
		supportedFlavors.add(new DataFlavor("text/html", "String"));
		supportedFlavors.add(new DataFlavor("text/html", "Reader"));
		supportedFlavors.add(new DataFlavor("text/html", "InputStream"));
	}
	
	public HtmlSelection(String html) {
		this.html = html;
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		if(String.class.equals(flavor.getRepresentationClass())) {
			return html;
		} else if (Reader.class.equals(flavor.getRepresentationClass())) {
			return new StringReader(html);
		} else if (InputStream.class.equals(flavor.getRepresentationClass())) {
			return new StringBufferInputStream(html);
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
