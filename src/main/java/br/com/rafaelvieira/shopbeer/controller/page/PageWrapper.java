package br.com.rafaelvieira.shopbeer.controller.page;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.util.UriComponentsBuilder;

public class PageWrapper<T> {

    private final Page<T> page;
    private final UriComponentsBuilder uriBuilder;

    public PageWrapper(Page<T> page, HttpServletRequest httpServletRequest) {
        this.page = page;
        String httpUrl = httpServletRequest.getRequestURL().append(
                        httpServletRequest.getQueryString() != null ? "?" + httpServletRequest.getQueryString() : "")
                .toString().replaceAll("\\+", "%20").replaceAll("excluded", "");
        this.uriBuilder = UriComponentsBuilder.fromHttpUrl(httpUrl);
    }

    public List<T> getContent() {
        return page.getContent();
    }

    public boolean isVazia() {
        return page.getContent().isEmpty();
    }

    public int getCurrent() {
        return page.getNumber();
    }

    public boolean isFirst() {
        return page.isFirst();
    }

    public boolean isLast() {
        return page.isLast();
    }

    public int getTotal() {
        return page.getTotalPages();
    }

    public String urlToPage(int page) {
        return uriBuilder.replaceQueryParam("page", page).build(true).encode().toUriString();
    }

    public String urlOrdered(String propriedade) {
        UriComponentsBuilder uriBuilderOrder = UriComponentsBuilder
                .fromUriString(uriBuilder.build(true).encode().toUriString());

        String valorSort = String.format("%s,%s", propriedade, reverseDirection(propriedade));

        return uriBuilderOrder.replaceQueryParam("sort", valorSort).build(true).encode().toUriString();
    }

    public String reverseDirection(String propriedade) {
        String direcao = "asc";

        Order order = page.getSort() != null ? page.getSort().getOrderFor(propriedade) : null;
        if (order != null) {
            direcao = Sort.Direction.ASC.equals(order.getDirection()) ? "desc" : "asc";
        }

        return direcao;
    }

    public boolean downward(String property) {
        return reverseDirection(property).equals("asc");
    }

    public boolean ordered(String property) {
        Order order = page.getSort() != null ? page.getSort().getOrderFor(property) : null;

        if (order == null) {
            return false;
        }

        return page.getSort().getOrderFor(property) != null ? true : false;
    }

}
