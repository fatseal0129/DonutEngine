package org.example.engine;

import org.example.DisplayManager;
import org.example.engine.entities.Camera;
import org.example.engine.entities.Entity;
import org.example.engine.entities.Light;
import org.example.engine.render.Loader;
import org.example.engine.render.MasterRenderer;
import org.example.engine.render.OBJLoader;
import org.example.engine.render.models.RawModel;
import org.example.engine.render.models.TexturedModel;
import org.example.engine.render.textures.ModelTexture;
import org.example.engine.terrains.Terrain;
import org.joml.Math;
import org.joml.Vector3f;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class MainLoop {
    private static long window;

    public static void main(String[] args) throws FileNotFoundException {
        window = DisplayManager.init();
        Loader loader = new Loader();

        //Model
        RawModel d_model = OBJLoader.loadObjModel("dddl",loader);

        //Texture
        ModelTexture d_texture = new ModelTexture(loader.loadTexture("Doughnut_Base"));

        //Combine Model+Texture
        TexturedModel d_Tmodel = new TexturedModel(d_model, d_texture);

        //Reflection
        ModelTexture Reflect_d = d_Tmodel.getTexture();
        Reflect_d.setReflectivity(0.2f);
        Reflect_d.setShineDamper(1);

        //Light
        Light light = new Light(new Vector3f(3000,2000,2000),new Vector3f(1,1,1));
        Camera camera = new Camera();

        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        List<Entity> allDonut = new ArrayList<>();
        // 甜甜圈派對！
        for (int i =0; i < 100 ; i++){
            float x = random.nextFloat() * 1000 -500;
            float y = 10;
            float z = random.nextFloat() * -300;
            allDonut.add(new Entity(d_Tmodel, new Vector3f(x,y,z), 0, 0,0,8));
        }
        //Terrain
        Terrain terrain = new Terrain(0,-1,loader, new ModelTexture(loader.loadTexture("grass")));
        Terrain terrain2 = new Terrain(-1,-1,loader, new ModelTexture(loader.loadTexture("grass")));

        MasterRenderer renderer = new MasterRenderer();

        while(!glfwWindowShouldClose(window)){
            camera.move();

            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);

            //Entity
            for (Entity donut : allDonut){
                renderer.processEntity(donut);
                donut.increaseRotation(1.5f,0,1f);
            }

            renderer.render(light,camera);
            DisplayManager.loop();
        }
        renderer.cleanUP();
        loader.cleanUP();
        DisplayManager.close();
    }
}
