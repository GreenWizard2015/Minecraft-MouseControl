package com.GreenWizard.MouseControl;

import net.minecraft.init.Blocks;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = MouseControl.MODID, version = MouseControl.VERSION, name=MouseControl.MODID)
public class MouseControl{
    public static final String MODID = "MouseControl";
    public static final String VERSION = "2.0";
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	if(event.getSide() == Side.CLIENT){
    		FMLCommonHandler.instance().bus().register(new MyHandler());
    	}
    }
}
