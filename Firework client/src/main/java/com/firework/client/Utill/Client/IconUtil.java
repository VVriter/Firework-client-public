package com.firework.client.Utill.Client;


import com.firework.client.Firework;
import net.minecraft.util.Util;
import org.lwjgl.opengl.Display;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class IconUtil {


    //Are u really need it???
    public static final IconUtil INSTANCE = new IconUtil();


    public ByteBuffer readImageToBuffer(InputStream inputStream) throws IOException {
        BufferedImage bufferedimage = ImageIO.read(inputStream);
        int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), null, 0, bufferedimage.getWidth());
        ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);
        Arrays.stream(aint).map(i -> i << 8 | (i >> 24 & 255)).forEach(bytebuffer::putInt);
        ((Buffer)bytebuffer).flip();
        return bytebuffer;
    }
}