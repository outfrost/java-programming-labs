/*
 *  Program EdytorGraficzny - aplikacja z graficznym interfejsem
 *   - obsługa zdarzeń od klawiatury, myszki i innych elementów GUI.
 *
 *  Autor: Paweł Rogalinski, Iwo Bujkiewicz
 *   Data: 04.11.2016
 */

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import static java.awt.event.KeyEvent.VK_UP;


abstract class Figure {
	
	static Random random = new Random();
	
	private boolean selected = false;
	
	public boolean isSelected() {
		return selected;
	}
	
	public void select() {
		selected = true;
	}
	
	public void select(boolean z) {
		selected = z;
	}
	
	public void unselect() {
		selected = false;
	}
	
	protected void setColor(Graphics g) {
		if (selected) g.setColor(Color.RED);
		else g.setColor(Color.BLACK);
	}
	
	public abstract boolean isInside(float px, float py);
	
	public boolean isInside(int px, int py) {
		return isInside((float) px, (float) py);
	}
	
	protected String properties() {
		String s = String.format("  Pole: %.0f  Obwod: %.0f", computeArea(), computePerimeter());
		if (isSelected()) s = s + "   [SELECTED]";
		return s;
	}
	
	abstract String getName();
	
	abstract float getX();
	
	abstract float getY();
	
	abstract float computeArea();
	
	abstract float computePerimeter();
	
	abstract void move(float dx, float dy);
	
	abstract void scale(float s);
	
	abstract void draw(Graphics g);
	
	@Override
	public String toString() {
		return getName();
	}
	
}


class Point extends Figure {
	
	private float x;
	private float y;
	
	Point() {
		this.x = random.nextFloat() * 400;
		this.y = random.nextFloat() * 400;
	}
	
	Point(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean isInside(float px, float py) {
		// by umożliwić zaznaczanie punktu myszkš
		// miejsca odległe nie więcej niż 6 leżš wewnštrz
		return (Math.sqrt((x - px) * (x - px) + (y - py) * (y - py)) <= 6);
	}
	
	
	@Override
	String getName() {
		return "Point(" + x + ", " + y + ")";
	}
	
	@Override
	float getX() {
		return x;
	}
	
	@Override
	float getY() {
		return y;
	}
	
	@Override
	float computeArea() {
		return 0;
	}
	
	@Override
	float computePerimeter() {
		return 0;
	}
	
	@Override
	void move(float dx, float dy) {
		x += dx;
		y += dy;
	}
	
	@Override
	void scale(float s) {
	}
	
	@Override
	void draw(Graphics g) {
		setColor(g);
		g.fillOval((int) (x - 3), (int) (y - 3), 6, 6);
	}
	
	String toStringXY() {
		return "(" + x + " , " + y + ")";
	}
	
}


class Circle extends Point {
	private float r;
	
	Circle() {
		super();
		r = random.nextFloat() * 100;
	}
	
	Circle(float px, float py, float pr) {
		super(px, py);
		r = pr;
	}
	
	@Override
	public boolean isInside(float px, float py) {
		return (Math.sqrt((getX() - px) * (getX() - px) + (getY() - py) * (getY() - py)) <= r);
	}
	
	@Override
	String getName() {
		return "Circle(" + getX() + ", " + getY() + ")";
	}
	
	@Override
	float computeArea() {
		return (float) Math.PI * r * r;
	}
	
	@Override
	float computePerimeter() {
		return (float) Math.PI * r * 2;
	}
	
	@Override
	void scale(float s) {
		r *= s;
	}
	
	@Override
	void draw(Graphics g) {
		setColor(g);
		g.drawOval((int) (getX() - r), (int) (getY() - r), (int) (2 * r), (int) (2 * r));
	}
	
}


class Triangle extends Figure {
	private Point point1, point2, point3;
	
	Triangle() {
		point1 = new Point();
		point2 = new Point();
		point3 = new Point();
	}
	
	Triangle(Point p1, Point p2, Point p3) {
		point1 = p1;
		point2 = p2;
		point3 = p3;
	}
	
