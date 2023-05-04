package org.example.engine.entities;

import org.example.DisplayManager;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.security.Key;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {

    private Vector3f position = new Vector3f(0,10,0);

    //看上下
    private float pitch;

    //看左右
    private float yaw;

    //自體旋轉 目前不需要
    private float roll;

    public Camera(){
        Keyboard keyboard = new Keyboard();
        GLFW.glfwSetKeyCallback(DisplayManager.getWindow(), keyboard::invoke);
    }

    public void move(){
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_W)){
            position.z -=0.2f;
        }
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_D)){
            position.x +=0.2f;
        }
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_A)){
            position.x -= 0.2f;
        }
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_S)){
            position.z += 0.2f;
        }
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_SPACE)){
            position.y += 0.2f;
        }
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)){
            position.y -= 0.2f;
        }
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_K)){
            yaw+= 1.0f;
        }
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_J)){
            yaw-= 1.0f;
        }
        if ( Keyboard.isKeyDown(GLFW_KEY_ESCAPE))
            glfwSetWindowShouldClose(DisplayManager.getWindow(), true); // We will detect this in the rendering loop
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
}
