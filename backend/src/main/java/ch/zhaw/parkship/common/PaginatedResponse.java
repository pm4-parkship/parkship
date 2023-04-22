package ch.zhaw.parkship.common;

import org.springframework.data.domain.Page;

import java.util.List;

public record PaginatedResponse<T>(
        int page, int size, long totalSize, int totalPages, List<T> data
) {
    public static <T> PaginatedResponse<T> fromPage(Page<T> page) {
        return new PaginatedResponse<T>(
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getContent()
        );
    }
}