	@Override
	public boolean isInside(float px, float py) {
		float d1, d2, d3;
		d1 = px * (point1.getY() - point2.getY()) + py * (point2.getX() - point1.getX()) +
			     (point1.getX() * point2.getY() - point1.getY() * point2.getX());
		d2 = px * (point2.getY() - point3.getY()) + py * (point3.getX() - point2.getX()) +
			     (point2.getX() * point3.getY() - point2.getY() * point3.getX());
		d3 = px * (point3.getY() - point1.getY()) + py * (point1.getX() - point3.getX()) +
			     (point3.getX() * point1.getY() - point3.getY() * point1.getX());
		return ((d1 <= 0) && (d2 <= 0) && (d3 <= 0)) || ((d1 >= 0) && (d2 >= 0) && (d3 >= 0));
	}
	
	@Override
	String getName() {
		return "Triangle{" + point1.toStringXY() +
			       point2.toStringXY() +
			       point3.toStringXY() + "}";
	}
	
	@Override
	float getX() {
		return (point1.getX() + point2.getX() + point3.getX()) / 3;
	}
	
	@Override
	float getY() {
		return (point1.getY() + point2.getY() + point3.getY()) / 3;
	}
	
	@Override
	float computeArea() {
		float a = (float) Math.sqrt((point1.getX() - point2.getX()) * (point1.getX() - point2.getX()) +
			                            (point1.getY() - point2.getY()) * (point1.getY() - point2.getY()));
		float b = (float) Math.sqrt((point2.getX() - point3.getX()) * (point2.getX() - point3.getX()) +
			                            (point2.getY() - point3.getY()) * (point2.getY() - point3.getY()));
		float c = (float) Math.sqrt((point1.getX() - point3.getX()) * (point1.getX() - point3.getX()) +
			                            (point1.getY() - point3.getY()) * (point1.getY() - point3.getY()));
		float p = (a + b + c) / 2;
		return (float) Math.sqrt(p * (p - a) * (p - b) * (p - c));
	}
	
	@Override
	float computePerimeter() {
		float a = (float) Math.sqrt((point1.getX() - point2.getX()) * (point1.getX() - point2.getX()) +
			                            (point1.getY() - point2.getY()) * (point1.getY() - point2.getY()));
		float b = (float) Math.sqrt((point2.getX() - point3.getX()) * (point2.getX() - point3.getX()) +
			                            (point2.getY() - point3.getY()) * (point2.getY() - point3.getY()));
		float c = (float) Math.sqrt((point1.getX() - point3.getX()) * (point1.getX() - point3.getX()) +
			                            (point1.getY() - point3.getY()) * (point1.getY() - point3.getY()));
		return a + b + c;
	}
	
	@Override
	void move(float dx, float dy) {
		point1.move(dx, dy);
		point2.move(dx, dy);
		point3.move(dx, dy);
	}
	
	@Override
	void scale(float s) {
		Point sr1 = new Point((point1.getX() + point2.getX() + point3.getX()) / 3,
			                     (point1.getY() + point2.getY() + point3.getY()) / 3);
		point1.scale(s);
		point2.scale(s);                                                                                // Seriously...
		point3.scale(s);
		Point sr2 = new Point((point1.getX() + point2.getX() + point3.getX()) / 3,
			                     (point1.getY() + point2.getY() + point3.getY()) / 3);
		float dx = sr1.getX() - sr2.getX();
		float dy = sr1.getY() - sr2.getY();
		point1.move(dx, dy);
		point2.move(dx, dy);
		point3.move(dx, dy);
	}
	
	@Override
	void draw(Graphics g) {
		setColor(g);
		g.drawLine((int) point1.getX(), (int) point1.getY(),
			(int) point2.getX(), (int) point2.getY());
		g.drawLine((int) point2.getX(), (int) point2.getY(),
			(int) point3.getX(), (int) point3.getY());
		g.drawLine((int) point3.getX(), (int) point3.getY(),
			(int) point1.getX(), (int) point1.getY());
	}
	
}

class Line extends Figure {
	
	private Point start, end;
	
