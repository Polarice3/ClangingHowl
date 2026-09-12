package com.mongoose.clanginghowl.client.events;

import net.neoforged.fml.common.EventBusSubscriber;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.client.audio.ItemIdleSound;
import com.mongoose.clanginghowl.client.audio.ItemLoopSound;
import com.mongoose.clanginghowl.client.render.WearRenderer;
import com.mongoose.clanginghowl.common.capabilities.CHCapHelper;
import com.mongoose.clanginghowl.common.effects.CHEffects;
import com.mongoose.clanginghowl.common.entities.utils.CameraShake;
import com.mongoose.clanginghowl.common.items.CHItems;
import com.mongoose.clanginghowl.common.items.curios.EnergyGlove;
import com.mongoose.clanginghowl.common.items.curios.XRayGoggles;
import com.mongoose.clanginghowl.common.items.energy.ChainsawItem;
import com.mongoose.clanginghowl.common.items.energy.ChainswordItem;
import com.mongoose.clanginghowl.common.items.energy.IEnergyItem;
import com.mongoose.clanginghowl.common.items.fuel.IFuel;
import com.mongoose.clanginghowl.common.network.CHNetwork;
import com.mongoose.clanginghowl.common.network.client.CActivateCurioKeyPacket;
import com.mongoose.clanginghowl.common.network.client.CJetBootsJumpPacket;
import com.mongoose.clanginghowl.config.CHConfig;
import com.mongoose.clanginghowl.init.CHKeybindings;
import com.mongoose.clanginghowl.init.CHSounds;
import com.mongoose.clanginghowl.utils.CHCuriosFinder;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.*;
import net.minecraft.client.gui.Gui;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;
import java.util.Random;

