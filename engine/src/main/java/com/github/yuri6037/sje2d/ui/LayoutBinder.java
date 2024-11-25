package com.github.yuri6037.sje2d.ui;

import com.github.yuri6037.sje2d.Application;
import com.github.yuri6037.sje2d.ui.asset.Layout;
import com.github.yuri6037.sje2d.ui.component.Component;
import com.github.yuri6037.sje2d.ui.core.SimpleLayoutManager;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * This class is a helper to auto-bind a Layout XML file to a Java class at runtime using reflection.
 */
public abstract class LayoutBinder {
    private final Layout layout;

    /**
     * Create a new instance of a base LayoutBinder.
     * @param app the app to load assets from.
     * @param layoutVpath the asset virtual path to the XML layout.
     */
    public LayoutBinder(final Application app, final String layoutVpath) {
        layout = app.getAssets().get(Layout.class, layoutVpath).get().instantiate();
        for (Field field : getClass().getDeclaredFields()) {
            BindLayout binder = field.getAnnotation(BindLayout.class);
            if (binder != null && Modifier.isPrivate(field.getModifiers())
                    && Component.class.isAssignableFrom(field.getType())) {
                Object component = Objects.requireNonNull(
                        layout.getComponent((Class<? extends Component>) field.getType(), field.getName()));
                field.setAccessible(true);
                try {
                    field.set(this, component);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                field.setAccessible(false);
            }
        }
    }

    /**
     * @return an instance of the bound layout to be rendered.
     */
    public final Layout getLayout() {
        return layout;
    }

    /**
     * Shows the layout attached to this binder in the given view.
     * @param manager the manager to present to.
     */
    public final void show(final SimpleLayoutManager manager) {
        manager.addLayout(layout);
    }

    /**
     * Removes the layout attached to this binder from the given view.
     * @param manager the manager to remove from.
     */
    public final void hide(final SimpleLayoutManager manager) {
        manager.removeLayout(layout);
    }
}
