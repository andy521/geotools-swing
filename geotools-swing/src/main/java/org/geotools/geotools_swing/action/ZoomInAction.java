package org.geotools.geotools_swing.action;

import javax.swing.ImageIcon;

import org.geotools.geotools_swing.MapPane;
import org.geotools.geotools_swing.tools.ZoomInTool;

/**
 * 放大操作
 * 
 * @author sam
 *
 */
public class ZoomInAction extends AbstractMapAction {

	private static final long serialVersionUID = -5480011168098849229L;

	/**
	 * 放大操作
	 * 
	 * @param mapPane
	 */
	public ZoomInAction(MapPane mapPane) {
		super(mapPane);
		this.putValue(SMALL_ICON, new ImageIcon(this.getClass().getResource("/mActionZoomIn.png")));
		this.putValue(NAME, "");
		this.putValue(SHORT_DESCRIPTION, "放大");
	}

	@Override
	public void actionPerformed(java.awt.event.ActionEvent e) {
		if (this.getMapPane() != null) {
			this.getMapPane().setCursorTool(new ZoomInTool());
		}
	}

}
