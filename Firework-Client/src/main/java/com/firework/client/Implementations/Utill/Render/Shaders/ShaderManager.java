package com.firework.client.Implementations.Utill.Render.Shaders;

import com.firework.client.Firework;
import com.firework.client.Implementations.Managers.Manager;
import org.apache.commons.compress.compressors.FileNameUtil;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ShaderManager extends Manager {

    public static ArrayList<CShader> shaders;
    public static String shaderDir = Firework.FIREWORK_DIRECTORY + "Shaders/";
    public static File shadersFolder = new File(shaderDir);
    public ShaderManager(){
        super(false);
        shaders = new ArrayList<>();

        new File(shaderDir).mkdirs();
        for(File file : shadersFolder.listFiles()){
            if(file.getName().endsWith(".frag"));
                shaders.add(new CShader(file.getName()));
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
            if(file.getName().endsWith(".frag"))
                names.add(FilenameUtils.removeExtension(file.getName()));
        }
        names.add("NONE");
        return names;
    }
}
