package main.service;

import main.model.Comment;

public interface CommentService {

	Comment save(Comment comment);

	Comment get(Long id);

	void delete(Comment comment);

	public Comment edit(Comment comment);
}
