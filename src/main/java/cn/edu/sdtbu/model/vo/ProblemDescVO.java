package cn.edu.sdtbu.model.vo;

import lombok.Data;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-02 13:43
 */
@Data
public class ProblemDescVO {
    private String title;

    private String description;

    private String input;

    private String output;

    private String sample;

    private String hint;

    private Integer timeLimit;

    private Integer memoryLimit;

    private Long submitCount;

    private Long acCount;
}
