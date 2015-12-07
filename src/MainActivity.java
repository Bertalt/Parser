
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class MainActivity{

private static String TEST_URL = "http://www.056.ua";
    private static String TEST_URL_PATH = "/job";
    private static int TEST_DEPTH = 1;
    public static void main(String [] args)  {

        //Scanner sc = new Scanner(System.in);

        System.out.println("¬ведите URL: "+ TEST_URL);
        System.out.println("¬ведите глубину : "+ TEST_DEPTH);
        String address = TEST_URL; // sc.nextLine();



        Parser mParser = new Parser(TEST_URL, TEST_URL_PATH, TEST_DEPTH);



    }





}
