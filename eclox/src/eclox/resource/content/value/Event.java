/*
 * Created on Dec 9, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package eclox.resource.content.value;

/**
 * Implement the value event class.
 * 
 * @author gbrocker
 */
public class Event extends java.util.EventObject {
	/**
	 * The value that is concerned by the event.
	 */
	public Value m_value;
	
	/**
	 * Constructor.
	 * 
	 * @param value	The value that is concerned by the event.
	 */
	public Event( Value value ) {
		super( value );
		m_value = value;
	}
}