/*******************************************************************************
 * 2008-2011 Public Domain
 * Contributors
 * Marco Lopes (marcolopes@netc.pt)
 *******************************************************************************/
package org.dma.utils.eclipse.ui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class DialogHandler {

	private static final int ERROR = 1;
	private static final int INFORMATION = 2;
	private static final int QUESTION = 3;
	private static final int CONFIRMATION = 4;

	private static String ERROR_MESSAGE = "Error";
	private static String INFORMATION_MESSAGE = "Information";
	private static String QUESTION_MESSAGE = "Attention";
	private static String CONFIRMATION_MESSAGE = "Attention";

	public static void setMessages(String error, String information, String question, String confirmation) {
		ERROR_MESSAGE=error;
		INFORMATION_MESSAGE=information;
		QUESTION_MESSAGE=question;
		CONFIRMATION_MESSAGE=confirmation;
	}


	/*
	 * Error
	 */
	public static boolean error(String message) {
		return open(message, ERROR);
	}


	/*
	 * Information
	 */
	public static boolean information(String message) {
		return open(message, INFORMATION);
	}

	public static boolean information(String operation, String message) {
		return open(operation+": "+message, INFORMATION);
	}


	/*
	 * Question
	 */
	public static boolean question(String message) {
		return open(message, QUESTION);
	}


	/*
	 * Confirmation
	 */
	public static boolean confirm(String message) {
		return open(message, CONFIRMATION);
	}


	/*
	 * Dialog
	 */
	public static boolean open(String message, int type) {
		return open(null, message, type);
	}


	public static boolean open(String title, String message, int type) {

		try{
			if(title==null) {

				switch(type) {
					case ERROR: title=ERROR_MESSAGE; break;
					case INFORMATION: title=INFORMATION_MESSAGE; break;
					case QUESTION: title=QUESTION_MESSAGE; break;
					case CONFIRMATION: title=CONFIRMATION_MESSAGE; break;
				}
			}

			Shell shell=Display.getCurrent().getActiveShell();
			switch(type) {
				case ERROR: MessageDialog.openError(shell, title, message); break;
				case INFORMATION: MessageDialog.openInformation(shell, title, message); break;
				case QUESTION: return MessageDialog.openQuestion(shell, title, message);
				case CONFIRMATION: return MessageDialog.openConfirm(shell, title, message);
			}

		}catch(Exception e){
			e.printStackTrace();
		}

		return true;
	}


}
