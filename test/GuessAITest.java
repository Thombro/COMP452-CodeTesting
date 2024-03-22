import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuessAITest {
    // tolerance used for floating point equality checks
    final double tol = 1E-9;

    @Test
    void initialGuess() {
        GuessAI ai = new GuessAI();
        assertEquals(501, ai.getLastGuess());
    }

    @Test
    void highGuess() {
        GuessAI ai = new GuessAI();
        int val = 999;
        boolean foundVal = val == ai.getLastGuess();
        //since this *should* be able to guess any number 1-1000 within 10 guesses, I'm giving it that many attempts
        for(int i=1; i<10; i++){
            foundVal = val == ai.nextGuess(val > ai.getLastGuess());
            if(foundVal) break;
        }
        assertEquals(val, ai.getLastGuess());
    }

    @Test
    void lowGuess() {
        GuessAI ai = new GuessAI();
        int val = 1;
        boolean foundVal = val == ai.getLastGuess();
        //since this *should* be able to guess any number 1-1000 within 10 guesses, I'm giving it that many attempts
        for(int i=1; i<10; i++){
            foundVal = val == ai.nextGuess(val > ai.getLastGuess());
            if(foundVal) break;
        }
        assertEquals(val, ai.getLastGuess());
    }

    @Test
    void arbitraryGuess() {
        GuessAI ai = new GuessAI();
        int val = 814;
        boolean foundVal = val == ai.getLastGuess();
        //since this *should* be able to guess any number 1-1000 within 10 guesses, I'm giving it that many attempts
        for(int i=1; i<10; i++){
            foundVal = val == ai.nextGuess(val > ai.getLastGuess());
            if(foundVal) break;
        }
        assertTrue(foundVal);
    }

    @Test
    void countMoves() {
        GuessAI ai = new GuessAI();
        int val = 814;
        boolean foundVal = val == ai.getLastGuess();
        //since this *should* be able to guess any number 1-1000 within 10 guesses, I'm giving it that many attempts
        for(int i=2; i<=10; i++){
            foundVal = val == ai.nextGuess(val > ai.getLastGuess());
            if(foundVal) {
                assertEquals(i, ai.getNumGuesses());
            }
        }
    }
}