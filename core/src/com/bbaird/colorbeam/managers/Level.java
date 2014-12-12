package com.bbaird.colorbeam.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.bbaird.colorbeam.ColorBeam;
import com.bbaird.colorbeam.entities.Point;
import com.bbaird.colorbeam.entities.Triangle;
import com.bbaird.colorbeam.entities.Wormhole;

public class Level {	
	private float triangleWidth;
	private float triangleHeight;
	
	private static float HOFFSET;
	private static float VOFFSET;
	
	private static final float HSCALE = 1f;
	private static final float VSCALE = 1f;
	
	private int numSidesX;
	private int numSidesY;
	
	private int movesFor3Stars;
	private int movesFor2Stars;
	
	private Point[] points;
	private Triangle[] triangles;
	
	private final List<Point> startPoints;
	private final List<Point> endPoints;
	
	private final List<Color> startColors;
	private final List<Color> endColors;
	
	private List<Point> intersectPoints;
	private List<Color> intersectColors;
	
	private List<Boolean> directionalFlags;
	
	private final List<Integer> startDirections;
	private final List<Wormhole> wormholes;
	
	private boolean validStartPos;

	private boolean[] win;
	private boolean[] loops;
	
	private List<Point> currentPoints;
	private List<Integer> currentDirections;

	private List<List<Point> > trail;
	private List<List<Point> > previousTrail;

	private Triangle currentTriangle;
	private int moveCount;
	
	private boolean additive;
	
