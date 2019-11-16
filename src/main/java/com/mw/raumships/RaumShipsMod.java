package com.mw.raumships;

import com.mw.raumships.common.RSCommonProxy;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Logger;

import static com.mw.raumships.RaumShipsMod.MODID;

@Mod(MODID)
public class RaumShipsMod {
    public static final String MODID = "raumships";

    @SidedProxy(clientSide = "com.mw.raumships.client.RSClientProxy", serverSide = "com.mw.raumships.server.RSServerProxy")
    public static RSCommonProxy proxy;

    @Instance(MODID)
    public static RaumShipsMod instance;

    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) throws Exception {
        proxy.preInit(event);
        logger = event.getModLog();
    }

    @EventHandler
    public void load(FMLInitializationEvent event) throws Exception {
        proxy.load(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
