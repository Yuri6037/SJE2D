package com.github.yuri6037.sje2d.asset.factory.animation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.yuri6037.sje2d.asset.engine.AssetURL;
import com.github.yuri6037.sje2d.asset.engine.system.stream.IAssetStream;
import com.github.yuri6037.sje2d.asset.engine.system.stream.StreamUtils;
import com.github.yuri6037.sje2d.config.AnimationType;

public final class AnimationZIPLoader extends AnimationLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnimationZIPLoader.class);
    private final InputStream stream;

    /**
     * Creates a new animation ZIP loader.
     * @param stream the asset stream.
     * @param url the asset url.
     */
    public AnimationZIPLoader(final IAssetStream stream, final AssetURL url) {
        super(url);
        this.stream = StreamUtils.makeInputStream(stream);
    }

    private AnimationType loadDescriptor(final byte[] buffer) throws Exception {
        JAXBContext ctx = JAXBContext.newInstance(AnimationType.class);
        AnimationType animation = ctx.createUnmarshaller().unmarshal(new StreamSource(new ByteArrayInputStream(buffer)),
            AnimationType.class).getValue();
        return animation;
    }

    private void loadAnimationFrames(final AnimationType animation,
        final HashMap<String, byte[]> items) throws IOException {
        for (String frame : animation.getFrames().getFrame()) {
            ByteArrayInputStream s = new ByteArrayInputStream(items.get(frame));
            frames.add(ImageIO.read(s));
        }
    }

    @Override
    protected void build() throws Exception {
        ZipInputStream zip =  new ZipInputStream(stream);
        HashMap<String, byte[]> items = new HashMap<>();
        AnimationType animation = null;
        ZipEntry entry = null;
        do {
            entry = zip.getNextEntry();
            if (entry == null || entry.getSize() <= 0) {
                continue;
            }
            LOGGER.debug("New ZIP entry: {}", entry.getName());
            byte[] buffer = new byte[(int) entry.getSize()];
            zip.read(buffer);
            if ("animation.xml".equals(entry.getName())) {
                animation = loadDescriptor(buffer);
            } else {
                items.put(entry.getName(), buffer);
            }
        } while (entry != null);
        if (animation != null) {
            loadAnimationFrames(animation, items);
            frameWidth = animation.getWidth().intValue();
            frameHeight = animation.getHeight().intValue();
            fps = animation.getFps().intValue();
        }
    }
}
