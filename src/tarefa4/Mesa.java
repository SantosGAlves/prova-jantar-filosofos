package tarefa4;

public class Mesa {

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

    public synchronized void pegarGarfos(int id) throws InterruptedException {
        estados[id] = FAMINTO; 
        

        testar(id);


        while (estados[id] != COMENDO) {
            wait(); 
        }
    }


    public synchronized void devolverGarfos(int id) {
        estados[id] = PENSANDO;


        int esquerda = (id + numFilosofos - 1) % numFilosofos;
        int direita = (id + 1) % numFilosofos;

        testar(esquerda);
        testar(direita);
    }


    private void testar(int id) {
        int esquerda = (id + numFilosofos - 1) % numFilosofos;
        int direita = (id + 1) % numFilosofos;

        if (estados[id] == FAMINTO && 
            estados[esquerda] != COMENDO && 
            estados[direita] != COMENDO) {
            
            estados[id] = COMENDO;

            notifyAll(); 
        }
    }
}