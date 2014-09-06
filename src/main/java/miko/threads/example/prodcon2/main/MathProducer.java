package miko.threads.example.prodcon2.main;

import java.util.Random;

/**
 * Created by miroslavkopecky on 05/09/14.
 */
public class MathProducer implements Runnable{

    private int maxMathEvents;

    private MathPriorityTransferQueue<MathEvent> buffer;

    public MathProducer(int maxMathEvents, MathPriorityTransferQueue<MathEvent> buffer){
        this.maxMathEvents = maxMathEvents;
        this.buffer = buffer;
    }

    /**
     * Store maxMathEvents events into Buffer with incremental priority
     */
    @Override
    public void run() {
        for(int i=0; i< maxMathEvents; i++){
            Random random=new Random();
            int priority= random.nextInt(100);
            MathEvent event = new MathEvent("MathProducer-" + Thread.currentThread().getName(), priority);
            buffer.put(event);
        }
    }

}
