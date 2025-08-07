package org.yann.integerasiorderkuota.orderkuota.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter

public class StatementResponse <T>{

    private int page;
    private int size;
    @JsonProperty("total_content")
    private long totalContent;
    @JsonProperty("total_pages")
    private int totalPages;
    @JsonProperty("has_next")
    private boolean hasNext;
    @JsonProperty("has_previous")
    private boolean hasPrevious;

    private List<T> data;

    @JsonProperty("total_data")
    private int pageTotalContent;


    public StatementResponse (Page<T> page) {
        this.page = page.getNumber();
        this.size = page.getSize();
        this.totalContent = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.data = page.getContent();
        this.hasNext = page.hasNext();
        this.hasPrevious = page.hasPrevious();
        this.pageTotalContent = page.getSize();
    }
}
