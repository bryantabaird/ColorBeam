package com.bbaird.colorbeam.states;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.bbaird.colorbeam.ColorBeam;
import com.bbaird.colorbeam.entities.Pair;
import com.bbaird.colorbeam.entities.Point;
import com.bbaird.colorbeam.entities.Triangle;
import com.bbaird.colorbeam.entities.TriangleMeshHelper;
import com.bbaird.colorbeam.entities.Wormhole;
import com.bbaird.colorbeam.managers.GameStateManager;
import com.bbaird.colorbeam.managers.InputGestures;
import com.bbaird.colorbeam.managers.InputProcessor;
import com.bbaird.colorbeam.managers.Level;
import com.bbaird.colorbeam.managers.Save;

public class PlayState extends GameState {
	
	private ShapeRenderer sr;

	
	private float timerTarget;
	
	private SpriteBatch batch;
	private TriangleMeshHelper meshHelper;
	
	private Level level;
	
	private static final int RIGHTDIRECTION = 0;
	private static final int UPRIGHTDIRECTION = 60;
	private static final int UPLEFTDIRECTION = 120;
	private static final int LEFTDIRECTION = 180;
	private static final int DOWNLEFTDIRECTION = 240;
	private static final int DOWNRIGHTDIRECTION = 300;
	
	private static final String RIGHTSIDE = "right";
	private static final String LEFTSIDE = "left";
	private static final String UPSIDE = "up";
	private static final String DOWNSIDE = "down";
	
	private static float SCREWWIDTH;
	private static float LASERWIDTH;
	private static float ENDPOINTSIDE;
	private static float STARTPOINTLENGTH;
	private static float INTERSECTPOINTLENGTH;
	
	private boolean isTapped;
	private boolean isDragging;
	private boolean dragStopped;
	
	private float startX;
	private float startY;
	private float currentX;
	private float currentY;
	
	private boolean isLevelFinished;
	
	// Bug: when dragging/selecting a triangle and not dropping it, dragging from
	// off the map will re-drag that same triangle
	
	// Don't forget in manifest to remove the preferExternal
	
