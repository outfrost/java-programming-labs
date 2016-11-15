/*
 * CollectionView - a wrapper class for displaying contents of various types of collections of custom objects in Swing tables
 *
 * Author: Iwo Bujkiewicz
 * Date: 13 Nov 2016
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Dimension;
import java.util.Collection;

final class RoomCollectionView extends JScrollPane {
	
	private JTable table;
	private DefaultTableModel tableModel;
	
	RoomCollectionView(String title) {
		this.tableModel = new DefaultTableModel(new String[] { title, "", "" }, 0);
		this.table = new JTable(tableModel);
		setViewportView(table);
		setPreferredSize(new Dimension(120, 220));
	}
	
	void refresh(Collection<Room> collection) {
		tableModel.setRowCount(0);
		for (Room entry : collection)
			tableModel.addRow(new String[] { entry.getBuilding(), entry.getRoomNumber(), entry.getDescription() });
	}
}
