package org.example.engine.render;

import org.example.engine.entities.Camera;
import org.example.engine.entities.Entity;
import org.example.engine.entities.Light;
import org.example.engine.render.models.TexturedModel;
import org.example.engine.render.shaders.StaticShader;
import org.example.engine.render.shaders.TerrainShader;
import org.example.engine.terrains.Terrain;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class MasterRenderer {
    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private Matrix4f projectionMatrix;

    private StaticShader shader = new StaticShader();
    private EntityRenderer entityRenderer;

    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader = new TerrainShader();

    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    private List<Terrain> terrains = new ArrayList<>();


    public MasterRenderer(){
        GL11.glEnable(GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        createProjectionMatrix();
        entityRenderer =  new EntityRenderer(shader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader,projectionMatrix);

    }

    public void processTerrain(Terrain terrain){
        terrains.add(terrain);
    }

    public void processEntity(Entity entity){
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        if (batch!= null){
            batch.add(entity);
        }else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    public void render(Light sun, Camera camera){
        prepare();
        shader.start();
        shader.loadLight(sun);
        shader.loadViewMatrix(camera);
        entityRenderer.render(entities);
        shader.stop();
        terrainShader.start();
        terrainShader.loadLight(sun);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();
        terrains.clear();
        entities.clear();
    }


    public void cleanUP(){
        shader.cleanUp();
        terrainShader.cleanUp();
    }

    public void prepare(){
        GL11.glEnable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(0.1f, 0.8f, 1f, 0.0f);
    }

    private void createProjectionMatrix(){
        projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(FOV), 1.0f, NEAR_PLANE, FAR_PLANE);
    }


}
