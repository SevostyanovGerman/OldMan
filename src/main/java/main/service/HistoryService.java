package main.service;

import main.model.Order;

public interface HistoryService {

	Order saveHistoryFromManager(Order order); //история от менеджера
	Order saveHistoryToManager(Order order);  //история от остальных
}
