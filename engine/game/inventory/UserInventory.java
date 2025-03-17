package org.aoclient.engine.game.inventory;

import imgui.ImGui;
import org.aoclient.engine.game.IntervalTimer;
import org.aoclient.engine.listeners.MouseListener;

import static org.aoclient.engine.scenes.Camera.TILE_PIXEL_SIZE;
import static org.aoclient.network.Protocol.writeEquipItem;
import static org.aoclient.network.Protocol.writeUseItem;

/**
 * Clase creada unicamente para el inventario de nuestro usuario, viene heredada de la clase Inventory.
 */

public final class UserInventory extends Inventory {

    public static final int FLAGORO = MAX_INVENTORY_SLOTS + 1;
    // Posicion original del picInv del frmMain original de AO
    private static final int MAIN_POS_X = 600;
    private static final int MAIN_POS_Y = 160;
    // Tamaño del inventario segun el frmMain original de AO
    private static final int MAIN_SIZE_WIDTH = 160;
    private static final int MAIN_SIZE_HEIGHT = 128;
    // Intervalos del inventario.
    private static final float INT_USE_ITEM = 0.240f;
    private static final float INT_EQUIP_ITEM = 0.15f;
    private final IntervalTimer intervalUseItem = new IntervalTimer(INT_USE_ITEM);
    private final IntervalTimer intervalEquipItem = new IntervalTimer(INT_EQUIP_ITEM);

    private boolean visible;

    /**
     * Inventory user constructor
     */
    public UserInventory() {
        super(MAIN_POS_X, MAIN_POS_Y, MAIN_SIZE_WIDTH, MAIN_SIZE_HEIGHT, MAX_INVENTORY_SLOTS);
        this.visible = true;
    }

    public void updateTimers() {
        intervalUseItem.update();
        intervalEquipItem.update();
    }

    /**
     * @desc: Actualiza y dibuja el inventario.
     */
    @Override
    public void drawInventory() {
        if (slots.length == 0) return;

        // posiciones por slot
        int iX = posX;
        int iY = posY;

        for (int i = 0; i < slots.length; i++) {
            if (slots[i].grhIndex > 0) {
                ImGui.setCursorPos(iX + 5, iY);
                ImGui.image(slots[i].objTexture.getId(), 32, 32);
                if (ImGui.isItemHovered()) ImGui.setTooltip(slots[i].name);
                if (i == slotSelected) {
                    ImGui.getWindowDrawList().addLine(iX + 31, iY, iX + 31, iY + 31, selectedColor);
                    ImGui.getWindowDrawList().addLine(iX, iY + 31, iX + 31, iY + 31, selectedColor);
                }
                ImGui.setCursorPos(iX + 5, iY + 20);
                ImGui.text(String.valueOf(slots[i].amount));
                if (slots[i].equipped) {
                    ImGui.setCursorPos(iX + 28, iY);
                    ImGui.textColored(ImGui.getColorU32(1f, 1f, 0f, 1f), "E");
                }
            }
            // actualizamos la posicion en forma de tabla.
            iX += TILE_PIXEL_SIZE;
            if ((iX - posX) / TILE_PIXEL_SIZE == this.cantColumns) {
                iY += TILE_PIXEL_SIZE;
                iX = posX;
            }
        }

    }

    /**
     * @desc Realiza una accion al hacer doble click dentro del inventario.
     */
    public void dobleClickInventory() {
        if (!visible) return;

        final int x = (int) ((MouseListener.getX() - posX) / TILE_PIXEL_SIZE);
        final int y = (int) ((MouseListener.getY() - posY) / TILE_PIXEL_SIZE);

        // esta el mouse dentro del inventario??
        if (inInventoryArea()) {
            // Esta selecionado primero?
            if (x + (this.cantColumns * y) == this.slotSelected) {
                if (slots[slotSelected].grhIndex > 0) {
                    // es equipable?
                    if (slots[slotSelected].objType.equippable) equipItem();
                    else useItem();
                }
            }
        }
    }

    public void useItem() {
        if (slots[slotSelected].grhIndex > 0 && intervalUseItem.check()) writeUseItem(this.slotSelected + 1);
    }

    public void equipItem() {
        // no vamos a mandar un paquete al pedo.
        if (slots[slotSelected].grhIndex > 0 && slots[slotSelected].objType.equippable && intervalEquipItem.check())
            writeEquipItem(this.slotSelected + 1);

    }

    public void setVisible(boolean value) {
        this.visible = value;
    }

}
