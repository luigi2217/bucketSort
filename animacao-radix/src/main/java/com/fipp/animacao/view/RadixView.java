package com.fipp.animacao.view;

import com.fipp.animacao.app.Main;
import com.fipp.animacao.util.Animacao;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RadixView {
    private HBox[] vet;
    public int[] valores;
    public AnchorPane pane;
    final int TL = 10;
    private static final int NUM_DIGITOS = 3;

    private static final double BASE_X = 60.0;
    private static final double BASE_Y = 100.0;
    private static final double ESPACO_Y = 60.0;
    private static final double AUX_X = 170.0;

    private Button btnOrdenar;

    private VBox codigoBox;
    private Label[] linhasCodigo;
    private int linhaAtual = -1;

    private HBox cntContainer;
    private Label[] lblCntValor;
    private VBox cntBoxWrapper;
    private Label lblCntTitulo;

    private VBox auxWrapper;
    private Label lblAuxTitulo;
    private Label lblVetTitulo;

    private static final String[] CODIGO_RADIX = {
        "void radixSort(int vet[], int n) {",            // 0
        "  for (int d = NUM_DIGITOS-1; d >= 0; d--) {",  // 1
        "    int cnt[] = new int[10];",                  // 2
        "    for (int i = 0; i < n; i++)",               // 3
        "      cnt[getDigito(vet[i],d)]++;",             // 4
        "    for (int i = 1; i < 10; i++)",              // 5
        "      cnt[i] += cnt[i-1];",                     // 6
        "    int aux[] = new int[n];",                   // 7
        "    for (int i = n-1; i >= 0; i--) {",          // 8
        "      int dig = getDigito(vet[i],d);",          // 9
        "      aux[--cnt[dig]] = vet[i];",               // 10
        "    }",                                         // 11
        "    for (int i = 0; i < n; i++)",               // 12
        "      vet[i] = aux[i];",                        // 13
        "  }",                                           // 14
        "}"                                              // 15
    };

    public RadixView (Stage stage) {
        pane = new AnchorPane();
        valores = Main.vetor(TL);
        vet = new HBox[TL];

        for(int i = 0; i < TL; ++i) {
            String s = String.valueOf(valores[i]);

            HBox hbox = new HBox(0);
            hbox.setLayoutX(BASE_X);
            hbox.setLayoutY(BASE_Y + i * ESPACO_Y);
            hbox.setPrefSize(90, 40);
            hbox.setAlignment(Pos.CENTER);
            hbox.setStyle(
                "-fx-background-color: black;"
            );

            for (int d = 0; d < NUM_DIGITOS; d++) {
                int charIndex = s.length() - NUM_DIGITOS + d;
                Label digito;
                if (charIndex < 0) {
                    digito = new Label();
                    digito.setText(null);
                } else {
                    digito = new Label(String.valueOf(s.charAt(charIndex)));
                }
                digito.setPrefSize(30, 40);
                digito.setAlignment(Pos.CENTER);
                digito.setStyle(
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 16px;" +
                    "-fx-background-color: black;"
                );
                hbox.getChildren().add(digito);
            }

            vet[i] = hbox;
            pane.getChildren().add(this.vet[i]);
        }

        lblVetTitulo = new Label("vet[ ]");
        lblVetTitulo.setPrefWidth(90);
        lblVetTitulo.setAlignment(Pos.CENTER);
        lblVetTitulo.setLayoutX(BASE_X);
        lblVetTitulo.setLayoutY(BASE_Y - 38);
        lblVetTitulo.setStyle(
            "-fx-text-fill: #d4d4d4;" +
            "-fx-font-size: 11px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-color: #252526;" +
            "-fx-padding: 4 0 4 0;" +
            "-fx-background-radius: 3;" +
            "-fx-border-color: #3c3c3c;" +
            "-fx-border-width: 1;"
        );
        pane.getChildren().add(lblVetTitulo);

        btnOrdenar = new Button("Ordenar Radix");
        btnOrdenar.setLayoutX(250);
        btnOrdenar.setLayoutY(20);
        btnOrdenar.setPrefSize(140, 30);
        btnOrdenar.setOnAction(e -> iniciarAnimacaoRadix());
        pane.getChildren().add(btnOrdenar);

        Button btnReset = new Button("Reset");
        btnReset.setLayoutX(400);
        btnReset.setLayoutY(20);
        btnReset.setPrefSize(80, 30);
        btnReset.setOnAction(e -> resetPosicoes());
        pane.getChildren().add(btnReset);

        criarSlotsAux();
        criarPainelCnt();
        criarPainelCodigo();

        stage.setScene(new Scene(pane, 600.0, 800.0));
        stage.setMaximized(true);
        stage.setTitle("Radix Sort - Counting Sort por digito (com aux visual)");
        stage.show();
    }

    private void criarSlotsAux() {
        auxWrapper = new VBox(2);
        auxWrapper.setAlignment(Pos.CENTER);
        auxWrapper.setLayoutX(AUX_X);
        auxWrapper.setLayoutY(BASE_Y - 38);
        auxWrapper.setPrefWidth(90);

        lblAuxTitulo = new Label("aux[ ]");
        lblAuxTitulo.setPrefWidth(90);
        lblAuxTitulo.setAlignment(Pos.CENTER);
        lblAuxTitulo.setStyle(
            "-fx-text-fill: #d4d4d4;" +
            "-fx-font-size: 11px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-color: #252526;" +
            "-fx-padding: 4 0 4 0;" +
            "-fx-background-radius: 3;" +
            "-fx-border-color: #3c3c3c;" +
            "-fx-border-width: 1;"
        );
        auxWrapper.getChildren().add(lblAuxTitulo);
        pane.getChildren().add(auxWrapper);
    }

    private void criarPainelCnt() {
        cntBoxWrapper = new VBox(4);
        cntBoxWrapper.setAlignment(Pos.CENTER);
        cntBoxWrapper.setPrefWidth(420);
        cntBoxWrapper.setStyle(
            "-fx-background-color: #252526;" +
            "-fx-border-color: #3c3c3c;" +
            "-fx-border-width: 1;" +
            "-fx-padding: 6;" +
            "-fx-background-radius: 4;"
        );

        lblCntTitulo = new Label("cnt[0..9]  (Counting Sort)");
        lblCntTitulo.setStyle(
            "-fx-text-fill: #d4d4d4;" +
            "-fx-font-size: 11px;" +
            "-fx-font-weight: bold;"
        );
        cntBoxWrapper.getChildren().add(lblCntTitulo);

        cntContainer = new HBox(3);
        cntContainer.setAlignment(Pos.CENTER);
        lblCntValor = new Label[10];
        for (int k = 0; k < 10; k++) {
            VBox col = new VBox(2);
            col.setAlignment(Pos.CENTER);
            Label lblDig = new Label(String.valueOf(k));
            lblDig.setPrefSize(36, 16);
            lblDig.setAlignment(Pos.CENTER);
            lblDig.setStyle(
                "-fx-text-fill: #9cdcfe;" +
                "-fx-font-size: 10px;" +
                "-fx-font-weight: bold;"
            );
            Label lblVal = new Label("0");
            lblVal.setPrefSize(36, 28);
            lblVal.setAlignment(Pos.CENTER);
            lblVal.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-color: #1e1e1e;" +
                "-fx-border-color: #3c3c3c;" +
                "-fx-border-width: 1;" +
                "-fx-background-radius: 3;"
            );
            lblCntValor[k] = lblVal;
            col.getChildren().add(lblDig);
            col.getChildren().add(lblVal);
            cntContainer.getChildren().add(col);
        }
        cntBoxWrapper.getChildren().add(cntContainer);

        pane.getChildren().add(cntBoxWrapper);
        AnchorPane.setRightAnchor(cntBoxWrapper, 20.0);
        AnchorPane.setTopAnchor(cntBoxWrapper, 430.0);
    }

    private void atualizarCntVisual(int[] cnt) {
        for (int k = 0; k < 10; k++) {
            lblCntValor[k].setText(String.valueOf(cnt[k]));
        }
    }

    private void destacarCntPos(int pos, boolean destacar) {
        if (pos < 0 || pos >= 10) return;
        if (destacar) {
            lblCntValor[pos].setStyle(
                "-fx-text-fill: black;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-color: lightgreen;" +
                "-fx-border-color: #3c3c3c;" +
                "-fx-border-width: 1;" +
                "-fx-background-radius: 3;"
            );
        } else {
            lblCntValor[pos].setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-color: #1e1e1e;" +
                "-fx-border-color: #3c3c3c;" +
                "-fx-border-width: 1;" +
                "-fx-background-radius: 3;"
            );
        }
    }

    private void limparDestaqueCnt() {
        for (int k = 0; k < 10; k++)
            destacarCntPos(k, false);
    }

    private void criarPainelCodigo() {
        VBox container = new VBox(0);
        container.setPrefWidth(420);
        container.setStyle(
            "-fx-background-color: #1e1e1e;" +
            "-fx-border-color: #3c3c3c;" +
            "-fx-border-width: 1;" +
            "-fx-padding: 8;"
        );

        Label titulo = new Label("* * *    Radix Sort  (Counting Sort)    * * *");
        titulo.setStyle(
            "-fx-text-fill: #d4d4d4;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 0 0 6 0;"
        );
        container.getChildren().add(titulo);

        linhasCodigo = new Label[CODIGO_RADIX.length];
        for (int i = 0; i < CODIGO_RADIX.length; i++) {
            Label linha = new Label(CODIGO_RADIX[i]);
            linha.setPrefWidth(420);
            linha.setStyle(
                "-fx-text-fill: #d4d4d4;" +
                "-fx-font-family: 'Consolas', 'Courier New', monospace;" +
                "-fx-font-size: 11.5px;" +
                "-fx-padding: 2 6 2 6;" +
                "-fx-background-color: transparent;"
            );
            linhasCodigo[i] = linha;
            container.getChildren().add(linha);
        }

        codigoBox = container;
        pane.getChildren().add(codigoBox);

        AnchorPane.setRightAnchor(codigoBox, 20.0);
        AnchorPane.setTopAnchor(codigoBox, 80.0);
    }

    private void destacarLinhaCodigo(int indice) {
        if (linhaAtual >= 0 && linhaAtual < linhasCodigo.length) {
            linhasCodigo[linhaAtual].setStyle(
                "-fx-text-fill: #d4d4d4;" +
                "-fx-font-family: 'Consolas', 'Courier New', monospace;" +
                "-fx-font-size: 11.5px;" +
                "-fx-padding: 2 6 2 6;" +
                "-fx-background-color: transparent;"
            );
        }
        linhaAtual = indice;
        if (indice >= 0 && indice < linhasCodigo.length) {
            linhasCodigo[indice].setStyle(
                "-fx-text-fill: #000000;" +
                "-fx-font-family: 'Consolas', 'Courier New', monospace;" +
                "-fx-font-size: 11.5px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 2 6 2 6;" +
                "-fx-background-color: lightgreen;" +
                "-fx-background-radius: 3;"
            );
        }
    }

    private void destacarLinhaCodigoSync(int indice, long pausaMs) throws InterruptedException {
        Platform.runLater(() -> destacarLinhaCodigo(indice));
        Thread.sleep(pausaMs);
    }

    private void marcarDigitoSelecionado(HBox box, int posDigito, boolean selecionado) {
        Label dig = (Label) box.getChildren().get(posDigito);
        if (selecionado) {
            dig.setStyle(
                "-fx-text-fill: black;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-color: lightgreen;"
            );
        } else {
            dig.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-background-color: black;"
            );
        }
    }

    private void limparMarcasDigitos(HBox[] arr, int posDigito) {
        for (int i = 0; i < arr.length; i++) marcarDigitoSelecionado(arr[i], posDigito, false);
    }

    private void resetPosicoes() {
        Platform.runLater(() -> {
            destacarLinhaCodigo(-1);
            limparDestaqueCnt();
            int[] zero = new int[10];
            atualizarCntVisual(zero);
        });
        for (int d = 0; d < NUM_DIGITOS; d++) {
            destacarDigito(d, false);
            limparMarcasDigitos(vet, d);
        }
        valores = Main.vetor(TL);
        for (int i = 0; i < TL; i++) {
            String s = String.valueOf(valores[i]);
            HBox hbox = vet[i];
            hbox.setLayoutX(BASE_X);
            hbox.setLayoutY(BASE_Y + i * ESPACO_Y);
            for (int d = 0; d < NUM_DIGITOS; d++) {
                int charIndex = s.length() - NUM_DIGITOS + d;
                Label digit = (Label) hbox.getChildren().get(d);
                if (charIndex < 0) {
                    digit.setText(null);
                } else {
                    digit.setText(String.valueOf(s.charAt(charIndex)));
                }
                digit.setStyle(
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 16px;" +
                    "-fx-background-color: black;"
                );
            }
        }
    }

    private void iniciarAnimacaoRadix() {
        btnOrdenar.setDisable(true);

        HBox[] ordemAtual = new HBox[TL];
        for (int i = 0; i < TL; i++) ordemAtual[i] = vet[i];

        Task<Void> master = new Task<>() {
            @Override
            protected Void call() throws Exception {

                destacarLinhaCodigoSync(0, 400);

                for (int d = NUM_DIGITOS - 1; d >= 0; d--) {
                    final int posDigito = d;

                    destacarLinhaCodigoSync(1, 350);
                    Thread.sleep(50);
                    Platform.runLater(() -> {
                        limparMarcasDigitos(ordemAtual, posDigito);
                        limparDestaqueCnt();
                    });
                    Thread.sleep(100);

                    // linha 2: int cnt[] = new int[10];
                    destacarLinhaCodigoSync(2, 350);
                    int[] cnt = new int[10];
                    for (int k = 0; k < 10; k++) cnt[k] = 0;
                    final int[] cntInit = cnt.clone();
                    Platform.runLater(() -> atualizarCntVisual(cntInit));
                    Thread.sleep(150);

                    // linhas 3-4: contagem de frequencia - cnt[getDigito]++
                    destacarLinhaCodigoSync(3, 250);
                    for (int i = 0; i < TL; i++) {
                        if (isCancelled()) break;
                        destacarLinhaCodigoSync(4, 180);
                        HBox box = ordemAtual[i];
                        int dig = extrairDigito(box, posDigito);
                        cnt[dig]++;
                        final int fDig = dig;
                        final int[] fCnt = cnt.clone();
                        Platform.runLater(() -> {
                            atualizarCntVisual(fCnt);
                            limparDestaqueCnt();
                            destacarCntPos(fDig, true);
                            marcarDigitoSelecionado(box, posDigito, true);
                        });
                        Thread.sleep(220);
                    }
                    Platform.runLater(() -> limparDestaqueCnt());
                    Thread.sleep(150);

                    // linhas 5-6: prefix sum cnt[i] += cnt[i-1]
                    destacarLinhaCodigoSync(5, 280);
                    for (int i = 1; i < 10; i++) {
                        destacarLinhaCodigoSync(6, 260);
                        cnt[i] += cnt[i-1];
                        final int fi = i;
                        final int[] fCnt2 = cnt.clone();
                        Platform.runLater(() -> {
                            atualizarCntVisual(fCnt2);
                            limparDestaqueCnt();
                            destacarCntPos(fi, true);
                            if (fi - 1 >= 0) destacarCntPos(fi - 1, true);
                        });
                        Thread.sleep(260);
                    }
                    Platform.runLater(() -> limparDestaqueCnt());
                    Thread.sleep(200);

                    // linha 7: int aux[] = new int[n];
                    destacarLinhaCodigoSync(7, 300);
                    HBox[] auxHBox = new HBox[TL];

                    // linhas 8-10: colocação estável reversa aux[--cnt[dig]] = vet[i] COM movimentação direta para aux
                    destacarLinhaCodigoSync(8, 250);
                    for (int i = TL - 1; i >= 0; i--) {
                        if (isCancelled()) break;
                        destacarLinhaCodigoSync(9, 200);
                        HBox box = ordemAtual[i];
                        int dig = extrairDigito(box, posDigito);
                        destacarLinhaCodigoSync(10, 200);
                        cnt[dig]--;
                        int pos = cnt[dig];
                        auxHBox[pos] = box;
                        final int fDig2 = dig;
                        final int fPos = pos;
                        final int[] fCnt3 = cnt.clone();
                        Platform.runLater(() -> {
                            atualizarCntVisual(fCnt3);
                            limparDestaqueCnt();
                            destacarCntPos(fDig2, true);
                            marcarDigitoSelecionado(box, posDigito, true);
                        });
                        Animacao.animarParaAuxEBloquear(box, pos);
                        Thread.sleep(140);
                    }
                    Platform.runLater(() -> {
                        limparDestaqueCnt();
                    });
                    destacarLinhaCodigoSync(11, 150);
                    Thread.sleep(80);

                    // linhas 12-13: copia vet[i] = aux[i] - anima de aux de volta para vet
                    destacarLinhaCodigoSync(12, 250);
                    for (int k = 0; k < TL; k++) {
                        destacarLinhaCodigoSync(13, 80);
                        HBox v = auxHBox[k];
                        Animacao.animarDeAuxParaVetEBloquear(v, k);
                        Thread.sleep(60);
                    }
                    destacarLinhaCodigoSync(14, 200);

                    Thread.sleep(300);

                    for (int i = 0; i < TL; i++) ordemAtual[i] = auxHBox[i];

                    HBox[] snapshot = new HBox[TL];
                    for (int i = 0; i < TL; i++) snapshot[i] = ordemAtual[i];
                    final int[] cntSnapshot = cnt.clone();
                    Platform.runLater(() -> {
                        for (int i = 0; i < TL; i++) vet[i] = snapshot[i];
                        limparMarcasDigitos(snapshot, posDigito);
                        atualizarCntVisual(cntSnapshot);
                        limparDestaqueCnt();
                    });

                    Thread.sleep(400);
                }

                destacarLinhaCodigoSync(15, 500);
                Platform.runLater(() -> {
                    destacarLinhaCodigo(-1);
                    limparDestaqueCnt();
                });

                return null;
            }

            @Override
            protected void succeeded() {
                btnOrdenar.setDisable(false);
                Platform.runLater(() -> {
                    destacarLinhaCodigo(-1);
                    limparDestaqueCnt();
                });
            }

            @Override
            protected void failed() {
                btnOrdenar.setDisable(false);
                Platform.runLater(() -> {
                    destacarLinhaCodigo(-1);
                    limparDestaqueCnt();
                });
                getException().printStackTrace();
            }

            @Override
            protected void cancelled() {
                btnOrdenar.setDisable(false);
                Platform.runLater(() -> {
                    destacarLinhaCodigo(-1);
                    limparDestaqueCnt();
                });
            }
        };

        Thread th = new Thread(master, "radix-master");
        th.setDaemon(true);
        th.start();
    }

    private int extrairDigito(HBox box, int posDigito) {
        Label lbl = (Label) box.getChildren().get(posDigito);
        String txt = lbl.getText();
        if (txt == null || txt.isEmpty()) return 0;
        try {
            return Integer.parseInt(txt);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void destacarDigito(int posDigito, boolean destacar) {
        for (int i = 0; i < vet.length; i++) {
            HBox box = vet[i];
            Label lbl = (Label) box.getChildren().get(posDigito);
            if (destacar) {
                lbl.setStyle(
                    "-fx-text-fill: black;" +
                    "-fx-font-size: 16px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: green;"
                );
            } else {
                lbl.setStyle(
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 16px;" +
                    "-fx-background-color: black;"
                );
            }
        }
    }
}
