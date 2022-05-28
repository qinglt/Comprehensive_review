package org.jit.sose.entity.zExpert.vo;

import lombok.Data;

import java.util.List;

/**
 * @author qinghua
 * @date 2022/5/26 19:47
 */
@Data
public class FolderVo {

    private Integer zDataId;

    private Integer indicatorId;

    private String fileName;

    private Integer lastId;

    private List<FolderVo> childList;

}
