package com.bbaird.colorbeam.entities;

import com.badlogic.gdx.graphics.Color;

public class Wormhole {
	private Triangle triangle1;
	private Triangle triangle2;
	
	private boolean rightFlag;
	
	private Color color;
	
	public Wormhole(Triangle t) {
		triangle1 = t;		
	}
	
	public boolean getRightFlag() {
		return rightFlag;
	}

	public void setRightFlag(boolean mod0) {
		rightFlag = mod0;
	}



	public Triangle getTriangle1() {
		return triangle1;
	}

	public Triangle getTriangle2() {
		return triangle2;
	}

	public void setOtherTriangle(Triangle t) {
		this.triangle2 = t;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public boolean isPointByWormholeT1(Point p) {
		if (p.equals(triangle1.getp1())) return true;
		else if (p.equals(triangle1.getp2())) return true;
		else if (p.equals(triangle1.getp3())) return true;
		return false;
	}
	
	public boolean isPointByWormholeT2(Point p) {
		if (p.equals(triangle2.getp1())) return true;
		else if (p.equals(triangle2.getp2())) return true;
		else if (p.equals(triangle2.getp3())) return true;
		return false;
	}

	public String toString() {
		String str = "";
		if (triangle1 != null)
			str += "Start t: " + triangle1.toString();
		if (triangle2 != null) 
			str += "\n  End t:" + triangle2.toString();
		if (color != null)
			str += "\n  Color:" + color.toString();
		return str;
	}

}

