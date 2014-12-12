package com.bbaird.colorbeam.managers;

import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class InputProcessor implements GestureListener {

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		 //TODO Auto-generated method stub
		
		InputGestures.setStartX(x);
		InputGestures.setStartY(y);
		InputGestures.setCurrentX(x);
		InputGestures.setCurrentY(y);
		
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		 //TODO Auto-generated method stub
		
		InputGestures.setTapped(true);
		return false;
		
	}

	@Override
    public boolean longPress(float x, float y) {
        return true;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        
		InputGestures.setDrag(true);
		
		InputGestures.setCurrentX(x);
		InputGestures.setCurrentY(y);
        return true;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
            Vector2 pointer1, Vector2 pointer2) {
        return true;
    }

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		 //TODO Auto-generated method stub
		
		InputGestures.setDrag(false);
		InputGestures.setEndDrag(true);
		return false;
	}

}
