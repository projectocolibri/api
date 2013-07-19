/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Sergio Gomes (sergiogomes@projectocolibri.com)
 *******************************************************************************/
package rcp.importador.workbench;

import java.io.File;

import org.dma.eclipse.swt.file.FileImport;
import org.dma.eclipse.swt.graphics.SWTColorUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import rcp.colibri.vars.database.PopulateVARS.ENTIDADESTIPOS;
import rcp.importador.SAFTx0101Import;

public class ImportadorView extends ViewPart {

	public static final String ID = "ImportadorView";

	private Group groupChooseXml;
	private Group groupItemsToImport;
	private Group groupLogInfo;

	private Label labelFile;
	private Label labelConsole;
	private Text textFile;

	private Button buttonFile;
	private Button checkArticles;
	private Button checkCustomer;
	private Button checkTax;
	private Button buttonStart;

	//XML Document
	private File file;

	public void createPartControl(Composite parent) {
		createContainer(parent);
		createActions();
		initializeToolBar();
		initializeMenu();
	}

	private void createActions() {
	}


	private void initializeToolBar() {
		//IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
	}


	private void initializeMenu() {
		//IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
	}


	public void setFocus() {
	}

	public void createContainer(Composite parent){

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		new Label(composite, SWT.NONE);

		// Group 1
		groupChooseXml = new Group(composite, SWT.NONE);
		groupChooseXml.setLayout(new GridLayout(3, false));
		groupChooseXml.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		groupChooseXml.setText("Choose XML");

		labelFile = new Label(groupChooseXml, SWT.NONE);
		labelFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		labelFile.setText("File");

		textFile = new Text(groupChooseXml, SWT.BORDER);
		textFile.setEnabled(false);
		textFile.setEditable(false);
		textFile.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		buttonFile = new Button(groupChooseXml, SWT.NONE);
		buttonFile.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		buttonFile.setText("[...]");
		buttonFile.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				actionChooseFile();
			}
		});

		new Label(composite, SWT.NONE);

		//Group 2
		groupItemsToImport = new Group(composite, SWT.NONE);
		groupItemsToImport.setLayout(new GridLayout(1, false));
		groupItemsToImport.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		groupItemsToImport.setText("Items to Import");

		checkArticles = new Button(groupItemsToImport, SWT.CHECK);
		checkArticles.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		checkArticles.setText("Articles");

		checkCustomer = new Button(groupItemsToImport, SWT.CHECK);
		checkCustomer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		checkCustomer.setText("Customer");

		checkTax = new Button(groupItemsToImport, SWT.CHECK);
		checkTax.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		checkTax.setText("Tax");

		buttonStart = new Button(composite, SWT.NONE);
		buttonStart.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
		buttonStart.setText("Start");
		buttonStart.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				actionStart();
			}
		});

		//Group 3
		groupLogInfo = new Group(composite, SWT.NONE);
		groupLogInfo.setLayout(new FillLayout(SWT.HORIZONTAL));
		groupLogInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		groupLogInfo.setText("Log Info");

		labelConsole = new Label(groupLogInfo, SWT.NONE);
		labelConsole.setBackground(SWTColorUtils.getColor(SWT.COLOR_WHITE));
		labelConsole.setText("Waiting for XML file.");

	}


	//actions login
	private void actionStart(){
		try {
			if(checkCustomer.getSelection() || checkArticles.getSelection() || checkTax.getSelection()){

				buttonStart.setEnabled(false);

				SAFTx0101Import saft=new SAFTx0101Import(false,
					checkCustomer.getSelection(),
					checkArticles.getSelection(),
					checkTax.getSelection(),
					ENTIDADESTIPOS.cliente.codigo){
					public void consoleOut(String string) {
						labelConsole.setText(string);
					}
				};
				saft.loadFile(file);
				saft.process();
				labelConsole.setText("Finished.");

				buttonStart.setEnabled(true);
			}

		} catch (Exception e) {
			labelConsole.setText("Error, please see log details.");
		}
	}


	//actions choose File
	private void actionChooseFile(){

		try {
			file=new FileImport(Display.getCurrent().getActiveShell(), "*.xml").filePicker();
			labelConsole.setText("File loaded. Choose the items to Import and press Start.");
			textFile.setText(file.getAbsolutePath());

		} catch (Exception e){}

	}


}
