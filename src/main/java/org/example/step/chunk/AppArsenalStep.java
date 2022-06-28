package org.example.step.chunk;

import org.example.model.AppComputador;
import org.example.process.AppComputadorProcessor;
import org.example.read.AppComputadorReader;
import org.example.write.AppComputadorWriter;
import org.example.partitioner.AppComputadorRangePartitioner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Slf4j
@Configuration
public class AppComputadorStep {

	@Autowired public StepBuilderFactory stepBuilderFactory;

	@Autowired public AppComputadorReader reader;

	@Autowired public AppComputadorProcessor processor;

	@Autowired public AppComputadorWriter writer;

	@Autowired public AppComputadorRangePartitioner partitioner;

	@Bean
	public Step partitionerStep() throws Exception {
		return stepBuilderFactory
				.get("partitionerStep")
				.partitioner(slaveStep().getName(), partitioner)
				.step(slaveStep())
				.gridSize(5)
				.taskExecutor(taskExecutor())
				.build();
	}

	@Bean
	public TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor("spring_batch");
		asyncTaskExecutor.setConcurrencyLimit(5);
		return asyncTaskExecutor;
	}

	@Bean
	public Step slaveStep() throws Exception {
		log.info("Iniciando step...");
		return stepBuilderFactory
				.get("slaveStep")
				.<AppComputador, AppComputador>chunk(1)
				.reader(reader.read())
				.processor(processor)
				.writer(writer)
				.faultTolerant()
				.skipLimit(5)
				.skip(FlatFileParseException.class)
				.build();
	}

}
