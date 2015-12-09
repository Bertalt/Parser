
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;


public class Parser {


    public static final String REG_EX_URL = "^*((https?|ftp)\\:\\/\\/)?(\\w{1})((\\.\\w)|(\\w))*\\.([a-z]{2,6})(\\/[a-z0-9_/]*)$*";
    public static final String REG_EX_TEL =  "^*((\\+38)?\\(?0\\d{2}?\\)?(\\d{7}|\\d{3}.\\d{2}.\\d{2}))\\+*";
    public static final String REG_EX_EMAIL = "^*([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$*";
    public static final String REG_EX_URL_DOT = "href=\"^?([a-z0-9_@?^=%&/~+#-]S*)*$*";


    private ExecutorService mExecutor;



    private static String mUrlHome;
    private static String mUrlPath;
    private static int mDepth;
    private HashSet<String>  mSetURL;
    private Set<String> mSynchronSet;


    public Parser(String URL_HOME, String URL_PATH, int depth)
    {
        mUrlHome = URL_HOME;
        mUrlPath = URL_PATH;
        mDepth = depth;

        mExecutor  = Executors.newFixedThreadPool(2);
       // mSetURL =  new HashSet<String>();

        start();
    }


    public void start()  {

        firstWalk();

    }

    private void firstWalk()
    {
        // int count= 0;
        try {
            Future<HashSet> result = mExecutor.submit(new ParserRun(mUrlHome, mUrlPath));
            while (!result.isDone()) {
                //ожидаем завершения парсинга
            }

            if (result.get() == null)
                return;
            for (Object o : result.get()) {
                String tmp = (String) o;

                System.out.println(tmp);

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // count++;
        mExecutor.shutdown();
    }




}
