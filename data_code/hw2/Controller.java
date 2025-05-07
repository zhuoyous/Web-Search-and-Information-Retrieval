package test_2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
    public final static String targetSite = "www.foxnews.com/";
    public static void main(String[] args) throws Exception {
        String crawlStorageFolder = "data/";
        String fetchFile = "fetch_foxnews.csv";
        String visitFile = "visit_foxnews.csv";
        String urlsFile = "urls_foxnews.csv";
        
        int numberOfCrawlers = 10;
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setMaxPagesToFetch(20000);
        config.setMaxDepthOfCrawling(16);
        config.setPolitenessDelay(2000);
        config.setIncludeHttpsPages(true);
        config.setFollowRedirects(true);
        config.setIncludeBinaryContentInCrawling(true);


        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(crawlStorageFolder+fetchFile));
            bw.write("URL, Status Codes\n");
            bw.close();

            bw = new BufferedWriter(new FileWriter(crawlStorageFolder+visitFile));
            bw.write("URL,Size,Outlinks,Content-Type\n");
            bw.close();

            bw = new BufferedWriter(new FileWriter(crawlStorageFolder+urlsFile));
            bw.write("encountered URL, indicator\n");
            bw.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        controller.addSeed("http://"+targetSite);
        controller.start(MyCrawler.class, numberOfCrawlers);
        System.out.println("Fethced attemps: "+MyCrawler.count);
    }
}