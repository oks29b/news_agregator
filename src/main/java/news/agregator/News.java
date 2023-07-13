//package news.agregator;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
//import com.sun.syndication.feed.synd.SyndEntry;
//import com.sun.syndication.feed.synd.SyndFeed;
//import com.sun.syndication.io.FeedException;
//import com.sun.syndication.io.SyndFeedInput;
//import com.sun.syndication.io.XmlReader;
//import news.agregator.dto.NewsDto;
//
//import java.io.IOException;
//import java.net.URL;
//import java.net.URLConnection;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class News {
//
//    public static void main(String[] args) {
//
//        News app = new News();
//
//        //пролучаем адреса rss из файла с ресурсами
//        Map<String, String> sources = app.getSources();
//        List<NewsDto> newsDtos = new ArrayList<>();
//
//        for(String rss : sources.values()){
//            SyndFeed syndFeed = app.feedFromUrl(rss);
//            if(syndFeed == null) continue;
//            List<SyndEntry> entries = (List<SyndEntry>) syndFeed.getEntries();
//            for(SyndEntry syndEntry : entries){
//                NewsDto newsDto = app.mapEntryToDto(syndEntry);
//                newsDtos.add(newsDto);
//            }
//        }
//        System.out.println(newsDtos);
//        System.out.println("________________");
//    }
//
//    private NewsDto mapEntryToDto(SyndEntry syndEntry){
//        return NewsDto.builder()
//                .title(syndEntry.getTitle())
//                .description(syndEntry.getDescription() == null ? "" : syndEntry.getDescription().getValue())
//                .localDate(syndEntry.getPublishedDate())
//                .link(syndEntry.getLink())
//                .build();
//    }
//
//    /**
//     * Считывает из yaml файла адреса rss и возвращает их как значения Map
//     */
//    private Map<String, String> getSources() {
//        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
//        try {
//            return mapper.readValue(
//                            News.class.getResource("/rss.yaml"),
//                            SourceList.class).getSources();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * Получает объект SyndFeed загружая rss из переданного url
//     *
//     * @param url
//     * @return SyndFeed - объект из пакета rome
//     */
//    private SyndFeed feedFromUrl(String url) {
//        final int TIMEOUT = 1000;
//        try {
//            URLConnection conn = new URL(url).openConnection();
//            conn.setConnectTimeout(TIMEOUT);
//            XmlReader reader = new XmlReader(conn); // reader из пакета rome
//            return new SyndFeedInput().build(reader);
//        } catch (IOException e) {
//            System.out.println("failed to connect - " + url + " skipped");
//            return null;
//        } catch (FeedException | NullPointerException e) {
//            System.out.println("failed to parse response from - " + url + " skipped");
//            return null;
//        }
//    }
//
//    /**
//     * Вспомогательный класс для десериализации из yaml файла
//     */
//    static class SourceList {
//        Map<String, String> sources;
//
//        public Map<String, String> getSources() {
//            return sources;
//        }
//    }
//}
