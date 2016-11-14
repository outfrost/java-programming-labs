/*
 * CollectionView
 *
 * Author: Iwo Bujkiewicz
 * Date: 13 Nov 2016
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;

final class CollectionView extends JScrollPane {
	
	private JTable table;
	private DefaultTableModel tableModel;
	
	CollectionView(String title) {
		this.tableModel = new DefaultTableModel(new String[] { title }, 0);
		this.table = new JTable(tableModel);
		setViewportView(table);
		setPreferredSize(new Dimension(106, 220));
	}
	
	void refresh(Collection<String> collection) {
		tableModel.setRowCount(0);
		for (String entry : collection)
			tableModel.addRow(new String[] { entry });
	}
}
