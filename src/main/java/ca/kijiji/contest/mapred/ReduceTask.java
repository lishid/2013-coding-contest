package ca.kijiji.contest.mapred;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import ca.kijiji.contest.CommonCalculations;
import ca.kijiji.contest.mapred.MapTask.MapperResultCollector;

public class ReduceTask extends MapReduceTask {
    List<MapperResultCollector> mapperResults;
    int partition;
    ReducerResultCollector resultCollector;
    
    public ReduceTask(TaskTracker taskTracker, List<MapperResultCollector> mapperResults, int partition) {
        super(taskTracker);
        this.mapperResults = mapperResults;
        this.partition = partition;
        resultCollector = new ReducerResultCollector();
    }
    
    public ReducerResultCollector getFutureResult() {
        return resultCollector;
    }

    /**
     * The reducer task: <br>
     * Take all mapper results for the same reducer partition and reduce it together. <br>
     * Sort the data before returning it. <br>
     */
    @Override
    public void performTask() throws Exception {
        // Reduce phase 1: Reduce data
        resultCollector.unsortedResult = null;
        for (MapperResultCollector mapperResult : mapperResults) {
            if (mapperResult.partitionedResult == null) {
                continue;
            }
            if (resultCollector.unsortedResult == null) {
                // Use the first one as the unsorted map to save some time and memory
                resultCollector.unsortedResult = mapperResult.partitionedResult.get(partition);
            }
            else {
                CommonCalculations.reduce(mapperResult.partitionedResult.get(partition), resultCollector.unsortedResult);
            }
        }
        
        // Reduce phase 2: Sort data
        resultCollector.result = CommonCalculations.sort(resultCollector.unsortedResult);
    }
    
    public static class ReducerResultCollector {
        Map<String, Integer> unsortedResult;
        SortedMap<String, Integer> result;
    }
}