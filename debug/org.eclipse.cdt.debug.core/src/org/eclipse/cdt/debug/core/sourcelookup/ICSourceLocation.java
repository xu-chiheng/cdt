/*******************************************************************************
 * Copyright (c) 2000, 2004 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     QNX Software Systems - Initial API and implementation
 *******************************************************************************/
package org.eclipse.cdt.debug.core.sourcelookup;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;

/**
 * 
 * A source location defines the location of a repository
 * of source code. A source location is capable of retrieving
 * source elements.
 * <p>
 * For example, a source location could be a project, zip/archive
 * file, or a directory in the file system.
 * </p>
 * 
 * @since Sep 23, 2002
 */
public interface ICSourceLocation extends IAdaptable
{
	/**
	 * Returns an object representing the source code
	 * for a file with the specified name, or <code>null</code>
	 * if none could be found. The source element 
	 * returned is implementation specific - for example, a
	 * resource, a local file, a zip file entry, etc.
	 * 
	 * @param name the name of the object for which source is being searched for
	 * 
	 * @return source element
	 * @exception CoreException if an exception occurs while searching for the specified source element
	 */
	Object findSourceElement( String name ) throws CoreException;

	/**
	 * Returns a memento for this source location from which this
	 * source location can be reconstructed.
	 * 
	 * @return a memento for this source location
	 * @exception CoreException if unable to create a memento
	 */
	String getMemento() throws CoreException;
	
	/**
	 * Initializes this source location from the given memento.
	 * 
	 * @param memento a memento generated by this source location
	 * @exception CoreException if unable to initialize this source
	 * 	location
	 */
	void initializeFrom( String memento ) throws CoreException;

	/**
	 * Returns whether to search for all source elements, or just the first match.
	 *  
	 * @return whether to search for all source elements, or just the first match
	 */
	boolean searchForDuplicateFiles();

	/**
	 * Sets the value of the 'search for duplicate source files' flag.
	 * 
	 * @param search - a value to set
	 */
	void setSearchForDuplicateFiles( boolean search );

	void dispose();
}