@EventBusSubscriber(modid = ClangingHowl.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onPlayerHoldItem(PlayerTickEvent.Post event) {
        if (event.getEntity().level() instanceof ClientLevel) {
            if (event.getEntity().isHolding(itemStack -> itemStack.getItem() instanceof ChainsawItem && !IEnergyItem.isEmpty(itemStack))) {
                playItemIdleLoop(CHSounds.CHAINSAW_IDLE.get(), event.getEntity(), CHItems.ADVANCED_CHAINSAW.get(), 0.4F, 1.0F);
            } else if (event.getEntity().isHolding(itemStack -> itemStack.getItem() instanceof ChainswordItem && !IEnergyItem.isEmpty(itemStack))) {
                playItemIdleLoop(CHSounds.CHAINSAW_IDLE.get(), event.getEntity(), CHItems.ADVANCED_CHAINSWORD.get(), 0.3F, 1.0F);
            }
        }
    }

    @SubscribeEvent
    public static void renderGlove(RenderArmEvent event){
        if (event.isCanceled()){
            return;
        }

        Optional<SlotResult> slotResult = CuriosApi.getCuriosInventory(event.getPlayer()).map(inv -> inv.findFirstCurio(itemStack -> itemStack.getItem() instanceof EnergyGlove))
                .orElse(Optional.empty());
        if (slotResult.isPresent()) {
            ItemStack itemStack = slotResult.get().stack();
            if (slotResult.get().slotContext().visible()) {
                WearRenderer renderer = WearRenderer.getRenderer(itemStack);
                if (renderer != null) {
                    renderer.renderFirstPersonArm(event.getPoseStack(), slotResult.get().stack(), event.getMultiBufferSource(), event.getPackedLight(), event.getPlayer(), itemStack.hasFoil());
                }
            }
        }
    }

    public static AbstractTickableSoundInstance ITEM_TICK;

    public static void playItemIdleLoop(SoundEvent soundEvent, LivingEntity livingEntity, Item item, float volume, float pitch){
        Minecraft minecraft = Minecraft.getInstance();
        if (soundEvent != null && livingEntity.isAlive()) {
            if (ITEM_TICK == null) {
                ITEM_TICK = new ItemIdleSound(soundEvent, livingEntity, item, volume, pitch);
            }
        } else {
            ITEM_TICK = null;
        }
        if (ITEM_TICK != null && !minecraft.getSoundManager().isActive(ITEM_TICK)) {
            Minecraft.getInstance().getSoundManager().play(ITEM_TICK);
        }
    }

    @SubscribeEvent
    public static void onItemUse(LivingEntityUseItemEvent.Start event){
        if (event.getEntity().level() instanceof ClientLevel){
            Minecraft minecraft = Minecraft.getInstance();
            SoundManager soundHandler = minecraft.getSoundManager();
            if (event.getItem().is(CHItems.ADVANCED_HAND_DRILL.get())){
                soundHandler.play(new ItemLoopSound(CHSounds.DRILLING.get(), event.getEntity()));
            }
            if (event.getItem().is(CHItems.ADVANCED_CHAINSAW.get())){
                soundHandler.play(new ItemLoopSound(CHSounds.CHAINSAW_CUT.get(), event.getEntity()));
            }
            if (event.getItem().is(CHItems.FLAMETHROWER.get())){
                soundHandler.play(new ItemLoopSound(CHSounds.FLAMETHROWER_BURNS.get(), event.getEntity()));
            }
        }
    }

    public static float PARTIAL_TICK = 0;

    @SubscribeEvent
    public static void renderTick(RenderFrameEvent.Pre event){
        PARTIAL_TICK = event.getPartialTick().getGameTimeDeltaPartialTick(false);
    }

    @SubscribeEvent
    public static void updateInputEvent(MovementInputUpdateEvent event) {
        Player player = event.getEntity();
        Input input = event.getInput();
        if (player instanceof LocalPlayer localPlayer) {
            if (localPlayer.isUsingItem() && !localPlayer.isPassenger()) {
                if (localPlayer.getUseItem().is(itemHolder -> itemHolder.value() instanceof IEnergyItem || itemHolder.value() instanceof IFuel)) {
                    input.leftImpulse *= 5.0F;
                    input.forwardImpulse *= 5.0F;
                }
            }
        }
    }

    @SubscribeEvent
    public static void RenderHealthBarPre(RenderGuiLayerEvent.Pre event) {
        if (!event.getName().equals(VanillaGuiLayers.PLAYER_HEALTH)) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null){
            return;
        }

        if (!minecraft.options.hideGui && minecraft.gameMode != null
                && minecraft.gameMode.canHurtPlayer() && player.hasEffect(CHEffects.NEUROTOXIN)) {
            setHearts(event);
        }
    }

    private static final ResourceLocation CUSTOM_HEARTS = ClangingHowl.location("textures/gui/custom_hearts.png");

    private static int lastHealth;
    private static int displayHealth;
    private static long lastHealthTime;
    private static long healthBlinkTime;

    private static void setHearts(RenderGuiLayerEvent.Pre event) {
        Player player = Minecraft.getInstance().player;
        Minecraft mc = Minecraft.getInstance();
        if (player == null){
            return;
        }
        Gui gui = mc.gui;
        GuiGraphics stack = event.getGuiGraphics();
        RenderSystem.defaultBlendFunc();
        int width = event.getGuiGraphics().guiWidth();
        int height = event.getGuiGraphics().guiHeight();
        event.setCanceled(true);
        RenderSystem.setShaderTexture(0, CUSTOM_HEARTS);
        RenderSystem.enableBlend();
        int health = Mth.ceil(player.getHealth());
        int tickCount = gui.getGuiTicks();
        boolean highlight = healthBlinkTime > (long)tickCount && (healthBlinkTime - (long)tickCount) / 3L % 2L == 1L;
        if (health < lastHealth && player.invulnerableTime > 0) {
            lastHealthTime = Util.getMillis();
            healthBlinkTime = (long)(tickCount + 20);
        } else if (health > lastHealth && player.invulnerableTime > 0) {
            lastHealthTime = Util.getMillis();
            healthBlinkTime = (long)(tickCount + 10);
        }

        if (Util.getMillis() - lastHealthTime > 1000L) {
            lastHealth = health;
            displayHealth = health;
            lastHealthTime = Util.getMillis();
        }

        lastHealth = health;
        int healthLast = displayHealth;
        float healthMax = (float) player.getAttributeValue(Attributes.MAX_HEALTH);
        int absorption = Mth.ceil(player.getAbsorptionAmount());
        int healthRows = Mth.ceil((healthMax + (float)absorption) / 2.0F / 10.0F);
        int rowHeight = Math.max(10 - (healthRows - 2), 3);
        Random random = new Random();
        random.setSeed((long)tickCount * 312871L);
        int left = width / 2 - 91;
        int top = height - gui.leftHeight;
        gui.leftHeight += healthRows * rowHeight;
        if (rowHeight != 10) {
            gui.leftHeight += 10 - rowHeight;
        }

        int regen = -1;
        if (player.hasEffect(MobEffects.REGENERATION)) {
            regen = tickCount % Mth.ceil(healthMax + 5.0F);
        }

        int TOP = player.level().getLevelData().isHardcore() ? 9 : 0;
        if (highlight){
            TOP = player.level().getLevelData().isHardcore() ? 27 : 18;
        }
        int BACKGROUND_X = highlight ? 25 : 16;
        int BACKGROUND_Y = player.level().getLevelData().isHardcore() ? 9 : 0;
        int heartX = 0;
        if (player.hasEffect(CHEffects.NEUROTOXIN)){
            heartX = 34;
        }
        float absorptionRemaining = (float)absorption;

        for(int i = Mth.ceil((healthMax + (float)absorption) / 2.0F) - 1; i >= 0; --i) {
            int row = Mth.ceil((float)(i + 1) / 10.0F) - 1;
            int x = left + i % 10 * 8;
            int y = top - row * rowHeight;
            if (health <= 4) {
                y += random.nextInt(2);
            }

            if (i == regen) {
                y -= 2;
            }

            stack.blit(CUSTOM_HEARTS, x, y, BACKGROUND_X, BACKGROUND_Y, 9, 9);
            if (highlight) {
                if (i * 2 + 1 < healthLast) {
                    stack.blit(CUSTOM_HEARTS, x, y, heartX, TOP, 9, 9);
                } else if (i * 2 + 1 == healthLast) {
                    stack.blit(CUSTOM_HEARTS, x, y, heartX + 9, TOP, 9, 9);
                }
            }

            if (absorptionRemaining > 0.0F) {
                if (absorptionRemaining == (float)absorption && (float)absorption % 2.0F == 1.0F) {
                    stack.blit(CUSTOM_HEARTS, x, y, heartX + 9, TOP, 9, 9);
                    --absorptionRemaining;
                } else {
                    stack.blit(CUSTOM_HEARTS, x, y, heartX, TOP, 9, 9);
                    absorptionRemaining -= 2.0F;
                }
            } else if (i * 2 + 1 < health) {
                stack.blit(CUSTOM_HEARTS, x, y, heartX, TOP, 9, 9);
            } else if (i * 2 + 1 == health) {
                stack.blit(CUSTOM_HEARTS, x, y, heartX + 9, TOP, 9, 9);
            }
        }

        RenderSystem.disableBlend();
        RenderSystem.setShaderTexture(0, CUSTOM_HEARTS);
    }

    private static boolean prevJumpBindState = false;

    @SubscribeEvent
    public static void TickEvents(ClientTickEvent.Post event) {
        {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player != null){
                Player player = minecraft.player;
                if (minecraft.options.keyJump.isDown() && !prevJumpBindState && !player.isInWater() && CHCapHelper.getTicksInAir(player) > 2 && !player.isCreative() && !player.isSpectator() && !player.isPassenger()) {
                    CHNetwork.sendToServer(new CJetBootsJumpPacket());
                    CHCapHelper.doubleJump(player);
                }
                prevJumpBindState = minecraft.options.keyJump.isDown();
            }
        }
    }

    @SubscribeEvent
    public static void onFogRender(ViewportEvent.RenderFog event) {
        Player player = Minecraft.getInstance().player;

        if (player == null) {
            return;
        }

        ItemStack stack = CHCuriosFinder.findCurio(player, CHItems.X_RAY_GOGGLES.get());

        if (!(stack.getItem() instanceof XRayGoggles) || !XRayGoggles.isActivated(stack) || !player.hasEffect(MobEffects.BLINDNESS) && !player.hasEffect(MobEffects.DARKNESS)) {
            return;
        }

        float f = 1.0F * (player.hasEffect(MobEffects.BLINDNESS) ? 9 : 1);

        event.scaleFarPlaneDistance(event.getFarPlaneDistance() * f);

        event.setCanceled(true);
    }

    /**
     * Ripped from @BobMowzie's codes:<a href="https://github.com/BobMowzie/MowziesMobs/blob/master/src/main/java/com/bobmowzie/mowziesmobs/client/ClientEventHandler.java#L211">...</a>
     */
    @SubscribeEvent
    public static void onSetupCamera(ViewportEvent.ComputeCameraAngles event) {
        Player player = Minecraft.getInstance().player;
        float delta = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
        if (player != null) {
            float ticksExistedDelta = player.tickCount + delta;
            if (CHConfig.CameraShake.get() && !Minecraft.getInstance().isPaused()) {
                float shakeAmplitude = 0;
                for (CameraShake cameraShake : player.level().getEntitiesOfClass(CameraShake.class, player.getBoundingBox().inflate(20))) {
                    if (cameraShake.distanceTo(player) < cameraShake.getRadius()) {
                        shakeAmplitude += cameraShake.getShakeAmount(player, delta);
                    }
                }
                if (shakeAmplitude > 1.0F) {
                    shakeAmplitude = 1.0F;
                }
                event.setPitch((float) (event.getPitch() + shakeAmplitude * Math.cos(ticksExistedDelta * 3.0D + 2.0D) * 25.0D));
                event.setYaw((float) (event.getYaw() + shakeAmplitude * Math.cos(ticksExistedDelta * 5.0D + 1.0D) * 25.0D));
                event.setRoll((float) (event.getRoll() + shakeAmplitude * Math.cos(ticksExistedDelta * 4.0D) * 25.0D));
            }
        }
    }

    @SubscribeEvent
    public static void KeyInputs(InputEvent.Key event) {
        Minecraft MINECRAFT = Minecraft.getInstance();

        if (MINECRAFT.player != null) {
            if (CHKeybindings.keyBindings[0].isDown() && MINECRAFT.player.isCrouching() && MINECRAFT.isWindowActive()) {
                CHNetwork.sendToServer(new CActivateCurioKeyPacket());
            }
        }
    }
}
