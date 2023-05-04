package org.example.engine.render.models;


//只會持有Model的數據
public class RawModel {
    private int vaoID;
    private int vertexCount;

    public RawModel(int vaoID, int vertexCount){
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    public int getVaoID(){
        return vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
