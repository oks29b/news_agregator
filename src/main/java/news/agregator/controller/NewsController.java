package news.agregator.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import news.agregator.dto.NewsDto;
import news.agregator.service.NewsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/news",
        produces = "application/json")
public class NewsController {
    private final NewsManager newsManager;

    @GetMapping
    public List<NewsDto> getAllNews(){
        return newsManager.findAllNews();
    }

}
