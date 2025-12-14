package tarefa4;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Filosofo extends Thread {
    private final int id;
    private final Mesa mesa;
    private final Random random = new Random();
    private final SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
    
    private int vezesComeu = 0;
    private long tempoTotalEspera = 0;
    private long tempoTotalComendo = 0;
    private boolean rodando = true;

    public Filosofo(int id, Mesa mesa) {
        this.id = id;
        this.mesa = mesa;
    }
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
                log("está PENSANDO.");
                tempoAleatorio();

                long inicioEspera = System.currentTimeMillis();
                
                log("está FAMINTO e pedindo permissão para COMER (Monitor/Mesa).");
                mesa.pegarGarfos(id); 
                
                long fimEspera = System.currentTimeMillis();
                tempoTotalEspera += (fimEspera - inicioEspera);
                
                log("conseguiu pegar ambos e começou a COMER.");
                long inicioComer = System.currentTimeMillis();
                vezesComeu++;
                tempoAleatorio();
                long fimComer = System.currentTimeMillis();
                tempoTotalComendo += (fimComer - inicioComer);

                log("terminou de comer e SOLTOU os Garfos (Monitor/Mesa).");
                mesa.devolverGarfos(id);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}