package icu.intelli.simpletest.youdao;

import lombok.Data;

import java.util.List;

/**
 * @author wangshuo
 * @date 2021/06/07
 */
@Data
public class PicGoUploadResponse {

    private Boolean success;

    private List<String> result;

}
