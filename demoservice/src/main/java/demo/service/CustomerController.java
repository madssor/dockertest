package demo.service;

import hello.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CustomerController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping("/customers")
    public List<Customer> customer() {
        return getCustomers();
    }

    @RequestMapping("/customer/add")
    public List<Customer> customer(@RequestParam(value="name", defaultValue = "") String names) {
        List<Object[]> splitUpNames = Arrays.asList(names.split(",")).stream()
                .map(name -> name.split(" "))
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?,?)", splitUpNames);
        return getCustomers();
    }

    private List<Customer> getCustomers() {
        return jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM customers ",
                (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name")));
    }
}
