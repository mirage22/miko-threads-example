package miko.threads.example.prodcon2.main;

/**
 * Created by miroslavkopecky on 05/09/14.
 *
 * MathEvent stores attributes of the an event : its MathTaskThread and priority
 * - implements the comparable interface to set priority queue to decide which MathEvent
 * has bigger priority
 */
public class MathEvent implements Comparable<MathEvent>{

    private String thread;

    private int priority;

    /**
     * Constructor of the MathEvent.
     * @param thread - number of the thread that generates the MathEvent
     * @param priority - priority of the MathEvent
     */
    public MathEvent(String thread, int priority) {
        this.thread = thread;
        this.priority = priority;
    }

    public String getThread() {
        return thread;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(MathEvent event) {
        if (this.priority > event.getPriority()) {
            return -1;
        } else if (this.priority <event.getPriority()) {
            return 1;
        } else {
            return 0;
        }
    }

}
