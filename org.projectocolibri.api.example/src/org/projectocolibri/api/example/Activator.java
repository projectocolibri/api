/*******************************************************************************
 * 2008-2017 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.api.example;

import org.dma.eclipse.swt.graphics.ColorManager;
import org.dma.eclipse.swt.graphics.ImageManager;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator implements BundleActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = Activator.class.getPackage().getName();

	public Activator() {
		System.err.println(PLUGIN_ID+"(ACTIVATOR)");
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator
	 */
	@Override
	public void start(BundleContext context) throws Exception {}

	@Override
	public void stop(BundleContext context) throws Exception {
		ImageManager.CACHE.clear();
		ColorManager.CACHE.clear();
	}

}
