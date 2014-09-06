package miko.threads.example.prodcon2.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by miroslavkopecky on 05/09/14.
 */
public class MathConsumer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MathConsumer.class);

    private MathPriorityTransferQueue<MathEvent> buffer;

    public MathConsumer(MathPriorityTransferQueue<MathEvent> buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while(buffer.peek() != null){
            try{
                MathEvent event = buffer.take();
                logger.debug("MathConsumer-" + Thread.currentThread().getName() +
                        " : " + event.getThread() + " priority: " + event.getPriority());
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
