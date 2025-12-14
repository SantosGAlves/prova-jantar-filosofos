package tarefa1;

public class Main {
    public static void main(String[] args) {
        int numFilosofos = 5;
        Garfo[] garfos = new Garfo[numFilosofos];
        Filosofo[] filosofos = new Filosofo[numFilosofos];

        for (int i = 0; i < numFilosofos; i++) {
            garfos[i] = new Garfo(i);
        }

        for (int i = 0; i < numFilosofos; i++) {
            Garfo garfoEsquerdo = garfos[i];
            Garfo garfoDireito = garfos[(i + 1) % numFilosofos];
            
            
            filosofos[i] = new Filosofo(i, garfoEsquerdo, garfoDireito);
        }
        
        System.out.println("Iniciando Jantar dos Filósofos (Com Deadlock)...");
        
        // Inicia as threads
        for (Filosofo f : filosofos) {
            f.start();
        }
        
    }
}