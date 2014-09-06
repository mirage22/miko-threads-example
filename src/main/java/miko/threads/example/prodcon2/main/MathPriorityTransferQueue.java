package miko.threads.example.prodcon2.main;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by miroslavkopecky on 05/09/14.
 * - helps to solve the problem Producer/Consumer. Elements are ordered by their priority
 * - counter is AtomicInteger, it stores the number of consumer that are waiting for taking MathEvent from
 * the Data Structure. When a consumer calls take() - counter is incremented. When consumer has finished
 * take() operation, the counter is decremented.
 */
public class MathPriorityTransferQueue<E> extends PriorityBlockingQueue<E> implements TransferQueue<E> {

    private static final long serialVersionUID = 22L;

    /*
     * Represents the number of consumers that are waiting
     * thread-safe atomic implementation. Can't be volatile because Volatile doesn't guarantee atomicity
     * it guaranties only happen-before
     */
    private AtomicInteger counter;

    private LinkedBlockingQueue<E> transferList;

    private ReentrantLock lock;

    public MathPriorityTransferQueue(){
        counter = new AtomicInteger(0);
        lock = new ReentrantLock();
        transferList = new LinkedBlockingQueue<>();
    }

    /**
     * try to transfer MathEvent to a consumer. If there is consumer waiting.
     * Put the MathEvent into the queue and return true else return false.
     */
    @Override
    public boolean tryTransfer(E e) {
        lock.lock();
        boolean result = counter.get() == 0;
        if(result) put(e);
        lock.unlock();
        return result;
    }

    /**
     * Transfer an MathEvent to the consumer. If there is a consumer waiting,
     * puts the MathEvent into the queue and return the true value. Else, puts the
     * value in the transferList queue and returns the false value. In this case, the
     * thread  makes the call will be blocked until a consumer takes the transferList
     * elements
     */
    @Override
    public void transfer(E event) throws InterruptedException{
        lock.lock();
        if(counter.get() != 0){
            put(event);
            lock.unlock();
        } else {
            transferList.add(event);
            lock.unlock();
            synchronized (event){
                event.wait();
            }
        }
    }

    /**
     * This method tries to transfer an MathElement to a consumer waiting a maximum period
     * of time. If there is a consumer waiting, puts the element in the queue. Else,
     * puts the element in the queue of transferList elements and wait the specified period of time
     * until that time pass or the thread is interrupted.
     */
    @Override
    public boolean tryTransfer(E event, long timeout, TimeUnit unit) throws InterruptedException {
        lock.lock();
        if(counter.get() != 0){
            put(event);
            lock.unlock();
            return true;
        } else {
            transferList.add(event);
            long someTimeout = TimeUnit.MILLISECONDS.convert(timeout, unit);
            lock.unlock();
            event.wait(someTimeout);
            lock.lock();
            if(transferList.contains(event)){
                transferList.remove(event);
                lock.unlock();
                return false;
            }else{
                lock.unlock();
                return true;
            }
        }
    }

    /**
     * Method that returns if the queue has waiting consumers
     */
    @Override
    public boolean hasWaitingConsumer(){
        return counter.get() != 0;
    }

    /**
     * Method that returns the number of waiting consumers
     */
    @Override
    public int getWaitingConsumerCount(){
        return counter.get();
    }

    /**
     * Method that returns the first element of the queue or is blocked if the queue
     * is empty. If there is transferList MathEvent, takes the first transferList MathEvent and
     * wake up the thread that is waiting for the transfer of that element else, takes the
     * first element of the queue or is blocked until there is one element in the queue
     */
    @Override
    public E take() throws InterruptedException {
        lock.lock();
        counter.incrementAndGet();
        E result = transferList.poll();

        if(result == null){
            lock.unlock();
            result = super.take();
            lock.lock();
        } else {
            synchronized (result){
                result.notifyAll();
            }
        }
        counter.decrementAndGet();
        lock.unlock();
        return result;
    }

    /**
     * Retrieves, but does not remove, the head of this queue, or returns null if this queue is empty.
     * @return - element MathElement
     */
    @Override
    public E peek() {
        lock.lock();
        E eventMain = super.peek();
        E eventTrans = transferList.peek();
        E result = eventMain != null ? eventMain : eventTrans;
        lock.unlock();
        return result;
    }
}
