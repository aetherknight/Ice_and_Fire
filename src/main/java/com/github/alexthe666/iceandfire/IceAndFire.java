package com.github.alexthe666.iceandfire;

import com.github.alexthe666.iceandfire.client.GuiHandler;
import com.github.alexthe666.iceandfire.core.ModEntities;
import com.github.alexthe666.iceandfire.core.ModRecipes;
import com.github.alexthe666.iceandfire.core.ModVillagers;
import com.github.alexthe666.iceandfire.event.EventLiving;
import com.github.alexthe666.iceandfire.event.StructureGenerator;
import com.github.alexthe666.iceandfire.message.*;
import com.github.alexthe666.iceandfire.misc.CreativeTab;
import com.github.alexthe666.iceandfire.world.village.ComponentAnimalFarm;
import com.github.alexthe666.iceandfire.world.village.MapGenSnowVillage;
import com.github.alexthe666.iceandfire.world.village.VillageAnimalFarmCreator;
import net.ilexiconn.llibrary.server.config.Config;
import net.ilexiconn.llibrary.server.network.NetworkWrapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

@Mod(modid = IceAndFire.MODID, dependencies = "required-after:llibrary@[" + IceAndFire.LLIBRARY_VERSION + ",)", version = IceAndFire.VERSION, name = IceAndFire.NAME)
public class IceAndFire {

	public static final String MODID = "iceandfire";
	public static final String VERSION = "1.3.0";
	public static final String LLIBRARY_VERSION = "1.7.7";
	public static final String NAME = "Ice And Fire";
	public static final Logger logger = LogManager.getLogger(NAME);
	@Instance(value = MODID)
	public static IceAndFire INSTANCE;
	@NetworkWrapper({MessageDaytime.class, MessageDragonArmor.class, MessageDragonControl.class, MessageHippogryphArmor.class, MessageStoneStatue.class, MessageUpdatePixieHouse.class, MessageUpdatePixieHouseModel.class, MessageUpdatePixieJar.class})
	public static SimpleNetworkWrapper NETWORK_WRAPPER;
	@SidedProxy(clientSide = "com.github.alexthe666.iceandfire.ClientProxy", serverSide = "com.github.alexthe666.iceandfire.CommonProxy")
	public static CommonProxy PROXY;
	public static CreativeTabs TAB;
	public static DamageSource dragon;
	public static DamageSource dragonFire;
	public static DamageSource dragonIce;
	public static DamageSource gorgon;
	public static Biome GLACIER;
	public static Potion FROZEN_POTION;
	@SuppressWarnings("deprecation")
	@Config
	public static IceAndFireConfig CONFIG;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new EventLiving());
		ModEntities.init();
		logger.info("A raven flies from the north to the sea");
		logger.info("A dragon whispers her name in the east");
		TAB = new CreativeTab(MODID);
	}


	@EventHandler
	public void init(FMLInitializationEvent event) {

		ModRecipes.init();
		ModVillagers.INSTANCE.init();
		logger.info("The watcher waits on the northern wall");
		logger.info("A daughter picks up a warrior's sword");
		MapGenStructureIO.registerStructure(MapGenSnowVillage.Start.class, "SnowVillageStart");
		MapGenStructureIO.registerStructureComponent(ComponentAnimalFarm.class, "AnimalFarm");
		VillagerRegistry.instance().registerVillageCreationHandler(new VillageAnimalFarmCreator());

		PROXY.render();
		GameRegistry.registerWorldGenerator(new StructureGenerator(), 0);
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		dragon = new DamageSource("dragon") {
			@Override
			public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
				String s = "death.attack.dragon";
				String s1 = s + ".player_" + new Random().nextInt(2);
				return new TextComponentString(entityLivingBaseIn.getDisplayName().getFormattedText() + " ").appendSibling(new TextComponentTranslation(s1, new Object[]{entityLivingBaseIn.getDisplayName()}));
			}
		};
		dragonFire = new DamageSource("dragon_fire") {
			@Override
			public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
				String s = "death.attack.dragon_fire";
				String s1 = s + ".player_" + new Random().nextInt(2);
				return new TextComponentString(entityLivingBaseIn.getDisplayName().getFormattedText() + " ").appendSibling(new TextComponentTranslation(s1, new Object[]{entityLivingBaseIn.getDisplayName()}));
			}
		}.setFireDamage();
		dragonIce = new DamageSource("dragon_ice") {
			@Override
			public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
				String s = "death.attack.dragon_ice";
				String s1 = s + ".player_" + new Random().nextInt(2);
				return new TextComponentString(entityLivingBaseIn.getDisplayName().getFormattedText() + " ").appendSibling(new TextComponentTranslation(s1, new Object[]{entityLivingBaseIn.getDisplayName()}));
			}
		};
		gorgon = new DamageSource("gorgon") {
			@Override
			public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
				String s = "death.attack.gorgon";
				String s1 = s + ".player_" + new Random().nextInt(2);
				return new TextComponentString(entityLivingBaseIn.getDisplayName().getFormattedText() + " ").appendSibling(new TextComponentTranslation(s1, new Object[]{entityLivingBaseIn.getDisplayName()}));
			}
		};
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		PROXY.postRender();

		logger.info("A brother bound to a love he must hide");
		logger.info("The younger's armor is worn in the mind");

		logger.info("A cold iron throne holds a boy barely grown");
		logger.info("And now it is known");
		logger.info("A claim to the prize, a crown laced in lies");
		logger.info("You win or you die");
	}
}
