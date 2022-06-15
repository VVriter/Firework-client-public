package com.firework.client.Implementations.GuiClassic.Components;

import com.firework.client.Implementations.GuiClassic.GuiInfo;
import com.firework.client.Implementations.Settings.Setting;

public class Button {

    public int x;
    public int y;

    public int width;
    public int height;

    public int originHeight;
    public int originOffset;

    public Setting setting = null;

    public int offset = 11;

    public int localIndex;

    public Button(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        GuiInfo.index++;
        this.localIndex = GuiInfo.index;
    }

    public Button(Setting setting, int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.originHeight = height;
        this.originOffset = offset;

        this.setting = setting;

        GuiInfo.index++;
        this.localIndex = GuiInfo.index;
    }

    public void draw(){
        if(setting != null){
            if(!setting.hidden) {
                this.height = setting.hidden ? 0 : originHeight;
                this.offset = setting.hidden ? 0 : originOffset;
            }else{
                return;
            }
        }
    }

    public void onKeyTyped(int keyCode) {}

    public void initialize(int mouseX, int mouseY){}

    public void initialize(int mouseX, int mouseY, int state){}

}
