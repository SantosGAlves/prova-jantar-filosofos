package tarefa4;

public class Mesa {
    // Estados possíveis de um filósofo
    private static final int PENSANDO = 0;
    private static final int FAMINTO = 1;
    private static final int COMENDO = 2;

    private final int[] estados;
    private final int numFilosofos;

    public Mesa(int numFilosofos) {
        this.numFilosofos = numFilosofos;
        this.estados = new int[numFilosofos];
        for (int i = 0; i < numFilosofos; i++) {
            estados[i] = PENSANDO;
        }
    }

    // Chamado pelo filósofo quando quer comer
    public synchronized void pegarGarfos(int id) throws InterruptedException {
        estados[id] = FAMINTO; // Declara que quer comer
        
        // Tenta adquirir os garfos
        testar(id);

        // Se não conseguiu comer, espera.
        // O uso de while é obrigatório para evitar "spurious wakeups"
        while (estados[id] != COMENDO) {
            wait(); // Espera até que algum vizinho solte os garfos e avise
        }
    }

    // Chamado pelo filósofo quando termina de comer
    public synchronized void devolverGarfos(int id) {
        estados[id] = PENSANDO;

        // Ao soltar os garfos, avisa os vizinhos (Esquerda e Direita)
        // para ver se eles querem comer agora.
        int esquerda = (id + numFilosofos - 1) % numFilosofos;
        int direita = (id + 1) % numFilosofos;

        testar(esquerda);
        testar(direita);
    }

    // Verifica se o filósofo 'id' pode comer
    private void testar(int id) {
        int esquerda = (id + numFilosofos - 1) % numFilosofos;
        int direita = (id + 1) % numFilosofos;

        // Só pode comer se:
        // 1. Ele estiver FAMINTO
        // 2. O vizinho da esquerda NÃO estiver comendo
        // 3. O vizinho da direita NÃO estiver comendo
        if (estados[id] == FAMINTO && 
            estados[esquerda] != COMENDO && 
            estados[direita] != COMENDO) {
            
            estados[id] = COMENDO;
            
            // Avisa todas as threads que o estado da mesa mudou.
            // Isso acorda quem estava no wait() para verificar se agora pode comer.
            notifyAll(); 
        }
    }
}