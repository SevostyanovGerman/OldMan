package main.service;

import main.model.Order;

public interface HistoryService {
	Order saveHistory(Order order);
}
