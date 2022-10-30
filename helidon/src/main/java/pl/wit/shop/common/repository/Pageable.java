package pl.wit.shop.common.repository;

public record Pageable(
        Sort sort,
        String sortField,
        int firstElement,
        int pageSize
){ }