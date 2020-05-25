package br.gov.ce.sefaz.siconfi.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.gov.ce.sefaz.siconfi.entity.ExtratoEntrega;

public class ExtratoEntregaResponse {

	private List<ExtratoEntrega> items;
	private Boolean hasMore;
	private Integer limit;
	private Integer offset;
	private Integer count;
	@JsonIgnore
	private String links = null;
	
	public List<ExtratoEntrega> getItems() {
		return items;
	}
	public void setItems(List<ExtratoEntrega> items) {
		this.items = items;
	}
	public Boolean getHasMore() {
		return hasMore;
	}
	public void setHasMore(Boolean hasMore) {
		this.hasMore = hasMore;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getLinks() {
		return links;
	}
	public void setLinks(String links) {
		this.links = links;
	}
	
	
}
