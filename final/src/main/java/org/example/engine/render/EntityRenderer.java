package org.example.engine.render;

import org.example.engine.entities.Entity;
import org.example.engine.render.models.RawModel;
import org.example.engine.render.models.TexturedModel;
import org.example.engine.render.shaders.StaticShader;
import org.example.engine.render.textures.ModelTexture;
import org.example.engine.tool.Maths;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class  EntityRenderer {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private StaticShader shader;

    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Map<TexturedModel, List<Entity>> entities){
        for (TexturedModel model: entities.keySet()){
            prepareTextureModel(model);
            List<Entity> batch = entities.get(model);
            for (Entity entity: batch ){
                prepareInstance(entity);
                GL11.glDrawElements(GL_TRIANGLES,
                        model.getRawModel().getVertexCount(), GL_UNSIGNED_INT, 0);
            }
            unbindTextureModel();
        }
    }

    private void prepareTextureModel(TexturedModel model){
        RawModel rawmodel = model.getRawModel();

        GL30.glBindVertexArray(rawmodel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        ModelTexture texture = model.getTexture();
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL_TEXTURE_2D, model.getTexture().getTextureID());

    }

    private void unbindTextureModel(){
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void prepareInstance(Entity entity){
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),
                entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
    }

}
