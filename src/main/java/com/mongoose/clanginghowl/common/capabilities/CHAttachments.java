package com.mongoose.clanginghowl.common.capabilities;

import com.mongoose.clanginghowl.ClangingHowl;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

/** Serializable per-entity state. Initial client synchronization is handled separately. */
@EventBusSubscriber(modid = ClangingHowl.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class CHAttachments {
    // Do not copy on death: the old provider did not preserve state on death either.
    // The attachment serializer also supplies NeoForge's deep-copy path for non-death copies.
    public static final AttachmentType<CHCapImp> STATE =
            AttachmentType.serializable(CHCapImp::new).build();

    private CHAttachments() {
    }

    @SubscribeEvent
    public static void registerAttachments(RegisterEvent event) {
        event.register(NeoForgeRegistries.Keys.ATTACHMENT_TYPES,
                helper -> helper.register(ClangingHowl.location("misc"), STATE));
    }
}