	public Level(String file) {
		
		//Will want to force scale the below dimensions to have equilateral triangles
		additive = Save.ad.getMixType();
		moveCount = 0;
		VOFFSET = (ColorBeam.HEIGHT - ColorBeam.HEIGHT * VSCALE) / 2;
		HOFFSET = (ColorBeam.WIDTH - ColorBeam.WIDTH * HSCALE) / 2;
		startPoints = new ArrayList<Point>();
		endPoints = new ArrayList<Point>();
		
		startColors = new ArrayList<Color>();
		endColors = new ArrayList<Color>();
		
		intersectPoints = new ArrayList<Point>();
		intersectColors = new ArrayList<Color>();
		startDirections = new ArrayList<Integer>();
		
		directionalFlags = new ArrayList<Boolean>();
		
		currentPoints = new ArrayList<Point>();
		currentDirections = new ArrayList<Integer>();
		wormholes = new ArrayList<Wormhole>();
		
		trail = new ArrayList<List<Point> >();
		previousTrail = new ArrayList<List<Point> >();
		
		// Assigns file input
        int currentObject = 0;
		List<String> objects = readFile(file);
		
		movesFor2Stars = Integer.parseInt(objects.get(currentObject++));
		movesFor3Stars = Integer.parseInt(objects.get(currentObject++));
		
		numSidesX = Integer.parseInt(objects.get(currentObject++));
		numSidesY = Integer.parseInt(objects.get(currentObject++));
		
		int startTriangleNum = Integer.parseInt(objects.get(currentObject++));
		
		for (int i = 0; i < startTriangleNum; i++) {
			int startX = Integer.parseInt(objects.get(currentObject++));
			int startY = Integer.parseInt(objects.get(currentObject++));
			startPoints.add(new Point(startX, startY));
		}		
		
		for (int i = 0; i < startTriangleNum; i++) {
			startPoints.get(i).setSide(objects.get(currentObject++));
		}
		
		for (int i = 0; i < startTriangleNum; i++) {
			int direction = Integer.parseInt(objects.get(currentObject++));
			startDirections.add(direction);
			if (direction % 120 == 0) directionalFlags.add(true);
			else directionalFlags.add(false);
			
		}
		
		for (int i = 0; i < startTriangleNum; i++) {
			startColors.add(getColor(objects.get(currentObject++)));
		}
		
		int endTriangleNum = Integer.parseInt(objects.get(currentObject++));
		for (int i = 0; i < endTriangleNum; i++) {
			int endX = Integer.parseInt(objects.get(currentObject++));
			int endY = Integer.parseInt(objects.get(currentObject++));
			endPoints.add(new Point(endX, endY));
		}		
		
		for (int i = 0; i < endTriangleNum; i++) {
			endPoints.get(i).setSide(objects.get(currentObject++));
		}
		
		for (int i = 0; i < endTriangleNum; i++) {
			endColors.add(getColor(objects.get(currentObject++)));
		}
		
		triangleWidth = (ColorBeam.WIDTH * HSCALE) / numSidesX;
		triangleHeight = (ColorBeam.HEIGHT * VSCALE) / numSidesY;
		
		System.out.println("before width: " + triangleWidth);
		System.out.println("before height: " + triangleHeight);
		System.out.println("horizontal ratio: " + (ColorBeam.WIDTH * HSCALE) / (numSidesX));
		System.out.println("vertiral ratio: " + (ColorBeam.HEIGHT * VSCALE) / (numSidesY));
		
		//Scales triangles to fit to screen vertically or horizontally
		if (triangleHeight > triangleWidth * Math.sqrt(3f)/2) {
			triangleHeight = (float) (triangleWidth * Math.sqrt(3f) / 2);
			VOFFSET = (ColorBeam.HEIGHT - triangleHeight * numSidesY) / 2;
		}
		else {
			triangleWidth = (float) (triangleHeight * 2 * Math.sqrt(3f) / 3);
			HOFFSET = (ColorBeam.WIDTH - triangleWidth * numSidesX) / 2;
		}
		
		System.out.println("after width: " + triangleWidth);
		System.out.println("after height: " + triangleHeight);
		System.out.println();
		
		createTriangles();
		setPointArray();
		
		int uniqueTriangles = (2 * numSidesX - 1) * numSidesY;
		
		List<String> tempWormholeList = new ArrayList<String>();
		for (int i = 0; i < uniqueTriangles; i++) {
			Triangle t = getTriangles()[i];
			
			
			if(objects.get(currentObject).equals("unavailable")) {
				t.setUnavailableTriangle();
			}
			else if(objects.get(currentObject).equals("empty")) {
				t.setEmptyTriangle();
			}
			else if(objects.get(currentObject).equals("regular-free")) {
				t.setFreeRegularTriangle();
			}
			else if(objects.get(currentObject).equals("regular-fixed")) {
				t.setFixedRegularTriangle();
			}
			else if(objects.get(currentObject).equals("absorbent-free")) {
				t.setFreeAbsorbentTriangle();
			}
			else if(objects.get(currentObject).equals("absorbent-fixed")) {
				t.setFixedAbsorbentTriangle();
			}
			else if(objects.get(currentObject).equals("glass-free")) {
				t.setFreeGlassTriangle();
			}
			else if(objects.get(currentObject).equals("glass-fixed")) {
				t.setFixedGlassTriangle();

			}
			else if(objects.get(currentObject).equals("transport-free")) {
				t.setFreeTransportTriangle();
				if (tempWormholeList.contains(objects.get(++currentObject))) {
					wormholes.get(Integer.parseInt(objects.get(currentObject))).setOtherTriangle(t);
				}
				else {
					wormholes.add(new Wormhole(t));
					tempWormholeList.add(objects.get(currentObject));
				}
			}
			else if(objects.get(currentObject).equals("transport-fixed")) {
				t.setFixedTransportTriangle();
				if (tempWormholeList.contains(objects.get(++currentObject))) {
					wormholes.get(Integer.parseInt(objects.get(currentObject))).setOtherTriangle(t);
				}
				else {
					wormholes.add(new Wormhole(t));
					tempWormholeList.add(objects.get(currentObject));
				}
			}
			
			else System.out.println("error in input!");
			
			currentObject++;
		}		
		
		for (Wormhole w : wormholes) {
			System.out.println(w.toString());
		}
		
		loops = new boolean[startTriangleNum];
		win = new boolean[endTriangleNum];
		

		createStartPoints(startPoints);
		createEndPoints(endPoints);
		
		currentTriangle = getTriangles()[0];
		
		initializeChain();
	}	
	
	public boolean getDirectionalFlag(int beam) {
		return directionalFlags.get(beam);
	}
	
	public void incrementMoveCount() {
		moveCount++;
	}
	
	public int getMoveCount() {
		return moveCount;
	}
	
	public List<Color> getIntersectColors() {
		return intersectColors;
	}
	
