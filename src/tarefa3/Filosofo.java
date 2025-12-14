package tarefa3;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Filosofo extends Thread {
    private final int id;
    private final Garfo garfoEsquerdo;
    private final Garfo garfoDireito;
    private final Semaphore mesa;
    private final Random random = new Random();
    private final SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
    

    private int vezesComeu = 0;
    private long tempoTotalEspera = 0;
    private long tempoTotalComendo = 0;
    private boolean rodando = true;

    public Filosofo(int id, Garfo esquerdo, Garfo direito, Semaphore mesa) {
        this.id = id;
        this.garfoEsquerdo = esquerdo;
        this.garfoDireito = direito;
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
                

                log("está FAMINTO e tentando ACESSAR A MESA (Semáforo).");
                mesa.acquire();
                
                try {

                    log("pegou a MESA. Tentando pegar Garfo " + garfoEsquerdo.getId() + " (Esq).");
                    synchronized (garfoEsquerdo) {

                        log("pegou Garfo " + garfoEsquerdo.getId() + ". Tentando pegar Garfo " + garfoDireito.getId() + " (Dir).");
                        synchronized (garfoDireito) {
                            long fimEspera = System.currentTimeMillis();
                            tempoTotalEspera += (fimEspera - inicioEspera);

                            log("conseguiu pegar ambos e começou a COMER.");
                            long inicioComer = System.currentTimeMillis();
                            vezesComeu++;
                            tempoAleatorio();
                            long fimComer = System.currentTimeMillis();
                            tempoTotalComendo += (fimComer - inicioComer);
                        }
                    }
                } finally {
                    log("terminou de comer e SOLTOU os Garfos e a MESA.");
                    mesa.release(); 
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}