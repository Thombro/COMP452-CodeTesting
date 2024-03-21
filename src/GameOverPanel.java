import com.opencsv.CSVWriter;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Panel displays after a game has concluded
 * Displays the game outcome
 * Writes the results to file (if human was guessing)
 *
 * TODO: Refactor the setGameResults method. Leave the rest of this file unchanged.
 */
public class GameOverPanel extends JPanel {

    private GameResult gameResult;

    private JLabel answerTxt;
    private JLabel numGuessesTxt;

    public GameOverPanel(JPanel cardsPanel){
        this.gameResult = null;

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JLabel title = new JLabel("Game Finished");
        this.add(title);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.add(Box.createRigidArea(new Dimension(0,20)));

        answerTxt = new JLabel("The answer was ___.");
        this.add(answerTxt);
        answerTxt.setAlignmentX(Component.CENTER_ALIGNMENT);

        numGuessesTxt = new JLabel("It took ___ ___ guesses.");
        this.add(numGuessesTxt);
        numGuessesTxt.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.add(Box.createRigidArea(new Dimension(0,60)));

        JPanel buttonPanel = new JPanel();

        JButton restart = new JButton("Play Again");
        restart.addActionListener(e -> {
            // See itemStateChanged in https://docs.oracle.com/javase/tutorial/uiswing/examples/layout/CardLayoutDemoProject/src/layout/CardLayoutDemo.java
            CardLayout cardLayout = (CardLayout) cardsPanel.getLayout();
            String screenName = (gameResult == null || gameResult.humanWasPlaying ?
                    ScreenID.HUMAN_PLAY.name() : ScreenID.COMPUTER_PLAY_LAUNCH.name());
            cardLayout.show(cardsPanel, screenName);
        });
        buttonPanel.add(restart);

        JButton quit = new JButton("Back to Home");
        quit.addActionListener(e -> {
            // See itemStateChanged in https://docs.oracle.com/javase/tutorial/uiswing/examples/layout/CardLayoutDemoProject/src/layout/CardLayoutDemo.java
            CardLayout cardLayout = (CardLayout) cardsPanel.getLayout();
            cardLayout.show(cardsPanel, ScreenID.HOME.name());
        });
        buttonPanel.add(quit);

        this.add(buttonPanel);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    /**
     * Sets the game results and updates the UI
     */
    public void setGameResults(GameResult result){
        setGameResults(result, answerTxt, numGuessesTxt);
    }

    /**
     * sets the game results and sends them to the UI
     * @param answer the text label that displays the correct value
     * @param guesses the text label that displays the number of guesses made
     */
    // I would have preferred to be able to test the
    // text sent to the UI separately from setGameResults,
    // but I can't edit the second lambda, so I had to compromise
    public void setGameResults(GameResult result, JLabel answer, JLabel guesses){
        this.gameResult = result;

        applyUIText(answer, guesses);
    }

    /**
     * writes game results to class StatsFile's static FILENAME field
     */
    public void writeGameResults(){
        try(CSVWriter writer = new CSVWriter(new FileWriter(StatsFile.FILENAME, true))) {
            writeGameResults(writer);
        } catch (IOException e) {
            // NOTE: In a full implementation, we would log this error and possibly alert the user
            // NOTE: For this project, you do not need unit tests for handling this exception.
        }
    }
    /**
     * writes game results to the writer parameter
     */
    public void writeGameResults(CSVWriter writer){
        String [] record = new String[2];
        record[0] = LocalDateTime.now().toString();
        record[1] = Integer.toString(gameResult.numGuesses);

        writer.writeNext(record);
    }

    /**
     * writes the text
     * @param answer the text label that displays the correct value
     * @param guesses the text label that displays the number of guesses made
     */
    private void applyUIText(JLabel answer, JLabel guesses){
        // no need to be able to test this trivial code
        answer.setText("The answer was " + gameResult.correctValue + ".");
        // the below can be tested by just running generateGuessText()
        guesses.setText(generateGuessText());
    }

    /**
     * generates the text for the number of guesses made
     * this allows for testing the text generation without involving UI
     * @return the text to be put into numGuessesTxt
     */
    public String generateGuessText(){
        if(gameResult.numGuesses == 1){
            return (gameResult.humanWasPlaying ? "You" : "I") + " guessed it on the first try!";
        }
        else {
            return "It took " + (gameResult.humanWasPlaying ? "you" : "me") + " " + gameResult.humanWasPlaying + " guesses.";
        }
    }
}
