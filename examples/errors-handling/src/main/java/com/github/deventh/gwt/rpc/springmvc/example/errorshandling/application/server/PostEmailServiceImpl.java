package com.github.deventh.gwt.rpc.springmvc.example.errorshandling.application.server;

import com.github.deventh.gwt.rpc.springmvc.example.errorshandling.application.client.PostEmailService;
import com.github.deventh.gwt.rpc.springmvc.example.errorshandling.application.shared.ApplicationException;
import com.github.deventh.gwt.rpc.springmvc.example.errorshandling.application.shared.TooManyPostsException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("greetingService")
public class PostEmailServiceImpl implements PostEmailService {
	@Resource
	JdbcTemplate jdbcTemplate;

	private final int total = 10;
	private int count = 0;

	@Override
	public List<String> post(String email) throws ApplicationException {
		if(count ++ > total) {
			throw new TooManyPostsException();
		}

		String e = email.replace("'", "");
		// email is unique
		// lets intercept if it already exists
		jdbcTemplate.execute("insert into users(name, email) values('"+e+"', '"+e+"')");

		// return all
		return new ArrayList<>(jdbcTemplate.query(
				"select u.email from users as u",
				(rs, rowNum) -> rs.getString(1)
		));
	}
}
