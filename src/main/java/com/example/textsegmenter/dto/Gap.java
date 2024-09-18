package com.example.textsegmenter.dto;

public class Gap {
	 private final float size;
	    private final float position;

	    public Gap(float size, float position) {
	        this.size = size;
	        this.position = position;
	    }

	    public float getSize() {
	        return size;
	    }

	    public float getPosition() {
	        return position;
	    }

}
