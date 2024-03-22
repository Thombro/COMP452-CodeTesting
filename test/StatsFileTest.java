import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class StatsFileTest {

    @Test
    void loadStatsFromFile_ValidInput_Success() {
        String csvData = "2024-03-20T10:15:30,3\n" +
                "2024-03-21T09:20:45,4\n" +
                "2024-02-28T15:30:00,5";

        try (CSVReader csvReader = new CSVReader(new StringReader(csvData))) {
            SortedMap<Integer, Integer> statsMap = new TreeMap<>();
            StatsFile statsFile = new StatsFile(statsMap);

            statsFile.loadStatsFromFile(csvReader);

            assertEquals(1, statsMap.get(3));
            assertEquals(1, statsMap.get(4));
            assertEquals(0, statsMap.get(5));
        } catch (IOException | CsvValidationException e) {
            fail("Exception occurred while testing loadStatsFromFile");
        }
    }

    @Test
    void loadStatsFromFile_NumberFormatExceptionThrown_ExceptionHandled() {
        String csvData = "2024-03-20T10:15:30,3\n" +
                "2024-03-21T09:20:45,invalid_number\n" +
                "2024-02-28T15:30:00,5";

        try (CSVReader csvReader = new CSVReader(new StringReader(csvData))) {
            SortedMap<Integer, Integer> statsMap = new TreeMap<>();
            StatsFile statsFile = new StatsFile(statsMap);

            statsFile.loadStatsFromFile(csvReader);

            fail("NumberFormatException should have been thrown");
        } catch (NumberFormatException | IOException | CsvValidationException e) {
            assertNotNull(e);
        }
    }

    @Test
    void loadStatsFromFile_DateTimeParseExceptionThrown_ExceptionHandled() {
        String csvData = "2024-03-20T10:15:30,3\n" +
                "invalid_timestamp,4\n" +
                "2024-02-28T15:30:00,5";

        try (CSVReader csvReader = new CSVReader(new StringReader(csvData))) {
            SortedMap<Integer, Integer> statsMap = new TreeMap<>();
            StatsFile statsFile = new StatsFile(statsMap);

            statsFile.loadStatsFromFile(csvReader);

            fail("DateTimeParseException should have been thrown");
        } catch (DateTimeParseException | IOException | CsvValidationException e) {
            assertNotNull(e);
        }
    }

    @Test
    void writeRecord_ValidInput_Success() {
        int numGuesses = 5;
        LocalDateTime timestamp = LocalDateTime.now();
        StringWriter stringWriter = new StringWriter();
        CSVWriter writer = new CSVWriter(stringWriter);

        StatsFile.writeRecord(numGuesses, timestamp.toString(), writer);

        String expectedRecord = timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "," + numGuesses + "\n";
        assertEquals(expectedRecord, stringWriter.toString());
    }

    @Test
    void getResults_ValidInput_Success() {
        SortedMap<Integer, Integer> statsMap = new TreeMap<>();
        statsMap.put(1, 5);
        statsMap.put(2, 10);
        statsMap.put(3, 15);
        statsMap.put(4, 20);
        statsMap.put(5, 25);
        StatsFile statsFile = new StatsFile(statsMap);
        int[] binsEdges = {1, 3, 5, 7};

        int[] results = statsFile.getResults(binsEdges);

        assertArrayEquals(new int[]{5 + 10 + 15, 15 + 20 + 25, 25}, results);
    }

    @Test
    void getResults_EmptyStatsMap_ReturnsZeroes() {
        SortedMap<Integer, Integer> statsMap = new TreeMap<>();
        StatsFile statsFile = new StatsFile(statsMap);
        int[] binsEdges = {1, 3, 5, 7};

        int[] results = statsFile.getResults(binsEdges);

        assertArrayEquals(new int[]{0, 0, 0}, results);
    }
}
