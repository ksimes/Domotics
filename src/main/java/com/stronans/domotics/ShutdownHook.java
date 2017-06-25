package com.stronans.domotics;

import org.apache.log4j.Logger;
import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * Created by S.King on 21/05/2017.
 */
public class ShutdownHook {
    private static final Logger logger = Logger.getLogger(Application.class);

    public static void init() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                logger.info("Application shutting down ...");
            }
        });

        SignalHandler handler = new SignalHandler() {
            public void handle(Signal sig) {
                logger.info("Signal " + sig);
                System.out.println("Shutting down database...");
            }
        };

        Signal.handle(new Signal("INT"), handler);
        Signal.handle(new Signal("TERM"), handler);
    }
}
