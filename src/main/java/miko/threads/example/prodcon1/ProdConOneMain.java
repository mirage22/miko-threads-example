package miko.threads.example.prodcon1;

import miko.threads.example.prodcon1.main.Consumer;
import miko.threads.example.prodcon1.main.Producer;
import miko.threads.example.prodcon1.main.SharedBuffer;
import miko.threads.example.prodcon1.util.CharacterLines;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by miroslavkopecky on 04/09/14.
 *
 * The lock itself maybe associated with 1 > more conditions. These Conditions are defined in the Conditions interface
 * Conditions allow to have a control of a lock. If condition is not fulfilled then threads are suspended until
 * another thread wakes them up.
 *
 * inspired by JavaConcurrency Book
 *
 */
public class ProdConOneMain {

    private static final int CONSUMER_NUMBER = 2;
    private static final int BUFFER_SIZE = 2;
    private static final int MAX_LINES = 10;
    private static final int MAX_LINE_CHARACTERS = 10;

    private static final Logger logger = LoggerFactory.getLogger(ProdConOneMain.class);

    public static void main(String... args){
        logger.debug("Start ProdCon One Example");

        /**
         * Create MAX_LINES lines of MAX_LINE_CHARACTERS
         */
        CharacterLines characterLines = new CharacterLines(MAX_LINES, MAX_LINE_CHARACTERS);

        /**
         * Shared buffer is limited to store max BUFFER_SIZE lines to show up how
         * Conditions are treated.
         */
        SharedBuffer buffer = new SharedBuffer(BUFFER_SIZE);

        Producer producer = new Producer(characterLines, buffer);
        Thread producerThread = new Thread(producer, "Producer ");

        /**
         * Create NUMBER of consumers of provided lines in SharedBuffer
         */
        Thread[] consumerThreads = new Thread[CONSUMER_NUMBER];

        for(int i=0; i<CONSUMER_NUMBER; i++){
            consumerThreads[i] = new Thread(new Consumer(buffer) ,"Consumer " + i);
        }

        /**
         * Starts the producer and the consumers
         */
        producerThread.start();
        for(int i=0; i < CONSUMER_NUMBER; i++){
            consumerThreads[i].start();
        }

    }
}
