package main.service;

import main.model.Comment;
import main.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

	private CommentRepository commentRepository;

	@Autowired
	public CommentServiceImpl(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}

	@Override
	public void save(Comment comment) {
		commentRepository.save(comment);
	}

	@Override
	public Comment get(Long id) {
		return commentRepository.getOne(id);
	}

	@Override
	public void delete(Comment comment) {
		commentRepository.delete(comment);
	}

	@Override
	public Comment edit(Comment comment) {
		return commentRepository.save(comment);
	}
}
