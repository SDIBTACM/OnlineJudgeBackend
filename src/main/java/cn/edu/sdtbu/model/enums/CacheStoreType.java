package cn.edu.sdtbu.model.enums;

/**
 * Will used cache store, Recommend redis
 * @author bestsort
 * @version 1.0
 * @date 2020-04-26 16:31
 */
public enum  CacheStoreType {
    /**
     * use default(db)
     */
    MEMORY,
    /**
     * use redis
     */
    REDIS;
}
