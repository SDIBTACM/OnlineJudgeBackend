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
public class CountPair {
    long accept;
    long submit;
    public void accepted() {
        this.accept++;
    }
    public void submitted() {
        this.submit++;
    }
    public static CountPair of(long accepted, long submitted) {
        return new CountPair(accepted, submitted);
    }
}