	// Metoda używa promienia chwytania wskaźnika myszy zdefiniowanego dla punktu.
	@Override
	public boolean isInside(float px, float py) {
		float projectionCoefficient = ((px - start.getX()) * (end.getX() - start.getX()) + (py - start.getY()) * (end.getY() - start.getY())) / (float) (Math.pow(start.getX() - end.getX(), 2.0d) + Math.pow(start.getY() - end.getY(), 2.0d));
		if (projectionCoefficient >= 0.0f) {
			if (projectionCoefficient <= 1.0f) {
				Point projection = new Point(projectionCoefficient * (end.getX() - start.getX()) + start.getX(), projectionCoefficient * (end.getY() - start.getY()) + start.getY());
				return projection.isInside(px, py);
			} else return end.isInside(px, py);
		} else return start.isInside(px, py);
	}
	
	@Override
	String getName() {
		return "Line{" + start.getName() + ", " + end.getName() + "}";
	}
	
	@Override
	float getX() {
		return start.getX();
	}
	
	@Override
	float getY() {
		return start.getY();
	}
	
	@Override
	float computeArea() {
		return 0.0f;
	}
	
	@Override
	float computePerimeter() {
		return 0.0f;
	}
	
	@Override
	void move(float dx, float dy) {
		start.move(dx, dy);
		end.move(dx, dy);
	}
	
	@Override
	void scale(float s) {
		Point oldStart = new Point(start.getX(), start.getY());
		start.move((s - 1) * 0.5f * (oldStart.getX() - end.getX()), (s - 1) * 0.5f * (oldStart.getY() - end.getY()));
		end.move((s - 1) * 0.5f * (end.getX() - oldStart.getX()), (s - 1) * 0.5f * (end.getY() - oldStart.getY()));
	}
	
	@Override
	void draw(Graphics g) {
		setColor(g);
		g.drawLine((int) start.getX(), (int) start.getY(), (int) end.getX(), (int) end.getY());
	}
}

class Rectangle extends Figure {
	
	private Point start;
	private float width, height;
	
	Rectangle() {
		this.start = new Point();
		this.width = random.nextFloat();
		this.height = random.nextFloat();
	}
	
	Rectangle(Point start, float width, float height) {
		this.start = start;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public boolean isInside(float px, float py) {
		return px >= start.getX() && px <= start.getX() + width && py >= start.getY() && py <= start.getY() + height;
	}
	
	@Override
	String getName() {
		return "Rectangle(" + start.getX() + ", " + start.getY() + ", " + width + ", " + height + ")";
	}
	
	@Override
	float getX() {
		return start.getX();
	}
	
	@Override
	float getY() {
		return start.getY();
	}
	
	@Override
	float computeArea() {
		return width * height;
	}
	
	@Override
	float computePerimeter() {
		return 2 * (width + height);
	}
	
	@Override
	void move(float dx, float dy) {
		start.move(dx, dy);
	}
	
	@Override
	void scale(float s) {
		width *= s;
		height *= s;
	}
	
	@Override
	void draw(Graphics g) {
		setColor(g);
		g.drawRect((int) start.getX(), (int) start.getY(), (int) width, (int) height);
	}
}

class Ellipse extends Figure {
	
	private Point start;
	private float width, height;
	
	Ellipse() {
		start = new Point();
		width = random.nextFloat();
		height = random.nextFloat();
	}
	
	Ellipse(Point start, float width, float height) {
		this.start = start;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public boolean isInside(float px, float py) {
		return px >= start.getX() && px <= start.getX() + width && py >= start.getY() && py <= start.getY() + height;
	}
	
	@Override
	String getName() {
		return "Ellipse(" + start.getX() + ", " + start.getY() + ", " + width + ", " + height + ")";
	}
	
	@Override
	float getX() {
		return start.getX();
	}
	
	@Override
	float getY() {
		return start.getY();
	}
	
	@Override
	float computeArea() {
		return (float) Math.PI * 0.25f * width * height;
	}
	
	@Override
	float computePerimeter() {
		return (float) Math.PI * (0.75f * (width + height) - (float) Math.sqrt(0.25f * width * height));
	}
	
	@Override
	void move(float dx, float dy) {
		start.move(dx, dy);
	}
	
	@Override
	void scale(float s) {
		width *= s;
		height *= s;
	}
	
	@Override
	void draw(Graphics g) {
		setColor(g);
		g.drawOval((int) start.getX(), (int) start.getY(), (int) width, (int) height);
	}
}


class Picture extends JPanel implements KeyListener, MouseListener {
	
