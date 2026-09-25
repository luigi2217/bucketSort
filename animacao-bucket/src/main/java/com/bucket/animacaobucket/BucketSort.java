package com.bucket.animacaobucket;

public class BucketSort {
    private int[] vet;
    private int TF;
    private int TL;

    public BucketSort(int tamanho) {
        this.vet = new int[tamanho];
        TL = 0;
        TF = tamanho;
    }

    public void inserir(int valor) {
        vet[TL] = valor;
        TL++;
    }

    public void exibir() {
        for (int i = 0; i < TL; i++)
            System.out.print(vet[i] + " ");
        System.out.println();
    }

    // BucketSort
    public void bucketSort(int nBaldes) {
        int maior = acharMaior();
        int[][] baldes = new int[nBaldes][TL];
        int[] tamBalde = new int[nBaldes];

        distribuir(baldes, tamBalde, maior, nBaldes);

        for (int i = 0; i < nBaldes; i++)
            insertionSort(baldes[i], tamBalde[i]);

        juntar(baldes, tamBalde, nBaldes);
    }

    private int acharMaior(){
        int maior = vet[0];
        for (int i = 1; i < TL; i++){
            if(maior < vet[i])
                maior = vet[i];
        }
        return maior;
    }

    private void distribuir(int[][] baldes, int[] tamBalde, int maior, int nBaldes) {
        for (int i = 0; i < TL; i++) {
            int indice = vet[i] * nBaldes / (maior + 1);
            baldes[indice][tamBalde[indice]] = vet[i];
            tamBalde[indice]++;
        }
    }

    private void insertionSort(int[] balde, int tam) {
        for (int i = 1; i < tam; i++) {
            int key = balde[i];
            int j = i - 1;
            while (j >= 0 && balde[j] > key) {
                balde[j + 1] = balde[j];
                j--;
            }
            balde[j + 1] = key;
        }
    }

    private void juntar(int[][] baldes, int[] tamBalde, int nBaldes) {
        int k = 0;
        for (int b = 0; b < nBaldes; b++) {
            for (int i = 0; i < tamBalde[b]; i++) {
                vet[k] = baldes[b][i];
                k++;
            }
        }
    }

}