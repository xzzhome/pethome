package com.xzz.basic;

import com.xzz.org.domain.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageList<T> {
    //数量
    private Integer totals = 0;
    //当前页数据
    private List<T> data = new ArrayList<>();
}