	private static final long serialVersionUID = 1L;
	
	
	Vector<Figure> figures = new Vector<Figure>();
	
	
	/*
	 * UWAGA: ta metoda będzie wywoływana automatycznie przy każdej potrzebie
	 * odrysowania na ekranie zawartoci panelu
	 *
	 * W tej metodzie NIE WOLNO !!! wywoływać metody repaint()
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Figure f : figures)
			f.draw(g);
	}
	
	
	void addFigure(Figure fig) {
		for (Figure f : figures) {
			f.unselect();
		}
		fig.select();
		figures.add(fig);
		repaint();
	}
	
	
	void moveAllSelectedFigures(float dx, float dy) {
		for (Figure f : figures) {
			if (f.isSelected()) f.move(dx, dy);
		}
		repaint();
	}
	
	void scaleAllSelectedFigures(float s) {
		for (Figure f : figures) {
			if (f.isSelected()) f.scale(s);
		}
		repaint();
	}
	
	public String toString() {
		String str = "Rysunek{ ";
		for (Figure f : figures)
			str += f.toString() + "\n         ";
		str += "}";
		return str;
	}
	
	
	/*
	 *  Impelentacja interfejsu KeyListener - obsługa zdarzeń generowanych
	 *  przez klawiaturę gdy focus jest ustawiony na ten obiekt.
	 */
	public void keyPressed(KeyEvent evt) {
		//Virtual keys (arrow keys, function keys, etc) - handled with keyPressed() listener.
		int keyCode = evt.getKeyCode();
		if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_DELETE) {
			int dist = evt.isShiftDown() ? 10 : 1;
			switch (keyCode) {
				case VK_UP:
					moveAllSelectedFigures(0, -dist);
					break;
				case KeyEvent.VK_DOWN:
					moveAllSelectedFigures(0, dist);
					break;
				case KeyEvent.VK_LEFT:
					moveAllSelectedFigures(-dist, 0);
					break;
				case KeyEvent.VK_RIGHT:
					moveAllSelectedFigures(dist, 0);
					break;
				case KeyEvent.VK_DELETE:
					Iterator<Figure> i = figures.iterator();
					while (i.hasNext()) {
						Figure figure = i.next();
						if (figure.isSelected())
							i.remove();
					}
					repaint();
					break;
			}
		}
	}
	
	public void keyReleased(KeyEvent evt) {
	}
	
	public void keyTyped(KeyEvent evt)
	//Characters (a, A, #, ...) - handled in the keyTyped() listener.
	{
		char znak = evt.getKeyChar();
		switch (znak) {
			case 'p':
				addFigure(new Point());
				break;
			case 'c':
				addFigure(new Circle());
				break;
			case 't':
				addFigure(new Triangle());
				break;
			case 'l':
				addFigure(new Line());
				break;
			case 'r':
				addFigure(new Rectangle());
				break;
			case 'e':
				addFigure(new Ellipse());
				break;
			
			case '+':
				scaleAllSelectedFigures(1.1f);
				break;
			case '-':
				scaleAllSelectedFigures(0.9f);
				break;
		}
	}
	
	
	/*
	 * Implementacja interfejsu MouseListener - obsługa zdarzeń generowanych przez myszkę
	 * gdy kursor myszki jest na tym panelu
	 */
	public void mouseClicked(MouseEvent e)
	// Invoked when the mouse button has been clicked (pressed and released) on a component.
	{
		int px = e.getX();
		int py = e.getY();
		for (Figure f : figures) {
			if (e.isAltDown() == false) f.unselect();
			if (f.isInside(px, py)) f.select(!f.isSelected());
		}
		repaint();
	}
	
	public void mouseEntered(MouseEvent e)
	//Invoked when the mouse enters a component.
	{
	}
	
	public void mouseExited(MouseEvent e)
	//Invoked when the mouse exits a component.
	{
	}
	
	
	public void mousePressed(MouseEvent e)
	// Invoked when a mouse button has been pressed on a component.
	{
	}
	
