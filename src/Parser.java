import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by Serg on 09.12.2015.
 */
public class Parser  extends Thread{

    private CustomUrl mURL;
    private int mTaskDepth;
    private LinkedHashSet<CustomUrl> firstStep;
    private int mAvailableThreads;
    private ExecutorService mExecutor;
    private ArrayList<Future> mFutureList;

    public Parser(CustomUrl URL, int depth, String name)
    {
        super(name);
        mURL = new CustomUrl(URL.getHome(),URL.getPath());
        mTaskDepth =depth;
        firstStep = new LinkedHashSet<>();
        ParsingWork mParsing = new ParsingWork(mURL);
        firstStep = mParsing.goAhead();
        mAvailableThreads = Runtime.getRuntime().availableProcessors();
        mExecutor = Executors.newFixedThreadPool(mAvailableThreads);
        mFutureList= new ArrayList<>();
    }

    @Override
    public void run() {

        int tmp_count= 0;

        if (mTaskDepth <= 1)
            return;
        int coef = firstStep.size() / mAvailableThreads;
        System.out.println("Available threads:  "+ mAvailableThreads);
        LinkedHashSet<CustomUrl> prepareTask = new LinkedHashSet<>();
       while(!firstStep.isEmpty()) {

            prepareTask.clear();
            if (firstStep.size() < coef*2)
            {
                prepareTask.addAll(firstStep);
                mExecutor.submit(new ThreadParser(prepareTask,mTaskDepth, ++tmp_count));
                firstStep.removeAll(prepareTask);
                break;
            }
            int counter =0;
            for (CustomUrl tmp: firstStep) {
                prepareTask.add(tmp);
                counter++;
                if (counter>=coef)
                    break;
            }

            mFutureList.add(mExecutor.submit(new ThreadParser(prepareTask,mTaskDepth, ++tmp_count)));
            firstStep.removeAll(prepareTask);

        }

        mExecutor.shutdown();
        try {
           mExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
