/*******************************************************************************
 * 2008-2014 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.example.workbench.actions;

import org.dma.eclipse.swt.graphics.SWTImageUtils;
import org.dma.java.utils.Debug;
import org.dma.java.utils.array.ErrorList;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import org.projectocolibri.rcp.birt.report.BIRTRender;
import org.projectocolibri.rcp.birt.report.BIRTReport.REPORT_ACTIONS;
import org.projectocolibri.rcp.colibri.core.PermissionsMap.COMMANDS;
import org.projectocolibri.rcp.colibri.core.vars.IconVARS;
import org.projectocolibri.rcp.colibri.dao.database.ColibriDatabase;
import org.projectocolibri.rcp.colibri.dao.database.populate.tables.DocumentostiposPopulate;
import org.projectocolibri.rcp.colibri.dao.model.classes.Documentosseries;
import org.projectocolibri.rcp.colibri.dao.model.classes.Documentostipos;
import org.projectocolibri.rcp.colibri.dao.model.classes.Entidadesdocumentos;
import org.projectocolibri.rcp.colibri.workbench.ColibriPerspective;
import org.projectocolibri.rcp.colibri.workbench.ColibriUI;
import org.projectocolibri.rcp.colibri.workbench.support.actions.OpenHelpViewAction;
import org.projectocolibri.rcp.colibri.workbench.support.views.actions.ColibriViewAction;
import org.projectocolibri.rcp.colibri.workbench.support.views.actions.OpenViewAction;
import org.projectocolibri.rcp.colibri.workbench.support.views.jobs.EmitirRelatorioJob;
import org.projectocolibri.rcp.colibri.workbench.support.views.jobs.GravarRegistoJob;
import org.projectocolibri.rcp.colibri.workbench.support.views.jobs.NovoRegistoJob;
import org.projectocolibri.rcp.colibri.workbench.views.artigos.ficheiro.ArtigosFicheiroView;
import org.projectocolibri.rcp.colibri.workbench.views.entidades.emissao.EntidadesEmissaoView;
import org.projectocolibri.rcp.example.workbench.ExamplePerspective;
import org.projectocolibri.rcp.example.workbench.ExampleView;

public class InterfaceExampleAction extends Action implements IWorkbenchAction {

	public InterfaceExampleAction() {
		setText("#interface");
		setImageDescriptor(SWTImageUtils.getImageDescriptor(IconVARS.COOLBAR_AJUDA));
	}


	@Override
	public final void run(){

		//comuta para a perspectiva COLIBRI
		ColibriUI.changePerspective(ColibriPerspective.ID);

		//abre vista de EXEMPLO
		new OpenViewAction(ExampleView.ID).run();

		//comuta para a perspectiva EXEMPLO
		ColibriUI.changePerspective(ExamplePerspective.ID);

		//abre vista de FICHEIRO de ARTIGOS
		new OpenViewAction(ArtigosFicheiroView.ID).run();

		//carrega FACTURA da primeira SERIE
		Documentostipos tipodocumento=ColibriDatabase.loadDocumentostipos(
				DocumentostiposPopulate.RECORDS.clientes_factura.codigo);
		Documentosseries serie=ColibriDatabase.loadDocumentosseries(tipodocumento.getCodigo(),0);
		Entidadesdocumentos documento=ColibriDatabase.loadEntidadesdocumentos(
				tipodocumento.getCodigo(), serie.getSerie(), serie.getNumero());

		if (documento!=null){
			//cria ACTION para EDICAO de FACTURA
			OpenViewAction action=new OpenViewAction(EntidadesEmissaoView.ID, documento, true);
			//abre e personaliza a vista
			if (action.run(false)) change((EntidadesEmissaoView)action.getView());
		}

		//cria ACTION para EMISSAO de FACTURA
		OpenViewAction action=new OpenViewAction(EntidadesEmissaoView.ID, tipodocumento);
		//abre e personaliza a vista
		if (action.run(false)) change2((EntidadesEmissaoView)action.getView());

	}

	/** Personaliza a vista */
	private void change(final EntidadesEmissaoView view) {

		//remove o separador COMUNICACAO
		view.getTab_comunicacao().dispose();

		//criacao de JOBS
		final NovoRegistoJob novoRegistoJob=new NovoRegistoJob<Entidadesdocumentos>(view.getObjectContainer()){
			public void done() {}
		};

		final GravarRegistoJob gravarRegistoJob=new GravarRegistoJob(view.getObjectContainer()){
			public void done(ErrorList error) {
				if (showError(error.getErrors())){
					view.jobBatch.cancelJobs();
				}else{
					view.updateViews();
				}
			}
		};

		final EmitirRelatorioJob emitirRelatorioJob=new EmitirRelatorioJob(view.getObjectContainer(), REPORT_ACTIONS.PREVIEW){
			public void done(BIRTRender render) {
				String printerName=view.getParameters().getTipodocumento().getImpressora();
				showError(render.output(printerName).getErrors());
			}
		};

		//substitui ACTION na TOOLBAR (com validacao)
		view.getActions().addToolItem(new ColibriViewAction(COMMANDS.Anular){
			//guarda action anterior
			final ColibriViewAction action=view.getActions().get(COMMANDS.Anular);
			public void execute() {
				Debug.out(ID);
				//codigo anterior
				action.execute();
			}
			public boolean canExecute() {
				return true;
			}
			public boolean isValid() {
				return action.isValid();
			}
		});

		//substitui ACTION na TOOLBAR (com validacao)
		view.getActions().addToolItem(new ColibriViewAction(COMMANDS.Gravar){
			//guarda action anterior
			final ColibriViewAction action=view.getActions().get(COMMANDS.Gravar);
			public void execute() {
				Debug.out(ID);
				//adiciona tarefas
				view.cancelEditing();
				view.jobBatch.add(gravarRegistoJob);
				view.jobBatch.add(novoRegistoJob);
				view.jobBatch.schedule();
			}
			public boolean canExecute() {
				return true;
			}
			public boolean isValid() {
				return action.isValid();
			}
		});

		//substitui ACTION na TOOLBAR (com validacao)
		view.getActions().addToolItem(new ColibriViewAction(COMMANDS.Emitir){
			//guarda action anterior
			final ColibriViewAction action=view.getActions().get(COMMANDS.Emitir);
			public void execute() {
				Debug.out(ID);
				view.cancelEditing();
				boolean enabled=view.getActions().get(COMMANDS.Gravar).isEnabled();
				//adiciona tarefas
				if(enabled) view.jobBatch.add(gravarRegistoJob);
				view.jobBatch.add(emitirRelatorioJob);
				if(enabled) view.jobBatch.add(novoRegistoJob);
				view.jobBatch.schedule();
			}
			public boolean canExecute() {
				return true;
			}
			public boolean isValid() {
				return action.isValid();
			}
		});

		//adiciona ACTION na TOOLBAR (sem validacao)
		view.getActions().addToolItem(new OpenHelpViewAction());

		//actualiza a TOOLBAR
		view.getActions().update();

	}


	private void change2(final EntidadesEmissaoView view) {

		view.getActions().addToolItem(new ColibriViewAction(COMMANDS.Emitir){
			final ColibriViewAction action=view.getActions().get(COMMANDS.Emitir);
			public void execute() {
				Debug.out(ID);
				view.cancelEditing();
				boolean enabled=view.getActions().get(COMMANDS.Gravar).isEnabled();
				//Gravar
				if(enabled) view.jobBatch.add(new GravarRegistoJob(view.getObjectContainer()){
					public void done(ErrorList error) {
						if (showError(error.getErrors())){
							view.jobBatch.cancelJobs();
						}else{
							view.updateViews();
						}
					}
				});
				//Emitir
				view.jobBatch.add(new CustomEmitirRelatorioJob(view.getParameters().getDocumentoentidades(), REPORT_ACTIONS.PREVIEW){
					public void done(BIRTRender render) {
						Debug.out("### DOCUMENTO ###", view.getParameters().getDocumentoentidades());
						String printerName=view.getParameters().getTipodocumento().getImpressora();
						showError(render.output(printerName).getErrors());
					}
				});
				//Novo
				if (enabled) view.jobBatch.add(new NovoRegistoJob(view.getObjectContainer()){
					public void done() {}
				});
				view.jobBatch.schedule();

			}
			public boolean canExecute() {
				return true;
			}
			public boolean isValid() {
				return action.isValid();
			}
		});

		//actualiza a TOOLBAR
		view.getActions().update();

	}


	@Override
	public void dispose() {}


}
