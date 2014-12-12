package com.bbaird.colorbeam.entities;


public class Pair {
	private Point p1;
	private Point p2;
	private int beam1;
	private int beam2;
	
	public Pair(Point a, Point b, int pos1, int pos2) {
		p1 = a;
		p2 = b;
		beam1 = pos1;
		beam2 = pos2;
	}
	
	public Point getFirstPoint() {
		return p1;
	}
	
	public Point getSecondPoint() {
		return p2;
	}
	
	public int getFirstBeam() {
		return beam1;
	}
	
	public int getSecondBeam() {
		return beam2;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pair p = (Pair) obj;
        
        if ((beam1 == p.getFirstBeam() && beam2 == p.getSecondBeam()
        		&& p1 == p.getFirstPoint() && p2 == p.getSecondPoint())
        	||(beam1 == p.getSecondBeam() && beam2 == p.getFirstBeam()
        		&& p1 == p.getSecondPoint() && p2 == p.getFirstPoint()))
        		return true;
        
        return false;
	}
}
