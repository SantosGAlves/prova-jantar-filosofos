package tarefa1;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Filosofo extends Thread {
    private final int id;
    private final Garfo garfoEsquerdo;
    private final Garfo garfoDireito;
    private final Random random = new Random();
    private final SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");

    public Filosofo(int id, Garfo esquerdo, Garfo direito) {
        this.id = id;
        this.garfoEsquerdo = esquerdo;
        this.garfoDireito = direito;
    }

    private void log(String acao) {
        System.out.println("[" + df.format(new Date()) + "] Filósofo " + id + " " + acao);
    }

    @Override
    public void run() {
        try {
            while (true) {
                // 1. Pensar (Muito rápido para voltar logo pra mesa)
                log("está pensando.");
                Thread.sleep(random.nextInt(100)); 

                // 2. Tentar pegar garfos
                log("está com fome e tentando pegar o garfo esquerdo (" + garfoEsquerdo.getId() + ").");
                
                synchronized (garfoEsquerdo) {
                    log("pegou o garfo esquerdo (" + garfoEsquerdo.getId() + ").");
                    
                    // FORÇANDO O DEADLOCK:
                    // Segura o garfo esquerdo por 3 segundos antes de tentar o direito.
                    // Isso dá tempo para TODOS os outros pegarem seus garfos esquerdos também.
                    log("está segurando o garfo esquerdo e esperando um pouco..."); 
                    Thread.sleep(3000); 

                    log("tentando pegar o garfo direito (" + garfoDireito.getId() + ").");
                    
                    synchronized (garfoDireito) {
                        // 3. Comer
                        log("pegou o garfo direito e começou a COMER.");
                        Thread.sleep(1000); // Come por 1 segundo
                        
                        log("terminou de comer e soltou os garfos.");
                    }
                }
            }
        } catch (InterruptedException e) {
            log("foi interrompido.");
            Thread.currentThread().interrupt();
        }
    }
}