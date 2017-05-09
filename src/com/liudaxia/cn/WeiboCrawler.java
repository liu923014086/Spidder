package com.liudaxia.cn;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequest;
import cn.edu.hfut.dmic.webcollector.net.HttpResponse;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 * 爬取微博
 * @author Alex
 *
 */
public class WeiboCrawler extends BreadthCrawler {

    private String cookie;

    public WeiboCrawler(String crawlPath, boolean autoParse) throws Exception {
        super(crawlPath, autoParse);
        cookie = WeiboCN.getSinaCookie("XXXXXXXXXXX", "XXXXXXXXXX");//账号、密码
    }

    @Override
    public HttpResponse getResponse(CrawlDatum crawlDatum) throws Exception {
        HttpRequest request = new HttpRequest(crawlDatum);
        request.setCookie(cookie);
        return request.getResponse();
    }

    public void visit(Page page, CrawlDatums next) {
        int pageNum = Integer.valueOf(page.getMetaData("pageNum"));
        Elements weibos = page.select("div.c");//或者Document doc = page.doc();
        for (Element weibo : weibos) {
            System.out.println("第" + pageNum + "页\t" + weibo.text());
        }
    }

    public static void main(String[] args) throws Exception {
        WeiboCrawler crawler = new WeiboCrawler("WeiboCrawler", false);
        crawler.setThreads(3);//线程数
        for (int i = 1; i <= 5; i++) {//爬取XXX前5页
            crawler.addSeed(new CrawlDatum("http://weibo.cn/zhouhongyi?vt=4&page=" + i).putMetaData("pageNum", i + ""));
        }
        //crawlerNews.setResumable(true);//断点续爬
        crawler.start(1);
    }

}