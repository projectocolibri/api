/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package example.api;

import rcpcolibri.ui.workbench.views.IView;

public class Examples {

	private IView view;

	public Examples() {

		//processou o login?
		if (LoginExample.processLogin()){

			openViews();
			backgroungPrint();
			//normalPrint();

		}

	}


	public void openViews() {
		try{
			ViewsExample viewsExample=new ViewsExample();
			viewsExample.openArtigos();
			view=viewsExample.openDocumentos();

		}catch(Exception e){
			e.printStackTrace();
		}

	}


	public void backgroungPrint() {
		try{
			PrintExample printExample=new PrintExample();
			printExample.backgroundProcess(view);

		}catch(Exception e){
			e.printStackTrace();
		}

	}


	public void normalPrint() {
		try{
			PrintExample printExample=new PrintExample();
			printExample.normalProcess(view);

		}catch(Exception e){
			e.printStackTrace();
		}

	}




}
