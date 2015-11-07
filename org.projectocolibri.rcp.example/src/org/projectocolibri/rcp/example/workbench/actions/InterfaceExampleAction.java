/*******************************************************************************
 * 2008-2015 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.example.workbench.actions;

import org.dma.java.util.Debug;
import org.dma.java.util.ErrorList;

import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import org.projectocolibri.rcp.birt.report.BIRTRender;
import org.projectocolibri.rcp.birt.report.BIRTReport.REPORT_ACTIONS;
import org.projectocolibri.rcp.colibri.RCPcolibri.COMMANDS;
import org.projectocolibri.rcp.colibri.core.vars.IconVARS;
import org.projectocolibri.rcp.colibri.dao.database.ColibriDatabase;
import org.projectocolibri.rcp.colibri.dao.database.model.Documentosseries;
import org.projectocolibri.rcp.colibri.dao.database.model.Documentostipos;
import org.projectocolibri.rcp.colibri.dao.database.model.Entidadesdocumentos;
import org.projectocolibri.rcp.colibri.dao.database.populate.tables.DocumentostiposPopulate;
import org.projectocolibri.rcp.colibri.dao.database.populate.tables.EntidadesPopulate;
import org.projectocolibri.rcp.colibri.workbench.ColibriPerspective;
import org.projectocolibri.rcp.colibri.workbench.ColibriUI;
import org.projectocolibri.rcp.colibri.workbench.support.ColibriAction;
import org.projectocolibri.rcp.colibri.workbench.support.actions.OpenHelpViewAction;
import org.projectocolibri.rcp.colibri.workbench.support.views.actions.ColibriViewAction;
import org.projectocolibri.rcp.colibri.workbench.support.views.actions.OpenViewAction;
import org.projectocolibri.rcp.colibri.workbench.support.views.jobs.EmitirRelatorioJob;
import org.projectocolibri.rcp.colibri.workbench.support.views.jobs.GravarRegistoJob;
import org.projectocolibri.rcp.colibri.workbench.support.views.jobs.NovoRegistoJob;
import org.projectocolibri.rcp.colibri.workbench.views.artigos.ficheiro.ArtigosFicheiroView;
import org.projectocolibri.rcp.colibri.workbench.views.entidades.emissao.EntidadesEmissaoView;
import org.projectocolibri.rcp.example.DocumentosExample;
import org.projectocolibri.rcp.example.workbench.ExamplePerspective;
import org.projectocolibri.rcp.example.workbench.ExampleView;

public class InterfaceExampleAction extends ColibriAction implements IWorkbenchAction {

	public InterfaceExampleAction() {
		setText("#interface");
		setImageDescriptor(IconVARS.COOLBAR_AJUDA);
	}


	@Override
	public final void run() {

		//comuta para a perspectiva COLIBRI
		ColibriUI.changePerspective(ColibriPerspective.ID);

		//abre vista de EXEMPLO
		new OpenViewAction(ExampleView.ID).run();

		//comuta para a perspectiva EXEMPLO
		ColibriUI.changePerspective(ExamplePerspective.ID);

		//abre vista de FICHEIRO de ARTIGOS
		new OpenViewAction(ArtigosFicheiroView.ID).run();

		//exemplos de edicao de documentos
		Documentostipos tipodocumento=ColibriDatabase.loadDocumentostipos(
				DocumentostiposPopulate.RECORDS.clientes_factura.codigo);

		example1(tipodocumento);
		example2(tipodocumento);

	}


	private void example1(Documentostipos tipodocumento) {

		Documentosseries serie=ColibriDatabase.loadDocumentosseries(
				tipodocumento.getCodigo(), 0);

		Entidadesdocumentos documento=ColibriDatabase.loadEntidadesdocumentos(
				tipodocumento.getCodigo(), serie.getSerie(), serie.getNumero());

		if (documento==null) return;

		//cria ACTION para EDICAO de FACTURA
		OpenViewAction viewAction=new OpenViewAction(EntidadesEmissaoView.ID, documento);

		//abre e personaliza a vista
		if (viewAction.run(false)){

			final EntidadesEmissaoView view=(EntidadesEmissaoView)viewAction.getView();

			//criacao de JOBS
			final NovoRegistoJob novoRegistoJob=new NovoRegistoJob(view.getObjectContainer()){
				@Override
				public void done() {}
			};

			final GravarRegistoJob gravarRegistoJob=new GravarRegistoJob(view.getObjectContainer()){
				@Override
				public void done(ErrorList error) {
					if (showError(error.getErrors())){
						view.jobBatch.cancelJobs();
					}else{
						view.updateViews();
					}
				}
			};

			final EmitirRelatorioJob emitirRelatorioJob=new EmitirRelatorioJob(view.getObjectContainer(), REPORT_ACTIONS.PREVIEW){
				@Override
				public void done(BIRTRender render) {
					String printerName=view.getParameters().getTipodocumento().getImpressora();
					showError(render.output(printerName).getErrors());
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
					view.jobBatch.add(gravarRegistoJob);
					view.jobBatch.add(novoRegistoJob);
					view.jobBatch.schedule();
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
					if(enabled) view.jobBatch.add(gravarRegistoJob);
					view.jobBatch.add(emitirRelatorioJob);
					if(enabled) view.jobBatch.add(novoRegistoJob);
					view.jobBatch.schedule();
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


	private void example2(Documentostipos tipodocumento) {

		Entidadesdocumentos documento=new DocumentosExample().
				createDocumento(tipodocumento.getCodigo(),
						EntidadesPopulate.RECORDS.cliente_final.key, "1");

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
				final ColibriViewAction action=view.getActions().get(COMMANDS.Emitir);
				@Override
				public void execute() {
					Debug.out(ID);
					view.cancelEditing();
					boolean enabled=view.getActions().get(COMMANDS.Gravar).isEnabled();
					//Gravar
					if(enabled) view.jobBatch.add(new GravarRegistoJob(view.getObjectContainer()){
						@Override
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
						@Override
						public void done(BIRTRender render) {
							Debug.out("### DOCUMENTO ###", view.getParameters().getDocumentoentidades());
							String printerName=view.getParameters().getTipodocumento().getImpressora();
							showError(render.output(printerName).getErrors());
						}
					});
					//Novo
					if (enabled) view.jobBatch.add(new NovoRegistoJob(view.getObjectContainer()){
						@Override
						public void done() {}
					});
					view.jobBatch.schedule();

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
