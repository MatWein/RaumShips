package com.mw.raumships.client.gui.rings;

public interface ILinkable {

	/**
	 * If this block is already linked to a controller of some kind.
	 * 
	 * @return isLinked. Usually <code>linkedPos != null</code>.
	 */
	boolean isLinked();
}
