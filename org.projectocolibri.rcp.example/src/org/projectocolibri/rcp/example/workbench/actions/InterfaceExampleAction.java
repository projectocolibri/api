/*******************************************************************************
 * 2008-2016 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.example.workbench.actions;

import org.dma.eclipse.swt.dialogs.message.ErrorDialog;
import org.dma.java.util.Debug;
import org.dma.java.util.ErrorList;
import org.dma.java.util.MethodCallback;

import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import org.projectocolibri.api.Colibri.COMMANDS;
import org.projectocolibri.api.IconVARS;
import org.projectocolibri.api.birt.report.BIRTRender;
import org.projectocolibri.api.birt.report.BIRTReport.REPORT_ACTIONS;
import org.projectocolibri.api.database.ColibriDatabase;
import org.projectocolibri.api.database.model.Documentosseries;
import org.projectocolibri.api.database.model.Documentostipos;
import org.projectocolibri.api.database.model.Entidadesdocumentos;
import org.projectocolibri.api.example.core.DocumentosExample;
import org.projectocolibri.rcp.colibri.RCPcolibri.PREFERENCES;
import org.projectocolibri.rcp.colibri.workbench.ColibriPerspective;
import org.projectocolibri.rcp.colibri.workbench.ColibriUI;
import org.projectocolibri.rcp.colibri.workbench.actions.OpenHelpViewAction;
import org.projectocolibri.rcp.colibri.workbench.support.ColibriAction;
import org.projectocolibri.rcp.colibri.workbench.support.views.actions.ColibriViewAction;
import org.projectocolibri.rcp.colibri.workbench.support.views.actions.OpenViewAction;
import org.projectocolibri.rcp.colibri.workbench.support.views.jobs.EmitirRelatorioJob;
import org.projectocolibri.rcp.colibri.workbench.support.views.jobs.GravarRegistoJob;
import org.projectocolibri.rcp.colibri.workbench.support.views.jobs.NovoRegistoJob;
import org.projectocolibri.rcp.colibri.workbench.views.ReportView;
import org.projectocolibri.rcp.colibri.workbench.views.artigos.ficheiro.ArtigosFicheiroView;
import org.projectocolibri.rcp.colibri.workbench.views.entidades.emissao.EntidadesEmissaoView;
import org.projectocolibri.rcp.example.workbench.ExamplePerspective;
import org.projectocolibri.rcp.example.workbench.ExampleView;

public class InterfaceExampleAction extends ColibriAction implements IWorkbenchAction {

	public InterfaceExampleAction() {
		setText("#interface");
		setImageDescriptor(IconVARS.TOOLBAR_NOVO);
	}


	@Override
	public final void run() {

		try{
			//comuta para a perspectiva COLIBRI
			ColibriUI.changePerspective(ColibriPerspective.ID);

			//abre vista de EXEMPLO
			new OpenViewAction(ExampleView.ID).run();

			//comuta para a perspectiva EXEMPLO
			ColibriUI.changePerspective(ExamplePerspective.ID);

			//abre vista de FICHEIRO de ARTIGOS
			new OpenViewAction(ArtigosFicheiroView.ID).run();

			//exemplos de edicao de documentos
			example1(Documentostipos.RECORDS.clientes_factura.codigo);
			example2(Documentostipos.RECORDS.clientes_factura.codigo);

		}catch(Exception e){
			e.printStackTrace();
		}

	}


	private void example1(String tipodocumento) throws Exception {

		Documentosseries serie=ColibriDatabase.loadDocumentosseries(tipodocumento, 0);

		Entidadesdocumentos documento=ColibriDatabase.loadEntidadesdocumentos(
				tipodocumento, serie.getSerie(), serie.getNumero());

		if (documento==null) return;

		//cria ACTION para EDICAO de FACTURA
		OpenViewAction viewAction=new OpenViewAction(EntidadesEmissaoView.ID, documento);

		//abre e personaliza a vista
		if (viewAction.run(false)){

			final EntidadesEmissaoView view=(EntidadesEmissaoView)viewAction.getView();

			//criacao de JOBS
			final NovoRegistoJob novoRegistoJob=new NovoRegistoJob<Entidadesdocumentos>(view.getObjectContainer()){
				@Override
				public void done(Entidadesdocumentos documento) {
					view.reset(documento);
					view.resetFocus();
				}
			};

			final GravarRegistoJob gravarRegistoJob=new GravarRegistoJob(view.getObjectContainer(),
					new MethodCallback<Entidadesdocumentos>(){
				@Override
				public boolean run(Entidadesdocumentos obj) {
					ErrorList error=obj.checkWarnings();
					return !ErrorDialog.open(error.errors());
				}}){
				@Override
				public void done(ErrorList error) {
					if (showError(error.errors())){
						view.cancelJobs();
					}else{
						view.updateViews();
					}
				}
			};

			final EmitirRelatorioJob emitirRelatorioJob=new EmitirRelatorioJob(view.getObjectContainer(), REPORT_ACTIONS.PREVIEW){
				@Override
				public void done(BIRTRender render) {
					if (!showError(render.errors())){
						new OpenViewAction(ReportView.ID, render).
							run(PREFERENCES.DETACH_REPORT_VIEW.value.getBoolean());
					}
				}
			};

			//criacao de ACTIONS
			ColibriViewAction anularRegistoAction=new ColibriViewAction(COMMANDS.Anular){
				//guarda action anterior
				final ColibriViewAction action=view.getActions().get(COMMANDS.Anular);
				@Override
				public void execute() {
					Debug.out(ID);
					//codigo anterior
					action.execute();
				}
				@Override
				public boolean canExecute() {
					return true;
				}
				@Override
				public boolean isValid() {
					return action.isValid();
				}
			};

			ColibriViewAction gravarRegistoAction=new ColibriViewAction(COMMANDS.Gravar){
				//guarda action anterior
				final ColibriViewAction action=view.getActions().get(COMMANDS.Gravar);
				@Override
				public void execute() {
					Debug.out(ID);
					//adiciona tarefas
					view.cancelEditing();
					view.addJob(gravarRegistoJob);
					view.addJob(novoRegistoJob);
					view.runJobs();
				}
				@Override
				public boolean canExecute() {
					return true;
				}
				@Override
				public boolean isValid() {
					return action.isValid();
				}
			};

			ColibriViewAction emitirRegistoAction=new ColibriViewAction(COMMANDS.Emitir){
				//guarda action anterior
				final ColibriViewAction action=view.getActions().get(COMMANDS.Emitir);
				@Override
				public void execute() {
					Debug.out(ID);
					view.cancelEditing();
					boolean enabled=view.getActions().get(COMMANDS.Gravar).isEnabled();
					//adiciona tarefas
					if(enabled) view.addJob(gravarRegistoJob);
					view.addJob(emitirRelatorioJob);
					if(enabled) view.addJob(novoRegistoJob);
					view.runJobs();
				}
				@Override
				public boolean canExecute() {
					return true;
				}
				@Override
				public boolean isValid() {
					return action.isValid();
				}
			};

			//remove o separador COMUNICACAO
			view.getTab_comunicacao().dispose();

			//substitui ACTIONS na TOOLBAR
			view.getActions().addToolItem(anularRegistoAction);
			view.getActions().addToolItem(gravarRegistoAction);
			view.getActions().addToolItem(emitirRegistoAction);

			//adiciona ACTIONS na TOOLBAR
			view.getActions().addToolItem(new OpenHelpViewAction());

			//actualiza a TOOLBAR
			view.getActions().update();
		}

	}


	private void example2(String tipodocumento) throws Exception {

		Entidadesdocumentos documento=new DocumentosExample().createDocumento(tipodocumento);

		//cria ACTION para EMISSAO de FACTURA
		OpenViewAction viewAction=documento==null ?
				//novo documento
				new OpenViewAction(EntidadesEmissaoView.ID, tipodocumento) :
				//documento inicializado
				new OpenViewAction(EntidadesEmissaoView.ID, documento);

		//abre e personaliza a vista
		if (viewAction.run(false)){

			final EntidadesEmissaoView view=(EntidadesEmissaoView)viewAction.getView();

			//criacao de ACTIONS
			ColibriViewAction emitirRegistoAction=new ColibriViewAction(COMMANDS.Emitir){
				//guarda action anterior
				final ColibriViewAction action=view.getActions().get(COMMANDS.Emitir);
				@Override
				public void execute() {
					Debug.out(ID);
					view.cancelEditing();
					boolean enabled=view.getActions().get(COMMANDS.Gravar).isEnabled();
					//Gravar
					if(enabled) view.addJob(new GravarRegistoJob(view.getObjectContainer(),
							new MethodCallback<Entidadesdocumentos>(){
						@Override
						public boolean run(Entidadesdocumentos obj) {
							ErrorList error=obj.checkWarnings();
							return !ErrorDialog.open(error.errors());
						}}){
						@Override
						public void done(ErrorList error) {
							if (showError(error.errors())){
								view.cancelJobs();
							}else{
								view.updateViews();
							}
						}
					});
					//Emitir
					view.addJob(new CustomEmitirRelatorioJob(view.getParameters().getDocumentoentidades(), REPORT_ACTIONS.PREVIEW){
						@Override
						public void done(BIRTRender render) {
							if (!showError(render.errors())){
								new OpenViewAction(ReportView.ID, render).
									run(PREFERENCES.DETACH_REPORT_VIEW.value.getBoolean());
							}
						}
					});
					//Novo
					if (enabled) view.addJob(new NovoRegistoJob<Entidadesdocumentos>(view.getObjectContainer()){
						@Override
						public void done(Entidadesdocumentos documento) {
							view.reset(documento);
							view.resetFocus();
						}
					});
					view.runJobs();

				}
				@Override
				public boolean canExecute() {
					return true;
				}
				@Override
				public boolean isValid() {
					return action.isValid();
				}
			};

			//substitui ACTIONS na TOOLBAR
			view.getActions().addToolItem(emitirRegistoAction);

			//actualiza a TOOLBAR
			view.getActions().update();

		}

	}


	@Override
	public void dispose() {}


}
