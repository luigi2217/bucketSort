package com.bucket.animacaobucket;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class BucketSortAnimation extends Application {

    private static final int TAMANHO = 9, N_BALDES = 3, DELAY = 400, ANIM_FRAMES = 30, ANIM_SLEEP = 12;
    private static final int VET_X = 40, VET_Y = 90, BOX_W = 60, BOX_H = 45, GAP = 6;
    private static final int BALDE_Y = 280, BALDE_GAP = 70, CODE_W = 420, CODE_MARGIN = 30;

    private int[] vet = new int[TAMANHO];
    private int[][] baldes = new int[N_BALDES][TAMANHO];
    private int[] tamBalde = new int[N_BALDES];
    private int TL = 0;

    private AnchorPane pane;
    private Button[] vetBotoes = new Button[TAMANHO];
    private Button[][] baldeBotoes = new Button[N_BALDES][TAMANHO];
    private Label[] linhasCodigo = new Label[45];
    private int totalLinhas = 0;
    private Label lblVariaveis, lblCodigo;
    private Label lblIndiceI;
    private Button btnIniciar, btnGerar, btnMovel;
    private Rectangle fundoCodigo;
    private double codeX = 680;
    private String[] coresBalde = {"#FF6B6B", "#4ECDC4", "#45B7D1"};

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Animacao Bucket Sort - Luigi Sardelari");
        stage.setMaximized(true);

        pane = new AnchorPane();
        pane.setStyle("-fx-background-color: #1E1E2E;");

        addLabel("Bucket Sort", VET_X, 15, "Consolas", 28, FontWeight.BOLD, "WHITE");
        addLabel("Vetor:", VET_X, VET_Y - 28, "Consolas", 15, FontWeight.BOLD, "#CDD6F4");

        for (int i = 0; i < TAMANHO; i++) {
            vetBotoes[i] = criarBotao(VET_X + i * (BOX_W + GAP), VET_Y, estiloVetorPadrao());
            pane.getChildren().add(vetBotoes[i]);
            addLabel("[" + i + "]", VET_X + i * (BOX_W + GAP) + 18, VET_Y + BOX_H + 3, "Consolas", 12, null, "#6C7086");
        }

        lblIndiceI = new Label("");
        lblIndiceI.setFont(Font.font("Consolas", FontWeight.BOLD, 13));
        lblIndiceI.setTextFill(Color.web("#F38BA8"));
        lblIndiceI.setLayoutY(VET_Y + BOX_H + 22);
        pane.getChildren().add(lblIndiceI);

        for (int b = 0; b < N_BALDES; b++) {
            Label lbl = addLabel("Balde " + b + ":", VET_X - 5, BALDE_Y + b * BALDE_GAP - 20, "Consolas", 13, FontWeight.BOLD, coresBalde[b]);

            Rectangle r = new Rectangle(VET_X - 5, BALDE_Y + b * BALDE_GAP, TAMANHO * (BOX_W + GAP) + 5, BOX_H + 8);
            r.setFill(Color.TRANSPARENT);
            r.setStroke(Color.web(coresBalde[b]));
            r.setStrokeWidth(1.5);
            r.setArcWidth(8);
            r.setArcHeight(8);
            pane.getChildren().add(r);

            for (int j = 0; j < TAMANHO; j++) {
                baldeBotoes[b][j] = criarBotao(VET_X + j * (BOX_W + GAP), BALDE_Y + b * BALDE_GAP + 4,
                        "-fx-background-color: transparent; -fx-text-fill: transparent;");
                baldeBotoes[b][j].setVisible(false);
                pane.getChildren().add(baldeBotoes[b][j]);
            }
        }

        lblCodigo = new Label("Codigo:");
        lblCodigo.setLayoutY(15);
        lblCodigo.setFont(Font.font("Consolas", FontWeight.BOLD, 15));
        lblCodigo.setTextFill(Color.web("#CDD6F4"));
        pane.getChildren().add(lblCodigo);

        fundoCodigo = new Rectangle(0, 40, CODE_W, 640);
        fundoCodigo.setFill(Color.web("#11111B"));
        fundoCodigo.setArcWidth(8);
        fundoCodigo.setArcHeight(8);
        pane.getChildren().add(fundoCodigo);

        String[] codigo = {
            "void bucketSort(int nBaldes) {", "  int maior = acharMaior();",
            "  int[][] baldes = new int[nBaldes][TL];", "  int[] tamBalde = new int[nBaldes];",
            "  distribuir(baldes, tamBalde, maior);", "  for (int i = 0; i < nBaldes; i++)",
            "    insertionSort(baldes[i], tamBalde[i]);", "  juntar(baldes, tamBalde, nBaldes);",
            "}", "", "int acharMaior() {", "  int maior = vet[0];",
            "  for (int i = 1; i < TL; i++)", "    if (maior < vet[i]) maior = vet[i];",
            "  return maior;", "}", "", "void distribuir(...) {",
            "  for (int i = 0; i < TL; i++) {", "    indice = vet[i]*nBaldes/(maior+1);",
            "    baldes[indice][tam[indice]] = vet[i];", "    tamBalde[indice]++;",
            "  }", "}", "", "void insertionSort(int[] b, int tam) {",
            "  for (int i = 1; i < tam; i++) {", "    key = b[i];  j = i - 1;",
            "    while (j >= 0 && b[j] > key)", "      b[j+1] = b[j];  j--;",
            "    b[j+1] = key;", "  }", "}", "", "void juntar(...) {",
            "  int k = 0;", "  for (int b = 0; b < nBaldes; b++)",
            "    for (int i = 0; i < tamBalde[b]; i++)", "      vet[k++] = baldes[b][i];", "}",
        };

        for (int i = 0; i < codigo.length; i++) {
            linhasCodigo[i] = new Label((i + 1) + "  " + codigo[i]);
            linhasCodigo[i].setLayoutY(50 + i * 15);
            linhasCodigo[i].setFont(Font.font("Consolas", 12));
            linhasCodigo[i].setTextFill(Color.web("#CDD6F4"));
            linhasCodigo[i].setStyle("-fx-padding: 0 5 0 5;");
            pane.getChildren().add(linhasCodigo[i]);
            totalLinhas++;
        }

        lblVariaveis = new Label("Variaveis: ");
        lblVariaveis.setLayoutX(VET_X);
        lblVariaveis.setLayoutY(BALDE_Y + N_BALDES * BALDE_GAP + 25);
        lblVariaveis.setFont(Font.font("Consolas", FontWeight.BOLD, 14));
        lblVariaveis.setTextFill(Color.web("#F5C2E7"));
        pane.getChildren().add(lblVariaveis);

        btnMovel = criarBotao(0, 0, "");
        btnMovel.setVisible(false);
        pane.getChildren().add(btnMovel);

        btnGerar = new Button("Gerar Aleatorio");
        btnGerar.setLayoutX(VET_X + 240);
        btnGerar.setLayoutY(15);
        btnGerar.setFont(Font.font("Consolas", FontWeight.BOLD, 13));
        btnGerar.setStyle("-fx-background-color: #89B4FA; -fx-text-fill: #1E1E2E; -fx-font-weight: bold;");
        btnGerar.setOnAction(e -> gerarAleatorio());
        pane.getChildren().add(btnGerar);

        btnIniciar = new Button("Ordenar");
        btnIniciar.setLayoutX(VET_X + 420);
        btnIniciar.setLayoutY(15);
        btnIniciar.setFont(Font.font("Consolas", FontWeight.BOLD, 13));
        btnIniciar.setStyle("-fx-background-color: #A6E3A1; -fx-text-fill: #1E1E2E; -fx-font-weight: bold;");
        btnIniciar.setOnAction(e -> iniciarAnimacao());
        btnIniciar.setDisable(true);
        pane.getChildren().add(btnIniciar);

        Scene scene = new Scene(pane, 1200, 700);
        scene.widthProperty().addListener((obs, oldW, newW) -> reposicionarCodigo(newW.doubleValue()));
        stage.setScene(scene);
        stage.show();
        reposicionarCodigo(scene.getWidth());
    }

    private Label addLabel(String text, double x, double y, String font, int size, FontWeight weight, String color) {
        Label l = new Label(text);
        l.setLayoutX(x);
        l.setLayoutY(y);
        l.setFont(weight != null ? Font.font(font, weight, size) : Font.font(font, size));
        l.setTextFill(color.equals("WHITE") ? Color.WHITE : Color.web(color));
        pane.getChildren().add(l);
        return l;
    }

    private Button criarBotao(double x, double y, String estilo) {
        Button b = new Button("");
        b.setLayoutX(x); b.setLayoutY(y);
        b.setMinWidth(BOX_W); b.setMaxWidth(BOX_W);
        b.setMinHeight(BOX_H); b.setMaxHeight(BOX_H);
        b.setFont(Font.font("Consolas", FontWeight.BOLD, 16));
        b.setStyle(estilo);
        return b;
    }

    private void reposicionarCodigo(double largura) {
        codeX = largura - CODE_W - CODE_MARGIN;
        lblCodigo.setLayoutX(codeX);
        fundoCodigo.setX(codeX - 5);
        for (int i = 0; i < totalLinhas; i++) linhasCodigo[i].setLayoutX(codeX);
    }

    private String estiloVetorPadrao() {
        return "-fx-background-color: #585B70; -fx-text-fill: white; -fx-border-color: #7F849C; -fx-border-width: 1;";
    }
    private String estiloVetorVazio() {
        return "-fx-background-color: #313244; -fx-text-fill: transparent; -fx-border-color: #45475A; -fx-border-width: 1;";
    }
    private String estiloBalde(int b) {
        return "-fx-background-color: " + coresBalde[b] + "; -fx-text-fill: #1E1E2E; -fx-font-weight: bold; -fx-border-color: white; -fx-border-width: 1;";
    }
    private String estiloDestaque(String cor) {
        return "-fx-background-color: " + cor + "; -fx-text-fill: #1E1E2E; -fx-font-weight: bold; -fx-border-color: white; -fx-border-width: 2;";
    }

    private void gerarAleatorio() {
        Random rand = new Random();
        TL = 0;
        for (int b = 0; b < N_BALDES; b++) {
            tamBalde[b] = 0;
            for (int j = 0; j < TAMANHO; j++) { baldeBotoes[b][j].setText(""); baldeBotoes[b][j].setVisible(false); }
        }
        for (int i = 0; i < TAMANHO; i++) {
            vet[i] = rand.nextInt(100); TL++;
            vetBotoes[i].setText(String.valueOf(vet[i]));
            vetBotoes[i].setStyle(estiloVetorPadrao());
        }
        lblVariaveis.setText("Variaveis: (clique em Ordenar)");
        limparDestaques();
        btnIniciar.setDisable(false);
    }

    private void animarMovimento(double x1, double y1, double x2, double y2, String valor, String estilo) {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> { btnMovel.setText(valor); btnMovel.setStyle(estilo); btnMovel.setLayoutX(x1); btnMovel.setLayoutY(y1); btnMovel.setVisible(true); btnMovel.toFront(); });
        for (int f = 1; f <= ANIM_FRAMES; f++) {
            double t = (double) f / ANIM_FRAMES, ease = t * t * (3 - 2 * t);
            double cx = x1 + (x2 - x1) * ease, cy = y1 + (y2 - y1) * ease;
            Platform.runLater(() -> { btnMovel.setLayoutX(cx); btnMovel.setLayoutY(cy); });
            try { Thread.sleep(ANIM_SLEEP); } catch (InterruptedException e) { break; }
        }
        Platform.runLater(() -> { btnMovel.setVisible(false); latch.countDown(); });
        try { latch.await(); } catch (InterruptedException e) {}
    }

    private void iniciarAnimacao() {
        btnIniciar.setDisable(true);
        btnGerar.setDisable(true);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                destacarLinha(2); vars("Chamando acharMaior()..."); dormir();
                destacarLinha(11); dormir();

                int maior = vet[0];
                destacarLinha(12); vars("maior = vet[0] = " + maior); dormir();

                for (int i = 0; i < TL; i++) {
                    final int fi = i;
                    destacarLinha(13);
                    Platform.runLater(() -> { vetBotoes[fi].setStyle(estiloDestaque("#F9E2AF")); lblIndiceI.setText("i=" + fi); lblIndiceI.setLayoutX(VET_X + fi * (BOX_W + GAP) + 10); });
                    dormir();
                    if (vet[i] > maior) maior = vet[i];
                    final int fm = maior;
                    destacarLinha(14);
                    Platform.runLater(() -> vars("i=" + fi + "  vet[i]=" + vet[fi] + "  maior=" + fm));
                    dormir();
                }

                final int maiorF = maior;
                destacarLinha(15);
                Platform.runLater(() -> { vars("acharMaior() retornou " + maiorF); for (int i = 0; i < TAMANHO; i++) vetBotoes[i].setStyle(estiloVetorPadrao()); lblIndiceI.setText(""); });
                dormir();

                destacarLinha(3); Platform.runLater(() -> vars("maior=" + maiorF + "  nBaldes=" + N_BALDES)); dormir();
                destacarLinha(4); dormir();
                destacarLinha(5); Platform.runLater(() -> vars("Chamando distribuir()...")); dormir();
                destacarLinha(18); dormir();

                for (int i = 0; i < TL; i++) {
                    final int fi = i;
                    int indice = vet[i] * N_BALDES / (maiorF + 1);
                    final int fIdx = indice, pos = tamBalde[indice];
                    baldes[indice][tamBalde[indice]] = vet[i];

                    destacarLinha(19);
                    Platform.runLater(() -> { lblIndiceI.setText("i=" + fi); lblIndiceI.setLayoutX(VET_X + fi * (BOX_W + GAP) + 10); });
                    dormir();

                    destacarLinha(20);
                    Platform.runLater(() -> { vetBotoes[fi].setStyle(estiloDestaque(coresBalde[fIdx])); vars("i=" + fi + "  vet[i]=" + vet[fi] + "  indice=" + fIdx + "  maior=" + maiorF); });
                    dormir();

                    destacarLinha(21);
                    double ox = VET_X + fi * (BOX_W + GAP), oy = VET_Y;
                    double dx = VET_X + pos * (BOX_W + GAP), dy = BALDE_Y + fIdx * BALDE_GAP + 4;
                    Platform.runLater(() -> { vetBotoes[fi].setText(""); vetBotoes[fi].setStyle(estiloVetorVazio()); });
                    animarMovimento(ox, oy, dx, dy, String.valueOf(vet[fi]), estiloDestaque(coresBalde[fIdx]));
                    Platform.runLater(() -> { baldeBotoes[fIdx][pos].setText(String.valueOf(vet[fi])); baldeBotoes[fIdx][pos].setVisible(true); baldeBotoes[fIdx][pos].setStyle(estiloBalde(fIdx)); });

                    destacarLinha(22);
                    tamBalde[indice]++;
                    dormir();
                }

                Platform.runLater(() -> lblIndiceI.setText("")); dormir();
                destacarLinha(6); dormir();

                for (int b = 0; b < N_BALDES; b++) {
                    final int fb = b;
                    destacarLinha(7); Platform.runLater(() -> vars("InsertionSort no Balde " + fb + " (" + tamBalde[fb] + " elementos)")); dormir();
                    destacarLinha(26); dormir();

                    for (int i = 1; i < tamBalde[b]; i++) {
                        int key = baldes[b][i], j = i - 1;
                        final int fi = i, fKey = key;

                        destacarLinha(27); dormir();
                        destacarLinha(28);
                        Platform.runLater(() -> { vars("Balde " + fb + ": i=" + fi + "  key=" + fKey + "  j=" + (fi - 1)); baldeBotoes[fb][fi].setStyle(estiloDestaque("#F9E2AF")); });
                        dormir();

                        while (j >= 0 && baldes[b][j] > key) {
                            final int fj = j;
                            baldes[b][j + 1] = baldes[b][j];
                            destacarLinha(29); dormir();
                            destacarLinha(30);
                            Platform.runLater(() -> { baldeBotoes[fb][fj + 1].setText(String.valueOf(baldes[fb][fj + 1])); baldeBotoes[fb][fj].setStyle(estiloDestaque("#F38BA8")); vars("Balde " + fb + ": shift b[" + (fj + 1) + "]=b[" + fj + "]  key=" + fKey + "  j=" + fj); });
                            dormir();
                            Platform.runLater(() -> baldeBotoes[fb][fj].setStyle(estiloBalde(fb)));
                            j--;
                        }
                        baldes[b][j + 1] = key;
                        final int jf = j + 1;
                        destacarLinha(31);
                        Platform.runLater(() -> { baldeBotoes[fb][jf].setText(String.valueOf(fKey)); baldeBotoes[fb][jf].setStyle(estiloDestaque("#A6E3A1")); vars("Balde " + fb + ": drop key=" + fKey + " na posicao [" + jf + "]"); });
                        dormir();
                        Platform.runLater(() -> { for (int x = 0; x < tamBalde[fb]; x++) baldeBotoes[fb][x].setStyle(estiloBalde(fb)); });
                        dormir();
                    }
                }

                destacarLinha(8); Platform.runLater(() -> vars("Chamando juntar()...")); dormir();
                destacarLinha(35); dormir();

                int k = 0;
                for (int b = 0; b < N_BALDES; b++) {
                    for (int i = 0; i < tamBalde[b]; i++) {
                        vet[k] = baldes[b][i];
                        final int fk = k, fb = b, fi = i;
                        destacarLinha(37); dormir();
                        destacarLinha(39);
                        double ox = VET_X + fi * (BOX_W + GAP), oy = BALDE_Y + fb * BALDE_GAP + 4;
                        double dx = VET_X + fk * (BOX_W + GAP), dy = VET_Y;
                        Platform.runLater(() -> baldeBotoes[fb][fi].setStyle("-fx-background-color: transparent; -fx-text-fill: transparent; -fx-border-color: transparent;"));
                        animarMovimento(ox, oy, dx, dy, String.valueOf(vet[fk]), estiloDestaque(coresBalde[fb]));
                        Platform.runLater(() -> { vetBotoes[fk].setText(String.valueOf(vet[fk])); vetBotoes[fk].setStyle(estiloDestaque(coresBalde[fb])); vars("vet[" + fk + "] = baldes[" + fb + "][" + fi + "] = " + vet[fk]); });
                        dormir();
                        k++;
                    }
                }

                Platform.runLater(() -> { vars("Ordenacao concluida!"); limparDestaques(); for (int i = 0; i < TAMANHO; i++) vetBotoes[i].setStyle(estiloDestaque("#A6E3A1")); lblIndiceI.setText(""); btnGerar.setDisable(false); });
                return null;
            }
        };
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }

    private void dormir() { try { Thread.sleep(DELAY); } catch (InterruptedException e) {} }
    private void vars(String t) { lblVariaveis.setText("Variaveis: " + t); }

    private void destacarLinha(int n) {
        Platform.runLater(() -> { limparDestaques(); if (n >= 1 && n <= totalLinhas) linhasCodigo[n - 1].setStyle("-fx-background-color: #F9E2AF; -fx-text-fill: #1E1E2E; -fx-padding: 0 5 0 5;"); });
    }

    private void limparDestaques() {
        for (int i = 0; i < totalLinhas; i++) { linhasCodigo[i].setStyle("-fx-padding: 0 5 0 5;"); linhasCodigo[i].setTextFill(Color.web("#CDD6F4")); }
    }
}
