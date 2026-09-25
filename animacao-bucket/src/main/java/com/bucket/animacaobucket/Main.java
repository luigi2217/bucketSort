package com.bucket.animacaobucket;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random rand = new Random();

        int tamanho = 9;
        int nBaldes = 3;

        BucketSort bs = new BucketSort(tamanho);

        for (int i = 0; i < tamanho; i++)
            bs.inserir(rand.nextInt(100));

        System.out.println("Tamanho: " + tamanho + " | Baldes: " + nBaldes);
        System.out.print("Antes:  ");
        bs.exibir();

        bs.bucketSort(nBaldes);

        System.out.print("Depois: ");
        bs.exibir();
    }
}
