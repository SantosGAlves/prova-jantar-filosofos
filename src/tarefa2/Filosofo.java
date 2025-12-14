package tarefa2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Filosofo extends Thread {
    private final int id;
    private final Garfo garfo1;
    private final Garfo garfo2;
    private final Random random = new Random();
    private final SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
    
    private int vezesComeu = 0;
    private long tempoTotalEspera = 0;
    private long tempoTotalComendo = 0;
    private boolean rodando = true;

    public Filosofo(int id, Garfo primeiro, Garfo segundo) {
        this.id = id;
        this.garfo1 = primeiro;
        this.garfo2 = segundo;
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
        int tempo = 1000 + random.nextInt(2001); // 1s a 3s
        Thread.sleep(tempo);
    }

    @Override
    public void run() {
        try {
            while (rodando) {

                log("está PENSANDO.");
                tempoAleatorio();

                long inicioEspera = System.currentTimeMillis();
                

                log("está FAMINTO e tentando pegar Garfo " + garfo1.getId() + " (1º garfo).");
                synchronized (garfo1) {
                    log("pegou Garfo " + garfo1.getId() + ". Tentando pegar Garfo " + garfo2.getId() + " (2º garfo).");
                    synchronized (garfo2) {
                        
                        long fimEspera = System.currentTimeMillis();
                        tempoTotalEspera += (fimEspera - inicioEspera);

                        log("conseguiu pegar ambos e começou a COMER.");
                        long inicioComer = System.currentTimeMillis();
                        vezesComeu++;
                        tempoAleatorio();
                        
                        long fimComer = System.currentTimeMillis();
                        tempoTotalComendo += (fimComer - inicioComer);


                        log("terminou de comer e SOLTOU os Garfos.");
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}