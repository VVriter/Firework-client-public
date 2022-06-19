package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketEnchantItem;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlaceRecipe;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketRecipeInfo;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraft.network.play.client.CPacketSeenAdvancements;
import net.minecraft.network.play.client.CPacketSpectate;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketAdvancementInfo;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketBlockAction;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketCamera;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.network.play.server.SPacketCombatEvent;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketCooldown;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.network.play.server.SPacketDisplayObjective;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.network.play.server.SPacketEntityHeadLook;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketKeepAlive;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketPlaceGhostRecipe;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRecipeBook;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraft.network.play.server.SPacketResourcePackSend;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketScoreboardObjective;
import net.minecraft.network.play.server.SPacketSelectAdvancementsTab;
import net.minecraft.network.play.server.SPacketServerDifficulty;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.network.play.server.SPacketSignEditorOpen;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.network.play.server.SPacketStatistics;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.network.play.server.SPacketUnloadChunk;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.network.play.server.SPacketUpdateScore;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraft.network.play.server.SPacketWindowProperty;
import net.minecraft.network.play.server.SPacketWorldBorder;

@ModuleArgs(name = "PacketCanceler",category = Module.Category.WORLD)
public class PacketCanceler extends Module {


    public Setting<Enum> page = new Setting<>("Page", pages.CPackets, this, pages.values());
    public enum pages{CPackets, SPackets}


