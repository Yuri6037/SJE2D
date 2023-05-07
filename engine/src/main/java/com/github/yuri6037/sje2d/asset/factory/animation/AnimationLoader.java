package com.github.yuri6037.sje2d.asset.factory.animation;

import com.github.yuri6037.sje2d.asset.Animation;
import com.github.yuri6037.sje2d.asset.engine.AssetURL;
import com.github.yuri6037.sje2d.asset.engine.map.AssetDepMap;
import com.github.yuri6037.sje2d.asset.factory.BaseLoader;
import com.github.yuri6037.sje2d.util.ImageUtils;
import com.github.yuri6037.sje2d.util.MathUtils;

//CHECKSTYLE OFF: AvoidStarImport
import static org.lwjgl.opengl.GL11.*;
//CHECKSTYLE ON

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class AnimationLoader extends BaseLoader<Animation> {
    private static final int MAX_TEXTURE_SIZE = 8192;

    private ByteBuffer buffer;
    private int numColumns;
    private int numRows;

    //CHECKSTYLE OFF: VisibilityModifier

    /**
     * The list of frames.
     */
    protected final List<BufferedImage> frames = new ArrayList<>();

    /**
     * The target animation FPS.
     */
    protected int fps = 30;

    /**
     * The width of a single frame of animation.
     */
    protected int frameWidth = 0;

    /**
     * The height of a single frame of animation.
     */
    protected int frameHeight = 0;

    //CHECKSTYLE ON

    /**
     * A base animation loader.
     * @param url the target URL to load.
     */
    public AnimationLoader(final AssetURL url) {
        super(url);
    }

    /**
     * Called to build the list of frames and the properties of this animation.
     * @throws Exception if the animation could not be built.
     */
    protected abstract void build() throws Exception;

    private BufferedImage genBitmap() {
        if (frames.isEmpty() || frameWidth == 0 || frameHeight == 0) {
            return null;
        }
        if (!MathUtils.isPowerOfTwo(frameWidth) || !MathUtils.isPowerOfTwo(frameHeight)) {
            throw new IllegalArgumentException("Animation frame size is not a power of 2");
        }
        numColumns = (int) Math.ceil((double) (frameHeight * frames.size()) / (double) MAX_TEXTURE_SIZE);
        numRows = Math.min(frames.size(), MAX_TEXTURE_SIZE / frameHeight);
        BufferedImage output = new BufferedImage(numColumns * frameWidth, numRows * frameHeight,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = output.createGraphics();
        int x = 0;
        int y = 0;
        for (BufferedImage frame: frames) {
            graphics.drawImage(frame, x, y, null);
            y += frameHeight;
            if (y >= MAX_TEXTURE_SIZE) {
                y = 0;
                x += frameWidth;
            }
        }
        return output;
    }

    @Override
    public final Result load(final AssetDepMap dependencies) throws Exception {
        build();
        BufferedImage bitmap = genBitmap();
        if (bitmap == null) {
            return Result.none();
        }
        buffer = ImageUtils.imageToBuffer(bitmap);
        return Result.ready();
    }

    @Override
    protected final Animation createAsset() throws Exception {
        Animation animation = new Animation(buffer, frameWidth, frameHeight, fps, numRows, numColumns, frames.size());
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        return animation;
    }
}
