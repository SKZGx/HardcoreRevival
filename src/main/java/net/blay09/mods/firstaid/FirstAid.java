package net.blay09.mods.firstaid;

import net.blay09.mods.firstaid.capability.CapabilityFirstAid;
import net.blay09.mods.firstaid.handler.DeathHandler;
import net.blay09.mods.firstaid.handler.PlayerHandler;
import net.blay09.mods.firstaid.handler.RescueHandler;
import net.blay09.mods.firstaid.handler.RestrictionHandler;
import net.blay09.mods.firstaid.network.NetworkHandler;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

@Mod(modid = FirstAid.MOD_ID, name = "First Aid", acceptedMinecraftVersions = "1.12")
@Mod.EventBusSubscriber
public class FirstAid {
	public static final String MOD_ID = "firstaid";

	@Mod.Instance(MOD_ID)
	public static FirstAid instance;

	@SidedProxy(clientSide = "net.blay09.mods.firstaid.client.ClientProxy", serverSide = "net.blay09.mods.firstaid.CommonProxy")
	public static CommonProxy proxy;

	public static Logger logger;

	public static final DamageSource notRescuedInTime = new DamageSource("not_rescued_in_time");

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new RestrictionHandler());
		MinecraftForge.EVENT_BUS.register(new PlayerHandler());
		MinecraftForge.EVENT_BUS.register(new DeathHandler());
		MinecraftForge.EVENT_BUS.register(new RescueHandler());
		MinecraftForge.EVENT_BUS.register(proxy);

		CapabilityFirstAid.register();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		NetworkHandler.init();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if(event.getSide() == Side.CLIENT && ModConfig.teamUpIntegration) {
			event.buildSoftDependProxy("teamup", "net.blay09.mods.firstaid.compat.TeamUpAddon");
		}

		if(event.getSide() == Side.CLIENT && ModConfig.pingIntegration) {
			event.buildSoftDependProxy("ping", "net.blay09.mods.firstaid.compat.PingAddon");
		}
	}

}
