/*
	eclox : Doxygen plugin for Eclipse.
	Copyright (C) 2003-2004 Guillaume Brocker
	
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

package eclox.core;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import eclox.build.BuildInProgressError;
import eclox.build.BuildJob;
import eclox.ui.view.BuildLogView;

/**
 * Provides plugin-wide services.
 *  
 * @author gbrocker
 */
public final class Services {

	/**
	 * Launches the build of the specified file.
	 *
	 * @param	doxyfile	a file resource to build
	 * 
	 * @throws PartInitException	the build log view could not be displayed
	 * @throws BuildInProgreeError	a build is already in progrees
	 * 
	 * @author gbrocker
	 */
	public static void buildDoxyfile( IFile doxyfile ) throws PartInitException, BuildInProgressError {
		BuildLogView.show();
		BuildJob.getDefault().setDoxyfile(doxyfile);
		BuildJob.getDefault().schedule();
	}
	
	/**
	 * Show an error with the specified message.
	 * 
	 * @param message	A string containing the message to display.
	 */
	public static void showError( String message ) {
		Shell		shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		MessageBox	messageBox = new MessageBox( shell, SWT.ICON_ERROR | SWT.OK );
		
		messageBox.setText( "Eclox" );
		messageBox.setMessage( message );
		messageBox.open();		
	}
	
	/**
	 * Show an error message relative to the specified throwable object.
	 * 
	 * @param	throwable	The object for which an error message must be shown.
	 */
	public static void showError(Throwable throwable) {
		String	message = throwable.getMessage();
		
		if(message != null) {
			showError(message);
		}
		else {
			showError("Caught exception of class "+throwable.getClass().getName()+" with no messages.");
		}
	}
}