package tarefa2;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Main {
    
    // Calcula o Coeficiente de Variação (CV)
    private static double calcularCoeficienteVariacao(List<Filosofo> filosofos) {
        List<Integer> refeicoes = filosofos.stream()
            .map(Filosofo::getVezesComeu)
            .collect(Collectors.toList());

        if (refeicoes.isEmpty()) return 0.0;

        double media = refeicoes.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        
        if (media == 0.0) return 0.0;

        double somaDiferencasQuadrado = refeicoes.stream()
            .mapToDouble(r -> Math.pow(r - media, 2))
            .sum();

        double desvioPadrao = Math.sqrt(somaDiferencasQuadrado / refeicoes.size());

        // CV = (Desvio Padrão / Média) * 100
        return (desvioPadrao / media) * 100;
    }

    public static void main(String[] args) {
        final long TEMPO_EXECUCAO_MS = 300000; // 5 minutos
        int numFilosofos = 5;
        Garfo[] garfos = new Garfo[numFilosofos];
        Filosofo[] filosofos = new Filosofo[numFilosofos];

        for (int i = 0; i < numFilosofos; i++) garfos[i] = new Garfo(i);

        for (int i = 0; i < numFilosofos; i++) {
            Garfo esquerda = garfos[i];
            Garfo direita = garfos[(i + 1) % numFilosofos];
            if (i == numFilosofos - 1) filosofos[i] = new Filosofo(i, direita, esquerda);
            else filosofos[i] = new Filosofo(i, esquerda, direita);
        }

        System.out.println("=== Tarefa 2: Prevenção de Deadlock Iniciada (5 min) ===");
        for (Filosofo f : filosofos) f.start();

        // Agendador para parar as threads após 5 minutos
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            for (Filosofo f : filosofos) f.parar();
            scheduler.shutdown();
        }, TEMPO_EXECUCAO_MS, TimeUnit.MILLISECONDS);


        // Espera todas as threads finalizarem
        try {
            for (Filosofo f : filosofos) f.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // COLETANDO MÉTRICAS
        long totalTempoComendo = 0;
        long totalRefeicoes = 0;
        long totalTempoEspera = 0;

        System.out.println("\n=== RESULTADOS TAREFA 2 ===");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("| %-2s | %-10s | %-10s | %-15s | %-10s | %-15s |%n", 
            "ID", "Refeições", "T. Total Esp. (ms)", "T. Médio Esp. (ms)", "T. Total Com. (ms)", "Taxa Util. (%)");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");

        for (Filosofo f : filosofos) {
            long tempoTotalEspera = f.getTempoTotalEspera();
            int refeicoes = f.getVezesComeu();
            long tempoTotalComendo = f.getTempoTotalComendo();
            
            double tempoMedioEspera = (refeicoes > 0) ? (double) tempoTotalEspera / refeicoes : 0.0;
            double taxaUtilizacao = (double) tempoTotalComendo / TEMPO_EXECUCAO_MS * 100;

            System.out.printf("| %-2d | %-10d | %-18d | %-18.2f | %-17d | %-14.2f |%n", 
                f.getFilosofoId(), refeicoes, tempoTotalEspera, tempoMedioEspera, tempoTotalComendo, taxaUtilizacao);

            totalTempoComendo += tempoTotalComendo;
            totalRefeicoes += refeicoes;
            totalTempoEspera += tempoTotalEspera;
        }
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");

        // MÉTRICAS GLOBAIS
        double cv = calcularCoeficienteVariacao(List.of(filosofos));
        double utilizacaoGarfosMedia = (totalTempoComendo / (double) numFilosofos) / TEMPO_EXECUCAO_MS * 100;

        System.out.printf("Refeições Totais: %d%n", totalRefeicoes);
        System.out.printf("Coeficiente de Variação (Fairness): %.2f%%%n", cv);
        System.out.printf("Taxa Média de Utilização de Garfos (Todos Garfos / 5 min): %.2f%%%n", utilizacaoGarfosMedia);
    }
}