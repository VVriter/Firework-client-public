package com.firework.client.Implementations.Utill.Render;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class RoundRenderUtils {
        public static void roundedRectangle(Rectangle rectangle, double radius, int color) {
                glPushAttrib(GL_POINTS);
                glScaled(0.5, 0.5, 0.5);

                rectangle.x *= 2.0;
                rectangle.y *= 2.0;

                rectangle.width = (rectangle.width * 2.0) + rectangle.x;
                rectangle.height = (rectangle.height * 2.0) + rectangle.y;

                GlStateManager.enableBlend();
                glDisable(GL_TEXTURE_2D);
                RainbowUtil.applyColor(color, GL11::glColor4f);
                glEnable(GL_LINE_SMOOTH);

                glBegin(GL_POLYGON);

                double pi = Math.PI;

                int i;
                for (i = 0; i <= 90; ++i) {
                        glVertex2d(rectangle.x + radius + Math.sin(i * pi / 180.0) * radius * -1.0,
                                rectangle.y + radius + Math.cos(i * pi / 180.0) * radius * -1.0);
                }

                for (i = 90; i <= 180; ++i) {
                        glVertex2d(rectangle.x + radius + Math.sin(i * pi / 180.0) * radius * -1.0,
                                rectangle.height - radius + Math.cos(i * pi / 180.0) * radius * -1.0);
                }

                for (i = 0; i <= 90; ++i) {
                        glVertex2d(rectangle.width - radius + Math.sin(i * pi / 180.0) * radius,
                                rectangle.height - radius + Math.cos(i * pi / 180.0) * radius);
                }

                for (i = 90; i <= 180; ++i) {
                        glVertex2d(rectangle.width - radius + Math.sin(i * pi / 180.0) * radius, rectangle.y + radius + Math.cos(i * pi / 180.0) * radius);
                }

                glEnd();
                glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                glEnable(GL_TEXTURE_2D);
                glDisable(GL_LINE_SMOOTH);
                GlStateManager.disableBlend();

                glScaled(2.0, 2.0, 0.0);
                glPopAttrib();
        }
}
