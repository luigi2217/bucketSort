package com.fipp.animacao.app;

import com.fipp.animacao.view.RadixView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Random;

public class Main extends Application {
    public static int[] vetor (int tamanho) {
        int[] valores = new int[tamanho];
        Random r = new Random();
        for (int i = 0; i < tamanho; i++) {
            valores[i] = r.nextInt(1000);
        }

        return valores;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        new RadixView(stage);
    }
}