	public List<Point> getIntersectPoints() {
		return intersectPoints;
	}
	
	public void addIntersectColor(Color c) {
		intersectColors.add(c);
	}
	
	public void addIntersectPoint(Point ip) {
		intersectPoints.add(ip);
	}
	
	public List<Color> getStartColors() {
		return startColors;
	}
	
	public List<Color> getEndColors() {
		return endColors;
	}
		
	public void setCurrentTriangle(Triangle t) {
		currentTriangle = t;
	}
	
	public Triangle getCurrentTriangle() {
		return currentTriangle;
	}

	public List<Point> getCurrentPoints() {
		return currentPoints;
	}

	public void setCurrentPoint(int beam, Point p) {
		currentPoints.set(beam, p);
	}

	public int getCurrentDirection(int beam) {
		return currentDirections.get(beam);
	}

	public void setCurrentDirection(int beam, int direction) {
		currentDirections.set(beam, direction);
	}

	public List<List<Point>> getTrail() {
		return trail;
	}

	public void setPreviousTrail(List<List<Point>> trail) {
		previousTrail.clear();
		for (List<Point> oldList : trail) {
			List<Point> newList = new ArrayList<Point>();
			for (Point oldPoint: oldList) {
				newList.add(new Point(oldPoint));
			}
			previousTrail.add(newList);
		}
	}
	
	public List<List<Point>> getPreviousTrail() {
		return previousTrail;
	}

	public void setTrail(List<List<Point>> trail) {
		this.trail = trail;
	}

	public boolean[] getWin() {
		return win;
	}

	public void setWin(int i, boolean win) {
		this.win[i] = win;
	}

	public boolean isLoop(int i) {
		return loops[i];
	}

	public void setLoop(int pos, boolean flag) {
		this.loops[pos] = flag;
	}

	public boolean isValidStartPos() {
		return validStartPos;
	}

	public void setValidStartPos(boolean pos) {
		validStartPos = pos;
	}

	public int getStartDirection(int beam) {
		return startDirections.get(beam);
	}
	
	public List<Wormhole> getWormholes() {
		return wormholes;
	}
	
	public int getTrianglePos(Triangle t) {
		for (int i = 0; i < triangles.length; i++) {
			if (t.equals(triangles[i])) {
				return i;
			}
		}
		System.out.println("error in getTriangle in Level");
		return -1;
	}
	
	public void setTriangle(Triangle t, int pos) {
		triangles[pos] = t;
	}
	
	public void initializeChain() {
		trail.clear();
		previousTrail.clear();
		currentPoints.clear();
		currentDirections.clear();
		//changeDirections.clear();
		intersectColors.clear();
		intersectPoints.clear();
		// Reset potential wins
		for (int i = 0; i < endPoints.size(); i++) {
			setWin(i, false);
		}
		
		for (int i = 0; i < startPoints.size(); i++) {
			trail.add(new ArrayList<Point>());
			trail.get(i).add(startPoints.get(i));
			currentDirections.add(startDirections.get(i));
			currentPoints.add(startPoints.get(i));
			setLoop(i, false);
		}
		
		for (int i = 0; i < getEndPoints().size(); i++) {
			win[i] = false;
		}
	}

	private void createEndPoints(List<Point> end) {
		for (Point p : end) {
			Triangle t = getTriangleAt(p);
			p.setFloatPoints(t, p.getSide());
		}
	}
	
	private List<String> readFile(String file) {
		FileHandle handle = Gdx.files.internal(file);
		file = handle.readString();
		List<String> objects = new ArrayList<String>();
		Scanner scan = new Scanner(file);
		scan.useDelimiter(" ");
		while(scan.hasNextLine()) {
			String line = scan.nextLine();
			if (!line.contains("#") && !line.isEmpty()) {
				String[] words = line.split(" ");
				for (String word : words) {
					objects.add(word);
				}
			}
		}
		scan.close();
		return objects;
	}
	
	private void createStartPoints(List<Point> start) {
		for (Point p : start) {
			Triangle t = getTriangleAt(p);
			p.setFloatPoints(t, p.getSide());
		}
	}
	
