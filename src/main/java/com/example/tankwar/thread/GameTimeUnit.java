package com.example.tankwar.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Class Description...
 */
public class GameTimeUnit {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameTimeUnit.class);

    public static void sleepMillis(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            LOGGER.error(e.toString(), e);
        }
    }
}
