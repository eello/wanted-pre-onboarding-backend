package jongseongkim.wantedpreonboardingbackend.dto;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PaginationDTO<E, D> {

	private Boolean isFirst;

	private Boolean isLast;

	private Boolean isEmpty;

	private Boolean hasNext;
	private Boolean hasPrevious;
	private Integer sizePerPage;
	private Integer totalPages;
	private Integer pageNum;
	private Long totalElements;
	private List<D> content;

	public PaginationDTO(Page<E> page, Function<E, D> mapper) {
		isFirst = page.isFirst();
		isLast = page.isLast();
		isEmpty = page.isEmpty();
		hasPrevious = page.hasPrevious();
		hasNext = page.hasNext();
		sizePerPage = page.getSize();
		totalPages = page.getTotalPages();
		pageNum = page.getNumber();
		totalElements = page.getTotalElements();
		content = page.getContent().stream()
			.map(mapper)
			.collect(Collectors.toList());
	}
}
