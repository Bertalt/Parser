
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class MainActivity{

 public static String TEST_URL = "http://gorod.dp.ua";
    private static String TEST_URL_PATH = "/";
    private static int TEST_DEPTH =5 ;
    private static HashSet<String> mSetEmails;
    public static Set<String> mSynchronSet;



    public static void main(String [] args)  {
        long start = System.currentTimeMillis();


        //Scanner sc = new Scanner(System.in);

        System.out.println("¬ведите URL: "+ TEST_URL);
        System.out.println("¬ведите глубину : "+ TEST_DEPTH);
        String address = TEST_URL; // sc.nextLine();
        mSetEmails = new HashSet<String>();
        mSynchronSet = Collections.synchronizedSet(mSetEmails);
        Parser mParser = new Parser( new CustomUrl(TEST_URL, TEST_URL_PATH), TEST_DEPTH, "mainThread");


            mParser.start();



        for(String s: mSynchronSet)
        {
            System.out.println(s);
        }


        long finish = System.currentTimeMillis();

        System.out.println("¬рем€ выполнени€ "+ (finish - start));

    }



}
