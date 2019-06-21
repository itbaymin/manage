package com.byc.dao.support.extend;

import lombok.Data;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

/**
 * Created by baiyc
 * 2019/5/22/022 10:28
 * Description：分页对象
 */
@Data
public class SystemRequest  implements Serializable {
    private static final long serialVersionUID = -4168104962029946743L;
    private int pageSize = 10;
    private int pageIndex = 1;
    private Sort.Direction sort = Sort.Direction.DESC;
    private String order = "id";

    public int getPageIndex(){
        return pageIndex-1;
    }
}

