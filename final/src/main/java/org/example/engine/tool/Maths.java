package org.example.engine.tool;

import org.example.engine.entities.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Maths {
    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz,
                                                      float scale){
        Matrix4f matrix = new Matrix4f();
        matrix.translate(translation).rotateX((float) Math.toRadians(rx)).
                rotateY((float) Math.toRadians(ry)).
                rotateZ((float) Math.toRadians(rz)).
                scale(scale);
//        matrix.translate(translation);
//        matrix.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0));
//        matrix.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0));
//        matrix.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1));
//        matrix.scale(new Vector3f(scale,scale,scale), matrix);
        return matrix;
    }

    public static Matrix4f createViewMatrix(Camera camera){
        Vector3f pos = camera.getPosition();
        Matrix4f matrix = new Matrix4f();

        matrix.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1,0,0)).
                rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0,1,0));
        matrix.translate(-pos.x,-pos.y,-pos.z);
        return matrix;
    }
}
