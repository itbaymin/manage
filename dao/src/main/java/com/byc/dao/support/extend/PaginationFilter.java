package com.byc.dao.support.extend;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by baiyc
 * 2019/5/22/022 10:32
 * Description：过滤器
 */
@Component("paginationFilter")
public class PaginationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String indexStr = request.getParameter("pageIndex");
        String sizeStr = request.getParameter("pageSize");
        String orderStr = request.getParameter("order");
        String sortStr = request.getParameter("sort");
        try {
            SystemRequest systemRequest = new SystemRequest();
            if(!StringUtils.isEmpty(indexStr)){
                systemRequest.setPageIndex(Integer.valueOf(indexStr));
            }
            if(!StringUtils.isEmpty(sizeStr)){
                systemRequest.setPageSize(Integer.valueOf(sizeStr));
            }
            if(!StringUtils.isEmpty(orderStr)){
                systemRequest.setOrder(orderStr);
            }
            if(!StringUtils.isEmpty(sortStr)){
                systemRequest.setSort(Sort.Direction.valueOf(sortStr));
            }
            SystemRequestHolder.initRequestHolder(systemRequest);
            chain.doFilter(request, response);
        } finally {
            SystemRequestHolder.remove();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}

}