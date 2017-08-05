/*******************************************************************************
 * 2008-2015 Projecto Colibri
 * Sergio Gomes (sergiogomes@projectocolibri.com)
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.importador.workbench;

import java.io.File;

import org.dma.eclipse.swt.dialogs.file.FileImport;
import org.dma.eclipse.swt.graphics.ColorUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import org.projectocolibri.api.database.model.Entidadestipos;
import org.projectocolibri.rcp.importador.SAFTx0101Import;

public class View extends ViewPart {

	public static final String ID = "View";

	private Button checkTaxes;
	private Button checkArticles;
	private Button checkCustomer;

	private Text textFile;
	private Button buttonStart;
	private Label labelConsole;

	//XML Document
	private File file;

	@Override
	public void createPartControl(Composite parent) {
		createContainer(parent);
		createActions();
		initializeToolBar();
		initializeMenu();
	}

	@Override
	public void setFocus() {}


	private void createActions() {}


	private void initializeToolBar() {
		//IToolBarManager toolBarManager = getViewSite().getActionBars().getToolBarManager();
	}


	private void initializeMenu() {
		//IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
	}

	public void createContainer(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		//Group 1
		Group group = new Group(composite, SWT.NONE);
		group.setLayout(new GridLayout(1, false));
		group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		group.setText("Tables to Import");

		checkTaxes = new Button(group, SWT.CHECK);
		checkTaxes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		checkTaxes.setText("Taxes");

		checkArticles = new Button(group, SWT.CHECK);
		checkArticles.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		checkArticles.setText("Articles");

		checkCustomer = new Button(group, SWT.CHECK);
		checkCustomer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		checkCustomer.setText("Customer");

		// Group 2
		group = new Group(composite, SWT.NONE);
		group.setLayout(new GridLayout(4, false));
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		group.setText("Choose XML");

		Label label = new Label(group, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		label.setText("File");

		textFile = new Text(group, SWT.BORDER);
		textFile.setEnabled(false);
		textFile.setEditable(false);
		textFile.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		Button button = new Button(group, SWT.NONE);
		button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		button.setText("[...]");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				file=new FileImport(getSite().getShell(), "*.xml").filePicker();
				if (file!=null){
					textFile.setText(file.getAbsolutePath());
					labelConsole.setText("File loaded.");
					buttonStart.setEnabled(true);
				}
			}
		});

		buttonStart = new Button(group, SWT.NONE);
		buttonStart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		buttonStart.setText("Start");
		buttonStart.setEnabled(false);
		buttonStart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				if(checkCustomer.getSelection() ||
					checkArticles.getSelection() ||
					checkTaxes.getSelection()) try{

						buttonStart.setEnabled(false);

						SAFTx0101Import saft=new SAFTx0101Import(false,
							checkTaxes.getSelection(),
							checkArticles.getSelection(),
							checkCustomer.getSelection(),
							Entidadestipos.RECORDS.cliente.codigo){
							@Override
							public void consoleOut(String string) {
								labelConsole.setText(string);
							}
						};
						if (saft.loadFile(file)) saft.process();
						labelConsole.setText("Finished.");

				}catch(Exception e){
					labelConsole.setText("Error! Please see log for details.");
				}else{
					labelConsole.setText("Choose the items to Import and press Start.");
				}
				buttonStart.setEnabled(true);
			}
		});

		//Group 3
		group = new Group(composite, SWT.NONE);
		group.setLayout(new FillLayout(SWT.HORIZONTAL));
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		group.setText("Log Info");

		labelConsole = new Label(group, SWT.NONE);
		labelConsole.setBackground(ColorUtils.getColor(SWT.COLOR_WHITE));
		labelConsole.setText("Waiting for XML file.");

	}


}
