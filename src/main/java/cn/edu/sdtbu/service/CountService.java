package cn.edu.sdtbu.service;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-03 15:00
 */
public interface CountService {
    /**
     * fetch total count by key, if not found, return 0
     * @param key must be not null
     * @return total
     */
    Long fetchCount(String key);
}
