/**********************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: 
 * IBM - Initial API and implementation
 **********************************************************************/
package org.eclipse.cdt.core.build.managed;

import org.eclipse.core.resources.IResource;

/**
 * This class represents targets for the managed build process.  A target
 * is some type of resource built using a given collection of tools.
 */
public interface ITarget {

	/**
	 * Gets the name for the target.
	 * 
	 * @return
	 */
	public String getName();
	
	/**
	 * Gets the parent for the target.
	 * 
	 * @return
	 */
	public ITarget getParent();
	
	/**
	 * Gets the resource that this target is applied to.
	 * 
	 * @return
	 */
	public IResource getOwner();
	
	/**
	 * Returns the list of platform specific tools associated with this
	 * platform.
	 * 
	 * @return
	 */
	public ITool[] getTools();

	/**
	 * Returns all of the configurations defined by this target.
	 * @return
	 */
	public IConfiguration[] getConfigurations();

	/**
	 * Creates a new configuration for the given resource.
	 * 
	 * @param resource
	 * @return
	 */
	public IConfiguration addConfiguration(IResource resource)
		throws BuildException;
	
	/**
	 * Creates a new configuration for the given resource based on the parent config
	 * @param resource
	 * @param parentConfig
	 * @return
	 */
	public IConfiguration addConfiguration(IResource resource, IConfiguration parentConfig)
		throws BuildException;

}
