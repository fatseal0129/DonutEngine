package org.example.engine.render;

import org.example.engine.entities.Entity;
import org.example.engine.render.models.RawModel;
import org.example.engine.render.models.TexturedModel;
import org.example.engine.render.shaders.TerrainShader;
import org.example.engine.render.textures.ModelTexture;
import org.example.engine.terrains.Terrain;
import org.example.engine.tool.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class TerrainRenderer {

    private TerrainShader shader;

    public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix){
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(List<Terrain> terrains){
        for (Terrain terrain: terrains){
            prepareTerrain(terrain);
            loadModelMatrix(terrain);
            GL11.glDrawElements(GL_TRIANGLES,
                    terrain.getModel().getVertexCount(), GL_UNSIGNED_INT, 0);
            unbindTextureModel();
        }
    }

    private void prepareTerrain(Terrain terrain){
        RawModel rawmodel = terrain.getModel();

        GL30.glBindVertexArray(rawmodel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        ModelTexture texture = terrain.getTexture();
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL_TEXTURE_2D, texture.getTextureID());

    }

    private void unbindTextureModel(){
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void loadModelMatrix(Terrain terrain){
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX(),0,terrain.getZ()),
                0, 0, 0, 1);
        shader.loadTransformationMatrix(transformationMatrix);
    }

}
