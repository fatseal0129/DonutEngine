package org.example.engine.render;

import org.example.engine.render.models.RawModel;
import org.lwjgl.opengl.*;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class Loader {
    //用來track vao/vbo 使其被關掉時能夠從memory中被刪除
    private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();

    private List<Integer> textures = new ArrayList<Integer>();

    public RawModel loadTOVAO(float[] positions, float[] textureCoords, float[] normals,int[] indices){
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0,3,positions);
        storeDataInAttributeList(1,2,textureCoords);
        storeDataInAttributeList(2,3,normals);
        unbindVAO();
        return new RawModel(vaoID, indices.length);
    }

//    public RawModel loadTOVAO(float[] positions, float[] textureCoords,int[] indices){
//        int vaoID = createVAO();
//        bindIndicesBuffer(indices);
//        storeDataInAttributeList(0,3,positions);
//        storeDataInAttributeList(1,2,textureCoords);
//        unbindVAO();
//        return new RawModel(vaoID, indices.length);
//    }

    private int createVAO(){
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        //啟動VAO
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    public int loadTexture(String filePath){
        int textureID;
        int width, height;
        ByteBuffer image;
        try(MemoryStack stack = MemoryStack.stackPush()){
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            image = STBImage.stbi_load("src/main/resources/textures/" + filePath + ".png", w, h, comp, 4);
            if (image ==null){
                throw new RuntimeException("Texture讀取失敗，原因：" + STBImage.stbi_failure_reason());
            }
            width = w.get();
            height = h.get();
        }
        textureID = GL11.glGenTextures();
        GL15.glBindTexture(GL_TEXTURE_2D, textureID);
        textures.add(textureID);
        GL11.glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S, GL_REPEAT);
        GL11.glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T, GL_REPEAT);
//        GL11.glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
//        GL11.glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
//        GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
//        GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        GL11.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
        STBImage.stbi_image_free(image);
        System.out.println(GL11.glGetInteger(GL_MAX_TEXTURE_SIZE));
        return textureID;
    }

    private void bindIndicesBuffer(int[] indices){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private void storeDataInAttributeList(int attributeNum, int coordinateSize, float[] data){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER ,vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        //因爲沒有要再改動所以使用Static draw就好
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNum, coordinateSize, GL11.GL_FLOAT,false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private IntBuffer storeDataInIntBuffer(int[] data){
        IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
//        try(MemoryStack stack = MemoryStack.stackPush()){
//            IntBuffer IndicesBuffer = stack.callocInt(data.length);
//            IndicesBuffer.put(0,data);
//            return IndicesBuffer;
//        }
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
//        try (MemoryStack stack = MemoryStack.stackPush()) {
//            FloatBuffer positionsBuffer = stack.callocFloat(data.length);
//            positionsBuffer.put(0, data);
//            return positionsBuffer;
//        }
    }

    private void unbindVAO(){
        GL30.glBindVertexArray(0);
    }

    public void cleanUP(){
        for (int vao:vaos){
            GL30.glDeleteVertexArrays(vao);
        }
        for (int vbo:vbos){
            GL15.glDeleteBuffers(vbo);
        }
        for (int texture: textures){
            GL11.glDeleteTextures(texture);
        }
    }
}
