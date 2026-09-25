package com.fipp.animacao.util;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.layout.HBox;

public class Animacao {
    private static final double PASSO = 5.0;
    private static final long DELAY_MS = 15;

    private static final double BASE_Y = 100.0;
    private static final double ESPACO_Y = 60.0;
    private static final double DESLOC_X = 120.0;

    public static Task<Void> animar(HBox hbox, double targetX, double targetY) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                double curX = hbox.getLayoutX();
                double curY = hbox.getLayoutY();

                while (!isCancelled() && (Math.abs(curX - targetX) > 0.1 || Math.abs(curY - targetY) > 0.1)) {

                    double dx = targetX - curX;
                    double dy = targetY - curY;

                    double stepX = Math.signum(dx) * Math.min(PASSO, Math.abs(dx));
                    double stepY = Math.signum(dy) * Math.min(PASSO, Math.abs(dy));

                    curX += stepX;
                    curY += stepY;

                    double fx = curX;
                    double fy = curY;
                    Platform.runLater(() -> {
                        hbox.setLayoutX(fx);
                        hbox.setLayoutY(fy);
                    });

                    Thread.sleep(DELAY_MS);
                }

                Platform.runLater(() -> {
                    hbox.setLayoutX(targetX);
                    hbox.setLayoutY(targetY);
                });

                return null;
            }
        };

        Thread th = new Thread(task, "animacao-hbox-" + hbox.hashCode());
        th.setDaemon(true);
        th.start();
        return task;
    }

    public static Task<Void> animarParaTemporario(HBox hbox, int novoIndice) {
        double targetX = hbox.getLayoutX() + DESLOC_X;
        double targetY = BASE_Y + novoIndice * ESPACO_Y;
        return animar(hbox, targetX, targetY);
    }

    private static final double AUX_X = 170.0;
    private static final double BASE_X_VET = 60.0;

    public static Task<Void> animarParaAux(HBox hbox, int posAux) {
        double targetX = AUX_X;
        double targetY = BASE_Y + posAux * ESPACO_Y;
        return animar(hbox, targetX, targetY);
    }

    public static Task<Void> animarDeAuxParaVet(HBox hbox, int posVet) {
        double targetX = BASE_X_VET;
        double targetY = BASE_Y + posVet * ESPACO_Y;
        return animar(hbox, targetX, targetY);
    }

    public static void animarParaAuxEBloquear(HBox hbox, int posAux) throws Exception {
        Task<Void> t = animarParaAux(hbox, posAux);
        t.get();
    }

    public static void animarDeAuxParaVetEBloquear(HBox hbox, int posVet) throws Exception {
        Task<Void> t = animarDeAuxParaVet(hbox, posVet);
        t.get();
    }
}
