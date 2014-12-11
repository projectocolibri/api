/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.example.workbench;

import org.dma.eclipse.swt.graphics.ImageManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import org.projectocolibri.rcp.colibri.core.vars.IconVARS;

/*
 * Informacao sobre SWT (Standard Widget Toolkit)
 * http://www.eclipse.org/swt/
 */
public class ExampleView extends ViewPart {

	public static final String ID = "ExampleView";

	@Override
	public void createPartControl(Composite parent) {

		setPartName("#view");
		setTitleImage(ImageManager.getImage(IconVARS.COOLBAR_AJUDA));

		Composite composite=new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());

		Label label=new Label(parent, SWT.NONE);
		label.setText("Hello World!!!");

	}


	public void setFocus() {}


}