package main.service;

import main.model.Comment;
import main.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CommentServiceImpl implements CommentService{
	@Autowired
	private CommentRepository commentRepository;

	@Override
	public void save(Comment comment) {
		commentRepository.save(comment);
	}

	@Override
	public Comment get(Long id) {
		return commentRepository.getOne(id);
	}
}
