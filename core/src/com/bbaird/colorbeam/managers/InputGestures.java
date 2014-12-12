package com.bbaird.colorbeam.managers;

public class InputGestures {
	private static boolean pTapped;
	private static boolean pDragged;
	
	private static boolean isTouched;
	private static boolean isTapped;
	private static boolean startDrag;
	private static boolean endDrag;
	private static float startX;
	private static float startY;
	private static float currentX;
	private static float currentY;
	
	public static void update() {
		pTapped = isTapped;
		pDragged = endDrag;
	}
	
	public static void setStartX(float x) {
		startX = x;
	}
	
	public static void setStartY(float y) {
		startY = y;
	}
	
	public static void setCurrentX(float x) {
		currentX = x;
	}
	
	public static void setCurrentY(float y) {
		currentY = y;
	}
	
	public static float getStartX() {
		return startX;
	}
	
	public static float getStartY() {
		return startY;
	}
	
	public static float getCurrentX() {
		return currentX;
	}
	
	public static float getCurrentY() {
		return currentY;
	}
	
	public static void setTouched(boolean touched) {
		isTouched = touched;
	}
	
	public static void setTapped(boolean tapped) {
		isTapped = tapped;
	}
	
	public static void setDrag(boolean start) {
		startDrag = start;
	}
	
	public static void setEndDrag(boolean end) {
		endDrag = end;
	}
	
	public static boolean isTouched() {
		return isTouched;
	}
	
	public static boolean isTapped() {
		return isTapped && !pTapped;
	}
	
	public static boolean isDragging() {
		return startDrag;
	}
	
	public static boolean isDoneDragging() {
		return endDrag && !pDragged;
	}
	
	
}