	public void createIntersectPoints(List<Point> intersect) {
		for (Point p : intersect) {
			Triangle t = getTriangleAt(p);
			p.setFloatPoints(t, p.getSide());
		}
	}
	
	private void createTriangles() {
		triangles = new Triangle[(2*numSidesX - 1) * numSidesY];
		boolean isUp = false;
		for (int i = 0; i < numSidesY; i++) {
			for (int j = 0; j < 2 * numSidesX - 1; j++) {
				float x = triangleWidth / 2 + (triangleWidth * j / 2) + HOFFSET;
				float y;
				if(isUp) y = triangleHeight * i + triangleHeight / 3 + VOFFSET;
				else y = triangleHeight * i + triangleHeight * 2 / 3 + VOFFSET;
				triangles[i * (2 * numSidesX - 1) + j] = new Triangle(x, y, isUp, triangleWidth, triangleHeight, j, i);
				isUp = !isUp;
			}
		}
	}
	
	public boolean directionsAreSimilar(int d1, int d2) {
		if (d1 - d2 == 180 || d2 - d1 == 180 || d1 == d2) return true;
		else return false;
	}
	
	private void setPointArray() {
		points = new Point[numSidesX * numSidesY + numSidesY / 2 + numSidesX];
		boolean isEven = true;
		int location = 0;
		for (int i = 0; i <= numSidesY; i++) {
			for(int j = 0; j <= numSidesX; j++) {
				if(isEven) {
					if (j != numSidesX) {
						Point point = new Point(j * triangleWidth + triangleWidth / 2 + HOFFSET, i * triangleHeight + VOFFSET);
						points[location] = point;
						location++;
					}
				}
				else {
					Point point = new Point(j * triangleWidth + HOFFSET, i * triangleHeight + VOFFSET);
					points[location] = point;
					location++;
				}
			}
			isEven = !isEven;
		}
	}
	
	public boolean isPointInsideTriangle(float x, float y, Triangle t) {
	    float ax = t.getX1();
	    float ay = ColorBeam.HEIGHT - t.getY1();
	    float bx = t.getX2();
	    float by = ColorBeam.HEIGHT - t.getY2();
	    float cx = t.getX3();
	    float cy = ColorBeam.HEIGHT - t.getY3();
		
		float as_x = x-ax;
	    float as_y = y-ay;

	    boolean s_ab = (bx-ax)*as_y-(by-ay)*as_x > 0;

	    if((cx-ax)*as_y-(cy-ay)*as_x > 0 == s_ab) return false;
	    if((cx-bx)*(y-by)-(cy-by)*(x-bx) > 0 != s_ab) return false;
		
		return true;
	}
	
	public boolean isPointInsideTriangle(float x, float y, float ax, float ay,
			float bx, float by, float cx, float cy) {
		float as_x = x-ax;
	    float as_y = y-ay;

	    boolean s_ab = (bx-ax)*as_y-(by-ay)*as_x > 0;

	    if((cx-ax)*as_y-(cy-ay)*as_x > 0 == s_ab) return false;
	    if((cx-bx)*(y-by)-(cy-by)*(x-bx) > 0 != s_ab) return false;
		
		return true;
	}	
	
