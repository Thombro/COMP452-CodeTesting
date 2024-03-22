public class GuessAI {
    private int numGuesses;
    private int lastGuess;

    // upperBound and lowerBound track the computer's knowledge about the correct number
    // They are updated after each guess is made
    private int upperBound; // correct number is <= upperBound
    private int lowerBound; // correct number is >= lowerBound

    public GuessAI(){
        numGuesses = 0;
        upperBound = 1000;
        lowerBound = 1;
        lastGuess = (lowerBound + upperBound + 1) / 2;
    }
    public void reset(){
        numGuesses = 0;
        upperBound = 1000;
        lowerBound = 1;
        lastGuess = (lowerBound + upperBound + 1) / 2;
    }
    public int getNumGuesses() {
        return numGuesses;
    }

    /**
     * used both to get the initial guess, and to get the correct guess when successful
     * @return the last guess made
     */
    public int getLastGuess() {
        return lastGuess;
    }

    public int nextGuess(boolean wasHigher){
        if(wasHigher) lowerBound = Math.max(lowerBound, lastGuess + 1);
        else upperBound = Math.min(upperBound, lastGuess);
        lastGuess = (lowerBound + upperBound + 1) / 2;
        numGuesses += 1;
        return lastGuess;
    }
}
