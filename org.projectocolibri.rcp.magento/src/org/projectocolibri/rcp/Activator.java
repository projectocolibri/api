/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp;

import org.dma.eclipse.core.BundleUtils;
import org.osgi.framework.BundleContext;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.projectocolibri.rcp.magento";

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {}

	public void start(BundleContext context) throws Exception {
		System.out.println(PLUGIN_ID+" ACTIVATOR <start>");
		super.start(context);
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		System.out.println(PLUGIN_ID+" ACTIVATOR <stop>");
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public static String pathResolver(String fullPath) {
		return BundleUtils.pathResolver(plugin.getBundle(), fullPath);
	}

}
