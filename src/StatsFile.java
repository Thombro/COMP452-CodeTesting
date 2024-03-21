import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * File-backed implementation of GameStats
 *
 * Returns the number of games *within the last 30 days* where the person took a given number of guesses
 */
public class StatsFile extends GameStats {
    public static final String FILENAME = "guess-the-number-stats.csv";


    // maps the number of guesses required to the number of games within
    // the past 30 days where the person took that many guesses
    private SortedMap<Integer, Integer> statsMap;

    public StatsFile(){
        statsMap = new TreeMap<>();
        LocalDateTime limit = LocalDateTime.now().minusDays(30);

        try (CSVReader csvReader = new CSVReader(new FileReader(FILENAME))) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                // values should have the date and the number of guesses as the two fields
                try {
                    LocalDateTime timestamp = LocalDateTime.parse(values[0]);
                    int numGuesses = Integer.parseInt(values[1]);

                    if (timestamp.isAfter(limit)) {
                        statsMap.put(numGuesses, 1 + statsMap.getOrDefault(numGuesses, 0));
                    }
                }
                catch(NumberFormatException nfe){
                    // NOTE: In a full implementation, we would log this error and possibly alert the user
                    throw nfe;
                }
                catch(DateTimeParseException dtpe){
                    // NOTE: In a full implementation, we would log this error and possibly alert the user
                    throw dtpe;
                }
            }
        } catch (CsvValidationException e) {
            // NOTE: In a full implementation, we would log this error and alert the user
            // NOTE: For this project, you do not need unit tests for handling this exception.
        } catch (IOException e) {
            // NOTE: In a full implementation, we would log this error and alert the user
            // NOTE: For this project, you do not need unit tests for handling this exception.
        }
    }

    @Override
    public int numGames(int numGuesses) {
        return statsMap.getOrDefault(numGuesses, 0);
    }

    @Override
    public int maxNumGuesses(){
        return (statsMap.isEmpty() ? 0 : statsMap.lastKey());
    }

    /*
    ==========================================
    THE BELOW ARE RELOCATED FROM GameOverPanel
    ==========================================
     */

    public static void writeRecord(int numGuesses){
        try(CSVWriter writer = new CSVWriter(new FileWriter(StatsFile.FILENAME, true))) {
            writeRecord(numGuesses, LocalDateTime.now().toString(), writer);
        } catch (IOException e) {
            // NOTE: In a full implementation, we would log this error and possibly alert the user
            // NOTE: For this project, you do not need unit tests for handling this exception.
        }
    }

    public static void writeRecord(int numGuesses, String time, CSVWriter writer){
        String [] record = new String[2];
        record[0] = time;
        record[1] = Integer.toString(numGuesses);

        writer.writeNext(record);
    }
}
