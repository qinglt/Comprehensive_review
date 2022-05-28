package org.jit.sose.entity.zExpert.vo;

import lombok.Data;

import java.util.List;

/**
 * @author qinghua
 * @date 2022/5/15 19:39
 */
@Data
public class ScoreVo {

    private Integer indicatorId;

    // 一级指标
    private String indicatorName1;

    // 二级指标
    private String indicatorName2;

    // 三级指标
    private String indicatorName3;

    private String guide;

    private String limit;

    private String standard;

    private Integer lastId;

    private String score;

    private List<ZDataVo> zDataVoList;

}
