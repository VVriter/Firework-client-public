package com.firework.client.Implementations.Managers.Memory;

public class MemoryManager {
     static long memory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576L;
     static long maxMemory = Runtime.getRuntime().maxMemory() / 1048576L;
     static long allocatedMemory = Runtime.getRuntime().totalMemory() / 1048576L;


    public static String memoryInfo(){
        return memory+"//"+allocatedMemory+"//"+maxMemory;
    }


}
