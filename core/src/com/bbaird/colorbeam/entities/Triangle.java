package com.bbaird.colorbeam.entities;


public class Triangle {
	private float x1;
	private float y1;
	private float x2;
	private float y2;
	private float x3;
	private float y3;
	private float centerX;
	private float centerY;
	private float hexRadius;
	
	private Point p1;
	private Point p2;
	private Point p3;
	
	private int xLocal;
	private int yLocal;
	
	private boolean up;
	private boolean empty;
	private boolean available;
	
	private boolean reflect;
	private boolean absorbent;
	private boolean fixed;
	private boolean glass;
	private boolean transport;
	
	public Triangle(float x, float y, boolean isUp, float w, float h, int xrel, int yrel) {
		up = isUp;
		xLocal = xrel;
		yLocal = yrel;
		centerX = x;
		centerY = y;
		
		p1 = new Point(xrel, yrel, "left");
		p2 = new Point(xrel, yrel, "right");
		if (isUp) p3 = new Point(xrel, yrel, "down");
		else p3 = new Point(xrel, yrel, "up");
				
		setCoordinates(w, h);
		
    	float px1 = (2*x3 + w/2) / 2;
    	float py1;
    	if(isUp) py1 = (2*y3 + 2*h/3) / 2;
    	else py1 = (2*y3 + 2*h/3) / 2;
    	hexRadius = (float) Math.sqrt((px1 - x)*(px1 - x) + (py1 - y)*(py1 - y));
	}
	
	private void setCoordinates(float w, float h) {
		if (up) {
			//Bottom left
			x1 = centerX - w / 2;
			y1 = centerY - h / 3;
			//Bottom right
			x2 = centerX + w / 2;
			y2 = centerY - h / 3;
			//Top
			x3 = centerX;
			y3 = centerY + 2 * h / 3;
		}
		else{
			//Top left
			x1 = centerX - w / 2;
			//y1 = y + (w * unitSegmentToCenter);
			y1 = centerY + h / 3;
			//Top right
			x2 = centerX + w / 2;
			//y2 = y + (w * unitSegmentToCenter);
			y2 = centerY + h / 3;
			//Bottom
			x3 = centerX;
			//y3 = y - (w * (unitTriangleHeight - unitSegmentToCenter));
			y3 = centerY - 2 * h / 3;
		}
	}
	
	
	
	public Point getp1() {
		return p1;
	}

	public void setp1(Point p1) {
		this.p1 = p1;
	}

	public Point getp2() {
		return p2;
	}

	public void setp2(Point p2) {
		this.p2 = p2;
	}

	public Point getp3() {
		return p3;
	}

	public void setp3(Point p3) {
		this.p3 = p3;
	}

	public void setCenterX(float x) {
		centerX = x; 	
	}
	public void setCenterY(float y) {
		centerY = y; 	
	}
	public void setHexRadius(float r) {
		hexRadius = r;
	}
	public int getLocalX() {
		return xLocal;
	}
	public int getLocalY() {
		return yLocal;
	}
	public void setLocalX(int x) {
		xLocal = x;
	}
	public void setLocalY(int y) {
		yLocal = y;
	}
	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}
	public boolean isFixed() {
		return fixed;
	}
	public void setReflect(boolean ref) {
		reflect = ref;
	}
	public boolean isReflectable() {
		return reflect;
	}
	public boolean isAbsorbent() {
		return absorbent;
	}
	public void setAbsorbent(boolean absorb) {
		absorbent = absorb;
	}
	
	public float getX1() {
		return x1;
	}
	public void setX1(float x1) {
		this.x1 = x1;
	}
	public float getY1() {
		return y1;
	}
	public void setY1(float y1) {
		this.y1 = y1;
	}
	public float getX2() {
		return x2;
	}
	public void setX2(float x2) {
		this.x2 = x2;
	}
	public float getY2() {
		return y2;
	}
	public void setY2(float y2) {
		this.y2 = y2;
	}
	public float getX3() {
		return x3;
	}
	public void setX3(float x3) {
		this.x3 = x3;
	}
	public float getY3() {
		return y3;
	}
	public void setY3(float y3) {
		this.y3 = y3;
	}
	public void setEmpty(boolean empty) {
		this.empty = empty;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public float getCenterX() {
		return centerX;
	}
	public float getCenterY() {
		return centerY;
	}
	public boolean isUp() {
		return up;
	}
	public boolean isEmpty() {
		return empty;
	}
	public boolean isAvailable() {
		return available;
	}
	public float getHexRadius() {
		return hexRadius;
	}
	public boolean isGlass() {
		return glass;
	}
	public void setGlass(boolean glass) {
		this.glass = glass;
	}
	public boolean isTransport() {
		return transport;
	}
	public void setTransport(boolean transport) {
		this.transport = transport;
	}
	public void setUnavailableTriangle() {
		available = false;
		empty = true;
	}
	
	@Override
	public String toString() {
		return "local x: " + xLocal + " local y: " + yLocal;
	}
	
	public void setEmptyTriangle() {
		available = true;
		empty = true;
		reflect = false;
		fixed = false;
		glass = false;
		transport = false;
		absorbent = false;
	}
	
	public void setFreeRegularTriangle() {
		available = true;
		empty = false;
		reflect = true;
		fixed = false;
		glass = false;
		transport = false;
		absorbent = false;
	}
	
	public void setFixedRegularTriangle() {
		available = true;
		empty = false;
		reflect = true;
		fixed = true;
		glass = false;
		transport = false;
		absorbent = false;
	}
	
	public void setFreeAbsorbentTriangle() {
		available = true;
		empty = false;
		reflect = false;
		fixed = false;
		glass = false;
		transport = false;
		absorbent = true;
	}
	
	public void setFixedAbsorbentTriangle() {
		available = true;
		empty = false;
		reflect = false;
		fixed = true;
		glass = false;
		transport = false;
		absorbent = true;
	}
	
	public void setFreeGlassTriangle() {
		available = true;
		empty = false;
		reflect = false;
		fixed = false;
		glass = true;
		transport = false;
		absorbent = false;
	}
	
	public void setFixedGlassTriangle() {
		available = true;
		empty = false;
		reflect = true;
		fixed = true;
		glass = true;
		transport = false;
		absorbent = false;
	}
	
	public void setFreeTransportTriangle() {
		available = true;
		empty = false;
		reflect = false;
		fixed = false;
		glass = false;
		transport = true;
		absorbent = false;
	}
	
	public void setFixedTransportTriangle() {
		available = true;
		empty = false;
		reflect = false;
		fixed = true;
		glass = false;
		transport = true;
		absorbent = false;
	}
	@Override
	public boolean equals(Object obj) {
       if (this == obj)
           return true;
       if (obj == null)
           return false;
       if (getClass() != obj.getClass())
           return false;
       Triangle t = (Triangle) obj;
        
       if (t.getLocalX() == xLocal && t.getLocalY() == yLocal)
    	   return true;
       return false;
       }
	
	;
}
