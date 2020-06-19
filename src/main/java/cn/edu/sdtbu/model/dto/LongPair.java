package cn.edu.sdtbu.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-06-19 21:13
 */
@Data
@AllArgsConstructor
public class LongPair {
    long first;
    long second;
    public void addFirst() {
        this.first++;
    }
    public void addSecond() {
        this.second++;
    }
    public static LongPair of(long first, long second) {
        return new LongPair(first, second);
    }
}