    public Setting<Boolean> Animations = new Setting<>("Animations", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> ChatMessage = new Setting<>("ChatMessage", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> ClickWindow = new Setting<>("ClickWindow", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> ClientSettings = new Setting<>("ClientSettings", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> ClientStatus = new Setting<>("ClientStatus", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> CloseWindows = new Setting<>("CloseWindows", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> ConfirmTeleport = new Setting<>("ConfirmTeleport", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> ConfirmTransactions = new Setting<>("ConfirmTransactions", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> CreativeInventoryAction = new Setting<>("CreativeInventoryAction", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> CustomPayloads = new Setting<>("CustomPayloads", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> EnchantItem = new Setting<>("EnchantItem", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> EntityAction = new Setting<>("EntityAction", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> HeldItemChanges = new Setting<>("HeldItemChanges", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> Input = new Setting<>("Input", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> KeepAlives = new Setting<>("KeepAlives", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> PlaceRecipe = new Setting<>("PlaceRecipe", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> Player = new Setting<>("Player", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> PlayerAbility = new Setting<>("PlayerAbility", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> PlayerDigging = new Setting<>("PlayerDigging", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> PlayerTryUseItem = new Setting<>("PlayerTryUseItem", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> PlayerTryUseItemOnBlock = new Setting<>("PlayerTryUseItemOnBlock", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> RecipeInfo = new Setting<>("RecipeInfo", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> ResourcePackStatus = new Setting<>("ResourcePackStatus", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> SeenAdvancements = new Setting<>("SeenAdvancements", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> Spectate = new Setting<>("Spectate", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> SteerBoat = new Setting<>("SteerBoat", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> TabCompletion = new Setting<>("TabCompletion", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> UpdateSign = new Setting<>("UpdateSign", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> UseEntity = new Setting<>("UseEntity", false, this).setVisibility(page,pages.CPackets);
    public Setting<Boolean> VehicleMove = new Setting<>("VehicleMove", false, this).setVisibility(page,pages.CPackets);

    public Setting<Boolean> AdvancementInfo = new Setting<>("AdvancementInfo", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> Animation = new Setting<>("Animation", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> BlockAction = new Setting<>("BlockAction", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> BlockBreakAnim = new Setting<>("BlockBreakAnim", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> BlockChange = new Setting<>("BlockChange", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> Camera = new Setting<>("Camera", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> ChangeGameState = new Setting<>("ChangeGameState", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> Chat = new Setting<>("Chat", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> ChunkData = new Setting<>("ChunkData", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> CloseWindow = new Setting<>("CloseWindow", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> CollectItem = new Setting<>("CollectItem", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> CombatEvent = new Setting<>("CombatEvent", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> ConfirmTransaction = new Setting<>("ConfirmTransaction", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> Cooldown = new Setting<>("Cooldown", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> CustomPayload = new Setting<>("CustomPayload", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> CustomSound = new Setting<>("CustomSound", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> DestroyEntities = new Setting<>("DestroyEntities", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> Disconnect = new Setting<>("Disconnect", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> DisplayObjective = new Setting<>("DisplayObjective", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> Effect = new Setting<>("Effect", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> Entity = new Setting<>("Entity", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> EntityAttach = new Setting<>("EntityAttach", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> EntityEffect = new Setting<>("EntityEffect", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> EntityEquipment = new Setting<>("EntityEquipment", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> EntityHeadLook = new Setting<>("EntityHeadLook", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> EntityMetadata = new Setting<>("EntityMetadata", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> EntityProperties = new Setting<>("EntityProperties", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> EntityStatus = new Setting<>("EntityStatus", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> EntityTeleport = new Setting<>("EntityTeleport", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> EntityVelocity = new Setting<>("EntityVelocity", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> Explosion = new Setting<>("Explosion", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> HeldItemChange = new Setting<>("HeldItemChange", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> JoinGame = new Setting<>("JoinGame", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> KeepAlive = new Setting<>("KeepAlive", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> Maps = new Setting<>("Maps", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> MoveVehicle = new Setting<>("MoveVehicle", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> MultiBlockChange = new Setting<>("MultiBlockChange", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> OpenWindow = new Setting<>("OpenWindow", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> Particles = new Setting<>("Particles", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> PlaceGhostRecipe = new Setting<>("PlaceGhostRecipe", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> PlayerAbilities = new Setting<>("PlayerAbilities", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> PlayerListHeaderFooter = new Setting<>("PlayerListHeaderFooter", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> PlayerListItem = new Setting<>("PlayerListItem", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> PlayerPosLook = new Setting<>("PlayerPosLook", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> RecipeBook = new Setting<>("RecipeBook", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> RemoveEntityEffect = new Setting<>("RemoveEntityEffect", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> ResourcePackSend = new Setting<>("ResourcePackSend", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> Respawn = new Setting<>("Respawn", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> ScoreboardObjective = new Setting<>("ScoreboardObjective", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> SelectAdvancementsTab = new Setting<>("SelectAdvancementsTab", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> ServerDifficulty = new Setting<>("ServerDifficulty", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> SetExperience = new Setting<>("SetExperience", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> SetPassengers = new Setting<>("SetPassengers", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> SetSlot = new Setting<>("SetSlot", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> SignEditorOpen = new Setting<>("SignEditorOpen", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> SoundEffect = new Setting<>("SoundEffect", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> SpawnExperienceOrb = new Setting<>("SpawnExperienceOrb", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> SpawnGlobalEntity = new Setting<>("SpawnGlobalEntity", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> SpawnMob = new Setting<>("SpawnMob", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> SpawnObject = new Setting<>("SpawnObject", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> SpawnPainting = new Setting<>("SpawnPainting", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> SpawnPlayer = new Setting<>("SpawnPlayer", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> SpawnPosition = new Setting<>("SpawnPosition", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> Statistics = new Setting<>("Statistics", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> TabComplete = new Setting<>("TabComplete", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> Teams = new Setting<>("Teams", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> TimeUpdate = new Setting<>("TimeUpdate", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> Title = new Setting<>("Title", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> UnloadChunk = new Setting<>("UnloadChunk", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> UpdateBossInfo = new Setting<>("UpdateBossInfo", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> UpdateHealth = new Setting<>("UpdateHealth", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> UpdateScore = new Setting<>("UpdateScore", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> UpdateTileEntity = new Setting<>("UpdateTileEntity", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> UseBed = new Setting<>("UseBed", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> WindowItems = new Setting<>("WindowItems", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> WindowProperty = new Setting<>("WindowProperty", false, this).setVisibility(page,pages.SPackets);
    public Setting<Boolean> WorldBorder = new Setting<>("WorldBorder", false, this).setVisibility(page,pages.SPackets);








    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketAnimation && this.Animations.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketChatMessage && this.ChatMessage.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketClickWindow && this.ClickWindow.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketClientSettings && this.ClientSettings.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketClientStatus && this.ClientStatus.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketCloseWindow && this.CloseWindows.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketConfirmTeleport && this.ConfirmTeleport.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketConfirmTransaction && this.ConfirmTransactions.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketCreativeInventoryAction && this.CreativeInventoryAction.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketCustomPayload && this.CustomPayloads.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketEnchantItem && this.EnchantItem.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketEntityAction && this.EntityAction.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketHeldItemChange && this.HeldItemChanges.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketInput && this.Input.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketKeepAlive && this.KeepAlives.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlaceRecipe && this.PlaceRecipe.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlayer && this.Player.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlayerAbilities && this.PlayerAbility.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlayerDigging && this.PlayerDigging.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlayerTryUseItem && this.PlayerTryUseItem.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && this.PlayerTryUseItemOnBlock.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketRecipeInfo && this.RecipeInfo.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketResourcePackStatus && this.ResourcePackStatus.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketSeenAdvancements && this.SeenAdvancements.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketSpectate && this.Spectate.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketSteerBoat && this.SteerBoat.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketTabComplete && this.TabCompletion.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketUpdateSign && this.UpdateSign.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketUseEntity && this.UseEntity.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketVehicleMove && this.VehicleMove.getValue().booleanValue()) {
            event.setCanceled(true);
        }
    }


    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketAdvancementInfo && this.AdvancementInfo.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketAnimation && this.Animation.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketBlockAction && this.BlockAction.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketBlockBreakAnim && this.BlockBreakAnim.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketBlockChange && this.BlockChange.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCamera && this.Camera.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketChangeGameState && this.ChangeGameState.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketChat && this.Chat.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketChunkData && this.ChunkData.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCloseWindow && this.CloseWindow.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCollectItem && this.CollectItem.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCombatEvent && this.CombatEvent.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketConfirmTransaction && this.ConfirmTransaction.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCooldown && this.Cooldown.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCustomPayload && this.CustomPayload.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCustomSound && this.CustomSound.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketDestroyEntities && this.DestroyEntities.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketDisconnect && this.Disconnect.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketChunkData && this.ChunkData.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCloseWindow && this.CloseWindow.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCollectItem && this.CollectItem.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketDisplayObjective && this.DisplayObjective.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEffect && this.Effect.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntity && this.Entity.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityAttach && this.EntityAttach.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityEffect && this.EntityEffect.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityEquipment && this.EntityEquipment.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityHeadLook && this.EntityHeadLook.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityMetadata && this.EntityMetadata.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityProperties && this.EntityProperties.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityStatus && this.EntityStatus.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityTeleport && this.EntityTeleport.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityVelocity && this.EntityVelocity.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketExplosion && this.Explosion.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketHeldItemChange && this.HeldItemChange.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketJoinGame && this.JoinGame.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketKeepAlive && this.KeepAlive.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketMaps && this.Maps.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketMoveVehicle && this.MoveVehicle.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketMultiBlockChange && this.MultiBlockChange.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketOpenWindow && this.OpenWindow.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketParticles && this.Particles.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketPlaceGhostRecipe && this.PlaceGhostRecipe.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketPlayerAbilities && this.PlayerAbilities.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketPlayerListHeaderFooter && this.PlayerListHeaderFooter.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketPlayerListItem && this.PlayerListItem.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook && this.PlayerPosLook.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketRecipeBook && this.RecipeBook.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketRemoveEntityEffect && this.RemoveEntityEffect.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketResourcePackSend && this.ResourcePackSend.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketRespawn && this.Respawn.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketScoreboardObjective && this.ScoreboardObjective.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSelectAdvancementsTab && this.SelectAdvancementsTab.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketServerDifficulty && this.ServerDifficulty.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSetExperience && this.SetExperience.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSetPassengers && this.SetPassengers.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSetSlot && this.SetSlot.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSignEditorOpen && this.SignEditorOpen.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSoundEffect && this.SoundEffect.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSpawnExperienceOrb && this.SpawnExperienceOrb.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSpawnGlobalEntity && this.SpawnGlobalEntity.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSpawnMob && this.SpawnMob.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSpawnObject && this.SpawnObject.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSpawnPainting && this.SpawnPainting.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSpawnPlayer && this.SpawnPlayer.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSpawnPosition && this.SpawnPosition.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketStatistics && this.Statistics.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketTabComplete && this.TabComplete.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketTeams && this.Teams.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketTimeUpdate && this.TimeUpdate.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketTitle && this.Title.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketUnloadChunk && this.UnloadChunk.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketUpdateBossInfo && this.UpdateBossInfo.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketUpdateHealth && this.UpdateHealth.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketUpdateScore && this.UpdateScore.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketUpdateTileEntity && this.UpdateTileEntity.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketUseBed && this.UseBed.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketWindowItems && this.WindowItems.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketWindowProperty && this.WindowProperty.getValue().booleanValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketWorldBorder && this.WorldBorder.getValue().booleanValue()) {
            event.setCanceled(true);
        }
    }

}
