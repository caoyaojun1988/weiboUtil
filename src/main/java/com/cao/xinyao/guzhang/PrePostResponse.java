package com.cao.xinyao.guzhang;

import java.io.Serializable;

public class PrePostResponse implements Serializable{
	 
	private static final long serialVersionUID = 1L;
	private String retcode;
	private String servertime;
	private String pcid;
	public String getRetcode() {
		return retcode;
	}
	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}
	public String getServertime() {
		return servertime;
	}
	public void setServertime(String servertime) {
		this.servertime = servertime;
	}
	public String getPcid() {
		return pcid;
	}
	public void setPcid(String pcid) {
		this.pcid = pcid;
	}
	public String getNonce() {
		return nonce;
	}
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	private String nonce;
}