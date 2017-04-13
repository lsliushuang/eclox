/*******************************************************************************
 * Copyright (C) 2003, 2004, 2007, 2013, Guillaume Brocker
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Guillaume Brocker - Initial API and implementation
 *
 ******************************************************************************/ 

package eclox.core.doxyfiles;


/**
 * Implements the default item that can be contained in a doxyfile.
 * 
 * A chunk is a piece of text extracted from e doxyfile. It can represent comments,
 * empty lines or whatever.
 * 
 * @author Guillaume Brocker
 */
public abstract class Chunk {
	
	/**
	 * the chunk owner (aka the doxyfile)
	 */
	private Doxyfile owner;
	
	/**
	 * Retrieves the chunk owner.
	 * 
	 * @return	the chunk owner
	 */
	public Doxyfile getOwner() {
		return owner;
	}
	
	/**
	 * Updates the chunk owner
	 * 
	 * @param	owner	the new owner
	 */
	public void setOwner( Doxyfile owner ) {
		this.owner = owner; 
	}
	
	/**
	 * Converts the chunk into a string representing its content.
	 * 
	 * @return	a string containing the chunk content
	 */
	public abstract String toString();
	
}