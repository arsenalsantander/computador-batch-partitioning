package org.example.partitioner;

import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

@Component
public class AppComputadorRangePartitioner implements Partitioner {

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		Map<String, ExecutionContext> map = new HashMap<>();
		for (int i = 0; i < gridSize; i++) {
			ExecutionContext executionContext = new ExecutionContext();
			executionContext.put("data", ":data" + i);
			String key = ":partition" + i;
			map.put(key, executionContext);
		}
		return map;
	}

}
