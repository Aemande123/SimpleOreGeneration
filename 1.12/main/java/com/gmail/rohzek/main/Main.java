package com.gmail.rohzek.main;

import java.io.File;

import com.gmail.rohzek.blocks.SGBlocks;
import com.gmail.rohzek.blocks.SGOres;
import com.gmail.rohzek.blocks.SetMiningLevels;
import com.gmail.rohzek.compatibility.CheckForMods;
import com.gmail.rohzek.events.OreSpawnBlockEvent;
import com.gmail.rohzek.items.SGItems;
import com.gmail.rohzek.lib.Reference;
import com.gmail.rohzek.proxys.CommonProxy;
import com.gmail.rohzek.smelting.SmeltingRecipes;
import com.gmail.rohzek.util.ConfigurationManager;
import com.gmail.rohzek.util.LoadModData;
import com.gmail.rohzek.util.LogHelper;
import com.gmail.rohzek.util.TimeOutput;
import com.gmail.rohzek.util.json.JsonLoader;
import com.gmail.rohzek.world.SGWorldGen;
import com.gmail.rohzek.world.SGWorldGenSurface;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
/**
 * Main mod file, we register things that it doesn't matter if it's on both sides or not here,
 * as well as tie all the files together.
 * @author Rohzek
 *
 */
@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES)
public class Main 
{
	@Instance(Reference.MODID)
	public static Main simpleoregen;
	
	@SidedProxy(clientSide = Reference.CLIENTSIDEPROXY, serverSide = Reference.SERVERSIDEPROXY)
	public static CommonProxy proxy;
	
	@EventHandler
	public static void PreLoad(FMLPreInitializationEvent preEvent)
	{
		LogHelper.log("Hello Minecraft, how are you?");
		LogHelper.log("Did you know that Tony loves Amy?");
		LogHelper.log("" + TimeOutput.getTimeTogether());
		
		LogHelper.debug("Beginning Pre-Initialization");
		
		Reference.LOCATION = new File(preEvent.getModConfigurationDirectory().getAbsolutePath() + "/" + Reference.MODID);
		
		JsonLoader.loadData();
		
		LogHelper.debug("Loading MCMOD replacement info");
		
		LoadModData.load(preEvent);
		
		// Configuration file loader
		ConfigurationManager manager = new ConfigurationManager(preEvent);
		
		// Ore Generation
		LogHelper.debug("Registering ore generation information");
		LogHelper.debug("Blocking vanilla ore spawns");
		MinecraftForge.ORE_GEN_BUS.register(new OreSpawnBlockEvent());
		OreSpawnBlockEvent.populateOreType();
		
		LogHelper.debug("Replacing vanilla ore spawns");
		GameRegistry.registerWorldGenerator(new SGWorldGen(), 0);
		GameRegistry.registerWorldGenerator(new SGWorldGenSurface(), 0);
		LogHelper.debug("Finished ore generation information");
		
		LogHelper.debug("Pre-Initialization Complete");
	}
	
	@EventHandler
	public static void load(FMLInitializationEvent event)
	{
		LogHelper.debug("Beginning Initialization");
		
		LogHelper.debug("Setting mining levels");
		SetMiningLevels.set();
		
		LogHelper.debug("Registering Proxy Renders");
		proxy.registerRenders();
		
		LogHelper.debug("Registering Waila module");
		FMLInterModComms.sendMessage("waila", "register", "com.gmail.rohzek.compatibility.waila.Waila.callbackRegister");
		
		LogHelper.debug("Initialization Complete");
	}
	
	@EventHandler
	public static void PostLoad(FMLPostInitializationEvent postEvent)
	{
		LogHelper.log("Checking for compatibility modules");
		if(CheckForMods.check("forestry"))
		{
			CheckForMods.checkForForestry();
		}
		
		else
		{
			LogHelper.log("Forestry not installed; Compatibility not loaded");
		}
		
		if(CheckForMods.check("ic2"))
		{
			CheckForMods.checkForIC();
		}
		
		else
		{
			LogHelper.log("IC2 not installed; Compatibility not loaded");
		}
		
		/*
		if(CheckForMods.check("immersiveengineering"))
		{
			CheckForMods.checkForIE();
		}
		
		else
		{
			LogHelper.log("Immersive Engineering not installed; Compatibility not loaded");
		}
		*/
		
		LogHelper.debug("Adding smelting and crafting recipes");
		SmeltingRecipes.mainRegistry();
		
		LogHelper.debug("Adding Ore Dict entries");
		SGItems.registerOreDict();
		SGOres.registerOreDict();
		SGBlocks.registerOreDict();
	}
}
