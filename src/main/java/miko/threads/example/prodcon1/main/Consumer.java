package miko.threads.example.prodcon1.main;

import java.util.Random;

/**
 * Created by miroslavkopecky on 04/09/14.
 *
 * Read line from the SharedBuffer and process them.
 * Lines are added by producer
 *
 */
public class Consumer implements Runnable{

    private SharedBuffer buffer;
    private Random random;

    public Consumer(SharedBuffer buffer) {
        this.buffer = buffer;
        this.random = new Random();
    }

    /**
     * continue only if there some pendingLines in the SharedBuffer
     */
    @Override
    public void run() {
        while(buffer.hasPendingLines()){
            processLine(buffer.getLine());
        }
    }

    /*
     * Private Methods
     */

    /**
     * Simulate process of randomly generated Line
     * @param line - Line of Random Charracters
     */
    private void processLine(String line){
        try{
            /*
             * Simulate some processing time to not be so fast :)
             * Let current Thread take a rest
             */
            Thread.sleep(random.nextInt(100));
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
