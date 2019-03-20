package frodez.service.task;

import frodez.service.task.base.ITask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Transactional
@Service
public class HelloWorldTask implements ITask {

	@Override
	public void run() {
		log.info("hello world!");
	}

	@Override
	public String getDescription() {
		return "hello world!";
	}

}
