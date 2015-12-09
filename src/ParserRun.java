import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Serg on 07.12.2015.
 */
public class ParserRun implements Callable<HashSet> {

    private Pattern PATTERN_URL;
    private Pattern PATTERN_URL_DOT;
    private Pattern PATTERN_EMAIL;
   // private HashSet foundUrl;
    private static String mUrlHome;
    private static String mUrlPath;
    private HashSet<String> mSetUrl;

    private final int bufferSize = 100000;
    private Set<String> mSynchronSet;

    public ParserRun(String home, String path)
    {
        mUrlHome = home;
        mUrlPath = path;

        mSetUrl = new HashSet<>();
        PATTERN_URL = Pattern.compile(Parser.REG_EX_URL);
        mSynchronSet = Collections.synchronizedSet(mSetUrl);
        PATTERN_URL_DOT = Pattern.compile(Parser.REG_EX_URL_DOT);
        PATTERN_EMAIL = Pattern.compile(Parser.REG_EX_EMAIL);
    }




    @Override
    public HashSet<String> call() throws Exception {


        BufferedReader buffer;
       // String nextLine = null;

        StringBuffer mStringBuffer = new StringBuffer();
        if((buffer = getBufferFromUrl(mUrlHome+mUrlPath))==null) {
             return null;

        }

        int count =0;
            while (true) {
                char [] nextLine= new char [bufferSize];
                if (buffer.read(nextLine)== -1)
                                        break;

                mStringBuffer.append(nextLine);
                count++;

            }
        System.out.println(count);
       // System.out.println(mStringBuffer.toString());
        checkStringByPatterns(mStringBuffer.toString());

        buffer.close();
        return mSetUrl;
    }

    private void checkStringByPatterns(String s)
    {

                Matcher matcher_dot = PATTERN_URL_DOT.matcher(s);
                while (matcher_dot.find())
                {
                    //System.out.println(s.substring((matcher_dot.start()),matcher_dot.end()));
                    String tmp =(new StringBuilder().append(mUrlHome).append(s.substring((matcher_dot.start()+6),matcher_dot.end()))).toString();

                    if(tmp.contains(mUrlHome+mUrlPath))    //проверяем является ли ссылка дочерней относительно исходной
                        //if (checkUrl(tmp))
                             mSynchronSet.add(tmp);


                }
        matcher_dot = PATTERN_EMAIL.matcher(s);

        while(matcher_dot.find())
        {
            MainActivity.mSynchronSet.add(s.substring(matcher_dot.start(), matcher_dot.end()));
        }



    }



    private boolean checkUrl(String link)  {
        URL url = null;
        try {
            url = new URL(link);

        HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
            httpcon.setConnectTimeout(0);
        httpcon.addRequestProperty("User-Agent", "Mozilla/4.76");

       if (httpcon.getResponseCode() == 200)
           return true;
        }

        catch (UnknownHostException e)
        {
            return false;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


    private BufferedReader getBufferFromUrl(String link)
    {
        URL url = null;
        HttpURLConnection httpcon = null;
        InputStreamReader ISR = null;

        try {
            url = new URL(link);
        httpcon = (HttpURLConnection)url.openConnection();
        httpcon.addRequestProperty("User-Agent", "Mozilla/4.76");


            ISR = new InputStreamReader(httpcon.getInputStream(), "utf-8");

        }
        catch (FileNotFoundException e)
        {
            return null;
        }

        catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return new BufferedReader(ISR);
    }
}
