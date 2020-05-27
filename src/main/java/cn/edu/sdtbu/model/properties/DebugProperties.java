package cn.edu.sdtbu.model.properties;

import lombok.Data;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-11 20:09
 */
@Data
public class DebugProperties {
    private Boolean generatorData = false;
    private Boolean refreshAllProblemSolutionCount = false;
    private Boolean refreshRankList = false;
}