/*******************************************************************************
 * 2008-2011 Public Domain
 * Contributors
 * Marco Lopes (marcolopes@netc.pt)
 *******************************************************************************/
package org.dma.utils.apache.mail;

import org.dma.utils.eclipse.core.Debug;

public class ServerParameters {

	private String hostname;
	private int smtpport;
	private boolean auth;
	private String username;
	private String password;

	public ServerParameters(String hostname, int smtpport, boolean auth, String username, String password) {

		this.hostname=hostname;
		this.smtpport=smtpport;
		this.auth=auth;
		this.username=username;
		this.password=password;

	}


	public void debug(){

		Debug.info("hostname", hostname);
		Debug.info("smtpport", smtpport);
		Debug.info("username", username);
		Debug.info("password", password);

	}



	//getters and setters
	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname=hostname;
	}

	public int getSmtpport() {
		return smtpport;
	}

	public void setSmtpport(int smtpport) {
		this.smtpport=smtpport;
	}

	public boolean isAuth() {
		return auth;
	}

	public void setAuth(boolean auth) {
		this.auth=auth;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username=username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password=password;
	}


}

