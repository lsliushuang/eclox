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

package eclox.ui.dialog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.IWorkbenchAdapter;

import eclox.core.Plugin;
import eclox.core.doxyfiles.ResourceCollector;


/**
 * Allow the user to choose one doxyfile among a list.
 * 
 * @author gbrocker
 */
public class DoxyfileSelecterDialog {
	
	/**
	 * Implement the tree contant provider for the dialog.
	 */
	private class MyContentProvider implements ITreeContentProvider {
		/**
		 * The doxyfile collection.
		 */
		private Collection m_input = null;
		
		/**
		 * Disposes of this content provider. This is called by the viewer when it is disposed.
		 */
		public void dispose() {}
		
		/**
		 * Notifies this content provider that the given viewer's input has been switched to a different element.
		 */
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			m_input = (Collection) newInput;
		}
		
		/**
		 * Returns the child elements of the given parent element.
		 *  
		 * @param	parentElement	the parent element 
		 *
		 * @return	an array of child elements
		 */
		public Object[] getChildren(Object parentElement) {
			Collection	children = new ArrayList();
			Iterator	doxyfileIt = m_input.iterator();
			
			while(doxyfileIt.hasNext()) {
				IFile	doxyfile = (IFile) doxyfileIt.next();
				
				if(doxyfile.getProject() == parentElement) {
					children.add(doxyfile);
				}				
			}			
			return children.toArray();
		}
		
		/**
		 * Returns the elements to display in the viewer when its input is set to the given element. These elements can be presented as rows in a table, items in a list, etc. The result is not modified by the viewer.
		 *  
		 * @param	inputElement	the input element
		 *  
		 * @return	the array of elements to display in the viewer
		 */
		public Object[] getElements(Object inputElement) {
			Collection	doxyfiles = (Collection) inputElement;
			Object[]	result = null;
			
			if(doxyfiles != null) {
				Collection	projects = new HashSet();
				Iterator	doxyfileIt = doxyfiles.iterator();
				
				while(doxyfileIt.hasNext() == true) {
					IFile	doxyfile = (IFile) doxyfileIt.next();
					
					projects.add(doxyfile.getProject());
				}			
				result = projects.toArray();
			}
			else {
				result = new Object[0];
			}
			return result;
		}
		
		/**
		 * Returns the parent for the given element, or null indicating that the parent can't be computed.
		 * 
		 * In this case the tree-structured viewer can't expand a given node correctly if requested.
		 * 
		 * @param	element	the element 
		 * 
		 * @return	the parent element, or null if it has none or if the parent cannot be computed.
		 */
		public Object getParent(Object element) {
			Object	result = null;
		
			if(element instanceof IProject) {
				return null;
			}
			else if(element instanceof IFile) {
				return ((IFile)element).getProject();
			}
			return result;
		}	
  
		/** 
		 * Returns whether the given element has children.
		 * 
		 * Intended as an optimization for when the viewer does not need the actual children. Clients may be able to implement this more efficiently than getChildren.
		 * 
		 * @param	element	the element
		 * 
		 * @return	true if the given element has children, and false if it has no children
		 */
		public boolean hasChildren(Object element) {
			return element instanceof IProject;
		}
	}
	
	/**
	 * Implement a label provider for the doxyfile selector tree vierwer. 
	 */
	private class MyLabelProvider extends LabelProvider {
		/**
		 * Returns the image for the label of the given element.
		 * The image is owned by the label provider and must not be disposed directly.
		 * Instead, dispose the label provider when no longer needed.
		 * 
		 * @param	element	the element for which to provide the label image 
		 * 
		 * @return	the image used to label the element,
		 * 			or null if there is no image for the given object
		 */
		public Image getImage(Object element) {
            // Pre-condition.
            assert element instanceof IResource;
            
            Image               result = null;
			IResource           resourse = (IResource) element;
            IWorkbenchAdapter   workbenchAdapter = (IWorkbenchAdapter) resourse.getAdapter( IWorkbenchAdapter.class );
			if( workbenchAdapter != null ) {
                result = workbenchAdapter.getImageDescriptor( element ).createImage();
            }
            return result;
		}		
		
		/**
		 * The LabelProvider implementation of this ILabelProvider method returns
		 * the element's toString string. Subclasses may override.
		 * 
		 * @param	element	the element for which to provide the label text
		 * 
		 * @return	the text string used to label the element,
		 * 			or null if there is no text label for the given object 
		 */
		public String getText(Object element) {
            // Pre-condition.
            assert element instanceof IResource;
            
            String              result = null;
            IResource           resourse = (IResource) element;
            IWorkbenchAdapter   workbenchAdapter = (IWorkbenchAdapter) resourse.getAdapter( IWorkbenchAdapter.class );
            if( workbenchAdapter != null ) {
                result = workbenchAdapter.getLabel( element );
            }
            return result;
		}
	}
	
	/**
	 * Implement a doxyfiel selection validator.
	 */
	private class MySelectionValidator implements ISelectionStatusValidator {
		/**
		 * Validates an array of elements and returns the resulting status.
		 * 
		 * @param	selection	The elements to validate
		 * 
		 * @return	The resulting status	
		 */
		public IStatus validate(Object[] selection) {
			IStatus	result = null;
			
			if(selection.length == 1 && selection[0] instanceof IFile) {
				result = new Status(
					Status.OK,
					Plugin.getDefault().getBundle().getSymbolicName(),
					0,
					"",
					null
				);
			}
			else {
				result = new Status(
					Status.ERROR,
					Plugin.getDefault().getBundle().getSymbolicName(),
					0,
					"You must choose a Doxyfile to build.",
					null);
			}
			return result;							
		}		
	}
	
	/**
	 * The selection dialog.
	 */
	private ElementTreeSelectionDialog selectionDialog;

	/**
	 * Constructor.
	 *
	 * @param	parentShell	The parent shell, or null to create a top-level shell.
	 */
	public DoxyfileSelecterDialog(Shell parentShell) {
		this.selectionDialog = new ElementTreeSelectionDialog(
			parentShell,
			new MyLabelProvider(),
			new MyContentProvider()
		);
	}
	
	/**
	 * Dispose all resources.
	 */
	public void dispose() {
		this.selectionDialog.close();
	}
	
	/**
	 * Retrieve the selected doxyfile.
	 * 
	 * @return	The selecter doxyfile, or null if none.
	 */
	public IFile getDoxyfile() {
		return (IFile) this.selectionDialog.getFirstResult();
	}
	
	/**
	 * Open the selection dialog.
	 * 
	 * @return	the dialog return value.
	 * 
	 * @throws CoreException
	 */
	public int open() throws CoreException {
		ResourceCollector	collector = ResourceCollector.run();
		
		this.selectionDialog.setAllowMultiple( false );
		this.selectionDialog.setInput( collector.getDoxyfiles() );
		this.selectionDialog.setValidator( new MySelectionValidator() );
        this.selectionDialog.setEmptyListMessage( "No Doxyfile found in opened projects." );
        this.selectionDialog.setMessage( "Select a Doxyfile to build:" );
        this.selectionDialog.setTitle( "Build Doxyfile" );

		// Query the current selection.
		ISelection	selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
		if(selection != null && selection instanceof IStructuredSelection) {
			this.selectionDialog.setInitialSelections(
				((IStructuredSelection)selection).toArray());
		}
		
		return this.selectionDialog.open();
	}
}