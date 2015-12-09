import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Serg on 09.12.2015.
 */
public class Parser {

    private CustomUrl mURL;


    private BlockingQueue<CustomUrl> blockingQueue;
    public static Set<CustomUrl> sSybchorizedSetForNextStep;
    private int mDepth=2;
    private int countSteps = 0;
    public Parser(CustomUrl URL)
    {
        mURL = new CustomUrl(URL.getHome(),URL.getPath());
        sSybchorizedSetForNextStep = Collections.synchronizedSet(new LinkedHashSet<CustomUrl>());
        blockingQueue = new ArrayBlockingQueue<CustomUrl>(Runtime.getRuntime().availableProcessors());

        goAhead();
    }

    public void goAhead()
    {

        ParsingWork mParsing = new ParsingWork(mURL);
        mParsing.goAhead();
        System.out.println(sSybchorizedSetForNextStep.size());
        for(CustomUrl tmp : sSybchorizedSetForNextStep)
        {
            System.out.println(tmp);
        }

    }


}
