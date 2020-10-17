package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	@Autowired
	DataSource datasource;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {

				Connection conn = null;
				PreparedStatement preparedStmt = null;
				try {
					conn = datasource.getConnection();
					String query = "insert into num_time_table(num, _timestamp) values(?, ?)";
					preparedStmt = conn.prepareStatement(query);
					java.util.Date today = new java.util.Date();
					java.sql.Timestamp timestamp = new java.sql.Timestamp(today.getTime());

					Random rand = new Random();

					for (int i = 0; i < 5; i++) {
						preparedStmt.setInt(1, rand.nextInt(5));
						preparedStmt.setTimestamp(2, timestamp);
						preparedStmt.executeUpdate();
					}

				} catch (SQLException throwables) {
					throwables.printStackTrace();
				}
				finally {
					try {
						conn.close();
						preparedStmt.close();
					} catch (SQLException throwables) {
						throwables.printStackTrace();
					}
				}
			}
		};

		Timer timer = new Timer();
		timer.schedule(timerTask, 1000L, 1000L);
	}
}
