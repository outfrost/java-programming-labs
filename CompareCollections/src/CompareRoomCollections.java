/*
 * CompareCollections - an application class for visualising differences and similarities between various collection types when handling entries of custom objects
 *
 * Author: Iwo Bujkiewicz
 * Date: 13 Nov 2016
 */

import javax.swing.*;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

final class CompareRoomCollections extends JFrame implements ActionListener {
	
	private LinkedHashMap<RoomCollectionView, Collection<Room>> collections;
	
	private JLabel buildingInputFieldLabel = new JLabel("Budynek");
	private JTextField buildingInputField = new JTextField(10);
	private JLabel roomNumberInputFieldLabel = new JLabel("Nr pomieszczenia");
	private JTextField roomNumberInputField = new JTextField(10);
	private JLabel descriptionInputFieldLabel = new JLabel("Opis");
	private JTextField descriptionInputField = new JTextField(10);
	private JButton addCollectionEntryButton = new JButton("Dodaj");
	private JButton removeCollectionEntryButton = new JButton("Usuń");
	private JButton clearCollectionsButton = new JButton("Wyczyść");
	private JButton sortListsButton = new JButton("Sortuj listy");
	private JButton showAboutMessageButton = new JButton("O programie");
	
	CompareRoomCollections() {
		super("Porównanie działania kolekcji");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(660, 356);
		
		this.collections = new LinkedHashMap<>();
		collections.put(new RoomCollectionView("Vector"), new Vector<>());
		collections.put(new RoomCollectionView("ArrayList"), new ArrayList<>());
		collections.put(new RoomCollectionView("LinkedList"), new LinkedList<>());
		collections.put(new RoomCollectionView("HashSet"), new HashSet<>());
		collections.put(new RoomCollectionView("TreeSet"), new TreeSet<>());
		
		JPanel wrapper = new JPanel();
		
		JPanel inputPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel dataPanel = new JPanel();
		
		inputPanel.add(buildingInputFieldLabel);
		inputPanel.add(buildingInputField);
		inputPanel.add(roomNumberInputFieldLabel);
		inputPanel.add(roomNumberInputField);
		inputPanel.add(descriptionInputFieldLabel);
		inputPanel.add(descriptionInputField);
		
		buttonPanel.add(addCollectionEntryButton);
		buttonPanel.add(removeCollectionEntryButton);
		buttonPanel.add(clearCollectionsButton);
		buttonPanel.add(sortListsButton);
		buttonPanel.add(showAboutMessageButton);
		
		for (Component component : buttonPanel.getComponents()) {
			if (component instanceof AbstractButton)
				((AbstractButton) component).addActionListener(this);
		}
		
		collections.keySet().forEach(roomCollectionView -> dataPanel.add(roomCollectionView));
		
		wrapper.add(inputPanel);
		wrapper.add(buttonPanel);
		wrapper.add(dataPanel);
		
		setContentPane(wrapper);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addCollectionEntryButton) {
			collections.forEach((roomCollectionView, collection) -> {
				collection.add(new Room(buildingInputField.getText(), roomNumberInputField.getText(), descriptionInputField.getText()));
				roomCollectionView.refresh(collection);
			});
		}
		else if (e.getSource() == removeCollectionEntryButton) {
			collections.forEach((roomCollectionView, collection) -> {
				collection.remove(new Room(buildingInputField.getText(), roomNumberInputField.getText(), descriptionInputField.getText()));
				roomCollectionView.refresh(collection);
			});
		}
		else if (e.getSource() == clearCollectionsButton) {
			collections.forEach((roomCollectionView, collection) -> {
				collection.clear();
				roomCollectionView.refresh(collection);
			});
		}
		else if (e.getSource() == sortListsButton) {
			collections.forEach((roomCollectionView, collection) -> {
				if (collection instanceof List)
					Collections.sort((List) collection);
				roomCollectionView.refresh(collection);
			});
		}
		else if (e.getSource() == showAboutMessageButton) {
			JOptionPane.showMessageDialog(this, "CompareRoomCollections v. 20161115\nAutor: Iwo Bujkiewicz\n15.11.2016");
		}
	}
	
	public static void main(String[] args) {
		new CompareRoomCollections();
	}
}
