package com.f139.shop.admin.common.response;

import lombok.*;

@Builder
@Data
@Setter
@Getter
public class ResponseResult{
    // 接口调用成功或者失败
    @Builder.Default
    private Integer code = 200;
    // 需要传递的信息，例如错误信息
    @Builder.Default
    private String status = "success";
    // 需要传递的数据
    @Builder.Default
    private Object data;

}