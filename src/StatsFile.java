import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import javax.swing.*;
import java.io.FileNotFoundException;
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

/*    public StatsFile(){
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
    }*/

    public StatsFile() {
        this(new TreeMap<>());
    }

    StatsFile(SortedMap<Integer, Integer> statsMap) {
        this.statsMap = statsMap;
        loadStatsFromFile();
    }

    private void loadStatsFromFile() {
        LocalDateTime limit = LocalDateTime.now().minusDays(30);

        try (CSVReader csvReader = new CSVReader(new FileReader(FILENAME))) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                try {
                    LocalDateTime timestamp = parseTimestamp(values[0]);
                    int numGuesses = Integer.parseInt(values[1]);

                    if (timestamp != null && timestamp.isAfter(limit)) {
                        statsMap.put(numGuesses, 1 + statsMap.getOrDefault(numGuesses, 0));
                    }
                } catch(NumberFormatException nfe){
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

    LocalDateTime parseTimestamp(String timestampStr) {
        try {
            return LocalDateTime.parse(timestampStr);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public void loadStatsFromFile(CSVReader csvReader) throws IOException, CsvValidationException {
        String[] values;
        while ((values = csvReader.readNext()) != null) {
            try {
                LocalDateTime timestamp = parseTimestamp(values[0]);
                int numGuesses = Integer.parseInt(values[1]);

                if (timestamp != null && timestamp.isAfter(LocalDateTime.now().minusDays(30))) {
                    statsMap.put(numGuesses, 1 + statsMap.getOrDefault(numGuesses, 0));
                }
            } catch(NumberFormatException | DateTimeParseException e) {
                throw e;
            }
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

    /*
    ==========================================
    THE BELOW ARE RELOCATED FROM StatsPanel
    ==========================================
     */

    public int[] getResults(int[] binsEdges) {
        int[] results = new int[binsEdges.length];

        for(int binIndex=0; binIndex<binsEdges.length; binIndex++){
            final int lowerBound = binsEdges[binIndex];

            results[binIndex] = binIndex == binsEdges.length-1 ? getBinSum(lowerBound, maxNumGuesses()-1) : getBinSum(lowerBound, binsEdges[binIndex+1]);
        }

        return results;
    }

    //this method returns that number of games with guess between a lower and upper bound
    private int getBinSum(int lowerBound, int upperBound) {
        int sum = 0;
        for(int numGuesses = lowerBound; numGuesses <= upperBound; numGuesses++) {
            sum += numGames(numGuesses);
        }
        return sum;
    }
}
