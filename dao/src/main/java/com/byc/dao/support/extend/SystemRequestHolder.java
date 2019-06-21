package com.byc.dao.support.extend;

import org.springframework.data.domain.PageRequest;

/**
 * Created by baiyc
 * 2019/5/22/022 10:33
 * Description：分页信息处理器
 */
public class SystemRequestHolder {
    private final static ThreadLocal<SystemRequest> systemRequesthreadLocal = new ThreadLocal();

    public static void initRequestHolder(SystemRequest systemRequest) {
        systemRequesthreadLocal.set(systemRequest);
    }

    public static SystemRequest getSystemRequest() {
        return systemRequesthreadLocal.get();
    }

    public static void remove() {
        systemRequesthreadLocal.remove();
    }

    public static PageRequest getPageRequest(){
        SystemRequest systemRequest = getSystemRequest();
        return PageRequest.of(systemRequest.getPageIndex(), systemRequest.getPageSize(), systemRequest.getSort(), systemRequest.getOrder());
    }

}
