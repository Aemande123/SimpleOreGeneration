package com.gmail.rohzek.util;

import com.gmail.rohzek.lib.Reference;

import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * A replacement from having a MCMOD.info file, works exactly like one would though.
 * @author Rohzek
 *
 */
public class LoadModData 
{
	public static void load(FMLPreInitializationEvent PreEvent)
	{
		ModMetadata  m = PreEvent.getModMetadata();
		
		m.autogenerated = false; //This is required otherwise it will not work
		
		m.modId = Reference.MODID;
		m.version = Reference.VERSION;
		m.name = Reference.NAME;
		m.url = "www.twitter.com/Rohzek";
		m.description = "A simple mod to make the vanilla spawn rates easier,"
				+ " as well as add ores to the Nether and the End.";
		m.logoFile = Reference.RESOURCEID + "logo.png";
		LogHelper.debug("Our logo should be loaded from: " + Reference.RESOURCEID + "logo.png");
		m.authorList.add("Rohzek");
		
	}
}