	public boolean isPointInsideHexagon(float x, float y, Triangle t) {
	    float ax = t.getX1();
	    float ay = ColorBeam.HEIGHT - t.getY1();
	    float bx = t.getX2();
	    float by = ColorBeam.HEIGHT - t.getY2();
	    float cx = t.getX3();
	    float cy = ColorBeam.HEIGHT - t.getY3();
	    
	    
	    if (t.isUp()) {
	    	if (isPointInsideTriangle(x, y, t.getCenterX(), ColorBeam.HEIGHT - t.getCenterY(), cx, cy, ax, ay - 2*triangleHeight/3)) return true; //top left
	    	if (isPointInsideTriangle(x, y, t.getCenterX(), ColorBeam.HEIGHT - t.getCenterY(), ax, ay, ax, ay - 2*triangleHeight/3)) return true; //left
	    	if (isPointInsideTriangle(x, y, t.getCenterX(), ColorBeam.HEIGHT - t.getCenterY(), ax, ay, cx, ay + triangleHeight/3)) return true; //bottom left
	    	if (isPointInsideTriangle(x, y, t.getCenterX(), ColorBeam.HEIGHT - t.getCenterY(), cx, cy, bx, by - 2*triangleHeight/3)) return true; //top right
	    	if (isPointInsideTriangle(x, y, t.getCenterX(), ColorBeam.HEIGHT - t.getCenterY(), bx, by, bx, by - 2*triangleHeight/3)) return true; //right
	    	if (isPointInsideTriangle(x, y, t.getCenterX(), ColorBeam.HEIGHT - t.getCenterY(), bx, by, cx, by + triangleHeight/3)) return true; //bottom right
	    }
	    else {
	    	if (isPointInsideTriangle(x, y, t.getCenterX(), ColorBeam.HEIGHT - t.getCenterY(), ax, ay, cx, ay - triangleHeight/3)) return true; //top left
	    	if (isPointInsideTriangle(x, y, t.getCenterX(), ColorBeam.HEIGHT - t.getCenterY(), ax, ay, ax, ay + 2*triangleHeight/3)) return true; //left
	    	if (isPointInsideTriangle(x, y, t.getCenterX(), ColorBeam.HEIGHT - t.getCenterY(), cx, cy, ax, ay + 2*triangleHeight/3)) return true; //bottom left
	    	if (isPointInsideTriangle(x, y, t.getCenterX(), ColorBeam.HEIGHT - t.getCenterY(), cx, cy, bx, by + 2*triangleHeight/3)) return true; //top right
	    	if (isPointInsideTriangle(x, y, t.getCenterX(), ColorBeam.HEIGHT - t.getCenterY(), bx, by, bx, by + 2*triangleHeight/3)) return true; //right
	    	if (isPointInsideTriangle(x, y, t.getCenterX(), ColorBeam.HEIGHT - t.getCenterY(), bx, by, cx, by - triangleHeight/3)) return true; //bottom right
	    }
	    
	    
	    
	    return false;
	}
	
	public Color getColor(String color) {
		Color c = new Color();
		
		if (additive) {
			if (color.equals("p1")) c.set(Color.RED);
			else if (color.equals("p2")) c.set(Color.BLUE);
			else if (color.equals("p3")) c.set(Color.GREEN);
			else if (color.equals("s1")) c.set(Color.MAGENTA);
			else if (color.equals("s2")) c.set(Color.CYAN);
			else if (color.equals("s3")) c.set(Color.YELLOW);
		}
		else {
			if (color.equals("p1")) c.set(Color.RED);
			else if (color.equals("p2")) c.set(Color.YELLOW);
			else if (color.equals("p3")) c.set(Color.BLUE);
			else if (color.equals("s1")) c.set(Color.ORANGE);
			else if (color.equals("s2")) c.set(Color.GREEN);
			else if (color.equals("s3")) c.set(Color.PURPLE);
		}
		return c;
	}
	
	public Color getColor(Color c1, Color c2) {
		float r, g, b;
		if (additive) {
			r = min(c1.r + c2.r, 255);
			g = min(c1.g + c2.g, 255);
			b = min(c1.b + c2.b, 255);
		}
		else {
			if ((c1.equals(Color.RED) && c2.equals(Color.YELLOW))
					|| (c2.equals(Color.RED) && c1.equals(Color.YELLOW))) return Color.ORANGE;
			else if ((c1.equals(Color.BLUE) && c2.equals(Color.YELLOW))
					|| (c2.equals(Color.BLUE) && c1.equals(Color.YELLOW))) return Color.GREEN;
			else if ((c1.equals(Color.RED) && c2.equals(Color.BLUE))
					|| (c2.equals(Color.RED) && c1.equals(Color.BLUE))) return Color.PURPLE;
			return new Color(0, 0, 0, 1);
		}
		return new Color(r, g, b, 1);
	}
	
