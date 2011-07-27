/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package example.api;

import rcpcolibri.ui.workbench.views.IView;
import example.api.database.ArtigosExample;
import example.api.database.DocumentosExample;

public class Examples {

	private IView view;

	private final ArtigosExample artigosExample=new ArtigosExample();
	private final DocumentosExample documentosExample=new DocumentosExample();
	private final ViewsExample viewsExample=new ViewsExample();
	private final PrintExample printExample=new PrintExample();

	public Examples() {

		//processou o login?
		if (LoginExample.processLogin()){

			//artigosExample.iterateArtigo();

			//documentosExample.storeDocumento();

			viewsExample.openArtigos();
			//view=viewsExample.openDocumentos();

			//printExample.backgroundProcess(view,1);
			//printExample.normalProcess(view,1);

		}

	}


}
