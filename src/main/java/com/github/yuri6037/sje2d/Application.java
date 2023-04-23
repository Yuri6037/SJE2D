// Copyright (c) 2023, SJE2D
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification,
// are permitted provided that the following conditions are met:
//
//     * Redistributions of source code must retain the above copyright notice,
//       this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above copyright notice,
//       this list of conditions and the following disclaimer in the documentation
//       and/or other materials provided with the distribution.
//     * Neither the name of BlockProject 3D nor the names of its contributors
//       may be used to endorse or promote products derived from this software
//       without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
// A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
// CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
// EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
// PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
// PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
// LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
// NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package com.github.yuri6037.sje2d;

import com.github.yuri6037.sje2d.asset.config.IAssetConfig;
import com.github.yuri6037.sje2d.asset.config.TypeRegistry;
import com.github.yuri6037.sje2d.asset.engine.AssetURL;
import com.github.yuri6037.sje2d.asset.engine.manager.AssetManager;
import com.github.yuri6037.sje2d.asset.engine.manager.AssetManagerProxy;
import com.github.yuri6037.sje2d.config.AppType;
import com.github.yuri6037.sje2d.input.IInputConfig;
import com.github.yuri6037.sje2d.screen.IScreen;
import com.github.yuri6037.sje2d.screen.InitScreen;
import com.github.yuri6037.sje2d.util.Bootstrap;
import com.github.yuri6037.sje2d.util.Timer;
import com.github.yuri6037.sje2d.window.IWindowConfig;
import com.github.yuri6037.sje2d.window.Window;
import com.github.yuri6037.sje2d.window.WindowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

public class Application {
    private static final Logger LOGGER
            = LoggerFactory.getLogger(Application.class);
    private final Timer timer = new Timer();
    private final Window window;
    private final AssetManagerProxy assets;
    private final AssetManager manager;
    private final XMLAppConfig config;
    private IScreen curScreen = null;

    private static Class<? extends Application> appClass;

    /**
     * Opens a resource contained in the application's jar file.
     * @param name the name of the resource to open.
     * @return an InputStream or null if the resource could not be found.
     */
    public static InputStream getResource(final String name) {
        return appClass.getClassLoader().getResourceAsStream(name);
    }

    private static AppType load(final InputStream stream) throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(AppType.class);
        return ctx.createUnmarshaller().unmarshal(new StreamSource(stream), AppType.class).getValue();
    }

    /**
     * Creates a new application.
     * This class is not intended for direct instantiation, it should instead be
     * instanced by "Application.main".
     * @param config the application configuration
     * @throws WindowException if the window could not be created or that OpenGL
     * failed to initialize.
     */
    public Application(final AppType config) throws WindowException {
        LOGGER.info("Loading Simple 2D Java Engine for {} - {}",
                config.getName(), config.getVersion());
        LOGGER.info("Engine version: {}", Constants.VERSION);
        this.config = new XMLAppConfig(config);
        LOGGER.debug("Creating window: title='{}' width={}, height={}, fullscreen={}, vsync={}...",
                getWindowConfig().getTitle(), getWindowConfig().getWidth(),
                getWindowConfig().getHeight(),
                getWindowConfig().isFullscreen(), getWindowConfig().isVsync());
        window = new Window(getWindowConfig());
        LOGGER.debug("Creating AssetManager...");
        TypeRegistry.Builder builder = new TypeRegistry.Builder();
        getAssetConfig().populateTypeRegistry(builder);
        manager = new AssetManager(builder.build());
        LOGGER.debug("Creating AssetManagerProxy...");
        assets = manager.newProxy();
    }

    /**
     * The front-end of the assets manager is what allows queueing new assets,
     * checking what assets are loaded, unloading assets, etc.
     * @return the command front-end of the assets manager.
     */
    public final AssetManagerProxy getAssets() {
        return assets;
    }

    /**
     * The timer is what computes the time each frame takes to render,
     * the delta time between frames used in animations, etc.
     * @return the timer of this application.
     */
    public final Timer getTimer() {
        return timer;
    }

    /**
     * @return the window of this application.
     */
    public final Window getWindow() {
        return window;
    }

    /**
     * Changes the current rendered screen.
     * @param screen the new screen to render.
     */
    public final void setScreen(final IScreen screen) {
        if (curScreen != null) {
            curScreen.close();
        }
        curScreen = screen;
        if (curScreen != null) {
            curScreen.open();
        }
    }

    /**
     * Called by onStart to register all application assets.
     * Override this function to queue custom assets.
     * @throws MalformedURLException if some asset had a bad url.
     */
    protected void registerAssets() throws MalformedURLException {
    }

    private void onStart() {
        try {
            assets.queue(new AssetURL("texture/jpg resource://init.jpg?scope=engine&namespace=Engine&vpath=Init"));
            assets.queue(new AssetURL("font/xml resource://font.xml?scope=engine&namespace=Engine"));
            syncAssetsManager();
            setScreen(new InitScreen(this));
            registerAssets();
        } catch (MalformedURLException e) {
            LOGGER.error("Failed to register assets", e);
        }
    }

    /**
     * Called once the game is ready to start executing.
     * Override this function to customize the startup behavior of the application.
     */
    public void init() {

    }

    /**
     * Called by run after the main loop.
     * Override this function to customize the shutdown behavior of the application.
     */
    public void shutdown() {

    }

    private void onTerminate() {
        if (curScreen != null) {
            curScreen.close();
        }
        shutdown();
    }

    /**
     * Immediately sync the AssetManager by waiting for all submitted operations
     * to finish.
     * WARNING: This method will block until all queued operations are finished.
     */
    public final void syncAssetsManager() {
        try {
            manager.waitAll();
        } catch (InterruptedException e) {
            LOGGER.error("Failed to wait for all assets to load", e);
        }
    }

    private void run() {
        LOGGER.info("Starting application");
        onStart();
        while (!window.shouldClose()) {
            timer.update();
            manager.update();
            if (curScreen != null) {
                curScreen.update(timer.getDeltaTime());
            }
            window.update();
        }
        LOGGER.info("Stopping application");
        onTerminate();
    }

    /**
     * @return the window configuration.
     */
    public final IWindowConfig getWindowConfig() {
        return config;
    }

    /**
     * @return the input configuration.
     */
    public final IInputConfig getInputConfig() {
        return config;
    }

    /**
     * @return the assets manager configuration.
     */
    public final IAssetConfig getAssetConfig() {
        return config;
    }

    //CHECKSTYLE OFF: HiddenField
    /**
     * Initializes a new Application. This is the main entry point for SJE2D, call this function from your main
     * function.
     * @param appClass your derived application class.
     * @param args the arguments passed to your main function.
     */
    public static void main(final Class<? extends Application> appClass,
                            final String[] args) {
        Application.appClass = appClass;
        if (!Bootstrap.isBootstrapped() && !Bootstrap.isOnMainThread()) {
            LOGGER.warn("Application is not on main thread, restarting on main thread...");
            Bootstrap.startOnMainThread(args);
        }
        InputStream stream = Application.getResource("app.xml");
        if (stream == null) {
            LOGGER.error("FATAL: could not find application configuration");
            return;
        }
        try {
            AppType config = Application.load(stream);
            stream.close();
            LOGGER.info("Creating application main class");
            Application app = appClass.getDeclaredConstructor(AppType.class).newInstance(config);
            app.run();
        } catch (IOException | JAXBException | InvocationTargetException | InstantiationException
                 | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    //CHECKSTYLE ON
}
