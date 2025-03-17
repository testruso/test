package org.aoclient.network;

import org.aoclient.engine.game.Dialogs;
import org.aoclient.engine.game.models.E_FontType;
import org.aoclient.engine.gui.forms.FComerce;
import org.aoclient.network.packets.ClientPacketID;
import org.aoclient.network.packets.eGMCommands;
import org.aoclient.network.packets.E_Messages;
import org.aoclient.network.packets.ServerPacketID;
import org.aoclient.engine.Sound;
import org.aoclient.engine.Window;
import org.aoclient.engine.game.Console;
import org.aoclient.engine.game.Rain;
import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.E_Heading;
import org.aoclient.engine.game.models.E_ObjType;
import org.aoclient.engine.game.models.E_Skills;
import org.aoclient.engine.gui.forms.FMessage;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.engine.utils.GameData;
import org.aoclient.engine.utils.inits.*;
import org.tinylog.Logger;

import java.nio.charset.StandardCharsets;

import static org.aoclient.engine.game.Dialogs.charDialogHitSet;
import static org.aoclient.engine.game.Dialogs.charDialogSet;
import static org.aoclient.network.Messages.*;
import static org.aoclient.engine.Sound.*;
import static org.aoclient.engine.game.models.Character.*;
import static org.aoclient.engine.game.models.E_Skills.FundirMetal;
import static org.aoclient.engine.utils.GameData.*;
import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Protocol {

    static ByteQueue incomingData = new ByteQueue();
    static ByteQueue outgoingData = new ByteQueue();
    static int pingTime = 0;
    static int lastPacket;
    private static final Console console = Console.get();

    public static void handleIncomingData() {
        lastPacket = incomingData.peekByte();

        if (lastPacket > ServerPacketID.values().length) return;
        ServerPacketID packet = ServerPacketID.values()[lastPacket];
        //Logger.debug(packet + " #" + p);

        switch (packet) {
            case logged: handleLogged(); break;
            case RemoveDialogs: handleRemoveDialogs(); break;
            case RemoveCharDialog: handleRemoveCharDialog(); break;
            case NavigateToggle: handleNavigateToggle(); break;
            case Disconnect: handleDisconnect(); break;
            case CommerceEnd: handleCommerceEnd(); break;
            case CommerceChat: handleCommerceChat();break;
            case BankEnd: handleBankEnd();break;
            case CommerceInit: handleCommerceInit();break;
            case BankInit: handleBankInit();break;
            case UserCommerceInit: handleUserCommerceInit();break;
            case UserCommerceEnd: handleUserCommerceEnd();break;
            case UserOfferConfirm: handleUserOfferConfirm();break;
            case ShowBlacksmithForm: handleShowBlacksmithForm();break;
            case ShowCarpenterForm: handleShowCarpenterForm();break;
            case UpdateSta: handleUpdateSta();break;
            case UpdateMana: handleUpdateMana();break;
            case UpdateHP: handleUpdateHP();break;
            case UpdateGold: handleUpdateGold();break;
            case UpdateBankGold: handleUpdateBankGold();break;
            case UpdateExp: handleUpdateExp();break;
            case ChangeMap: handleChangeMap();break;
            case PosUpdate: handlePosUpdate();break;
            case ChatOverHead: handleChatOverHead();break;
            case ConsoleMsg: handleConsoleMessage();break;
            case GuildChat: handleGuildChat();break;
            case ShowMessageBox: handleShowMessageBox();break;
            case UserIndexInServer: handleUserIndexInServer();break;
            case UserCharIndexInServer: handleUserCharIndexInServer();break;
            case CharacterCreate: handleCharacterCreate();break;
            case CharacterRemove: handleCharacterRemove();break;
            case CharacterChangeNick: handleCharacterChangeNick();break;
            case CharacterMove: handleCharacterMove();break;
            case ForceCharMove: handleForceCharMove();break;
            case CharacterChange: handleCharacterChange();break;
            case ObjectCreate: handleObjectCreate();break;
            case ObjectDelete: handleObjectDelete();break;
            case BlockPosition: handleBlockPosition();break;
            case PlayMIDI: handlePlayMIDI();break;
            case PlayWave: handlePlayWave();break;
            case guildList: handleGuildList();break;
            case AreaChanged: handleAreaChanged();break;
            case PauseToggle: handlePauseToggle();break;
            case RainToggle: handleRainToggle();break;
            case CreateFX: handleCreateFX();break;
            case UpdateUserStats: handleUpdateUserStats();break;
            case WorkRequestTarget: handleWorkRequestTarget();break;
            case ChangeInventorySlot: handleChangeInventorySlot();break;
            case ChangeBankSlot: handleChangeBankSlot();break;
            case ChangeSpellSlot: handleChangeSpellSlot();break;
            case Atributes: handleAtributes();break;
            case BlacksmithWeapons: handleBlacksmithWeapons();break;
            case BlacksmithArmors: handleBlacksmithArmors();break;
            case CarpenterObjects: handleCarpenterObjects();break;
            case RestOK: handleRestOK();break;
            case ErrorMsg: handleErrorMessage();break;
            case Blind: handleBlind();break;
            case Dumb: handleDumb();break;
            case ShowSignal: handleShowSignal();break;
            case ChangeNPCInventorySlot: handleChangeNPCInventorySlot();break;
            case UpdateHungerAndThirst: handleUpdateHungerAndThirst();break;
            case Fame: handleFame();break;
            case MiniStats: handleMiniStats();break;
            case LevelUp: handleLevelUp();break;
            case AddForumMsg: handleAddForumMessage();break;
            case ShowForumForm: handleShowForumForm();break;
            case SetInvisible: handleSetInvisible();break;
            case DiceRoll: handleDiceRoll();break;
            case MeditateToggle: handleMeditateToggle();break;
            case BlindNoMore: handleBlindNoMore();break;
            case DumbNoMore: handleDumbNoMore();break;
            case SendSkills: handleSendSkills();break;
            case TrainerCreatureList: handleTrainerCreatureList();break;
            case guildNews: handleGuildNews();break;
            case OfferDetails: handleOfferDetails();break;
            case AlianceProposalsList: handleAlianceProposalsList();break;
            case PeaceProposalsList: handlePeaceProposalsList();break;
            case CharacterInfo: handleCharacterInfo();break;
            case GuildLeaderInfo: handleGuildLeaderInfo();break;
            case GuildDetails: handleGuildDetails();break;
            case ShowGuildFundationForm: handleShowGuildFundationForm();break;
            case ParalizeOK: handleParalizeOK();break;
            case ShowUserRequest: handleShowUserRequest();break;
            case TradeOK: handleTradeOK();break;
            case BankOK: handleBankOK();break;
            case ChangeUserTradeSlot: handleChangeUserTradeSlot();break;
            case SendNight: handleSendNight();break;
            case Pong: handlePong();break;
            case UpdateTagAndStatus: handleUpdateTagAndStatus();break;
            case GuildMemberInfo: handleGuildMemberInfo();break;

            //*******************
            // GM messages
            //*******************
            case SpawnList: handleSpawnList();break;
            case ShowSOSForm: handleShowSOSForm();break;
            case ShowMOTDEditionForm: handleShowMOTDEditionForm();break;
            case ShowGMPanelForm: handleShowGMPanelForm();break;
            case UserNameList: handleUserNameList();break;
            case ShowGuildAlign: handleShowGuildAlign();break;
            case ShowPartyForm: handleShowPartyForm();break;
            case UpdateStrenghtAndDexterity: handleUpdateStrenghtAndDexterity();break;
            case UpdateStrenght: handleUpdateStrenght();break;
            case UpdateDexterity: handleUpdateDexterity();break;
            case AddSlots: handleAddSlots();break;
            case MultiMessage: handleMultiMessage();break;
            case StopWorking: handleStopWorking();break;
            case CancelOfferItem: handleCancelOfferItem();break;
            default: return;
        }

        // Done with this packet, move on to next one
        if (incomingData.length() > 0) handleIncomingData();

    }

    private static void handleCancelOfferItem() {
        // Remove packet ID
        incomingData.readByte();

        int slot = incomingData.readByte();

        //With InvOfferComUsu(0)
        //        Amount = .Amount(slot)
        //
        //        ' No tiene sentido que se quiten 0 unidades
        //        If Amount <> 0 Then
        //            ' Actualizo el inventario general
        //            Call frmComerciarUsu.UpdateInvCom(.OBJIndex(slot), Amount)
        //
        //            ' Borro el item
        //            Call .SetItem(slot, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, "")
        //        End If
        //    End With
        //
        //    ' Si era el único ítem de la oferta, no puede confirmarla
        //    If Not frmComerciarUsu.HasAnyItem(InvOfferComUsu(0)) And _
        //        Not frmComerciarUsu.HasAnyItem(InvOroComUsu(1)) Then Call frmComerciarUsu.HabilitarConfirmar(False)
        //
        //    With FontTypes(FontTypeNames.FONTTYPE_INFO)
        //        Call frmComerciarUsu.PrintCommerceMsg("¡No puedes comerciar ese objeto!", FontTypeNames.FONTTYPE_INFO)
        //    End With
    }

    private static void handleStopWorking() {
        // Remove packet ID
        incomingData.readByte();

        //With FontTypes(FontTypeNames.FONTTYPE_INFO)
        //        Call ShowConsoleMsg("¡Has terminado de trabajar!", .red, .green, .blue, .bold, .italic)
        //    End With
        //
        //    If //FrmMain.macrotrabajo.Enabled Then Call //FrmMain.DesactivarMacroTrabajo
    }

    private static void handleMultiMessage() {
        int bodyPart;
        short damage;

        // Remove packet ID
        incomingData.readByte();

        int m = incomingData.readByte();

        if (m > E_Messages.values().length) return;
        E_Messages msg = E_Messages.values()[m];

        switch (msg) {
            case DontSeeAnything:
                console.addMsgToConsole(MENSAJE_NO_VES_NADA_INTERESANTE, false, false, new RGBColor(0.25f, 0.75f, 0.60f));
                break;

            case NPCSwing:
                console.addMsgToConsole(MENSAJE_CRIATURA_FALLA_GOLPE, false, false, new RGBColor(1f, 0f, 0f));
                break;

            case NPCKillUser:
                console.addMsgToConsole(MENSAJE_CRIATURA_MATADO, false, false, new RGBColor(1f, 0f, 0f));
                break;

            case BlockedWithShieldUser:
                console.addMsgToConsole(MENSAJE_RECHAZO_ATAQUE_ESCUDO, false, false, new RGBColor(1f, 0f, 0f));
                break;

            case BlockedWithShieldOther:
                console.addMsgToConsole(MENSAJE_USUARIO_RECHAZO_ATAQUE_ESCUDO, false, false, new RGBColor(1f, 0f, 0f));
                break;

            case UserSwing:
                console.addMsgToConsole(MENSAJE_FALLADO_GOLPE, false, false, new RGBColor(1f, 0f, 0f));
                charDialogHitSet(User.get().getUserCharIndex(), "*Fallas*");
                break;

            case SafeModeOn:
                console.addMsgToConsole("MODO SEGURO ACTIVADO", false, false, new RGBColor(0f, 1f, 0f));
                break;

            case SafeModeOff:
                console.addMsgToConsole("MODO SEGURO DESACTIVADO", false, false, new RGBColor(1f, 0f, 0f));
                break;

            case ResuscitationSafeOff:
                console.addMsgToConsole("MODO RESURECCION ACTIVADO", false, false, new RGBColor(0f, 1f, 0f));
                break;

            case ResuscitationSafeOn:
                console.addMsgToConsole("MODO RESURECCION DESACTIVADO", false, false, new RGBColor(1f, 0f, 0f));
                break;

            case NobilityLost:
                console.addMsgToConsole(MENSAJE_PIERDE_NOBLEZA, false, false, new RGBColor(1f, 0f, 0f));
                break;

            case CantUseWhileMeditating:
                console.addMsgToConsole(MENSAJE_USAR_MEDITANDO, false, false, new RGBColor(1f, 0f, 0f));
                break;

            case NPCHitUser:
                switch (incomingData.readByte()) {
                    case 1: // bCabeza
                        console.addMsgToConsole(MENSAJE_GOLPE_CABEZA + " " + incomingData.readInteger(),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 2: // bBrazoIzquierdo
                        console.addMsgToConsole(MENSAJE_GOLPE_BRAZO_IZQ + " " + incomingData.readInteger(),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 3: // bBrazoDerecho
                        console.addMsgToConsole(MENSAJE_GOLPE_BRAZO_DER + " " + incomingData.readInteger(),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 4: // bPiernaIzquierda
                        console.addMsgToConsole(MENSAJE_GOLPE_PIERNA_IZQ + " " + incomingData.readInteger(),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 5: // bPiernaDerecha
                        console.addMsgToConsole(MENSAJE_GOLPE_PIERNA_DER + " " + incomingData.readInteger(),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 6: // bTorso
                        console.addMsgToConsole(MENSAJE_GOLPE_TORSO + " " + incomingData.readInteger(),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;
                }
                break;

            case UserHitNPC:
                final int d = incomingData.readLong();
                console.addMsgToConsole(MENSAJE_GOLPE_CRIATURA_1 + " " + d,
                        false, false, new RGBColor(1f, 0f, 0f));

                charDialogHitSet(User.get().getUserCharIndex(), d);

                break;

            case UserAttackedSwing:
                final short charIndexAttaker = incomingData.readInteger();

                console.addMsgToConsole(MENSAJE_1 + " " + charList[charIndexAttaker].getName() + MENSAJE_ATAQUE_FALLO,
                        false, false, new RGBColor(1f, 0f, 0f));

                charDialogHitSet(charIndexAttaker, "*Falla*");
                break;

            case UserHittedByUser:
                final int charIndexHitAttaker = incomingData.readInteger();
                String attackerName = "<" + charList[charIndexHitAttaker].getName() + ">";
                bodyPart = incomingData.readByte();
                damage = incomingData.readInteger();

                switch (bodyPart) {
                    case 1: // bCabeza
                        console.addMsgToConsole(MENSAJE_1 + attackerName + MENSAJE_RECIVE_IMPACTO_CABEZA + damage + MENSAJE_2,
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 2: // bBrazoIzquierdo
                        console.addMsgToConsole(MENSAJE_1 + attackerName + MENSAJE_RECIVE_IMPACTO_BRAZO_IZQ + damage + MENSAJE_2,
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 3: // bBrazoDerecho
                        console.addMsgToConsole(MENSAJE_1 + attackerName + MENSAJE_RECIVE_IMPACTO_BRAZO_DER + damage + MENSAJE_2,
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 4: // bPiernaIzquierda
                        console.addMsgToConsole(MENSAJE_1 + attackerName + MENSAJE_RECIVE_IMPACTO_PIERNA_IZQ + damage + MENSAJE_2,
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 5: // bPiernaDerecha
                        console.addMsgToConsole(MENSAJE_1 + attackerName + MENSAJE_RECIVE_IMPACTO_PIERNA_DER + damage + MENSAJE_2,
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 6: // bTorso
                        console.addMsgToConsole(MENSAJE_1 + attackerName + MENSAJE_RECIVE_IMPACTO_TORSO + damage + MENSAJE_2,
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;
                }

                charDialogHitSet(charIndexHitAttaker, damage);

                break;

            case UserHittedUser:
                final int charIndexVictim = incomingData.readInteger();
                final String victimName = "<" + charList[charIndexVictim].getName() + ">";
                bodyPart = incomingData.readByte();
                damage = incomingData.readInteger();

                switch (bodyPart) {
                    case 1: // bCabeza
                        console.addMsgToConsole(MENSAJE_PRODUCE_IMPACTO_1 + victimName + MENSAJE_PRODUCE_IMPACTO_CABEZA + damage + MENSAJE_2,
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 2: // bBrazoIzquierdo
                        console.addMsgToConsole(MENSAJE_PRODUCE_IMPACTO_1 + victimName + MENSAJE_PRODUCE_IMPACTO_BRAZO_IZQ + damage + MENSAJE_2,
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 3: // bBrazoDerecho
                        console.addMsgToConsole(MENSAJE_PRODUCE_IMPACTO_1 + victimName + MENSAJE_RECIVE_IMPACTO_BRAZO_DER + damage + MENSAJE_2,
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 4: // bPiernaIzquierda
                        console.addMsgToConsole(MENSAJE_PRODUCE_IMPACTO_1 + victimName + MENSAJE_RECIVE_IMPACTO_PIERNA_IZQ + damage + MENSAJE_2,
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 5: // bPiernaDerecha
                        console.addMsgToConsole(MENSAJE_PRODUCE_IMPACTO_1 + victimName + MENSAJE_RECIVE_IMPACTO_PIERNA_DER + damage + MENSAJE_2,
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 6: // bTorso
                        console.addMsgToConsole(MENSAJE_PRODUCE_IMPACTO_1 + victimName + MENSAJE_RECIVE_IMPACTO_TORSO + damage + MENSAJE_2,
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;
                }
                charDialogHitSet(charIndexVictim, damage);

                break;

            case WorkRequestTarget:
                final int usingSkill = incomingData.readByte();
                User.get().setUsingSkill(usingSkill);

                Window.get().setCursorCrosshair(true);

                switch (E_Skills.values()[usingSkill - 1]) {
                    case Magia:
                        console.addMsgToConsole(MENSAJE_TRABAJO_MAGIA, false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;

                    case Pesca:
                        console.addMsgToConsole(MENSAJE_TRABAJO_PESCA, false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;

                    case Robar:
                        console.addMsgToConsole(MENSAJE_TRABAJO_ROBAR, false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;

                    case Talar:
                        console.addMsgToConsole(MENSAJE_TRABAJO_TALAR, false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;

                    case Mineria:
                        console.addMsgToConsole(MENSAJE_TRABAJO_MINERIA, false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;

                    case Proyectiles:
                        console.addMsgToConsole(MENSAJE_TRABAJO_PROYECTILES, false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;
                }

                if (usingSkill == FundirMetal) {
                    console.addMsgToConsole(MENSAJE_TRABAJO_FUNDIRMETAL, false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                }
                break;

            case HaveKilledUser:
                console.addMsgToConsole(MENSAJE_HAS_MATADO_A + charList[incomingData.readInteger()].getName() + MENSAJE_22,
                        false, false, new RGBColor(1f, 0f, 0f));

                final int level = incomingData.readLong();

                console.addMsgToConsole(MENSAJE_HAS_GANADO_EXPE_1 + level + MENSAJE_HAS_GANADO_EXPE_2,
                        false, false, new RGBColor(1f, 0f, 0f));

                // sistema de captura al matar.
                break;

            case UserKill:
                console.addMsgToConsole(charList[incomingData.readInteger()].getName() + MENSAJE_TE_HA_MATADO,
                        false, false, new RGBColor(1f, 0f, 0f));
                break;

            case EarnExp:
                console.addMsgToConsole(MENSAJE_HAS_GANADO_EXPE_1 + incomingData.readLong() + MENSAJE_HAS_GANADO_EXPE_2,
                        false, false, new RGBColor(1f, 0f, 0f));
                break;

            case GoHome:
                int distance = incomingData.readByte();
                short time = incomingData.readInteger();
                String hogar = incomingData.readASCIIString();

                console.addMsgToConsole("Estas a " + distance + " mapas de distancia de " + hogar + ", este viaje durara " + time + " segundos.",
                        false, false, new RGBColor(1f, 0f, 0f));
                break;

            case FinishHome:
                console.addMsgToConsole(MENSAJE_HOGAR, false, false, new RGBColor());
                break;

            case CancelGoHome:
                console.addMsgToConsole(MENSAJE_HOGAR_CANCEL, false, false, new RGBColor(1f, 0f, 0f));
                break;
        }

    }

    private static void handleAddSlots() {
        // Remove packet ID
        incomingData.readByte();
        int maxInventorySlots = incomingData.readByte();
    }

    private static void handleUpdateDexterity() {
        if (incomingData.checkPacketData(2)) return;

        // Remove packet ID
        incomingData.readByte();

        User.get().setUserDext(incomingData.readByte());
    }

    private static void handleUpdateStrenght() {
        if (incomingData.checkPacketData(2)) return;

        // Remove packet ID
        incomingData.readByte();

        User.get().setUserStrg(incomingData.readByte());
    }

    public static void writeDestroyItems() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.DestroyItems.ordinal());
    }

    public static void writeChaosLegionKick(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ChaosLegionKick.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeRoyalArmyKick(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.RoyalArmyKick.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeForceMIDIAll(int midiID) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ForceMIDIAll.ordinal());

        outgoingData.writeByte(midiID);
    }

    public static void writeForceWAVEAll(int waveID) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ForceWAVEAll.ordinal());

        outgoingData.writeByte(waveID);
    }

    public static void writeRemovePunishment(String userName, int punishment, String newText) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.RemovePunishment.ordinal());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeByte(punishment);
        outgoingData.writeASCIIString(newText);
    }

    public static void writeTileBlockedToggle() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.TileBlockedToggle.ordinal());
    }

    public static void writeKillNPCNoRespawn() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.KillNPCNoRespawn.ordinal());
    }

    public static void writeKillAllNearbyNPCs() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.KillAllNearbyNPCs.ordinal());
    }

    public static void writeLastIP(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.LastIP.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeChangeMOTD() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ChangeMOTD.ordinal());
    }

    public static void writeSystemMessage(String message) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.SystemMessage.ordinal());

        outgoingData.writeASCIIString(message);
    }

    public static void writeCreateNPC(Short NPCIndex) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.CreateNPC.ordinal());

        outgoingData.writeInteger(NPCIndex);
    }

    public static void writeCreateNPCWithRespawn(Short NPCIndex) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.CreateNPCWithRespawn.ordinal());

        outgoingData.writeInteger(NPCIndex);
    }

    public static void writeImperialArmour(int armourIndex, short objectIndex) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ImperialArmour.ordinal());

        outgoingData.writeByte(armourIndex);
        outgoingData.writeInteger(objectIndex);
    }

    public static void writeChaosArmour(int armourIndex, short objectIndex) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ChaosArmour.ordinal());

        outgoingData.writeByte(armourIndex);
        outgoingData.writeInteger(objectIndex);
    }

    public static void writeNavigateToggle() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.NavigateToggle.ordinal());
    }

    public static void writeServerOpenToUsersToggle() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ServerOpenToUsersToggle.ordinal());
    }

    public static void writeTurnOffServer() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.TurnOffServer.ordinal());
    }

    public static void writeTurnCriminal(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.TurnCriminal.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeResetFactions(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ResetFactions.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeRemoveCharFromGuild(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.RemoveCharFromGuild.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeRequestCharMail(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.RequestCharMail.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeAlterPassword(String userName, String copyFrom) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.AlterPassword.ordinal());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeASCIIString(copyFrom);
    }

    public static void writeAlterMail(String userName, String newMail) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.AlterMail.ordinal());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeASCIIString(newMail);
    }

    public static void writeAlterName(String userName, String newName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.AlterName.ordinal());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeASCIIString(newName);
    }

    public static void writeCheckSlot(String userName, int slot) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.AlterName.ordinal());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeByte(slot);
    }

    public static void writeToggleCentinelActivated() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ToggleCentinelActivated.ordinal());
    }

    public static void writeDoBackup() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.DoBackUp.ordinal());
    }

    public static void writeShowGuildMessages(String guild) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ShowGuildMessages.ordinal());

        outgoingData.writeASCIIString(guild);
    }

    public static void writeSaveMap() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.SaveMap.ordinal());
    }

    public static void writeChangeMapInfoPK(boolean isPK) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ChangeMapInfoPK.ordinal());

        outgoingData.writeBoolean(isPK);
    }

    public static void writeChangeMapInfoBackup(boolean backup) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ChangeMapInfoBackup.ordinal());

        outgoingData.writeBoolean(backup);
    }

    public static void writeChangeMapInfoRestricted(String restrict) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ChangeMapInfoBackup.ordinal());

        outgoingData.writeASCIIString(restrict);
    }

    public static void writeChangeMapInfoNoMagic(boolean noMagic) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ChangeMapInfoNoMagic.ordinal());

        outgoingData.writeBoolean(noMagic);
    }

    public static void writeChangeMapInfoNoInvi(boolean noInvi) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ChangeMapInfoNoInvi.ordinal());

        outgoingData.writeBoolean(noInvi);
    }

    public static void writeChangeMapInfoNoResu(boolean noResu) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ChangeMapInfoNoResu.ordinal());

        outgoingData.writeBoolean(noResu);
    }

    public static void writeChangeMapInfoLand(String land) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ChangeMapInfoLand.ordinal());

        outgoingData.writeASCIIString(land);
    }

    public static void writeChangeMapInfoZone(String zone) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ChangeMapInfoZone.ordinal());

        outgoingData.writeASCIIString(zone);
    }

    public static void writeSaveChars() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.SaveChars.ordinal());
    }

    public static void writeCleanSOS() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.CleanSOS.ordinal());
    }

    public static void writeNight() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.night.ordinal());
    }

    public static void writeKickAllChars() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.KickAllChars.ordinal());
    }

    public static void writeReloadNPCs() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ReloadNPCs.ordinal());
    }

    public static void writeReloadServerIni() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ReloadServerIni.ordinal());
    }

    public static void writeReloadSpells() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ReloadSpells.ordinal());
    }

    public static void writeReloadObjects() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ReloadObjects.ordinal());
    }

    public static void writeRestart() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.Restart.ordinal());
    }

    public static void writeResetAutoUpdate() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ResetAutoUpdate.ordinal());
    }

    public static void writeChatColor(int r, int g, int b) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ChatColor.ordinal());

        outgoingData.writeByte(r);
        outgoingData.writeByte(g);
        outgoingData.writeByte(b);
    }

    public static void writeIgnored() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.Ignored.ordinal());
    }

    public static void writePing() {
        if (pingTime != 0) return;
        outgoingData.writeByte(ClientPacketID.Ping.ordinal());
        pingTime = (int) glfwGetTime();
    }

    public static void writeSetIniVar(String sLlave, String sClave, String sValor) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.SetIniVar.ordinal());

        outgoingData.writeASCIIString(sLlave);
        outgoingData.writeASCIIString(sClave);
        outgoingData.writeASCIIString(sValor);
    }

    public static void writeHome() {
        outgoingData.writeByte(ClientPacketID.Home.ordinal());
    }

}
