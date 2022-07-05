package com.firework.client.Implementations.Utill.Render.Shaders;

import com.firework.client.Firework;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ShaderManager {

    public static ArrayList<CShader> shaders;
    public static String shaderDir = Firework.FIREWORK_DIRECTORY + "\\Shaders\\";
    public static File shadersFolder = new File(shaderDir);
    public ShaderManager(){
        shaders = new ArrayList<>();

        setupDirectory();
        for(File file : shadersFolder.listFiles()){
            if(file.getName().endsWith(".frag"));
                shaders.add(new CShader(file.getName()));
        }
    }

    public void setupDirectory(){
        if(!shadersFolder.exists()) {
            shadersFolder.mkdirs();
        }
    }

    public CShader getShaderByName(String name){
        for(CShader cShader : shaders)
            if(cShader.fragmentShader.startsWith(name))
                return cShader;
        return null;
    }

    public ArrayList<String> getShadersNames(){
        ArrayList<String> names = new ArrayList<>();
        for(File file : shadersFolder.listFiles()){
            names.add(file.getName());
        }
        return names;
    }
}
