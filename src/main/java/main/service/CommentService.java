package main.service;

import main.model.Comment;

public interface CommentService {
	void save (Comment comment);
	Comment get(Long id);
}
