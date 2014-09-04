package miko.threads.example.prodcon1.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by miroslavkopecky on 04/09/14.
 *
 * create defined number of lines. Each line contains randomly generated characters
 *
 */
public class CharacterLines {

    private static final Logger logger = LoggerFactory.getLogger(CharacterLines.class);

    private String[] lines;

    private int index;

    /**
     * Prepare lines with random characters
     * @param size - number of lines
     * @param length - length of line
     */
    public CharacterLines(int size, int length) {
        lines = new String[size];
        for(int i = 0; i < size; i++){
            StringBuilder builder = new StringBuilder(length);
            for(int j=0; j < length; j++){
                int characterIndicator = (int)(Math.random() * 255);
                builder.append((char)characterIndicator);
            }
            lines[i] = builder.toString();
        }
        /*
         * Index of the current line
         */
        index = 0;
    }

    //Helper functions
    public boolean hasMoreLines(){
        return index < lines.length;
    }

    public String getNextLine(){
        String result = null;
        if(hasMoreLines()){
            logger.debug("Waiting Lines: " + (lines.length - index));
            result = lines[index++];
        }
        return result;
    }
}