	public void mouseReleased(MouseEvent e)
	//Invoked when a mouse button has been released on a component.
	{
	}
	
}


public class GraphicEditor extends JFrame implements ActionListener {
	
	
	private static final long serialVersionUID = 3727471814914970170L;
	
	
	private final String DESCRIPTION = "OPIS PROGRAMU\n\n" + "Aktywna klawisze:\n"
		                                   + "   strzalki ==> przesuwanie figur\n"
		                                   + "   SHIFT + strzalki ==> szybkie przesuwanie figur\n"
		                                   + "   +,-  ==> powiekszanie, pomniejszanie\n"
		                                   + "   DEL  ==> kasowanie figur\n"
		                                   + "   p  ==> dodanie nowego punktu\n"
		                                   + "   c  ==> dodanie nowego kola\n"
		                                   + "   t  ==> dodanie nowego trojkata\n"
		                                   + "\nOperacje myszka:\n" + "   klik ==> zaznaczanie figur\n"
		                                   + "   ALT + klik ==> zmiana zaznaczenia figur\n"
		                                   + "   przeciaganie ==> przesuwanie figur";
	
	
	protected Picture picture;
	
	private JMenu[] menu = {new JMenu("Figury"),
		new JMenu("Edytuj")};
	
	private JMenuItem[] items = {new JMenuItem("Punkt"),
		new JMenuItem("Kolo"),
		new JMenuItem("Trojkat"),
		new JMenuItem("Wypisz wszystkie"),
		new JMenuItem("Przesun w gore"),
		new JMenuItem("Przesun w dol"),
		new JMenuItem("Powieksz"),
		new JMenuItem("Pomniejsz"),
	};
	
	private JButton buttonPoint = new JButton("Punkt");
	private JButton buttonCircle = new JButton("Kolo");
	private JButton buttonTriangle = new JButton("Trojkat");
	
	
	public GraphicEditor() {
		super("Edytor graficzny");
		setSize(400, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		for (int i = 0; i < items.length; i++)
			items[i].addActionListener(this);
		
		// dodanie opcji do menu "Figury"
		menu[0].add(items[0]);
		menu[0].add(items[1]);
		menu[0].add(items[2]);
		menu[0].addSeparator();
		menu[0].add(items[3]);
		
		// dodanie opcji do menu "Edytuj"
		menu[1].add(items[4]);
		menu[1].add(items[5]);
		menu[1].addSeparator();
		menu[1].add(items[6]);
		menu[1].add(items[7]);
		
		// dodanie do okna paska menu
		JMenuBar menubar = new JMenuBar();
		for (int i = 0; i < menu.length; i++)
			menubar.add(menu[i]);
		setJMenuBar(menubar);
		
		picture = new Picture();
		picture.addKeyListener(picture);
		picture.setFocusable(true);
		picture.addMouseListener(picture);
		picture.setLayout(new FlowLayout());
		
		buttonPoint.addActionListener(this);
		buttonCircle.addActionListener(this);
		buttonTriangle.addActionListener(this);
		
		picture.add(buttonPoint);
		picture.add(buttonCircle);
		picture.add(buttonTriangle);
		
		setContentPane(picture);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent evt) {
		Object zrodlo = evt.getSource();
		
		if (zrodlo == buttonPoint)
			picture.addFigure(new Point());
		if (zrodlo == buttonCircle)
			picture.addFigure(new Circle());
		if (zrodlo == buttonTriangle)
			picture.addFigure(new Triangle());
		
		if (zrodlo == items[0])
			picture.addFigure(new Point());
		if (zrodlo == items[1])
			picture.addFigure(new Circle());
		if (zrodlo == items[2])
			picture.addFigure(new Triangle());
		if (zrodlo == items[3])
			JOptionPane.showMessageDialog(null, picture.toString());
		
		if (zrodlo == items[4])
			picture.moveAllSelectedFigures(0, -10);
		if (zrodlo == items[5])
			picture.moveAllSelectedFigures(0, 10);
		if (zrodlo == items[6])
			picture.scaleAllSelectedFigures(1.1f);
		if (zrodlo == items[7])
			picture.scaleAllSelectedFigures(0.9f);
		
		picture.requestFocus(); // przywrocenie ogniskowania w celu przywrocenia
		// obslugi zadarezń pd klawiatury
		repaint();
	}
	
	public static void main(String[] args) {
		new GraphicEditor();
	}
	
}