	public Color getPrimaryColors(Color c, int pos) {
		if (additive) {
			if (c.equals(Color.YELLOW)) {
				if (pos == 1) return new Color(Color.RED);
				else return new Color(Color.GREEN);
			}
			if (c.equals(Color.CYAN))  {
				if (pos == 1) return new Color(Color.BLUE);
				else return new Color(Color.GREEN);
			}
			if (c.equals(Color.MAGENTA)) {
				if (pos == 1) return new Color(Color.RED);
				else return new Color(Color.BLUE);
			}
		}
		
		else {
			if (c.equals(Color.PURPLE)) {
				if (pos == 1) return new Color(Color.RED);
				else return new Color(Color.BLUE);
			}
			if (c.equals(Color.GREEN))  {
				if (pos == 1) return new Color(Color.YELLOW);
				else return new Color(Color.BLUE);
			}
			if (c.equals(Color.ORANGE)) {
				if (pos == 1) return new Color(Color.RED);
				else return new Color(Color.YELLOW);
			}
		}
		
		System.out.println("error in getting primary color");
		return null;
	}
	
	public boolean isSecondaryColor(Color c) {
		if (additive) {
			if (c.equals(Color.CYAN)) return true;
			if (c.equals(Color.MAGENTA)) return true;
			if (c.equals(Color.YELLOW)) return true;
		}
		else {
			if (c.equals(Color.PURPLE)) return true;
			if (c.equals(Color.GREEN)) return true;
			if (c.equals(Color.ORANGE)) return true;
		}
		return false;
	}
	
	public void swapPoints(Point a, Point b) {
	    String temp1 = a.getSide();
	    a.setSide(b.getSide());
	    b.setSide(temp1);
	    
	    int temp2 = a.getIntX();
	    a.setIntX(b.getIntX());
	    b.setIntX(temp2);
	    
	    int temp3 = a.getIntY();
	    a.setIntY(b.getIntY());
	    b.setIntY(temp3);
	    
	    float temp4 = a.getFloatX();
	    a.setFloatX(b.getFloatX());
	    b.setFloatX(temp4);
	    
	    float temp5 = a.getFloatY();
	    a.setFloatY(b.getFloatY());
	    b.setFloatY(temp5);
	}
	
	public int getStars() {
		if (moveCount <= movesFor3Stars) return 3;
		else if (moveCount <= movesFor2Stars) return 2;
		else return 1;
		
	}
	
	public float getCenter(float a, float b, float c) {
		return (a + b + c) / 3;
	}
	
	public int getSegmentsX() {
		return numSidesX;
	}
	
	public int getSegmentsY() {
		return numSidesY;
	}
	
	public float getTriangleWidth() {
		return triangleWidth;
	}
	
	public float getTriangleHeight() {
		return triangleHeight;
	}
	
	public Point[] getPoints() {
		return points;
	}
	
	public Triangle[] getTriangles() {
		return triangles;
	}
	
	public List<Point> getStartPoints() {
		return startPoints;
	}
	
	public Point getStartPoint(int beam) {
		return startPoints.get(beam);
	}
	
	public List<Point> getEndPoints() {
		return endPoints;
	}
	
	public int add120(int direction) {
		int newDirection = direction + 120;
		if (newDirection >= 360) return newDirection - 360;
		else return newDirection;
	}
	
	public int subtract120(int direction) {
		int newDirection = direction - 120;
		if (newDirection < 0) return newDirection + 360;
		else return newDirection;
	}
	
	public Triangle getTriangleAt(Point p) {
		int x = p.getIntX();
		int y = p.getIntY();
		if (isInBounds(p)) return triangles[y * (2 * (numSidesX) - 1) + x];
		else return null;
	}
	
	public boolean isInBounds(Point p) {
		int x = p.getIntX();
		int y = p.getIntY();
		if (x >= 0 && x < numSidesX * 2 - 1 && y >= 0 && y < numSidesY) return true;
		else return false;
	}
	
	public Point calculateMidPoint(Point p1, Point p2) {
		Point point = new Point();
		point.setIntX( (p1.getIntX() + p2.getIntX()) / 2);
		point.setIntY( (p1.getIntY() + p2.getIntY()) / 2);
		point.setFloatX( (p1.getFloatX() + p2.getFloatX()) / 2);
		point.setFloatY( (p1.getFloatY() + p2.getFloatY()) / 2);
		point.setSide("center");
		return point;		
	}
	
	public float min(float a, float b) {
		return (a > b ? b : a);
	}
	
}
