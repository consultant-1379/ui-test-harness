package com.ericsson.cifwk.taf.ui.core;

public class SimpleDelayStrategy implements DelayStrategy {
    @Override
    public void delay() {
        try {
            Thread.sleep(WaitConstants.SLEEP_STEP);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
