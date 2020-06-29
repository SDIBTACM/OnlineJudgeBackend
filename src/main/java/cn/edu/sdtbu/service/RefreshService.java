package cn.edu.sdtbu.service;

import cn.edu.sdtbu.model.enums.RankType;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-25 20:51
 */
public interface RefreshService {
    /**
     * 此接口用以刷新总排行榜榜记录（周榜、日榜、月榜不刷新）
     * @param reloadCount
     * @param freshUserEntity
     */
    void refreshOverAllRankList(Boolean reloadCount, boolean freshUserEntity);

    /**
     * 此接口用于刷新周榜、日榜、月榜记录. 只根据Solution表中记录进行统计, 不刷新User表以及Problem表中记录数
     */

    void reloadRankList(RankType type, boolean isAdd);
}
