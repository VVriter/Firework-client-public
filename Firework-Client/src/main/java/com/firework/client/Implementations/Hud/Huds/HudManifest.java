package com.firework.client.Implementations.Hud.Huds;

import com.firework.client.Features.Modules.Module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HudManifest {
    String name();
    boolean addModule() default false;
}
