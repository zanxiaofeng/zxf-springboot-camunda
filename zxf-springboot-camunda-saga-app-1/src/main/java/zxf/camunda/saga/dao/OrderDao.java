package zxf.camunda.saga.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderDao {
    @Autowired
    private NamedParameterJdbcTemplate restTemplate;

    public boolean createOrder(String orderId) {
        String sql = "INSERT INTO TBL_ORDER(ORDER_ID) value(:orderId)";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("orderId", orderId);
        return restTemplate.update(sql, parameterSource) > 0;
    }
}
