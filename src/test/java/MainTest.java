import org.example.Main;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {

    @ParameterizedTest
    @CsvFileSource(resources = "exampleSource.txt", delimiter = '|')
    public void isStringValid_shouldReturnCorrectValues(String string, String ans) {
        String[] stringNums = string.split(";");
        Boolean expected = Boolean.parseBoolean(ans);
        assertEquals(expected, Main.isStringValid(stringNums));
    }

    @Test
    public void readFileTest() {
        File file = new File("exampleSource.txt");
        boolean expectedResult = true;
        boolean actualResult = false;
        if (!file.exists()){
            actualResult = true;
        }
        assertEquals(expectedResult, actualResult);
    }


}
