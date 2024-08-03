package org.allaymc.server.network.processor;

import org.allaymc.api.entity.interfaces.EntityPlayer;
import org.allaymc.api.eventbus.event.player.PlayerChatEvent;
import org.allaymc.api.network.processor.PacketProcessor;
import org.allaymc.api.server.Server;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacketType;
import org.cloudburstmc.protocol.bedrock.packet.TextPacket;

/**
 * Allay Project 11/22/2023
 *
 * @author Cool_Loong
 */
public class TextPacketProcessor extends PacketProcessor<TextPacket> {
    @Override
    public void handleSync(EntityPlayer player, TextPacket packet, long receiveTime) {
        if (packet.getType() == TextPacket.Type.CHAT) {
            var event = new PlayerChatEvent(player, "<" + player.getDisplayName() + "> ", packet.getMessage());
            Server.getInstance().getEventBus().callEvent(event);
            if (event.isCancelled()) {
                return;
            }

            Server.getInstance().broadcastMessage(event.buildChat());
        }
    }

    @Override
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.TEXT;
    }
}
