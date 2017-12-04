package main.service;

import main.model.FuncMenu;
import main.repository.FuncMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@Transactional
public class FuncMenuServiceImpl implements FuncMenuService {

	@Autowired
	FuncMenuRepository functionRepository;

	@Override
	public void save(FuncMenu function) {
		functionRepository.save(function);
	}
}