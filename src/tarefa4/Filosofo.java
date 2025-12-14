package tarefa4;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Filosofo extends Thread {
    private final int id;
    private final Mesa mesa;
    private final Random random = new Random();
    private final SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
    
    // Métricas
    private int vezesComeu = 0;
    private long tempoTotalEspera = 0;
    private long tempoTotalComendo = 0;
    private boolean rodando = true;

    public Filosofo(int id, Mesa mesa) {
        this.id = id;
        this.mesa = mesa;
    }

    // Métodos para Métricas (getters)
    public int getFilosofoId() { return id; }
    public int getVezesComeu() { return vezesComeu; }
    public long getTempoTotalEspera() { return tempoTotalEspera; }
    public long getTempoTotalComendo() { return tempoTotalComendo; }

    public void parar() { this.rodando = false; }

    private void log(String acao) {
        if (Thread.currentThread().isAlive() && !rodando) return; 
        System.out.println("[" + df.format(new Date()) + "] Filósofo " + id + " " + acao);
    }

    private void tempoAleatorio() throws InterruptedException {
        int tempo = 1000 + random.nextInt(2001);
        Thread.sleep(tempo);
    }

    @Override
    public void run() {
        try {
            while (rodando) {
                // 1. Pensar
                log("está PENSANDO.");
                tempoAleatorio();

                long inicioEspera = System.currentTimeMillis();
                
                // 2. Pedir para comer (Monitor bloqueia aqui se necessário)
                log("está FAMINTO e pedindo permissão para COMER (Monitor/Mesa).");
                mesa.pegarGarfos(id); // Bloqueia se vizinhos estiverem comendo
                
                long fimEspera = System.currentTimeMillis();
                tempoTotalEspera += (fimEspera - inicioEspera);

                // Se o método retornou, ele está COMENDO
                
                // 3. Comer
                log("conseguiu pegar ambos e começou a COMER.");
                long inicioComer = System.currentTimeMillis();
                vezesComeu++;
                tempoAleatorio();
                long fimComer = System.currentTimeMillis();
                tempoTotalComendo += (fimComer - inicioComer);

                // 4. Devolver
                log("terminou de comer e SOLTOU os Garfos (Monitor/Mesa).");
                mesa.devolverGarfos(id);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}