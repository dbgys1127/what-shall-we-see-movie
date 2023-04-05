package shallwe.movie.movie.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import shallwe.movie.movie.entity.Movie;
import shallwe.movie.movie.repository.MovieRepository;
import shallwe.movie.sawmovie.entity.SawMovie;
import shallwe.movie.sawmovie.service.SawMovieService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class MovieConcurrencyTest {
    @Autowired
    MovieService movieService;

    @Autowired
    SawMovieService sawMovieService;


    @DisplayName("평균 시청횟수 동시성 테스트")
    @Transactional
    @Test
    void 평균_시청횟수_동시성_테스트() throws InterruptedException {
        Movie movie = movieService.is_exist_movie("movie20");
        List<SawMovie> sawMovies = new ArrayList<>();
        for (int i =1;i<6;i++) {
            SawMovie sawMovie = SawMovie.builder()
                    .movieSawCount(i)
                    .build();
            sawMovies.add(sawMovie);
        }
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            int j = i;
            executorService.submit(()->{
                try {
                    sawMovieService.setMovieRelation(movie, sawMovies.get(j) );
                }finally {
                    countDownLatch.countDown();
                }
            });
        }
        log.info("movie avgCount={}",movie.getAvgSawCount());
        countDownLatch.await();
    }
}
