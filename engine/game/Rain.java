package org.aoclient.engine.game;

import org.aoclient.engine.Sound;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.engine.renderer.Surface;
import org.aoclient.engine.renderer.Texture;
import org.lwjgl.system.windows.RECT;

import static org.aoclient.engine.renderer.Drawn.geometryBoxRender;
import static org.aoclient.engine.utils.GameData.bLluvia;
import static org.aoclient.engine.utils.GameData.options;
import static org.aoclient.engine.utils.Time.deltaTime;

public class Rain {

    // sonidos de la lluvia
    private static final String SND_LLUVIAIN = "resources/sounds/lluviain.ogg";
    private static final String SND_LLUVIAOUT = "resources/sounds/lluviaout.ogg";
    private static final String SND_LLUVIAINEND = "resources/sounds/lluviainend.ogg";
    private static final String SND_LLUVIAOUTEND = "resources/sounds/lluviaoutend.ogg";
    private static Rain instance;
    private final Texture rainTexture;
    private final RECT[] RLluvia;
    private final int[] LTLluvia;
    private final Sound[] rainSounds;
    private boolean bRain;
    private float timeToChangeFrame;
    private int iFrameIndex;

    private Rain() {
        this.bRain = false;
        this.RLluvia = new RECT[8];
        this.LTLluvia = new int[5];
        this.rainSounds = new Sound[4];

        this.rainSounds[0] = new Sound(SND_LLUVIAIN, true);
        this.rainSounds[1] = new Sound(SND_LLUVIAOUT, true);
        this.rainSounds[2] = new Sound(SND_LLUVIAINEND, false);
        this.rainSounds[3] = new Sound(SND_LLUVIAOUTEND, false);

        // tiempo para cambiar el frame
        this.timeToChangeFrame = 0.1f;
        this.iFrameIndex = 0;

        this.loadData();

        // dios mio.
        this.rainTexture = Surface.get().createTexture("graphics.ao", "15168", false);
    }

    public static Rain get() {
        if (instance == null) instance = new Rain();
        return instance;
    }

    private void loadData() {
        // Set up te rain rects
        for (int i = 0; i < 8; i++)
            RLluvia[i] = new RECT();

        RLluvia[0].top = 0;
        RLluvia[1].top = 0;
        RLluvia[2].top = 0;
        RLluvia[3].top = 0;
        RLluvia[0].left = 0;
        RLluvia[1].left = 128;
        RLluvia[2].left = 256;
        RLluvia[3].left = 384;
        RLluvia[0].right = 128;
        RLluvia[1].right = 256;
        RLluvia[2].right = 384;
        RLluvia[3].right = 512;
        RLluvia[0].bottom = 128;
        RLluvia[1].bottom = 128;
        RLluvia[2].bottom = 128;
        RLluvia[3].bottom = 128;

        RLluvia[4].top = 128;
        RLluvia[5].top = 128;
        RLluvia[6].top = 128;
        RLluvia[7].top = 128;
        RLluvia[4].left = 0;
        RLluvia[5].left = 128;
        RLluvia[6].left = 256;
        RLluvia[7].left = 384;
        RLluvia[4].right = 128;
        RLluvia[5].right = 256;
        RLluvia[6].right = 384;
        RLluvia[7].right = 512;
        RLluvia[4].bottom = 256;
        RLluvia[5].bottom = 256;
        RLluvia[6].bottom = 256;
        RLluvia[7].bottom = 256;

        LTLluvia[0] = 224;
        LTLluvia[1] = 352;
        LTLluvia[2] = 480;
        LTLluvia[3] = 608;
        LTLluvia[4] = 736;
    }

    public void render(RGBColor color) {
        if (!bLluvia[User.get().getUserMap()] || !bRain) return;

        this.renderSound();

        // actualizacion de index para RLluvia
        if (timeToChangeFrame <= 0) {
            timeToChangeFrame = 0.1f;
            iFrameIndex++;
            if (iFrameIndex > 7) iFrameIndex = 0;
        }

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                geometryBoxRender(rainTexture, LTLluvia[y] - 213, LTLluvia[x] - 77,
                        128,
                        128,
                        RLluvia[iFrameIndex].left,
                        RLluvia[iFrameIndex].top, false, 1.0f, color);
            }
        }

        timeToChangeFrame -= deltaTime;
    }

    private void renderSound() {
        if (!options.isSound()) return;
        if (bLluvia[User.get().getUserMap()] && bRain) {
            if (User.get().isUnderCeiling()) {
                if (rainSounds[1].isPlaying()) rainSounds[1].stop();
                if (!rainSounds[0].isPlaying()) rainSounds[0].play();
            } else {
                if (rainSounds[0].isPlaying()) rainSounds[0].stop();
                if (!rainSounds[1].isPlaying()) rainSounds[1].play();
            }
        }
    }

    public void stopRainingSoundLoop() {
        if (!options.isSound()) return;
        if (User.get().isUnderCeiling()) rainSounds[0].stop();
        else rainSounds[1].stop();
    }

    public void playEndRainSound() {
        if (!options.isSound()) return;
        if (bLluvia[User.get().getUserMap()]) {
            if (User.get().isUnderCeiling()) rainSounds[2].play();
            else rainSounds[3].play();
        }
    }

    public boolean isRaining() {
        return bRain;
    }

    public void setRainValue(boolean bRain) {
        this.bRain = bRain;
    }

    static class RECT {
        private int top, left, right, bottom;
    }

}
