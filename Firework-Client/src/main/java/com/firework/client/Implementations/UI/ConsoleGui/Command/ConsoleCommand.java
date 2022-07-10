package com.firework.client.Implementations.UI.ConsoleGui.Command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConsoleCommand {
    String label();
    String usage() default "";
    String description() default "";
}