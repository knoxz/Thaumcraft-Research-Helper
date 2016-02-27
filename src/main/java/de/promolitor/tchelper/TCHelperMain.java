package de.promolitor.tchelper;

import de.promolitor.tchelper.commands.CommandSetScale;
import de.promolitor.tchelper.helper.AspectCalculation;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = TCHelperMain.MODID, version = TCHelperMain.VERSION, name = TCHelperMain.VERSION, clientSideOnly = true, canBeDeactivated = true)
public class TCHelperMain {

	public static final String VERSION = "1.4";
	public static final String MODID = "tchelper";
	public static final String MODNAME = "TC Research-Helper";

	@Instance(MODID)
	public static TCHelperMain instance;

	public static Configuration config;
	protected static final Minecraft mc = Minecraft.getMinecraft();
	public static final int GuiMainID = 0;
	public static boolean debugging = true;
	public static Property aspectScale;
	public static Property topDistance;
	public static Property leftSide;
	// Says where the client and server 'proxy' code is loaded.
	// @SidedProxy(clientSide = "de.promolitor.tchelper.proxies.ClientProxy",
	// serverSide = "de.promolitor.tchelper.proxies.CommonProxy")
	// public static CommonProxy proxy;

	@SideOnly(Side.CLIENT)
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Config
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		aspectScale = config.get("AspectScale", Configuration.CATEGORY_GENERAL, 8, "How big the Overlay should be!");
		topDistance = config.get("TopDistance", Configuration.CATEGORY_GENERAL, 20,
				"Distance between top and first Aspect-Path");
		leftSide = config.get("LeftSide?", Configuration.CATEGORY_GENERAL, true,
				"Switch to false to put the Aspect-Paths on the right side.");
		config.save();
		MinecraftForge.EVENT_BUS.register(new KeyInputEvent());
		ClientCommandHandler.instance.registerCommand(new CommandSetScale());

	}

	@SideOnly(Side.CLIENT)
	@EventHandler
	public void init(FMLInitializationEvent event) {
		// GUI & Keybindings
		KeyBindings.init();

	}

	@SideOnly(Side.CLIENT)
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		AspectCalculation.createCosts();
		MinecraftForge.EVENT_BUS.register(new RenderGuiHandler());
	}

}
