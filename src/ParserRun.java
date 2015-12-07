import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Serg on 07.12.2015.
 */
public class ParserRun implements Callable<HashSet> {

    private Pattern PATTERN_URL;
    private Pattern PATTERN_URL_DOT;
   // private HashSet foundUrl;
    private static String mUrlHome;
    private static String mUrlPath;
    private HashSet<String> mSetUrl;

    public ParserRun(String home, String path)
    {
        mUrlHome = home;
        mUrlPath = path;
        //foundUrl = new HashSet<String>();
        mSetUrl = new HashSet<>();
        PATTERN_URL = Pattern.compile(Parser.REG_EX_URL);
        PATTERN_URL_DOT = Pattern.compile(Parser.REG_EX_URL_DOT);
    }




    @Override
    public HashSet<String> call() throws Exception {

        BufferedReader buffer;
        String nextLine;
        if((buffer = getBufferFromUrl(mUrlHome+mUrlPath))==null) {
             return null;
        }
        while ((nextLine =buffer.readLine())!=null)
        {
            checkStringToUrl(nextLine);
        }

        buffer.close();
        return mSetUrl;
    }

    private void checkStringToUrl(String s)
    {
        Matcher matcher = PATTERN_URL.matcher(s);


        //System.out.println(matcher.find());
        while (matcher.find())
        {
            String tmp = s.substring(matcher.start(),matcher.end());
            if (tmp.contains(mUrlHome))
            //   if ((getBufferFromUrl(tmp)!=null))
                mSetUrl.add(tmp);
        }


        Matcher matcher_dot = PATTERN_URL_DOT.matcher(s);
        while (matcher_dot.find())
        {
            //System.out.println(s.substring((matcher_dot.start()),matcher_dot.end()));
            String tmp =s.substring((matcher_dot.start()+6),matcher_dot.end());

              // if ((getBufferFromUrl(mUrlHome+tmp)!=null))
                mSetUrl.add(mUrlHome+tmp);

        }

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
        catch (IOException e) {
            e.printStackTrace();
        }
        return new BufferedReader(ISR);
    }
}
