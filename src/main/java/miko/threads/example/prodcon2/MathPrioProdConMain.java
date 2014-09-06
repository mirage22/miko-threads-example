package miko.threads.example.prodcon2;

import miko.threads.example.prodcon2.main.MathConsumer;
import miko.threads.example.prodcon2.main.MathEvent;
import miko.threads.example.prodcon2.main.MathPriorityTransferQueue;
import miko.threads.example.prodcon2.main.MathProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by miroslavkopecky on 05/09/14.
 *
 * Producer/Consumer Problem solution with PriorityTransfer Queue
 * Queue represents the Mathematical Calculations
 */
public class MathPrioProdConMain {

    private static final Logger logger = LoggerFactory.getLogger(MathPrioProdConMain.class);

    private static final int NUMBER_CONSUMER = 2;

    private static final int NUMBER_PRODUCERS= 5;
    private static final int NUMBER_PRODUCER_EVENTS= 200;

    public static void main(String... args){
        MathPriorityTransferQueue<MathEvent> buffer = new MathPriorityTransferQueue<>();

        /*
         * Launch NUMBER_PRODUCER
         */
        Thread[] producerThread = new Thread[NUMBER_PRODUCERS];
        for(int i=0; i< NUMBER_PRODUCERS; i++){
            producerThread[i] = new Thread(new MathProducer(NUMBER_PRODUCER_EVENTS, buffer));
            producerThread[i].start();
        }

        /*
         * Create MathConsumers
         */
        Thread[] consumerThread = new Thread[NUMBER_CONSUMER];
        for(int i=0; i<NUMBER_CONSUMER; i++){
            consumerThread[i] = new Thread(new MathConsumer(buffer));
            consumerThread[i].start();
        }

        logger.debug("MathPrioProdConMain: Buffer waiting consumers = " + buffer.getWaitingConsumerCount());

        try{
            MathEvent event = new MathEvent("Transfer Math SuperEvent ", 22);
            buffer.transfer(event);

            /*
             * Finalize all MathProducers
             */
            for(int i=0; i<NUMBER_PRODUCERS; i++)
                producerThread[i].join();

            /*
             * Write the actual consumer count
             */
            logger.debug("MathPrioProdConMain: Buffer waiting consumers = " + buffer.getWaitingConsumerCount());

            event = new MathEvent("Transfer Math Calculation Event ", 1);
            buffer.transfer(event);

            for(int i=0; i<NUMBER_CONSUMER; i++)
                consumerThread[i].join();

        } catch (InterruptedException e){
            logger.error("ERROR = " + e);
        }

    }

}