	// Todo: take out intersects with same beam with || operator, but complicates
	// head on collisions mid-triangle
	
	
	public PlayState(final GameStateManager gsm) {
		super(gsm);
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(new GestureDetector(new InputProcessor()));
		inputMultiplexer.addProcessor(backButtonAdapter);
        Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void init() {

		timerTarget = 0;
		
		batch = new SpriteBatch();
		meshHelper = new TriangleMeshHelper();
		ColorBeam.background = new Sprite(textures[currentSection-1], Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		sr = new ShapeRenderer();	
		level = new Level("levels/" + currentSection + "/" + currentSection + "-" + currentLevel + ".txt");
		
		float width = level.getTriangleWidth();
		SCREWWIDTH = width/8;
		LASERWIDTH = width*3/20;
		ENDPOINTSIDE = width / 4;
		STARTPOINTLENGTH = width / 3;
		INTERSECTPOINTLENGTH = width / 3.5f;
		

		
		
		ColorBeam.glassTriangleSprite.setAlpha(.7f);
		isLevelFinished = false;
		addMeshes();
	}
	
	public void addMeshes() {
		// create filled triangle
		// bug exploited when no triangles are available! 
		// however, that should never happen
		
		for (int i = 0; i < level.getTriangles().length; i++) {
			Triangle t = level.getTriangles()[i];
			if(t.isAvailable()) {
				
				float w = ColorBeam.WIDTH;
				float h = ColorBeam.HEIGHT;
				float x1 = (t.getX1() - w/2)/(w/2);
				float y1 = (t.getY1() - h/2)/(h/2);
				float x2 = (t.getX2() - w/2)/(w/2);
				float y2 = (t.getY2() - h/2)/(h/2);
				float x3 = (t.getX3() - w/2)/(w/2);
				float y3 = (t.getY3() - h/2)/(h/2);
						
				if(t.isUp())
					meshHelper.addMesh(new float[] { x1, y1, Color.toFloatBits(233, 233, 233, 150),
						x2, y2, Color.toFloatBits(233, 233, 233, 80),
						x3, y3, Color.toFloatBits(233, 233, 233, 80) });
				else meshHelper.addMesh(new float[] { x1, y1, Color.toFloatBits(233, 233, 233, 80),
						x2, y2, Color.toFloatBits(233, 233, 233, 150),
						x3, y3, Color.toFloatBits(233, 233, 233, 150) });
			}			
		}
	}
	
	public void reverseDirection(int direction, int beam) {
		if (direction - 180 < 0) level.setCurrentDirection(beam, 360 - (180 - direction));
		else level.setCurrentDirection(beam, direction - 180);
	}
	
	private float rotateImage(Point p1, Point p2) {
		float p1x = p1.getFloatX();
		float p1y = p1.getFloatY();
		float p2x = p2.getFloatX();
		float p2y = p2.getFloatY();
		
		if (p1x < p2x && p1y < p2y) {
			return (float) Math.toDegrees(Math.atan((p2y-p1y)/(p2x-p1x)));
		}
		else if (p1x > p2x && p1y > p2y) {
			return (float) Math.toDegrees(Math.atan((p2y-p1y)/(p2x-p1x))) + 180;
		}
		else if (p1x < p2x && p1y > p2y) {
			return 360 - (float) Math.toDegrees(Math.atan((p1y-p2y)/(p2x-p1x)));
		}
		else if (p1x > p2x && p1y < p2y) {
			return 180 - (float) Math.toDegrees(Math.atan((p2y-p1y)/(p1x-p2x)));
		}
		else if (p1x > p2x) {
			return 180;
		}
		else if (p1x < p2x) {
			return 0;
		}
		System.out.println("error in rotation!!");
		return -1;
	}
	
	private float getHypotenuse(Point p1, Point p2) {
		float p1x = p1.getFloatX();
		float p1y = p1.getFloatY();
		float p2x = p2.getFloatX();
		float p2y = p2.getFloatY();
		return (float) Math.sqrt(Math.pow(Math.abs(p1x-p2x), 2) + Math.pow(Math.abs(p1y-p2y), 2));
	}
	
	public void swapPoints(Point p1, Point p2) {
		int ix = p1.getIntX();
		int iy = p1.getIntY();
		float fx = p1.getFloatX();
		float fy = p1.getFloatY();
		String side = p1.getSide();
		
		p1.setIntX(p2.getIntX());
		p1.setIntY(p2.getIntY());
		p1.setFloatX(p2.getFloatX());
		p1.setFloatY(p2.getFloatY());
		p1.setSide(p2.getSide());
		
		p2.setIntX(ix);
		p2.setIntY(iy);
		p2.setFloatX(fx);
		p2.setFloatY(fy);
		p2.setSide(side);
	}
	
	public void swapTriangles(Triangle t1, Triangle t2) {		
		int pos1 = level.getTrianglePos(t1);
		int pos2 = level.getTrianglePos(t2);
		
		float x1 = t1.getX1();
		float y1 = t1.getY1();
		float x2 = t1.getX2();
		float y2 = t1.getY2();
		float x3 = t1.getX3();
		float y3 = t1.getY3();
		float centerX = t1.getCenterX();
		float centerY = t1.getCenterY();
		float hexRadius = t1.getHexRadius();
		int xLocal = t1.getLocalX();
		int yLocal = t1.getLocalY();
		
		t1.setX1(t2.getX1());
		t2.setX1(x1);
		t1.setY1(t2.getY1());
		t2.setY1(y1);
		t1.setX2(t2.getX2());
		t2.setX2(x2);
		t1.setY2(t2.getY2());
		t2.setY2(y2);
		t1.setX3(t2.getX3());
		t2.setX3(x3);
		t1.setY3(t2.getY3());
		t2.setY3(y3);
		t1.setCenterX(t2.getCenterX());
		t2.setCenterX(centerX);
		t1.setCenterY(t2.getCenterY());
		t2.setCenterY(centerY);
		t1.setHexRadius(t2.getHexRadius());
		t2.setHexRadius(hexRadius);
		t1.setLocalX(t2.getLocalX());
		t2.setLocalX(xLocal);
		t1.setLocalY(t2.getLocalY());
		t2.setLocalY(yLocal);
		
		swapPoints(t1.getp1(), t2.getp1());
		swapPoints(t1.getp2(), t2.getp2());
		swapPoints(t1.getp3(), t2.getp3());
		
		Triangle temp = level.getTriangles()[pos1];
		level.setTriangle(temp, pos2);
		level.setTriangle(t2, pos1);
		

	}
	
	@Override
	public void update(float dt) {
		if (!isLevelFinished) {
			timerTarget += dt;
			//if (!level.getPreviousTrail().equals(level.getTrail())) {
				
			handleInput();
			List<List<Point> > trail = level.getTrail();
			
			level.setPreviousTrail(trail);
			
			
			for (int i = 0; i < level.getEndPoints().size(); i++) {
				level.setWin(i, false);
			}
			
			level.setValidStartPos(false);
			
			setCurrentTriangle();
	
			for (int currentBeam = 0; currentBeam < trail.size(); currentBeam++) {
				
				/*
				 * 1. Check if point is in bounds
				 * 2. Get type of next triangle, if any
				 * 3. Update direction
				 * 4. Update next point
				 */
				Point currentPoint = level.getCurrentPoints().get(currentBeam);
				Triangle currentTriangle = level.getTriangleAt(currentPoint);
				
				Point nextPoint = getPotentialNextPoint(currentBeam, currentTriangle);
	
				// Check if out of bounds
				if (level.isInBounds(nextPoint)) {
					
					Triangle nextTriangle = level.getTriangleAt(nextPoint);
					
					
					if(!nextTriangle.isEmpty()) {
						
						if (nextTriangle.isGlass()) {
							level.getCurrentPoints().set(currentBeam, nextPoint);
							updateDirection(nextTriangle, currentBeam);
							nextPoint = getNextPoint(nextTriangle, currentBeam);
						}
						
						else if (nextTriangle.isReflectable()) {
							updateDirection(currentTriangle, currentBeam);
							nextPoint = getNextPoint(currentTriangle, currentBeam);
							//--- this could be recursive??
							nextTriangle = level.getTriangleAt(nextPoint);
							if (nextTriangle.isGlass()) {
								level.getCurrentPoints().set(currentBeam, nextPoint);
								updateDirection(nextTriangle, currentBeam);
								nextPoint = getNextPoint(nextTriangle, currentBeam);
							}
							//------------------------------------
							level.getCurrentPoints().set(currentBeam, nextPoint);
						}
						
						else if (nextTriangle.isAbsorbent()) {
							level.setLoop(currentBeam, true);
							level.getCurrentPoints().set(currentBeam, nextPoint);
						}
						
						else if (nextTriangle.isTransport()) {
							nextPoint = getNextPoint(nextTriangle, currentBeam);
							level.getCurrentPoints().set(currentBeam, nextPoint);
						}
						

						
						if (!level.isLoop(currentBeam)) {
							trail.get(currentBeam).add(nextPoint);
							//checkForLoops(currentBeam, nextPoint, currentTriangle);
						}
						
						
					}
					
					else if(!level.isLoop(currentBeam)) {
						level.getCurrentPoints().set(currentBeam, nextPoint);
						if (!currentTriangle.isAvailable() || nextTriangle.isEmpty() || trail.get(currentBeam).size() == 1) {
							trail.get(currentBeam).add(nextPoint);
							//checkForLoops(currentBeam, nextPoint, currentTriangle);
						}
						else level.setLoop(currentBeam, true);
						
					}
				}
				
			}
			
			
			manageRichochets();
			
			manageIntersections();
		
			//manageRotation();
	
			manageTriangleDrop();
		//}
		
			if (level.getPreviousTrail().equals(level.getTrail())) {
				manageFinishedLevel();
				isLevelFinished = true;
				for (boolean b : level.getWin()) {
					if (!b) {
						isLevelFinished = false;
						break;
					}
				}
				if (isLevelFinished) {
					//Should only run once					
					Save.ad.setStars(level.getStars(), currentSection - 1, currentLevel - 1);
					Save.ad.setMoves(level.getMoveCount(), currentSection - 1, currentLevel - 1);
					Save.ad.setFinishedLevels(true, currentSection - 1, currentLevel - 1);
					
					gsm.setState(GameStateManager.LEVELWON);
				}
			}
		}
		
	}
	
	public void updateDirection(Triangle t, int beam) {
		level.setCurrentDirection(beam, getNextDirection(t, beam));
	}

	public int getNextDirection(Triangle t, int beam) {
		int oldDirection = level.getCurrentDirection(beam);		
		if (t.isUp()) { 
			if (t.isGlass()) {
				if (oldDirection % 120 == 60) return level.add120(oldDirection);
				else return level.subtract120(oldDirection);
			}
			else { 
				if (oldDirection % 120 == 0) return level.subtract120(oldDirection);
				else return level.add120(oldDirection);
			}
		}
		else { 
			if (t.isGlass()) { 
				if (oldDirection % 120 == 0) return level.add120(oldDirection);
				else return level.subtract120(oldDirection);
			}
			else { 
				if (oldDirection % 120 == 60) return level.subtract120(oldDirection);
				else return level.add120(oldDirection);
			}
		}
	}
	
	public Point getPotentialNextPoint(int beam, Triangle t) {
		Point p = level.getCurrentPoints().get(beam);
		Point newPoint = null;
		int x = p.getIntX();
		int y = p.getIntY();
		
		switch (level.getCurrentDirection(beam)) {
		case RIGHTDIRECTION:
			newPoint = new Point(x + 1, y, RIGHTSIDE);
			break;
		case UPRIGHTDIRECTION:
			if (t.isUp()) newPoint = new Point(x + 1, y, UPSIDE);
			else newPoint = new Point(x, y + 1, RIGHTSIDE);
			break;
		case UPLEFTDIRECTION:
			if (t.isUp()) newPoint = new Point(x - 1, y, UPSIDE);
			else newPoint = new Point(x, y + 1, LEFTSIDE);
			break;
		case LEFTDIRECTION:
			newPoint = new Point(x - 1, y, LEFTSIDE);
			break;
		case DOWNLEFTDIRECTION:
			if (t.isUp()) newPoint = new Point(x, y - 1, LEFTSIDE);
			else newPoint = new Point(x - 1, y, DOWNSIDE);
			break;
		case DOWNRIGHTDIRECTION:
			if (t.isUp()) newPoint = new Point(x, y - 1, RIGHTSIDE);
			else newPoint = new Point(x + 1, y, DOWNSIDE);
			break;
		default:
			System.out.println("error in getNextPoint");
		}
		
		Triangle temp = level.getTriangleAt(newPoint);
		if (temp != null) newPoint.setFloatPoints(temp, newPoint.getSide());
		return newPoint;
	}
	
	public Point getNextPoint(Triangle t, int beam) {
		// Used when next triangle is not empty, pass
		int direction = level.getCurrentDirection(beam);
		// Returns itself when absorbent
		Point point = new Point(level.getCurrentPoints().get(beam));
		if (t.isUp()) {
			if (t.isTransport()) {
				//set next point to exit triangle
				for (Wormhole w : level.getWormholes()) {
					if (t.equals(w.getTriangle1())) {
						Triangle t2 = w.getTriangle2();
						point = new Point(t2.getLocalX(), t2.getLocalY());
						t = t2;
					}
					else if (t.equals(w.getTriangle2())) {
						Triangle t1 = w.getTriangle1();
						point = new Point(t1.getLocalX(), t1.getLocalY());
						t = t1;
					}
				}
			}
			if (t.isTransport()|| t.isGlass() || t.isEmpty()) {
				if (direction == RIGHTDIRECTION || direction == UPRIGHTDIRECTION)
					point.setSide(RIGHTSIDE);
				else if (direction == UPLEFTDIRECTION || direction == LEFTDIRECTION) 
					point.setSide(LEFTSIDE);
				else if (direction == DOWNLEFTDIRECTION || direction == DOWNRIGHTDIRECTION) 
					point.setSide(DOWNSIDE);
				point.setFloatPoints(t, point.getSide());
			}
		}
		else {
			if (t.isTransport()) {
				//set next point to exit triangle
				for (Wormhole w : level.getWormholes()) {
					if (t.equals(w.getTriangle1())) {
						Triangle t2 = w.getTriangle2();
						point = new Point(t2.getLocalX(), t2.getLocalY());
						t = t2;
					}
					else if (t.equals(w.getTriangle2())) {
						Triangle t1 = w.getTriangle1();
						point = new Point(t1.getLocalX(), t1.getLocalY());
						t = t1;
					}
				}
			}
			if (t.isTransport() || t.isGlass() || t.isEmpty()) {
				if (direction == UPRIGHTDIRECTION || direction == UPLEFTDIRECTION)
					point.setSide(UPSIDE);
				else if (direction == LEFTDIRECTION || direction == DOWNLEFTDIRECTION) 
					point.setSide(LEFTSIDE);
				else if (direction == DOWNRIGHTDIRECTION || direction == RIGHTDIRECTION) 
					point.setSide(RIGHTSIDE);
				point.setFloatPoints(t, point.getSide());
			}
		}
		
		
		
		return point;
	}
	
	public void checkForLoops(int currentBeam, Point nextPoint, Triangle t) {
		// General infinite loop check
//		if (nextPoint.equals(level.getStartPoint(currentBeam))) {
//			
//			int currentDirection = level.getCurrentDirection(currentBeam);
//			int originalDirection = level.getStartDirection(currentBeam);
//			
//			// Special case where triangle blocks start point of infinite loop check
//			if (currentDirection == originalDirection) {
//				System.out.println("infinite loop2");
//				level.setLoop(currentBeam, true);
//			}	
//			
//			// Normal cases of infinite loop checking
//			else if (getNextDirection(t, currentBeam) == originalDirection) {
//				Point start = level.getStartPoint(currentBeam);
//				if (!level.getTriangleAt(start).isEmpty()) {
//					System.out.println("infinite loop1");
//					level.setLoop(currentBeam, true);
//				}
//			}
//		}
	}
	
	public void manageRichochets() {
		// will need to check for 3 way collision
		List<Pair> pointsToChange = new ArrayList<Pair>();
		List<Pair> pointsToRemove = new ArrayList<Pair>();
		List<List<Point>> trail = level.getTrail();
		
		for (int thisBeamPos = 0; thisBeamPos < trail.size(); thisBeamPos++) {
			for (int otherBeamPos = 0; otherBeamPos < trail.size(); otherBeamPos++) {
				
				List<Point> thisBeam = trail.get(thisBeamPos);
				List<Point> otherBeam = trail.get(otherBeamPos);
				Point thisLastPoint = thisBeam.get(thisBeam.size() - 1);
				Point otherLastPoint = otherBeam.get(otherBeam.size() - 1);
				Point otherFirstPoint = otherBeam.get(0);
				Point thisSecondToLastPoint = thisBeam.get(0);
				Point otherSecondToLastPoint = otherBeam.get(0);
				Point thisThirdToLastPoint = thisBeam.get(0);
				Point otherThirdToLastPoint = otherBeam.get(0);
				
				// Case where 2 different beams share the same next point in head on
				if (thisLastPoint.equals(otherLastPoint) && thisBeamPos!=otherBeamPos) {
					if (level.directionsAreSimilar(level.getCurrentDirection(thisBeamPos), level.getCurrentDirection(otherBeamPos))) {
						level.setLoop(thisBeamPos, true);
					}
				}
				
				// Case where a beam strikes the start point of any beam including itself
				if (thisLastPoint.equals(otherFirstPoint)) {
					level.setLoop(thisBeamPos, true);
				}
				
				if (thisBeam.size() == 2) {
					thisSecondToLastPoint = thisBeam.get(thisBeam.size() - 2);
				}
				else if (thisBeam.size() > 2){
					thisSecondToLastPoint = thisBeam.get(thisBeam.size() - 2);
					thisThirdToLastPoint = thisBeam.get(thisBeam.size() - 3);
				}
				
				if (otherBeam.size() == 2) {
					otherSecondToLastPoint = otherBeam.get(otherBeam.size() - 2);
				}
				else if (otherBeam.size() > 2){
					otherSecondToLastPoint = otherBeam.get(otherBeam.size() - 2);
					otherThirdToLastPoint = otherBeam.get(otherBeam.size() - 3);
				}
				
				
				
				if (thisBeamPos!=otherBeamPos) {
					// Case where 2 different beams will have head on collision mid-point
					if (thisLastPoint.equals(otherSecondToLastPoint) && otherLastPoint.equals(thisSecondToLastPoint)) {
						level.setLoop(thisBeamPos, true);
						Pair pair = new Pair(thisLastPoint, otherLastPoint, thisBeamPos, otherBeamPos);
						if (!pointsToChange.contains(pair)) pointsToChange.add(pair);
					}
					// Case where 2 beams just had a head on collision
					if (thisSecondToLastPoint.equals(otherSecondToLastPoint)) {
						if (thisThirdToLastPoint.equals(otherLastPoint) 
								&& otherThirdToLastPoint.equals(thisLastPoint)) {
							level.setLoop(thisBeamPos, true);
							Pair pair = new Pair(thisLastPoint, otherLastPoint, thisBeamPos, otherBeamPos);
							if (!pointsToRemove.contains(pair)) pointsToRemove.add(pair);
						}
					}
				}
			}
		}
		correctLastPoints(pointsToChange);
		removeLastPoints(pointsToRemove);
		
	}
	
	public void correctLastPoints(List<Pair> points) {
		List<List<Point>> trail = level.getTrail();
		for (Pair p : points) {
			Point p1 = p.getFirstPoint();
			Point p2 = p.getSecondPoint();
			Point newPoint = level.calculateMidPoint(p1, p2); 
			List<Point> firstBeam = trail.get(p.getFirstBeam());
			List<Point> secondBeam = trail.get(p.getSecondBeam());
			firstBeam.set(firstBeam.size() - 1, newPoint);
			secondBeam.set(secondBeam.size() - 1, newPoint);
		}
	}
	
	public void removeLastPoints(List<Pair> points) {
		List<List<Point>> trail = level.getTrail();
		for (Pair p : points) {
			int firstEndPos = trail.get(p.getFirstBeam()).size()-1;
			int secondEndPos = trail.get(p.getSecondBeam()).size()-1;
			trail.get(p.getFirstBeam()).remove(firstEndPos);
			trail.get(p.getSecondBeam()).remove(secondEndPos);
		}
	}
	
	public void manageIntersections() {
		List<List<Point>> trail = level.getTrail();
		List<Color> startColors = level.getStartColors();
		List<Point> intersectPoints = level.getIntersectPoints();
		for (int currentPos = 0; currentPos < trail.size(); currentPos++) {			
			List<Point> thisBeam = trail.get(currentPos);
			int lastElement = thisBeam.size() - 1;
			Point thisLastPoint = thisBeam.get(lastElement);
			for (int otherPos = 0; otherPos < trail.size(); otherPos++) {
				List<Point> otherBeam = trail.get(otherPos);
				for (int pos = 0; pos < otherBeam.size(); pos++) {
					// If current point is not itself
					if (!(currentPos == otherPos && pos == lastElement)) {  
						if (thisLastPoint.equals(otherBeam.get(pos))) {
							if (!intersectPoints.contains(thisLastPoint)) {							
 								Color c1 = startColors.get(currentPos);
 								Color c2 = startColors.get(otherPos); 
 								level.addIntersectPoint(thisLastPoint);
 								level.addIntersectColor(level.getColor(c1, c2));
 							}
						}
					}
				}
			}
		}
		level.createIntersectPoints(intersectPoints);
	}
	
	public void manageRotation() {
		Triangle currentTriangle = level.getCurrentTriangle();
		if(isTapped) {
			for (int trianglePos = 0; trianglePos < level.getTriangles().length; trianglePos++) {
				if (level.isPointInsideTriangle(currentX, currentY, level.getTriangles()[trianglePos])) {
					if (level.getTriangles()[trianglePos].isAvailable()) {
						currentTriangle.setEmpty(!currentTriangle.isEmpty());
						currentTriangle.setReflect(true);
						level.incrementMoveCount();
						level.initializeChain();						
						break;
					}
				}
			}
		}
	}
	
	public void manageTriangleDrop() {
		Triangle currentTriangle = level.getCurrentTriangle();
		if(dragStopped && !currentTriangle.isEmpty()) {
			for (Triangle t : level.getTriangles()) {
				if (level.isPointInsideHexagon(currentX, currentY, t)) {
					if (t.isAvailable() && t.isEmpty()) {
						if(t.isUp() == currentTriangle.isUp()) {
							if (!currentTriangle.isFixed()) {
								swapTriangles(t, currentTriangle);
								level.incrementMoveCount();
								level.initializeChain();
								break;
							}
						}
					}
				}
			}
		}
	}
	
	public void manageFinishedLevel() {
		List<List<Point>> trail = level.getTrail();
		List<Point> endPoints = level.getEndPoints();
		
		for (int endPos = 0; endPos < endPoints.size(); endPos++) {
			Point endPoint = level.getEndPoints().get(endPos);
			Color endColor = level.getEndColors().get(endPos);
			boolean potentialAdd =  true;
			for (int beam = 0; beam < trail.size(); beam++) {
				boolean isEndPointInBeam = trail.get(beam).contains(endPoint);

				if (level.isSecondaryColor(endColor)) {
					Color endColor1 = level.getPrimaryColors(endColor, 1);
					Color endColor2 = level.getPrimaryColors(endColor, 2);
					if (level.getStartColors().get(beam).equals(endColor1)
							|| level.getStartColors().get(beam).equals(endColor2)
							|| level.getStartColors().get(beam).equals(endColor)) {
						if (!isEndPointInBeam) potentialAdd = false;
					}
					else if (isEndPointInBeam) potentialAdd = false;
				}
				else {
					if (level.getStartColors().get(beam).equals(endColor)) {
						if (!isEndPointInBeam) potentialAdd = false;
					}
					else if (isEndPointInBeam) potentialAdd = false;
				}				
			}
			if (potentialAdd) level.setWin(endPos, true);
		}
		

	}
	
	public void setCurrentTriangle() {
		// Grab current triangle
		for (Triangle t : level.getTriangles()) {
			if(level.isPointInsideTriangle(startX, startY, t)) {
				level.setCurrentTriangle(t);
				if(t.isAvailable() && !t.isEmpty()) level.setValidStartPos(true);
			}
		}
	}
	
	public void drawPath() {
		List<List<Point> > trail = level.getTrail();
		for (int j = 0; j < level.getStartPoints().size(); j++) {
			Point p1 = level.getStartPoints().get(j);
			Color color = level.getStartColors().get(j);
			for (int i = 0; i < trail.get(j).size() - 1; i++) {
				Point p2 = trail.get(j).get(i + 1);	
				Triangle tEntry = null;
				Triangle tExit = null;
				boolean atWormhole = false;
				for (Wormhole w : level.getWormholes()) {
					if (w.isPointByWormholeT1(p1)) {
						if (w.isPointByWormholeT2(p2)) {
							atWormhole = true;
							tEntry = w.getTriangle1();
							tExit = w.getTriangle2();
						}
					}
					else if (w.isPointByWormholeT2(p1)) {
						if (w.isPointByWormholeT1(p2)) {
							atWormhole = true;
							tEntry = w.getTriangle2();
							tExit = w.getTriangle1();
						}
					}
				}
				if (atWormhole) { 
					batch.begin();
					boolean flag = level.getDirectionalFlag(j);
					drawWormholeBeam(j, tEntry, p1, flag);
					drawWormholeBeam(j, tExit, p2, !flag);
					batch.end();
				}
				else {
					batch.begin();
					if (!p2.equals(trail.get(j).get(trail.get(j).size()-1))) {
						ColorBeam.laserSprite.setOrigin(LASERWIDTH/4, LASERWIDTH/2);
						ColorBeam.laserSprite.setColor(color);
						ColorBeam.laserSprite.setRotation(rotateImage(p1, p2));
						
						ColorBeam.laserSprite.setBounds(p1.getFloatX() - LASERWIDTH/4, p1.getFloatY() - LASERWIDTH/2, getHypotenuse(p1, p2) + LASERWIDTH/2, LASERWIDTH);
						ColorBeam.laserSprite.draw(batch);
						
					}
					else {
						ColorBeam.endLaserSprite.setOrigin(LASERWIDTH/4, LASERWIDTH/2);
						ColorBeam.endLaserSprite.setColor(color);
						ColorBeam.endLaserSprite.setRotation(rotateImage(p1, p2));
						
						ColorBeam.endLaserSprite.setBounds(p1.getFloatX() - LASERWIDTH/4, p1.getFloatY() - LASERWIDTH/2, getHypotenuse(p1, p2) + LASERWIDTH/2, LASERWIDTH);
						ColorBeam.endLaserSprite.draw(batch);
					}
					batch.end();
				}
					

				p1 = p2;

			}
		}
	}
	
	public void drawEndPoints() {
		batch.begin();
		for (int i = 0; i < level.getEndPoints().size(); i++) {			
			Point p = level.getEndPoints().get(i);			
//			if (timerTarget < .1) {
				ColorBeam.endPointSprite1.setBounds(p.getFloatX() - ENDPOINTSIDE/2, p.getFloatY() - ENDPOINTSIDE/2, ENDPOINTSIDE, ENDPOINTSIDE);
				ColorBeam.endPointSprite1.setColor(level.getEndColors().get(i));
				ColorBeam.endPointSprite1.draw(batch);
//			}
//			else if (timerTarget < .2) {
//				ColorBeam.endPointSprite2.setBounds(p.getFloatX() - ENDPOINTSIDE/2, p.getFloatY() - ENDPOINTSIDE/2, ENDPOINTSIDE, ENDPOINTSIDE);
//				ColorBeam.endPointSprite2.setColor(level.getEndColors().get(i));
//				ColorBeam.endPointSprite2.draw(batch);
//			}
//			else if (timerTarget < .3) {
//				ColorBeam.endPointSprite3.setBounds(p.getFloatX() - ENDPOINTSIDE/2, p.getFloatY() - ENDPOINTSIDE/2, ENDPOINTSIDE, ENDPOINTSIDE);
//				ColorBeam.endPointSprite3.setColor(level.getEndColors().get(i));
//				ColorBeam.endPointSprite3.draw(batch);
//			}
//			else {
//				if (timerTarget > .4) timerTarget = 0;
//				ColorBeam.endPointSprite4.setBounds(p.getFloatX() - ENDPOINTSIDE/2, p.getFloatY() - ENDPOINTSIDE/2, ENDPOINTSIDE, ENDPOINTSIDE);
//				ColorBeam.endPointSprite4.setColor(level.getEndColors().get(i));
//				ColorBeam.endPointSprite4.draw(batch);
//			}
			
		}
		batch.end();
	}
	
	
	public void drawIntersectPoints() {
		batch.begin();
		for (int i = 0; i < level.getIntersectPoints().size(); i++) {
			Point p = level.getIntersectPoints().get(i);
			ColorBeam.intersectPointSprite.setBounds(p.getFloatX() - INTERSECTPOINTLENGTH/2, p.getFloatY() - INTERSECTPOINTLENGTH/2, INTERSECTPOINTLENGTH, INTERSECTPOINTLENGTH);
			ColorBeam.intersectPointSprite.setColor(level.getIntersectColors().get(i));
			ColorBeam.intersectPointSprite.draw(batch);
		}	
		batch.end();
	}
	
	public void drawStartPoints() {
		batch.begin();
		for (int i = 0; i < level.getStartPoints().size(); i++) {
			Point p = level.getStartPoints().get(i);
			ColorBeam.startPointSprite.setBounds(p.getFloatX() - STARTPOINTLENGTH/2, p.getFloatY() - STARTPOINTLENGTH/2, STARTPOINTLENGTH, STARTPOINTLENGTH);
			ColorBeam.startPointSprite.setColor(level.getStartColors().get(i));
			ColorBeam.startPointSprite.draw(batch);
		}
		batch.end();
	}

	public void drawTriangles() {
		sr.begin(ShapeType.Filled);
		Triangle currentTriangle = level.getCurrentTriangle();
		batch.begin();
		// Draw triangles
		for (int i = 0; i < level.getTriangles().length; i++) {
			Triangle thisTriangle = level.getTriangles()[i];
			if(!thisTriangle.isEmpty()) {				
				if (thisTriangle == currentTriangle && isDragging && !thisTriangle.isFixed())
					// Called if triangle is being dragged from this location
					drawTriangle(currentTriangle, thisTriangle.getCenterX(), thisTriangle.getCenterY(), .5f);
				// Commonly called to show current setup
				else drawTriangle(thisTriangle, thisTriangle.getCenterX(), thisTriangle.getCenterY(), 1f);
			}
			// Draws potential drop location of triangle
			else if (isDragging && currentTriangle.isUp() == thisTriangle.isUp()) {
				if (level.isValidStartPos() && thisTriangle.isAvailable() && level.isPointInsideHexagon(currentX, currentY, thisTriangle)) {
					if (!currentTriangle.isFixed()) {
						drawTriangle(currentTriangle, thisTriangle.getCenterX(), thisTriangle.getCenterY(), .8f);
					}
				}
			}
		}
		
		// Draws triangle being dragged	
		if(isDragging && !currentTriangle.isEmpty() && !currentTriangle.isFixed()) {
			drawTriangle(currentTriangle, currentX,	ColorBeam.HEIGHT - currentY, .5f);
		}
		
		batch.end();
		sr.end();
	}
	
	public void drawScrews() {
		batch.begin();
		for (int i = 0; i < level.getTriangles().length; i++) {
			Triangle t = level.getTriangles()[i];
			float tOffsetX = level.getTriangleWidth() / 6;
			float tOffsetY = level.getTriangleHeight() / 9;
			if (!t.isEmpty() && t.isFixed()) {
				if (t.isUp()) {
					ColorBeam.screwSprite.setBounds(t.getX1() + tOffsetX - SCREWWIDTH/2, t.getY1() + tOffsetY - SCREWWIDTH/2, SCREWWIDTH, SCREWWIDTH);
					ColorBeam.screwSprite.draw(batch);
					ColorBeam.screwSprite.setBounds(t.getX2() - tOffsetX - SCREWWIDTH/2, t.getY2() + tOffsetY - SCREWWIDTH/2, SCREWWIDTH, SCREWWIDTH);
					ColorBeam.screwSprite.draw(batch);
					ColorBeam.screwSprite.setBounds(t.getX3() - SCREWWIDTH/2, t.getY3() - SCREWWIDTH - tOffsetY * 1.5f, SCREWWIDTH, SCREWWIDTH);
					ColorBeam.screwSprite.draw(batch);
				}
				else {
					ColorBeam.screwSprite.setBounds(t.getX1() + tOffsetX - SCREWWIDTH/2, t.getY1() - tOffsetY - SCREWWIDTH/2, SCREWWIDTH, SCREWWIDTH);
					ColorBeam.screwSprite.draw(batch);
					ColorBeam.screwSprite.setBounds(t.getX2() - tOffsetX - SCREWWIDTH/2, t.getY2() - tOffsetY - SCREWWIDTH/2, SCREWWIDTH, SCREWWIDTH);
					ColorBeam.screwSprite.draw(batch);
					ColorBeam.screwSprite.setBounds(t.getX3() - SCREWWIDTH/2, t.getY3() + tOffsetY * 1.5f, SCREWWIDTH, SCREWWIDTH);
					ColorBeam.	screwSprite.draw(batch);
				}
				
			}
		}
		batch.end();
	}
	
	public void drawTriangle(Triangle t, float centerX, float centerY, float alpha) {
		//Only call in open batch!!
		float width = level.getTriangleWidth();
		float height = level.getTriangleHeight();
		
		if (t.isUp()) {
			if (t.isGlass()) {
				ColorBeam.glassTriangleSprite.setBounds(centerX - width/2, centerY - height/3, width, height);
				ColorBeam.glassTriangleSprite.draw(batch, alpha);
			}
			else if (t.isReflectable()) {
				ColorBeam.regularTriangleSprite.setBounds(centerX - width/2, centerY - height/3, width, height);
				ColorBeam.regularTriangleSprite.draw(batch, alpha);
			}
			else if (t.isAbsorbent()) {
				ColorBeam.absorbentTriangleSprite.setBounds(centerX - width/2, centerY - height/3, width, height);
				ColorBeam.absorbentTriangleSprite.draw(batch, alpha);
			}
			else if (t.isTransport()){
				ColorBeam.wormholeBackSprite.setBounds(centerX - width/2, centerY - height/3, width, height);
				ColorBeam.wormholeBackSprite.draw(batch, alpha);
				

			}//other types
		}
		else {
			if (t.isGlass()) {
				ColorBeam.glassTriangleSprite.setBounds(centerX - width/2, centerY + height/3, width, -height);
				ColorBeam.glassTriangleSprite.draw(batch, alpha);
			}
			else if (t.isReflectable()) {
				ColorBeam.regularTriangleSprite.setBounds(centerX - width/2, centerY + height/3, width, -height);
				ColorBeam.regularTriangleSprite.draw(batch, alpha);
			}
			else if (t.isAbsorbent()){
				ColorBeam.absorbentTriangleSprite.setBounds(centerX - width/2, centerY + height/3, width, -height);
				ColorBeam.absorbentTriangleSprite.draw(batch, alpha);
			}			
			else if (t.isTransport()) {
				ColorBeam.wormholeBackSprite.setBounds(centerX - width/2, centerY + height/3, width, -height);
				ColorBeam.wormholeBackSprite.draw(batch, alpha);

			}
		}
	}
	
	public void drawWormholeBeam(int beam, Triangle t, Point p, boolean directionalFlag) {
		// Only call in open batch!!
		float width = level.getTriangleWidth();
		float height = level.getTriangleHeight();
		
		if (directionalFlag) {
			if (t.isUp()) {
				ColorBeam.wormholeFrontSpriteA.setOrigin(level.getTriangleWidth()/2, level.getTriangleHeight()/3);
				ColorBeam.wormholeFrontSpriteA.setBounds(t.getCenterX() - width/2, t.getCenterY() - height/3, width, height);
				if (t.getp1().equals(p)) ColorBeam.wormholeFrontSpriteA.setRotation(240);
				else if (t.getp2().equals(p)) ColorBeam.wormholeFrontSpriteA.setRotation(120);
				else if (t.getp3().equals(p)) ColorBeam.wormholeFrontSpriteA.setRotation(0);
			}
			else {
				ColorBeam.wormholeFrontSpriteA.setOrigin(level.getTriangleWidth()/2, -level.getTriangleHeight()/3);
				ColorBeam.wormholeFrontSpriteA.setBounds(t.getCenterX() - width/2, t.getCenterY() + height/3, width, -height);
				if (t.getp1().equals(p)) ColorBeam.wormholeFrontSpriteA.setRotation(120);
				else if (t.getp2().equals(p)) ColorBeam.wormholeFrontSpriteA.setRotation(240);
				else if (t.getp3().equals(p)) ColorBeam.wormholeFrontSpriteA.setRotation(0);
			}
			ColorBeam.wormholeFrontSpriteA.setColor(level.getStartColors().get(beam));
			ColorBeam.wormholeFrontSpriteA.draw(batch);
		}
		else {
			if (t.isUp()) {
				ColorBeam.wormholeFrontSpriteB.setOrigin(level.getTriangleWidth()/2, level.getTriangleHeight()/3);
				ColorBeam.wormholeFrontSpriteB.setBounds(t.getCenterX() - width/2, t.getCenterY() - height/3, width, height);
				if (t.getp1().equals(p)) ColorBeam.wormholeFrontSpriteB.setRotation(240);
				else if (t.getp2().equals(p)) ColorBeam.wormholeFrontSpriteB.setRotation(120);
				else if (t.getp3().equals(p)) ColorBeam.wormholeFrontSpriteB.setRotation(0);
			}
			else {
				ColorBeam.wormholeFrontSpriteB.setOrigin(level.getTriangleWidth()/2, -level.getTriangleHeight()/3);
				ColorBeam.wormholeFrontSpriteB.setBounds(t.getCenterX() - width/2, t.getCenterY() + height/3, width, -height);
				if (t.getp1().equals(p)) ColorBeam.wormholeFrontSpriteB.setRotation(120);
				else if (t.getp2().equals(p)) ColorBeam.wormholeFrontSpriteB.setRotation(240);
				else if (t.getp3().equals(p)) ColorBeam.wormholeFrontSpriteB.setRotation(0);
			}
			ColorBeam.wormholeFrontSpriteB.setColor(level.getStartColors().get(beam));
			ColorBeam.wormholeFrontSpriteB.draw(batch);
		}
	}

	
	@Override
	public void draw() {
		
		batch.begin();
		ColorBeam.background.draw(batch);
		batch.end();
		
		meshHelper.drawMesh();
		
		drawEndPoints();
		drawStartPoints();
		drawTriangles();
		drawScrews();
		drawPath();
		drawIntersectPoints();
	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		isTapped = InputGestures.isTapped();
		
		isDragging = InputGestures.isDragging();
		dragStopped = InputGestures.isDoneDragging();
		
		startX = InputGestures.getStartX();
		startY = InputGestures.getStartY();
		currentX = InputGestures.getCurrentX();
		currentY = InputGestures.getCurrentY();
		
		InputGestures.setTapped(false);
		InputGestures.setEndDrag(false);
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		meshHelper.dispose();
		batch.dispose();
		sr.dispose();
		

		
	}
	
	public void printTrail() {
		String myTrail = "TRAIL:\n";
		for (int i = 0; i < level.getTrail().size(); i++) {
			myTrail += "------------------\n";
			for (int j = 0; j < level.getTrail().get(i).size(); j++) {
				Point p = level.getTrail().get(i).get(j);
				myTrail += "x: " + p.getIntX() + " y: " + p.getIntY() + " side: " + p.getSide()  + " x: " + p.getFloatX() + " y: " + p.getFloatY() + "\n";
			}
		}
		myTrail += "------------------";
		System.out.println(myTrail);
	}
	
	public void printPreviousTrail() {
		String myTrail = "PREVIOUS TRAIL:\n";
		for (int i = 0; i < level.getPreviousTrail().size(); i++) {
			myTrail += "------------------\n";
			for (int j = 0; j < level.getPreviousTrail().get(i).size(); j++) {
				Point p = level.getPreviousTrail().get(i).get(j);
				myTrail += "x: " + p.getIntX() + " y: " + p.getIntY() + " side: " + p.getSide()  + " x: " + p.getFloatX() + " y: " + p.getFloatY() + "\n";
			}
		}
		myTrail += "------------------";
		System.out.println(myTrail);
	}

}
