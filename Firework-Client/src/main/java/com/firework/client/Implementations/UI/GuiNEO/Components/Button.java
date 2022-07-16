package com.firework.client.Implementations.UI.GuiNEO.Components;

import com.firework.client.Implementations.UI.GuiNEO.GuiInfo;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.UI.GuiNEO.GuiValueStorage;

public class Button {

    public int x;
    public int y;

    public int width;
    public int height;

    public int originHeight;
    public int originOffset;

    public Setting setting = null;

    public int offset = 10;

    public int localIndex;

    public Button(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        GuiInfo.index++;
        this.localIndex = GuiInfo.index;

        if(GuiValueStorage.values.size() < localIndex)
            GuiValueStorage.values.add(null);
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

        if(GuiValueStorage.values.size() < localIndex)
            GuiValueStorage.values.add(null);
    }

    public void draw(int mouseX, int mouseY){
        if(setting != null){
            if(setting.isVisible()) {
                this.height = !setting.isVisible() ? 0 : originHeight;
                this.offset = !setting.isVisible() ? 0 : originOffset;
            }else{
                return;
            }
        }
    }

    public void onKeyTyped(int keyCode) {}

    public void initialize(int mouseX, int mouseY){}

    public void initialize(int mouseX, int mouseY, int state){}

}
