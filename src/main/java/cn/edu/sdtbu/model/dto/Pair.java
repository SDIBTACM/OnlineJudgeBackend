package cn.edu.sdtbu.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-06-19 21:10
 */
@Data
@AllArgsConstructor
public class Pair<F, S> {
    F first;
    S second;
    public static <F, S> Pair<F, S> of(F first, S second) {
        return new Pair<>(first, second);
    }
}
