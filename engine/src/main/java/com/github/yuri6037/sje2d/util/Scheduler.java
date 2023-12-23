package com.github.yuri6037.sje2d.util;

import java.util.ArrayList;

public final class Scheduler {
    static final class Function {
        private final double time;
        private final Runnable runnable;

        Function(final double time, final Runnable runnable) {
            this.time = time;
            this.runnable = runnable;
        }
    }

    private final ArrayList<Function> functions = new ArrayList<>();
    private final Timer timer;

    /**
     * Creates a new Scheduler.
     * @param timer the timer this scheduler should be linked to.
     */
    public Scheduler(final Timer timer) {
        this.timer = timer;
    }

    /**
     * Schedules a new Runnable to run after a certain number of seconds.
     * @param delta the number of seconds from now at which to run the Runnable.
     * @param runnable the Runnable to run after delta seconds.
     */
    public void schedule(final float delta, final Runnable runnable) {
        functions.add(new Function(timer.getTime() + delta, runnable));
    }

    /**
     * Updates the Scheduler and automatically run any scheduled function if needed.
     * Call this function each cycle in the main loop.
     */
    public void update() {
        for (int i = functions.size() - 1; i != -1; --i) {
            Function f = functions.get(i);
            if (timer.getTime() >= f.time) {
                f.runnable.run();
                functions.remove(i);
            }
        }
    }
}
