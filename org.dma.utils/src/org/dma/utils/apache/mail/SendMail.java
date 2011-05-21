/*******************************************************************************
 * 2008-2011 Public Domain
 * Contributors
 * Marco Lopes (marcolopes@netc.pt)
 *******************************************************************************/
package org.dma.utils.apache.mail;

import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.dma.utils.eclipse.core.Debug;

public class SendMail {

	public static boolean send(ServerParameters server, String fromemail, String fromname, String toemail, String toname, String subject, String message) {

		try {
			SimpleEmail email = new SimpleEmail();
			email.setDebug(false);
			email.setHostName(server.getHostname());
			email.setSmtpPort(server.getSmtpport());
			if (server.isAuth()) email.setAuthentication(server.getUsername(), server.getPassword());
			email.setFrom(fromemail, fromname);
			email.addTo(toemail, toname);
			email.setSubject(subject);
			email.setMsg(message);

			email.send();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;

	}


	public static boolean send(ServerParameters server, String fromemail, String fromname, String toemail, String toname, String subject, String message, EmailAttachment attachment) {

		try {
			server.debug();
			Debug.info("fromemail", fromemail);
			Debug.info("fromname", fromname);
			Debug.info("toemail", toemail);
			Debug.info("toname", toname);
			Debug.info("subject", subject);
			Debug.info("message", message);
			attachment.debug();

			MultiPartEmail email = new MultiPartEmail();
			email.setDebug(false);
			email.setHostName(server.getHostname());
			email.setSmtpPort(server.getSmtpport());
			if (server.isAuth()) email.setAuthentication(server.getUsername(), server.getPassword());
			email.setFrom(fromemail, fromname);
			email.addTo(toemail, toname);
			email.setSubject(subject);
			email.setMsg(message);
			email.attach(attachment);

			email.send();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;

	}


	public static void main(String arg[]) {

		ServerParameters server=new ServerParameters("mail.projectocolibri.com", 25, true, "marcolopes@projectocolibri.com", "***");

		send(server, "suporte@projectocolibri.com", "FROM: Projecto Colibri", "marcolopes@projectocolibri.com", "TO: Marco Lopes", "SUBJECT: Teste Email Simples", "MESSAGE: Teste Email Simples");
		send(server, "suporte@projectocolibri.com", "FROM: Projecto Colibri", "marcolopes@projectocolibri.com", "TO: Marco Lopes", "SUBJECT: Teste Email Simples", "MESSAGE: Teste Email com Attachment",
			new EmailAttachment("/colibri/email.txt", "EMAIL", "TEXT: Texto do Attachment"));

	}


}