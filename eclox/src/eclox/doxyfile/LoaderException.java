/*
	eclox : Doxygen plugin for Eclipse.
	Copyright (C) 2003 Guillaume Brocker

	This file is part of eclox.

	eclox is free software; you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation; either version 2 of the License, or
	any later version.

	eclox is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with eclox; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA	
*/


package eclox.doxyfile;


/**
 * Exception class for docygen data item creation errors.
 * 
 * @author gbrocker
 */
public class LoaderException extends Exception {

	/**
	 * Constructor.
	 * 
	 * @param	text	A string containing the message.
	 * @param	cause	The object that is the cause of the exception.
	 */
	public LoaderException( String text, java.lang.Throwable cause ) {
		super( text, cause );
	}
}