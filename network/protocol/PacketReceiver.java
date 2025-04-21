package org.aoclient.network.protocol;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.handlers.*;
import org.aoclient.network.protocol.handlers.gm.*;
import org.aoclient.network.packets.ServerPacketID;

import java.util.HashMap;
import java.util.Map;

/**
 * Patron Command para los handlers.
 */

public class PacketReceiver {

    private final Map<ServerPacketID, PacketHandler> handlers = new HashMap<>();

    public PacketReceiver() {
        registerHandlers();
    }

    private void registerHandlers() {
        handlers.put(ServerPacketID.logged, new LoggedPacketHandler());
        handlers.put(ServerPacketID.Disconnect, new DisconnectHandler());
    }

    public void processIncomingData(ByteQueue data) {
        if (data.length() == 0) return; // TODO Y si llega a ser -1?

        // Obtiene el tipo de paquete
        int packetId = data.peekByte();
        if (packetId >= ServerPacketID.values().length) return;

        ServerPacketID packet = ServerPacketID.values()[packetId];

        // Logger.debug(packet + " #" + packet); // ?

        // Identifica el tipo de paquete y llama al handler correspondiente
        PacketHandler handler = handlers.get(packet);

        if (handler != null) {
            handler.handle(data);

            // Si quedan datos, continua procesando
            if (data.length() > 0) processIncomingData(data);

        }

    }

}
