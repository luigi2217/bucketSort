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

    // ==================== CONFIGURACOES ====================
    private static final int TAMANHO = 9;
    private static final int N_BALDES = 3;
    private static final int DELAY = 400;
    private static final int ANIM_FRAMES = 30;
    private static final int ANIM_SLEEP = 12;

    // ==================== DADOS ====================
    private int[] vet = new int[TAMANHO];
    private int[][] baldes = new int[N_BALDES][TAMANHO];
    private int[] tamBalde = new int[N_BALDES];
    private int TL = 0;

    // ==================== ELEMENTOS DA TELA ====================
    private AnchorPane pane;
    private Button[] vetBotoes = new Button[TAMANHO];
    private Button[][] baldeBotoes = new Button[N_BALDES][TAMANHO];
    private Label[] linhasCodigo = new Label[45];
    private int totalLinhas = 0;
    private Label lblVariaveis;
    private Label lblIndiceI;
    private Label lblIndiceJ;
    private Label[] lblNomeBalde = new Label[N_BALDES];
    private Button btnIniciar;
    private Button btnGerar;
    private Button btnMovel;

    // Elementos do painel de codigo
    private Label lblCodigo;
    private Rectangle fundoCodigo;

    // Posicoes base
    private static final int VET_X = 40;
    private static final int VET_Y = 90;
    private static final int BOX_W = 60;
    private static final int BOX_H = 45;
    private static final int GAP = 6;
    private static final int BALDE_Y = 280;
    private static final int BALDE_GAP = 70;

    private static final int CODE_W = 420;
    private static final int CODE_MARGIN = 30;
    private double codeX = 680;

    private String[] coresBalde = {"#FF6B6B", "#4ECDC4", "#45B7D1"};

    // ==================== MAIN ====================
    public static void main(String[] args) {
        launch(args);
    }

    // ==================== START ====================
    @Override
    public void start(Stage stage) {
        stage.setTitle("Animacao Bucket Sort - Luigi Sardelari");
        stage.setMaximized(true);

        pane = new AnchorPane();
        pane.setStyle("-fx-background-color: #1E1E2E;");

        // --- Titulo ---
        Label titulo = new Label("Bucket Sort");
        titulo.setLayoutX(VET_X);
        titulo.setLayoutY(15);
        titulo.setFont(Font.font("Consolas", FontWeight.BOLD, 28));
        titulo.setTextFill(Color.WHITE);
        pane.getChildren().add(titulo);

        // --- Label "Vetor:" ---
        Label lblVetor = new Label("Vetor:");
        lblVetor.setLayoutX(VET_X);
        lblVetor.setLayoutY(VET_Y - 28);
        lblVetor.setFont(Font.font("Consolas", FontWeight.BOLD, 15));
        lblVetor.setTextFill(Color.web("#CDD6F4"));
        pane.getChildren().add(lblVetor);

        // --- Botoes do vetor ---
        for (int i = 0; i < TAMANHO; i++) {
            vetBotoes[i] = new Button("");
            vetBotoes[i].setLayoutX(VET_X + i * (BOX_W + GAP));
            vetBotoes[i].setLayoutY(VET_Y);
            vetBotoes[i].setMinWidth(BOX_W);
            vetBotoes[i].setMaxWidth(BOX_W);
            vetBotoes[i].setMinHeight(BOX_H);
            vetBotoes[i].setMaxHeight(BOX_H);
            vetBotoes[i].setFont(Font.font("Consolas", FontWeight.BOLD, 16));
            vetBotoes[i].setStyle(estiloVetorPadrao());
            pane.getChildren().add(vetBotoes[i]);

            Label idx = new Label("[" + i + "]");
            idx.setLayoutX(VET_X + i * (BOX_W + GAP) + 18);
            idx.setLayoutY(VET_Y + BOX_H + 3);
            idx.setFont(Font.font("Consolas", 12));
            idx.setTextFill(Color.web("#6C7086"));
            pane.getChildren().add(idx);
        }

        // --- Labels dos indices ---
        lblIndiceI = new Label("");
        lblIndiceI.setFont(Font.font("Consolas", FontWeight.BOLD, 13));
        lblIndiceI.setTextFill(Color.web("#F38BA8"));
        lblIndiceI.setLayoutY(VET_Y + BOX_H + 22);
        pane.getChildren().add(lblIndiceI);

        lblIndiceJ = new Label("");
        lblIndiceJ.setFont(Font.font("Consolas", FontWeight.BOLD, 13));
        lblIndiceJ.setTextFill(Color.web("#A6E3A1"));
        lblIndiceJ.setLayoutY(VET_Y + BOX_H + 22);
        pane.getChildren().add(lblIndiceJ);

        // --- Baldes ---
        for (int b = 0; b < N_BALDES; b++) {
            lblNomeBalde[b] = new Label("Balde " + b + ":");
            lblNomeBalde[b].setLayoutX(VET_X - 5);
            lblNomeBalde[b].setLayoutY(BALDE_Y + b * BALDE_GAP - 20);
            lblNomeBalde[b].setFont(Font.font("Consolas", FontWeight.BOLD, 13));
            lblNomeBalde[b].setTextFill(Color.web(coresBalde[b]));
            pane.getChildren().add(lblNomeBalde[b]);

            Rectangle fundoBalde = new Rectangle(
                    VET_X - 5, BALDE_Y + b * BALDE_GAP,
                    TAMANHO * (BOX_W + GAP) + 5, BOX_H + 8
            );
            fundoBalde.setFill(Color.TRANSPARENT);
            fundoBalde.setStroke(Color.web(coresBalde[b]));
            fundoBalde.setStrokeWidth(1.5);
            fundoBalde.setArcWidth(8);
            fundoBalde.setArcHeight(8);
            pane.getChildren().add(fundoBalde);

            for (int j = 0; j < TAMANHO; j++) {
                baldeBotoes[b][j] = new Button("");
                baldeBotoes[b][j].setLayoutX(VET_X + j * (BOX_W + GAP));
                baldeBotoes[b][j].setLayoutY(BALDE_Y + b * BALDE_GAP + 4);
                baldeBotoes[b][j].setMinWidth(BOX_W);
                baldeBotoes[b][j].setMaxWidth(BOX_W);
                baldeBotoes[b][j].setMinHeight(BOX_H);
                baldeBotoes[b][j].setMaxHeight(BOX_H);
                baldeBotoes[b][j].setFont(Font.font("Consolas", FontWeight.BOLD, 16));
                baldeBotoes[b][j].setStyle("-fx-background-color: transparent; -fx-text-fill: transparent;");
                baldeBotoes[b][j].setVisible(false);
                pane.getChildren().add(baldeBotoes[b][j]);
            }
        }

        // --- Codigo-fonte (TODOS os metodos) ---
        // Linhas numeradas:
        //  1  void bucketSort(int nBaldes) {
        //  2    int maior = acharMaior();
        //  3    int[][] baldes = new int[nBaldes][TL];
        //  4    int[] tamBalde = new int[nBaldes];
        //  5    distribuir(baldes, tamBalde, maior);
        //  6    for (int i = 0; i < nBaldes; i++)
        //  7      insertionSort(baldes[i], tamBalde[i]);
        //  8    juntar(baldes, tamBalde, nBaldes);
        //  9  }
        // 10
        // 11  int acharMaior() {
        // 12    int maior = vet[0];
        // 13    for (int i = 1; i < TL; i++)
        // 14      if (maior < vet[i]) maior = vet[i];
        // 15    return maior;
        // 16  }
        // 17
        // 18  void distribuir(...) {
        // 19    for (int i = 0; i < TL; i++) {
        // 20      indice = vet[i]*nBaldes/(maior+1);
        // 21      baldes[indice][tam[indice]] = vet[i];
        // 22      tamBalde[indice]++;
        // 23    }
        // 24  }
        // 25
        // 26  void insertionSort(int[] b, int tam) {
        // 27    for (int i = 1; i < tam; i++) {
        // 28      key = b[i];  j = i - 1;
        // 29      while (j >= 0 && b[j] > key)
        // 30        b[j+1] = b[j];  j--;
        // 31      b[j+1] = key;
        // 32    }
        // 33  }
        // 34
        // 35  void juntar(...) {
        // 36    int k = 0;
        // 37    for (int b = 0; b < nBaldes; b++)
        // 38      for (int i = 0; i < tamBalde[b]; i++)
        // 39        vet[k++] = baldes[b][i];
        // 40  }

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
                "void bucketSort(int nBaldes) {",
                "  int maior = acharMaior();",
                "  int[][] baldes = new int[nBaldes][TL];",
                "  int[] tamBalde = new int[nBaldes];",
                "  distribuir(baldes, tamBalde, maior);",
                "  for (int i = 0; i < nBaldes; i++)",
                "    insertionSort(baldes[i], tamBalde[i]);",
                "  juntar(baldes, tamBalde, nBaldes);",
                "}",
                "",
                "int acharMaior() {",
                "  int maior = vet[0];",
                "  for (int i = 1; i < TL; i++)",
                "    if (maior < vet[i]) maior = vet[i];",
                "  return maior;",
                "}",
                "",
                "void distribuir(...) {",
                "  for (int i = 0; i < TL; i++) {",
                "    indice = vet[i]*nBaldes/(maior+1);",
                "    baldes[indice][tam[indice]] = vet[i];",
                "    tamBalde[indice]++;",
                "  }",
                "}",
                "",
                "void insertionSort(int[] b, int tam) {",
                "  for (int i = 1; i < tam; i++) {",
                "    key = b[i];  j = i - 1;",
                "    while (j >= 0 && b[j] > key)",
                "      b[j+1] = b[j];  j--;",
                "    b[j+1] = key;",
                "  }",
                "}",
                "",
                "void juntar(...) {",
                "  int k = 0;",
                "  for (int b = 0; b < nBaldes; b++)",
                "    for (int i = 0; i < tamBalde[b]; i++)",
                "      vet[k++] = baldes[b][i];",
                "}",
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

        // --- Variaveis ---
        lblVariaveis = new Label("Variaveis: ");
        lblVariaveis.setLayoutX(VET_X);
        lblVariaveis.setLayoutY(BALDE_Y + N_BALDES * BALDE_GAP + 25);
        lblVariaveis.setFont(Font.font("Consolas", FontWeight.BOLD, 14));
        lblVariaveis.setTextFill(Color.web("#F5C2E7"));
        pane.getChildren().add(lblVariaveis);

        // --- Botao movel ---
        btnMovel = new Button("");
        btnMovel.setMinWidth(BOX_W);
        btnMovel.setMaxWidth(BOX_W);
        btnMovel.setMinHeight(BOX_H);
        btnMovel.setMaxHeight(BOX_H);
        btnMovel.setFont(Font.font("Consolas", FontWeight.BOLD, 16));
        btnMovel.setVisible(false);
        pane.getChildren().add(btnMovel);

        // --- Botoes de controle ---
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

    // ==================== REPOSICIONAR CODIGO ====================
    private void reposicionarCodigo(double larguraJanela) {
        codeX = larguraJanela - CODE_W - CODE_MARGIN;
        lblCodigo.setLayoutX(codeX);
        fundoCodigo.setX(codeX - 5);
        for (int i = 0; i < totalLinhas; i++)
            linhasCodigo[i].setLayoutX(codeX);
    }

    // ==================== ESTILOS ====================
    private String estiloVetorPadrao() {
        return "-fx-background-color: #585B70; -fx-text-fill: white; "
                + "-fx-border-color: #7F849C; -fx-border-width: 1;";
    }

    private String estiloVetorVazio() {
        return "-fx-background-color: #313244; -fx-text-fill: transparent; "
                + "-fx-border-color: #45475A; -fx-border-width: 1;";
    }

    private String estiloBalde(int b) {
        return "-fx-background-color: " + coresBalde[b] + "; "
                + "-fx-text-fill: #1E1E2E; -fx-font-weight: bold; "
                + "-fx-border-color: white; -fx-border-width: 1;";
    }

    private String estiloDestaque(String cor) {
        return "-fx-background-color: " + cor + "; -fx-text-fill: #1E1E2E; "
                + "-fx-font-weight: bold; -fx-border-color: white; -fx-border-width: 2;";
    }

    // ==================== GERAR ALEATORIO ====================
    private void gerarAleatorio() {
        Random rand = new Random();
        TL = 0;

        for (int b = 0; b < N_BALDES; b++) {
            tamBalde[b] = 0;
            for (int j = 0; j < TAMANHO; j++) {
                baldeBotoes[b][j].setText("");
                baldeBotoes[b][j].setVisible(false);
            }
        }

        for (int i = 0; i < TAMANHO; i++) {
            vet[i] = rand.nextInt(100);
            TL++;
            vetBotoes[i].setText(String.valueOf(vet[i]));
            vetBotoes[i].setStyle(estiloVetorPadrao());
        }

        lblVariaveis.setText("Variaveis: (clique em Ordenar)");
        limparDestaques();
        btnIniciar.setDisable(false);
    }

    // ==================== ANIMACAO DE MOVIMENTO ====================
    private void animarMovimento(double x1, double y1, double x2, double y2,
                                  String valor, String estilo) {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            btnMovel.setText(valor);
            btnMovel.setStyle(estilo);
            btnMovel.setLayoutX(x1);
            btnMovel.setLayoutY(y1);
            btnMovel.setVisible(true);
            btnMovel.toFront();
        });

        for (int f = 1; f <= ANIM_FRAMES; f++) {
            double t = (double) f / ANIM_FRAMES;
            double ease = t * t * (3 - 2 * t);
            double cx = x1 + (x2 - x1) * ease;
            double cy = y1 + (y2 - y1) * ease;
            Platform.runLater(() -> {
                btnMovel.setLayoutX(cx);
                btnMovel.setLayoutY(cy);
            });
            try { Thread.sleep(ANIM_SLEEP); } catch (InterruptedException e) { break; }
        }

        Platform.runLater(() -> {
            btnMovel.setVisible(false);
            latch.countDown();
        });
        try { latch.await(); } catch (InterruptedException e) { /* ok */ }
    }

    // ==================== INICIAR ANIMACAO ====================
    private void iniciarAnimacao() {
        btnIniciar.setDisable(true);
        btnGerar.setDisable(true);

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {

                // ===== PASSO 1: Achar maior =====
                // Linha 2: int maior = acharMaior();
                destacarLinha(2);
                atualizarVariaveis("Chamando acharMaior()...");
                dormir();

                // Entra no metodo acharMaior (linha 11)
                destacarLinha(11);
                dormir();

                int maior = vet[0];
                // Linha 12: int maior = vet[0];
                destacarLinha(12);
                atualizarVariaveis("maior = vet[0] = " + maior);
                dormir();

                for (int i = 0; i < TL; i++) {
                    final int fi = i;
                    // Linha 13: for (int i = 1; i < TL; i++)
                    destacarLinha(13);
                    Platform.runLater(() -> {
                        destacarBotaoVetor(fi, "#F9E2AF");
                        lblIndiceI.setText("i=" + fi);
                        lblIndiceI.setLayoutX(VET_X + fi * (BOX_W + GAP) + 10);
                    });
                    dormir();

                    if (vet[i] > maior) maior = vet[i];
                    final int fMaior = maior;
                    // Linha 14: if (maior < vet[i]) maior = vet[i];
                    destacarLinha(14);
                    Platform.runLater(() -> atualizarVariaveis("i=" + fi + "  vet[i]=" + vet[fi] + "  maior=" + fMaior));
                    dormir();
                }

                final int maiorFinal = maior;
                // Linha 15: return maior;
                destacarLinha(15);
                Platform.runLater(() -> {
                    atualizarVariaveis("acharMaior() retornou " + maiorFinal);
                    for (int i = 0; i < TAMANHO; i++)
                        vetBotoes[i].setStyle(estiloVetorPadrao());
                    lblIndiceI.setText("");
                });
                dormir();

                // ===== PASSO 2: Criar baldes =====
                // Linha 3: int[][] baldes = new int[nBaldes][TL];
                destacarLinha(3);
                Platform.runLater(() -> atualizarVariaveis("maior=" + maiorFinal + "  nBaldes=" + N_BALDES));
                dormir();
                // Linha 4: int[] tamBalde = new int[nBaldes];
                destacarLinha(4);
                dormir();

                // ===== PASSO 3: Distribuir =====
                // Linha 5: distribuir(baldes, tamBalde, maior);
                destacarLinha(5);
                Platform.runLater(() -> atualizarVariaveis("Chamando distribuir()..."));
                dormir();

                // Entra no metodo distribuir (linha 18)
                destacarLinha(18);
                dormir();

                for (int i = 0; i < TL; i++) {
                    final int fi = i;
                    int indice = vet[i] * N_BALDES / (maiorFinal + 1);
                    final int fIndice = indice;
                    final int posNoBalde = tamBalde[indice];

                    baldes[indice][tamBalde[indice]] = vet[i];

                    // Linha 19: for (int i = 0; i < TL; i++)
                    destacarLinha(19);
                    Platform.runLater(() -> {
                        lblIndiceI.setText("i=" + fi);
                        lblIndiceI.setLayoutX(VET_X + fi * (BOX_W + GAP) + 10);
                    });
                    dormir();

                    // Linha 20: indice = vet[i]*nBaldes/(maior+1);
                    destacarLinha(20);
                    Platform.runLater(() -> {
                        destacarBotaoVetor(fi, coresBalde[fIndice]);
                        atualizarVariaveis("i=" + fi + "  vet[i]=" + vet[fi]
                                + "  indice=" + fIndice + "  maior=" + maiorFinal);
                    });
                    dormir();

                    // Linha 21: baldes[indice][tam[indice]] = vet[i];
                    destacarLinha(21);

                    double origemX = VET_X + fi * (BOX_W + GAP);
                    double origemY = VET_Y;
                    double destinoX = VET_X + posNoBalde * (BOX_W + GAP);
                    double destinoY = BALDE_Y + fIndice * BALDE_GAP + 4;

                    Platform.runLater(() -> {
                        vetBotoes[fi].setText("");
                        vetBotoes[fi].setStyle(estiloVetorVazio());
                    });

                    animarMovimento(origemX, origemY, destinoX, destinoY,
                            String.valueOf(vet[fi]), estiloDestaque(coresBalde[fIndice]));

                    Platform.runLater(() -> {
                        baldeBotoes[fIndice][posNoBalde].setText(String.valueOf(vet[fi]));
                        baldeBotoes[fIndice][posNoBalde].setVisible(true);
                        baldeBotoes[fIndice][posNoBalde].setStyle(estiloBalde(fIndice));
                    });

                    // Linha 22: tamBalde[indice]++;
                    destacarLinha(22);
                    tamBalde[indice]++;
                    dormir();
                }

                Platform.runLater(() -> lblIndiceI.setText(""));
                dormir();

                // ===== PASSO 4: InsertionSort em cada balde =====
                // Linha 6: for (int i = 0; i < nBaldes; i++)
                destacarLinha(6);
                dormir();

                for (int b = 0; b < N_BALDES; b++) {
                    final int fb = b;
                    // Linha 7: insertionSort(baldes[i], tamBalde[i]);
                    destacarLinha(7);
                    Platform.runLater(() -> atualizarVariaveis("InsertionSort no Balde " + fb
                            + " (" + tamBalde[fb] + " elementos)"));
                    dormir();

                    // Entra no metodo (linha 26)
                    destacarLinha(26);
                    dormir();

                    for (int i = 1; i < tamBalde[b]; i++) {
                        int key = baldes[b][i];
                        int j = i - 1;
                        final int fi = i;
                        final int fKey = key;

                        // Linha 27: for (int i = 1; i < tam; i++)
                        destacarLinha(27);
                        dormir();

                        // Linha 28: key = b[i]; j = i - 1;
                        destacarLinha(28);
                        Platform.runLater(() -> {
                            atualizarVariaveis("Balde " + fb + ": i=" + fi
                                    + "  key=" + fKey + "  j=" + (fi - 1));
                            baldeBotoes[fb][fi].setStyle(estiloDestaque("#F9E2AF"));
                        });
                        dormir();

                        while (j >= 0 && baldes[b][j] > key) {
                            final int fj = j;
                            baldes[b][j + 1] = baldes[b][j];

                            // Linha 29: while (j >= 0 && b[j] > key)
                            destacarLinha(29);
                            dormir();

                            // Linha 30: b[j+1] = b[j]; j--;
                            destacarLinha(30);
                            Platform.runLater(() -> {
                                baldeBotoes[fb][fj + 1].setText(String.valueOf(baldes[fb][fj + 1]));
                                baldeBotoes[fb][fj].setStyle(estiloDestaque("#F38BA8"));
                                atualizarVariaveis("Balde " + fb + ": shift b[" + (fj + 1)
                                        + "]=b[" + fj + "]  key=" + fKey + "  j=" + fj);
                            });
                            dormir();

                            Platform.runLater(() ->
                                    baldeBotoes[fb][fj].setStyle(estiloBalde(fb)));
                            j--;
                        }
                        baldes[b][j + 1] = key;
                        final int fjFinal = j + 1;

                        // Linha 31: b[j+1] = key;
                        destacarLinha(31);
                        Platform.runLater(() -> {
                            baldeBotoes[fb][fjFinal].setText(String.valueOf(fKey));
                            baldeBotoes[fb][fjFinal].setStyle(estiloDestaque("#A6E3A1"));
                            atualizarVariaveis("Balde " + fb + ": drop key=" + fKey
                                    + " na posicao [" + fjFinal + "]");
                        });
                        dormir();

                        Platform.runLater(() -> {
                            for (int x = 0; x < tamBalde[fb]; x++)
                                baldeBotoes[fb][x].setStyle(estiloBalde(fb));
                        });
                        dormir();
                    }
                }

                // ===== PASSO 5: Juntar =====
                // Linha 8: juntar(baldes, tamBalde, nBaldes);
                destacarLinha(8);
                Platform.runLater(() -> atualizarVariaveis("Chamando juntar()..."));
                dormir();

                // Entra no metodo juntar (linha 35)
                destacarLinha(35);
                dormir();

                int k = 0;
                for (int b = 0; b < N_BALDES; b++) {
                    for (int i = 0; i < tamBalde[b]; i++) {
                        vet[k] = baldes[b][i];
                        final int fk = k;
                        final int fb = b;
                        final int fi = i;

                        // Linha 37-38: for loops
                        destacarLinha(37);
                        dormir();

                        // Linha 39: vet[k++] = baldes[b][i];
                        destacarLinha(39);

                        double origemX = VET_X + fi * (BOX_W + GAP);
                        double origemY = BALDE_Y + fb * BALDE_GAP + 4;
                        double destinoX = VET_X + fk * (BOX_W + GAP);
                        double destinoY = VET_Y;

                        Platform.runLater(() -> {
                            baldeBotoes[fb][fi].setStyle(
                                    "-fx-background-color: transparent; -fx-text-fill: transparent; "
                                            + "-fx-border-color: transparent;");
                        });

                        animarMovimento(origemX, origemY, destinoX, destinoY,
                                String.valueOf(vet[fk]), estiloDestaque(coresBalde[fb]));

                        Platform.runLater(() -> {
                            vetBotoes[fk].setText(String.valueOf(vet[fk]));
                            vetBotoes[fk].setStyle(estiloDestaque(coresBalde[fb]));
                            atualizarVariaveis("vet[" + fk + "] = baldes[" + fb + "][" + fi
                                    + "] = " + vet[fk]);
                        });
                        dormir();
                        k++;
                    }
                }

                // Finalizado!
                Platform.runLater(() -> {
                    atualizarVariaveis("Ordenacao concluida!");
                    limparDestaques();
                    for (int i = 0; i < TAMANHO; i++)
                        vetBotoes[i].setStyle(estiloDestaque("#A6E3A1"));
                    lblIndiceI.setText("");
                    lblIndiceJ.setText("");
                    btnGerar.setDisable(false);
                });

                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    // ==================== METODOS AUXILIARES ====================

    private void dormir() {
        try { Thread.sleep(DELAY); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    private void destacarLinha(int numLinha) {
        Platform.runLater(() -> {
            limparDestaques();
            if (numLinha >= 1 && numLinha <= totalLinhas)
                linhasCodigo[numLinha - 1].setStyle(
                        "-fx-background-color: #F9E2AF; -fx-text-fill: #1E1E2E; -fx-padding: 0 5 0 5;");
        });
    }

    private void limparDestaques() {
        for (int i = 0; i < totalLinhas; i++) {
            linhasCodigo[i].setStyle("-fx-padding: 0 5 0 5;");
            linhasCodigo[i].setTextFill(Color.web("#CDD6F4"));
        }
    }

    private void destacarBotaoVetor(int pos, String cor) {
        vetBotoes[pos].setStyle(estiloDestaque(cor));
    }

    private void atualizarVariaveis(String texto) {
        lblVariaveis.setText("Variaveis: " + texto);
    }
}
