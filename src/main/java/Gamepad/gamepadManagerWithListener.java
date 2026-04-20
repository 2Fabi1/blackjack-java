package Gamepad;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

public class gamepadManagerWithListener {

    private ControllerManager controllers;
    private gamepadListener listener;
    private boolean running = false;

    private boolean prevA = false;
    private boolean prevB = false;
    private boolean prevX = false;

    public gamepadManagerWithListener(gamepadListener listener) {
        this.listener = listener;
        controllers = new ControllerManager();
        controllers.initSDLGamepad();
    }

    public void startPolling() {
        running = true;
        new Thread(() -> {
            while (running) {
                ControllerState state = controllers.getState(0);

                if(state.isConnected)
                {
                    if (state.a && !prevA) listener.onAPressed();
                    prevA = state.a;

                    if (state.b && !prevB) listener.onBPressed();
                    prevB = state.b;

                    if (state.x && !prevX) listener.onXPressed();
                    prevX = state.x;
                }

                try {
                    Thread.sleep(20); // 50Hz polling
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    public void stop() {
        running = false;
        controllers.quitSDLGamepad();
    }
}