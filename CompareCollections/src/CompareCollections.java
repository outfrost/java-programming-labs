/*
 * CompareCollections - an application class for visualising differences and similarities between various collection types
 *
 * Author: Iwo Bujkiewicz
 * Date: 13 Nov 2016
 */

import javax.swing.*;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

final class CompareCollections extends JFrame implements ActionListener {
	
	private LinkedHashMap<CollectionView, Collection<String>> collections;
	
	private JTextField inputField = new JTextField(10);
	private JButton addCollectionEntryButton = new JButton("Dodaj");
	private JButton removeCollectionEntryButton = new JButton("Usuń");
	private JButton clearCollectionsButton = new JButton("Wyczyść");
	private JButton sortListsButton = new JButton("Sortuj listy");
	private JButton showAboutMessageButton = new JButton("O programie");
	
	CompareCollections() {
		super("Porównanie działania kolekcji");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(600, 320);
		
		this.collections = new LinkedHashMap<>();
		collections.put(new CollectionView("Vector"), new Vector<>());
		collections.put(new CollectionView("ArrayList"), new ArrayList<>());
		collections.put(new CollectionView("LinkedList"), new LinkedList<>());
		collections.put(new CollectionView("HashSet"), new HashSet<>());
		collections.put(new CollectionView("TreeSet"), new TreeSet<>());
		
		JPanel wrapper = new JPanel();
		
		JPanel buttonPanel = new JPanel();
		JPanel dataPanel = new JPanel();
		
		buttonPanel.add(inputField);
		buttonPanel.add(addCollectionEntryButton);
		buttonPanel.add(removeCollectionEntryButton);
		buttonPanel.add(clearCollectionsButton);
		buttonPanel.add(sortListsButton);
		buttonPanel.add(showAboutMessageButton);
		
		for (Component component : buttonPanel.getComponents()) {
			if (component instanceof AbstractButton)
				((AbstractButton) component).addActionListener(this);
		}
		
		collections.keySet().forEach(collectionView -> dataPanel.add(collectionView));
		
		wrapper.add(buttonPanel);
		wrapper.add(dataPanel);
		
		setContentPane(wrapper);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addCollectionEntryButton) {
			collections.forEach((collectionView, collection) -> {
				collection.add(inputField.getText());
				collectionView.refresh(collection);
			});
		}
		else if (e.getSource() == removeCollectionEntryButton) {
			collections.forEach((collectionView, collection) -> {
				collection.remove(inputField.getText());
				collectionView.refresh(collection);
			});
		}
		else if (e.getSource() == clearCollectionsButton) {
			collections.forEach((collectionView, collection) -> {
				collection.clear();
				collectionView.refresh(collection);
			});
		}
		else if (e.getSource() == sortListsButton) {
			collections.forEach((collectionView, collection) -> {
				if (collection instanceof List)
					Collections.sort((List) collection);
				collectionView.refresh(collection);
			});
		}
		else if (e.getSource() == showAboutMessageButton) {
			JOptionPane.showMessageDialog(this, "CompareCollections v. 20161114\nAutor: Iwo Bujkiewicz\n14.11.2016");
		}
	}
	
	public static void main(String[] args) {
		new CompareCollections();
	}
}
