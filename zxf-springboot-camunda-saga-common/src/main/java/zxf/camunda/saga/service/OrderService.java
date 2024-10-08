package zxf.camunda.saga.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zxf.camunda.saga.dao.OrderDao;

@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;

    @Transactional(rollbackFor = Throwable.class, value = "businessTransactionManager")
    public boolean createOrder(String orderId) {
        return orderDao.createOrder(orderId);
    }
}
