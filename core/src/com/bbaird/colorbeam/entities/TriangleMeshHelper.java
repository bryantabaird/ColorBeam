package com.bbaird.colorbeam.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TriangleMeshHelper extends Actor {
	private List<Mesh> meshes;
    
    private ShaderProgram meshShader;
 
    public TriangleMeshHelper() {
        createShader();
        meshes = new ArrayList<Mesh>();
    }
 
    public void addMesh(float[] vertices) {
    	Mesh mesh = new Mesh(true, vertices.length, 0,
    			new VertexAttribute(Usage.Position, 2, "a_position"),
    			new VertexAttribute(Usage.ColorPacked, 4, "a_color"));
        mesh.setVertices(vertices);
        
        meshes.add(mesh);
    }
 
    public void drawMesh() {
        // this should be called in render()
        if (meshes.isEmpty())
            throw new IllegalStateException("drawMesh called before a mesh has been created.");
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        meshShader.begin();
        for (Mesh m : meshes) {
        	m.render(meshShader, GL20.GL_TRIANGLES);
        }
        meshShader.end();
    }
 
    private void createShader() {
        // this shader tells opengl where to put things
    	// this shader tells opengl where to put things
    	String vertexShader = "attribute vec4 a_position;    \n"
    						+ "attribute vec4 a_color;       \n"
    						+ "varying vec4 v_color;         \n"
    						+ "void main()                   \n"
    						+ "{                             \n"
    						+ "   v_color = a_color;         \n"
    						+ "   gl_Position = a_position;  \n"
    						+ "}                             \n";
 
        // this one tells it what goes in between the points (i.e
        // colour/texture)
    	// this one tells it what goes in between the points (i.e colour/texture)
    	String fragmentShader = "#ifdef GL_ES                \n"
    						  + "precision mediump float;    \n"
    						  + "#endif                      \n"
    						  + "varying vec4 v_color;       \n"
    						  + "void main()                 \n"
    						  + "{                           \n"
    						  + "  gl_FragColor = v_color;   \n"
    						  + "}                           \n";
 
        // make an actual shader from our strings
        meshShader = new ShaderProgram(vertexShader, fragmentShader);
 
        // check there's no shader compile errors
        if (meshShader.isCompiled() == false)
            throw new IllegalStateException(meshShader.getLog());
    }
    public void dispose() {
        for (Mesh m : meshes) {
        	m.dispose();
        }
        meshShader.dispose();
    }
 
}
