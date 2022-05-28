package org.jit.sose.entity.zExpert;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.core.toolkit.SerializationUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qinghua
 * @date 2022/5/14 12:28
 */
@Data
public class ZData implements Cloneable, Serializable {

    private Integer id;

    private Integer dataType;

    private Integer indicatorId;

    private Integer level;

    private Integer lastId;

    private String fileName;

    private String accessUrl;

    private String type;

    @TableLogic
    private String state;

    /**
     * 深克隆
     */
    @Override
    public Object clone() {
        return SerializationUtils.clone(this);
    }
}
