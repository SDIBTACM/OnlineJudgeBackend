package cache;

import cn.edu.sdtbu.Application;
import cn.edu.sdtbu.manager.RedisManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.data.util.Pair;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Tuple;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-22 23:05
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RedisTest {
    @Autowired
    RedisManager manager;
    @Test
    public void managerCRUDTest() {
        manager.put("1", "abc", 100, TimeUnit.MINUTES);
        assert manager.get("1").equals("abc");
        manager.put("1", "1", 100, TimeUnit.SECONDS);
        manager.inc("1", 1);
        assert manager.get("1").equals("2");
    }
    @Test
    public void managerSortedListTest() {
        Map<String, Double> map = new TreeMap<>();
        for (double i = 0; i < 30; i++){
            map.put("value" + i, i);
        }
        manager.sortedListAdd("test_list", map);
        Pageable pageable = PageRequest.of(1, 2);
        Set<Tuple> collection = manager.fetchRanksByPage("test_list", pageable, false);
        for (Tuple item : collection) {
            System.out.println(item);
        }
    }
}
