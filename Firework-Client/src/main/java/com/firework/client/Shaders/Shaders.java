package com.firework.client.Shaders;

import net.minecraft.util.ResourceLocation;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Shaders
{
    public GLSLSandboxShader currentshader;
    public long time;

    public void init( )
    {
        try
        {
            Object[ ] shader = getRandomShader( );
            if( shader == null )
            {
                currentshader = null;
                System.out.println("ERROR2");
            }
            else
            {
                String name = ( String )shader[ 0 ];
                InputStream is = ( InputStream )shader[ 1 ];

                currentshader = new GLSLSandboxShader( name, is );
                if( !currentshader.initialized )
                    currentshader = null;
                else
                    time = System.currentTimeMillis( );
            }
        }
        catch( Exception e )
        {
            e.printStackTrace( );
            currentshader = null;
        }
    }

    public Object[ ] getRandomShader( ) throws FileNotFoundException
    {
        File folder = new File( "Firework-Client/shader/" );
        if( !folder.exists( ) ) return null;

        List< String > shaders = new ArrayList< >( );

        for( File file : folder.listFiles( ) )
        {
            String name = file.getName( );
            if( name.endsWith( ".fsh" ) )
                shaders.add( name );
        }

        if( shaders.size( ) == 0 ) return null;

        String randomname = shaders.get( new Random( ).nextInt( shaders.size( ) ) );
        FileInputStream fis = new FileInputStream( new File( "Firework-Client/shader/" + randomname ) );
        return new Object[ ]{ randomname, fis };
    }
}