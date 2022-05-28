package org.jit.sose.entity.zExpert;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author qinghua
 * @since 2022-05-07
 */
@Data
public class ZIndicator implements Serializable {

    private static final long serialVersionUID=1L;

    private Integer id;

    /**
     * 指标名称
     */
    private String name;

    /**
     * 指标备注
     */
    private String content;

    /**
     * 指标等级
     */
    private Integer level;

    /**
     * 上级指标标识
     */
    private Integer lastId;

    /**
     * 牵头单位
     */
    private String company;

    /**
     * 具体制定人
     */
    private Integer userId;

    /**
     * 计划标识
     */
    private Integer planId;

    private String guide;

    private String limit;

    private String standard;

    List<ZIndicator> childList;

    /**
     * 状态：在用-'A';删除-'X'
     */
    @TableLogic
    private String state;

}
