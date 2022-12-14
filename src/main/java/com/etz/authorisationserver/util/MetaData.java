package com.etz.authorisationserver.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.domain.Page;

import static com.etz.authorisationserver.constant.AppConstant.PAGE;

@Data
public class MetaData<T>{

    @JsonProperty("total_page")
    private Long total;

    @JsonProperty("per_page")
    private int perPage;

    @JsonProperty("current_page")
    private int currentPage;

    @JsonProperty("last_page")
    private int lastPage;

    @JsonProperty("next_page")//("next_page_url")
    private String nextPageUrl;

    @JsonProperty("prev_page")//("prev_page_url")
    private String prevPageUrl;

    private Long from;
    private Long to;

    public MetaData(Page<T> result){

        setTotal(result.getTotalElements());

        setPerPage(Integer.parseInt(RequestUtil.perPage()));

        int nextPage = RequestUtil.getPage() <= 0 ? 1 : RequestUtil.getPage() + 1;

        int prevPage = RequestUtil.getPage() <= 0 ? 0 : RequestUtil.getPage() - 1;

        if(!result.getContent().isEmpty()){
            if(result.isFirst() && result.isLast()){
                setNextPageUrl(null);
                setPrevPageUrl(null);
            }else if(!result.isFirst() && !result.isLast()){
                setPrevPageUrl( RequestUtil.getRequest().getRequestURL().append("?").append(PAGE).append("=").append(prevPage).toString() );
                setNextPageUrl( RequestUtil.getRequest().getRequestURL().append("?").append(PAGE).append("=").append(nextPage).toString() );
            }else if(result.isFirst() && !result.isLast()){
                setPrevPageUrl( null );
                setNextPageUrl( RequestUtil.getRequest().getRequestURL().append("?").append(PAGE).append("=").append(nextPage).toString() );
            }

            setLastPage(result.getTotalPages());
            setCurrentPage(RequestUtil.getPage());
            int $from = ((RequestUtil.getPage() - 1) * getPerPage()) + 1;
            int $to = ((RequestUtil.getPage() - 1) * getPerPage()) + result.getContent().size();
            setFrom(Long.valueOf($from + ""));
            setTo(Long.valueOf($to + ""));
        }
    }
}
