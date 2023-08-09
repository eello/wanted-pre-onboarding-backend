package jongseongkim.wantedpreonboardingbackend.dto;

import java.util.Objects;
import java.util.function.Function;

import com.fasterxml.jackson.annotation.JsonInclude;

import jongseongkim.wantedpreonboardingbackend.entity.Board;
import jongseongkim.wantedpreonboardingbackend.entity.User;
import lombok.Getter;

@Getter
public class BoardDTO {

	private WriterDTO writer;
	private Long id;
	private String title;

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	private String content;

	public static Function<Board, BoardDTO> mapper() {
		return BoardDTO::of;
	}

	public static BoardDTO of(Board board) {
		BoardDTO dto = new BoardDTO();
		dto.writer = WriterDTO.of(board.getWriter());
		dto.id = board.getId();
		dto.title = board.getTitle();
		dto.content = board.getContent();

		return dto;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof BoardDTO))
			return false;
		BoardDTO boardDTO = (BoardDTO)o;
		return writer.equals(boardDTO.writer) && id.equals(boardDTO.id) && title.equals(boardDTO.title)
			&& Objects.equals(content, boardDTO.content);
	}

	@Override
	public int hashCode() {
		return Objects.hash(writer, id, title, content);
	}

	@Getter
	public static class WriterDTO {

		private Long id;
		private String email;

		public static WriterDTO of(User user) {
			WriterDTO dto = new WriterDTO();
			dto.id = user.getId();
			dto.email = user.getEmail();
			return dto;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			WriterDTO writerDTO = (WriterDTO)o;
			return id.equals(writerDTO.id) && email.equals(writerDTO.email);
		}

		@Override
		public int hashCode() {
			return Objects.hash(id, email);
		}
	}
}
