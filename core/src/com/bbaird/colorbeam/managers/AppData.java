package com.bbaird.colorbeam.managers;

import java.io.Serializable;

import com.bbaird.colorbeam.entities.Triangle;

public class AppData implements Serializable {
	private static final long serialVersionUID = 1;
	
	private final int MAX_LEVELS = 9;
	private final int MAX_SECTIONS = 4;
	
	private int currentSection;
	private int currentLevel;
	
	private boolean additive;
	
	private Triangle[] triangles;
	
	private int[][] stars;
	private int[][] moves;
	private boolean[][] finished;
			
	public AppData() {
		stars = new int[MAX_SECTIONS][MAX_LEVELS];
		moves = new int[MAX_SECTIONS][MAX_LEVELS];
		finished = new boolean[MAX_SECTIONS][MAX_LEVELS];
		currentSection = 1;
		currentLevel = 1;
		additive = false;
		
		// Set all stars as 0
		for (int i = 0; i < MAX_SECTIONS; i++) {
			for (int j = 0; j < MAX_LEVELS; j++) {
				stars[i][j] = 0;
			}
		}
		
		// Set all levels as not finished
		for (int i = 0; i < MAX_SECTIONS; i++) {
			for (int j = 0; j < MAX_LEVELS; j++) {
				finished[i][j] = true;
			}
		}
	}
	
	public void setAdditive(boolean type) {
		additive = type;
	}
	
	public boolean getAdditive() {
		return additive;
	}
	
	public int getTotalSections() {
		return MAX_SECTIONS;
	}
	
	public int getTotalLevels() {
		return MAX_LEVELS;
	}
	
	public boolean getMixType() {
		return additive;
	}
	
	public void setMixType(boolean newType) {
		additive = newType;
	}

	public int getCurrentSection() {
		return currentSection;
	}

	public void setCurrentSection(int currentSection) {
		this.currentSection = currentSection;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}

	public Triangle[] getTriangles() {
		return triangles;
	}

	public void setTriangles(Triangle[] triangles) {
		this.triangles = triangles;
	}

	public int[][] getStars() {
		return stars;
	}

	public void setStars(int star, int section, int level) {
		stars[section][level] = star;
	}
	
	public int[][] getMoves() {
		return moves;
	}

	public void setMoves(int move, int section, int level) {
		moves[section][level] = move;
	}

	public boolean[][] getFinishedLevels() {
		return finished;
	}

	public void setFinishedLevels(boolean finish, int section, int level) {
		finished[section][level] = finish;
	}
	
	
	
}
