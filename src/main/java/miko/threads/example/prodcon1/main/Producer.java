package miko.threads.example.prodcon1.main;

import miko.threads.example.prodcon1.util.CharacterLines;

/**
 * Created by miroslavkopecky on 04/09/14
 *
 * Read Simulated line from CharacterLines and store in into SharedBuffer
 * only if there is space for it.
 *
 */
public class Producer implements Runnable{

    private CharacterLines characterLines;

    private SharedBuffer buffer;

    public Producer(CharacterLines characterLines, SharedBuffer buffer) {
        this.characterLines = characterLines;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        buffer.setPendingLines(true);
        while(characterLines.hasMoreLines()){
            buffer.insertLine(characterLines.getNextLine());
        }
        buffer.setPendingLines(false);
    }
}
