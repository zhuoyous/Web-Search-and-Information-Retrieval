package test_2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {
    private final static Pattern FILTERS = Pattern.compile(".*(css|feed|rss|svg|js|mp3|zip|gz|vcf|xml).*");
    String crawlStorageFolder = "data/";
    String fetchFile = "fetch_foxnews.csv";
    String visitFile = "visit_foxnews.csv";
    String urlsFile = "urls_foxnews.csv";
    public static int count = 0;
    public static int[] sizeCount = new int[5];

    @Override
    protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
 
        count++;
        try{
            synchronized(this){
                BufferedWriter bw = new BufferedWriter(new FileWriter(crawlStorageFolder+fetchFile,true));
                bw.write(webUrl.getURL().replace(",", "_")+","+statusCode+"\n");
                bw.close();
                }
            } catch(IOException e){
                e.printStackTrace();
            }
    }

  
    @Override

    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        try{
            synchronized(this){
                BufferedWriter bw = new BufferedWriter(new FileWriter(crawlStorageFolder+urlsFile, true));
                if(href.startsWith("http://"+Controller.targetSite) || href.startsWith("https://" +Controller.targetSite))
                    bw.write(url.getURL().replace(",", "_") + ", OK\n");
                else
                    bw.write(url.getURL().replace(",", "_")+ ", N_OK\n");
                bw.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        if(!(href.startsWith("http://"+Controller.targetSite) || (href.startsWith("https://"+Controller.targetSite))))
            return false;

        return !FILTERS.matcher(href).matches();
                
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        System.out.println("URL: " + url);
        int size = page.getContentData().length;
        int numOfOutlink = page.getParseData().getOutgoingUrls().size();
        String contentType = page.getContentType();
        contentType = contentType.toLowerCase().indexOf(";") > -1
                      ? contentType.replace(contentType.substring(contentType.indexOf(";"), contentType.length()), ""):contentType;
        try{
            synchronized (this){
                BufferedWriter bw = new BufferedWriter(new FileWriter(crawlStorageFolder + visitFile, true));
                bw.write(url.replace(",", "_") + "," + size + "," + numOfOutlink + "," + contentType +"\n");
                bw.close();
                System.out.println(crawlStorageFolder + visitFile);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}