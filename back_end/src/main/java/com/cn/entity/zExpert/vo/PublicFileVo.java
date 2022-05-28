package org.jit.sose.entity.zExpert.vo;

import lombok.Data;

import java.util.List;

/**
 * @author qinghua
 * @date 2022/5/27 19:18
 */
@Data
public class PublicFileVo {

    private Integer id;

    private Integer dataType;

    private Integer level;

    private Integer lastId;

    private String fileName;

    private String accessUrl;

    private String type;

    private List<PublicFileVo> childList;

}
