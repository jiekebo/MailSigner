package com.mailsigner.model;


/**
 * Pojo for holding the completed signature texts. Useful in a hashmap 
 * as a key value mapping between the signature title and this.
 * @author jiekebo
 *
 */
public class CompletedSignature {
	private String title;
	private String html;
	private String rtf;
	private String txt;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public String getRtf() {
		return rtf;
	}
	public void setRtf(String rtf) {
		this.rtf = rtf;
	}
	public String getTxt() {
		return txt;
	}
	public void setTxt(String txt) {
		this.txt = txt;
	}
	
	
}
