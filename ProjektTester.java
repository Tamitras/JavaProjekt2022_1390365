import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

public class ProjektTester {
    // Configuration of your main class. Please specify fully qualified class name plus package.
    // E.g. the class MyProject in the package dhbw.java, must read: 'dhbw.java.MyProject'.
    private static final String MAIN_CLASS = "Main";

    public static void main(String[] args) {
        // project.model.Tests are passing
        boolean passed = true;

        // kontaktpersonen --> contactperson
        // ContactPerson for Mia
        passed = passedTestNetzwerk("--kontaktpersonen=1", "Aaron, Amelie, Ben, Emil, Emilia, Emily, Felix, Hannah, Hannes, Julius, Leonard, Levi, Louis, Malia, Marlene, Ole, Rosalie, Sophia, Victoria");
        // ContactPerson for Emilia
        passed &= passedTestNetzwerk("--kontaktpersonen=2", "Amelie, Carla, Carlotta, Charlotte, Emma, Eva, Hannah, Hannes, Jonas, Joshua, Malia, Maria, Mattis, Melina, Mia, Noah, Ole, Sophia, Tom");
        // ContactPerson for Ole
        passed &= passedTestNetzwerk("--kontaktpersonen=158", "Ben, Carla, Emilia, Emily, Joshua, Malia, Maria, Mia, Sophia");

        // Visitors for bakery "2021-05-15T14:16:00"
        passed &= passedTestNetzwerk("--besucher=1,\"2021-05-15T14:16:00\"", "Adam, Amelie, Carla, Carlotta, Charlotte, Elli, Emil, Emilia, Emily, Emma, Eva, Fiona, Hannah, Hannes, Jonah, Jonas, Joshua, Konstantin, Lian, Lisa, Luisa, Malia, Mara, Maria, Mattis, Max, Melina, Mia, Mohammed, Noah, Ole, Sophia, Tim, Tom, Toni, Victoria");
        // Visitors for Supermarkt am "2021-05-15T14:16:00"
        passed &= passedTestNetzwerk("--besucher=2,\"2021-05-15T14:16:00\"", "Alma, Anni, Arthur, Jannik, Mats, Mika, Paula, Pia");
        // Visitors for Zoo am "2021-05-15T11:00:00"
        passed &= passedTestNetzwerk("--besucher=3,\"2021-05-15T11:00:00\"", "Anna, Charlotte, Emilia, Leonie, Marie, Mia");

        if (passed) {
            System.out.println("Yippie - all tests passed - :-)");
        } else {
            System.out.println("Unfortunately not all tests passed:-(");
        }
    }

    /**
     * Checks if the call contains the expected output string..
     *
     * @param arg          Programmargument
     * @param resultString String which is expected as output
     * @return
     */
    private static boolean passedTestNetzwerk(String arg, String resultString) {
        // The System.out stream must be wrapped so that it can be checked later.
        PrintStream normalerOutput = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        String[] args = {arg};
        try {
            // Get MainClass using reflection and call main method
            Class<?> mainClass = Class.forName(MAIN_CLASS);
            Method mainMethod = mainClass.getDeclaredMethod("main", String[].class);
            mainMethod.invoke(null, (Object) args);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Main class could not be loaded, please check configuration.");
            System.exit(1);
        } finally {
            // reset System.out
            System.setOut(normalerOutput);
        }

        // Check results.
        String output = baos.toString();
        String[] lines = output.split(System.lineSeparator());
        // Check if a line in the output matches the format
        for (String line : lines) {
            // do not include spaces
            if (line.replace(" ", "").equals(resultString.replace(" ", ""))) {
                return true;
            }
        }
        System.err.println("Error at: '" + arg + "'. Expected result: '" + resultString + "', Result obtained: '" + output.replace(System.lineSeparator(), "") + "'");
        return false;
    }

}