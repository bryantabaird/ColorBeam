package com.bbaird.colorbeam.entities;



public class Point {
	private int ix;
	private int iy;
	private float fx;
	private float fy;
	private String side;
	
	private static final String RIGHTSIDE = "right";
	private static final String LEFTSIDE = "left";
	private static final String UPSIDE = "up";
	private static final String DOWNSIDE = "down";
	private static final String CENTERSIDE = "center";
	
	public Point(){}
	
	public Point(int x, int y) {
		this.ix = x;
		this.iy = y;
	}
	
	public Point(float x, float y) {
		this.fx = x;
		this.fy = y;
	}
	
	public Point(int x, int y, String side) {
		this.ix = x;
		this.iy = y;
		this.side = side;
	}
	
	public Point(float x, float y, String side) {
		this.fx = x;
		this.fy = y;
		this.side = side;
	}
	
	public Point(Point p) {
		side = p.side;
		ix = p.ix;
		iy = p.iy;
		fx = p.fx;
		fy = p.fy;
	}
	

	
	public int getIntX() {
		return ix;
	}
	
	public int getIntY() {
		return iy;
	}
	
	public float getFloatX() {
		return fx;
	}
	
	public float getFloatY() {
		return fy;
	}
	
	public String getSide() {
		return side;
	}
	
	public void setIntX(int x) {
		this.ix = x;
	}
	
	public void setIntY(int y) {
		this.iy = y;
	}
	
	public void setFloatX(float x) {
		this.fx = x;
	}
	
	public void setFloatY(float y) {
		this.fy = y;
	}
	
	public void setFloatPoints(Triangle t, String side) {
		if(side.equals("up")) {
			fx = (t.getX1() + t.getX2()) / 2;
			fy = (t.getY1() + t.getY2()) / 2;
		}
		else if(side.equals("down")) {
			fx = (t.getX1() + t.getX2()) / 2;
			fy = (t.getY1() + t.getY2()) / 2;
		}
		else if(side.equals("left")) {
			fx = (t.getX1() + t.getX3()) / 2;
			fy = (t.getY1() + t.getY3()) / 2;
		}
		else if(side.equals("right")) {
			fx = (t.getX2() + t.getX3()) / 2;
			fy = (t.getY2() + t.getY3()) / 2;
		}
	}
	
	public void setSide(String s) {
		this.side = s;
	}
	
	@Override
	public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Point temp = (Point) obj;
        Point temp2 = null;
        
        String tempSide = temp.getSide();
        int tempX = temp.getIntX();
        int tempY = temp.getIntY();
       
		if (tempSide.equals(LEFTSIDE))
			temp2 = new Point(tempX - 1, tempY, RIGHTSIDE);
		else if (tempSide.equals(RIGHTSIDE))
			temp2 = new Point(tempX + 1, tempY, LEFTSIDE);
		else if (tempSide.equals(DOWNSIDE))
			temp2 = new Point(tempX, tempY - 1, UPSIDE);
		else if (tempSide.equals(UPSIDE))
			temp2 = new Point(tempX, tempY + 1, DOWNSIDE);
		else if (tempSide.equals(CENTERSIDE))
			temp2 = new Point(tempX, tempY, CENTERSIDE);
		
       if (ix != tempX || iy != tempY || !side.equals(tempSide))
    	   if (ix != temp2.getIntX() || iy != temp2.getIntY() || !side.equals(temp2.getSide())) {
    		   return false;
    	   }
    		   
           
       return true;
       
       
       
       

       
       
    }
	
	public String toString() {
		return "x: " + ix + " y: " + iy + " side: " + side + " x: " + fx + " y: " + fy;
	}
	
	
	

}


