package miko.threads.example.prodcon1.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by miroslavkopecky on 04/09/14.
 *
 * This class implements a shared buffer to store character lines between producer and
 * customer
 *
 */
public class SharedBuffer {

    private static final Logger logger = LoggerFactory.getLogger(SharedBuffer.class);

    /**
     * Buffer is LinkedList which is not Synchronized!
     * Check the log.
     *
     */
    private LinkedList<String> buffer;

    private int maxSize;

    /**
     * ReentrantLock to control access to the buffer
     */
    private ReentrantLock lock;

    /**
     * Define conditions for the buffer empty space and lines
     */
    private Condition lines;
    private Condition space;

    /**
     * control pending lines in the buffer
     */
    private boolean pendingLines;

    /**
     * Constructor of the class. Initialize all the objects
     */
    public SharedBuffer(int maxSize){
        this.maxSize = maxSize;
        buffer = new LinkedList<>();
        lock = new ReentrantLock();
        lines = lock.newCondition();
        space = lock.newCondition();
        pendingLines = true;
    }

    public void insertLine(String line){
        lock.lock();

        try{
            while(buffer.size() == maxSize){
                space.await();
            }
            buffer.offer(line);
            logger.debug(Thread.currentThread()
                    .getName() + ": BUFFER Inserted Line: " + buffer.size());
            lines.signalAll();

        } catch (InterruptedException e){
            logger.error(e.toString());
        } finally {
            lock.unlock();
        }
    }

    public  String getLine(){
        String result = null;
        lock.lock();

        try{
            while((buffer.size() == 0) && (hasPendingLines())){
                lines.await();
            }

            if(hasPendingLines()){
                result = buffer.poll();
                logger.debug(Thread.currentThread().getName() + ": BUFFER Line to Read: " + buffer.size());
                space.signalAll();
            }

        }catch (InterruptedException e){
            logger.error(e.toString());
        } finally {
            lock.unlock();
        }

        return result;

    }

    /*
     * Control mechanism for Producer and Consumer
     */
    public void setPendingLines(boolean status){
        this.pendingLines = status;
    }

    public boolean hasPendingLines(){
        return pendingLines || buffer.size() > 0;
    }

}
