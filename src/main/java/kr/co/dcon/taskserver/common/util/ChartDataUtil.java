package kr.co.dcon.taskserver.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;

public class ChartDataUtil {

	public static final String OTHERS = "others";
	public static Map<String, Object> getTopFiveChartData(List<Map<String,Object>> dbRst, String xAixField, String groupField, String dataField){

		Map<String,Object> resultMap = new HashMap<>();
		Map<String,String> chartFilter = new HashMap<>();
		List<Map<String,Object>> resultList = new ArrayList<>();
		Map<String, Map<String,Double>> xAxisMap = new HashMap<>();

		for(Map<String, Object> dataResult:dbRst) {

			Map<String, Double> mnthlyGroup = new HashMap<>();
			if(xAxisMap.containsKey(dataResult.get(xAixField))) {
				mnthlyGroup = xAxisMap.get(dataResult.get(xAixField));
			}


			mnthlyGroup.put((String)dataResult.get(groupField), (double)dataResult.get(dataField));
			xAxisMap.put(dataResult.get(xAixField).toString(),mnthlyGroup);

		}

		Integer topCount = 5;
		Map<String,Double> xAxisGroupValue = xAxisMap.values().stream().flatMap(m -> m.entrySet().stream()).collect(groupingBy(Entry::getKey, summingDouble(Entry::getValue)));
		Stream<Entry<String,Double>> groupFielTopValue = xAxisGroupValue.entrySet().stream().sorted(Entry.<String,Double>comparingByValue().reversed()).limit(topCount);
		List<String> vouTopKeyList = new ArrayList<>();

		groupFielTopValue.forEach(x-> vouTopKeyList.add(x.getKey( )) ) ;


		for(String xAxisStr : xAxisMap.keySet().stream().sorted().collect(Collectors.toList())) {
			Double total = 0.0d;
			List<Double> groupBottomList = new ArrayList<>();
			Map<String, Object> result = new HashMap<>();

			result.put(xAixField,xAxisStr);

			 if (xAxisMap.values().stream().flatMap(m -> m.entrySet().stream()).collect(groupingBy(Entry::getKey)).size() > topCount) {

				 makeGroupBottomList(chartFilter, xAxisMap, vouTopKeyList, xAxisStr, groupBottomList, result);

				 chartFilter.put(OTHERS, OTHERS);
	        		result.put(OTHERS,groupBottomList.stream().collect(Collectors.summingDouble(Double::doubleValue) )) ;
	            	result.put("total",xAxisMap.get(xAxisStr).entrySet().stream().mapToDouble(Entry<String,Double>::getValue).sum());
	            	resultList.add(result);

			 }else {
						for(String cntrctId : xAxisMap.get(xAxisStr).keySet()) {
							Double wrbtr2OfcntrctId = xAxisMap.get(xAxisStr).get(cntrctId);
							if ( wrbtr2OfcntrctId != null) {
								total += wrbtr2OfcntrctId ;
							}else {
								total += 0.0d;
							}
							result.put(cntrctId, xAxisMap.get(xAxisStr).get(cntrctId));
							chartFilter.put(cntrctId, cntrctId);

						}
					xAxisMap.get(xAxisStr).entrySet().stream().forEach(x-> chartFilter.put(x.getKey() , x.getKey() ));
					result.put("total", total);
					resultList.add(result);
			 }
		}
		resultMap.put("chartData", resultList);
		resultMap.put("chartFilter", chartFilter.keySet());

		return resultMap;
	}

	private static void makeGroupBottomList(Map<String, String> chartFilter, Map<String, Map<String, Double>> xAxisMap, List<String> vouTopKeyList, String xAxisStr, List<Double> groupBottomList, Map<String, Object> result) {
		xAxisMap.get(xAxisStr).entrySet().stream().forEachOrdered(x -> {
			if(vouTopKeyList.contains(x.getKey() ) ) {
				result.put(x.getKey() , x.getValue());

				if(!OTHERS.equals(x.getKey())) {
					chartFilter.put(x.getKey() , x.getKey());
				}else {
					groupBottomList.add( x.getValue() );
					chartFilter.put(OTHERS, OTHERS);
				}

			}else {
				groupBottomList.add( x.getValue() );
			}
		});
	}

	private ChartDataUtil() {
		throw new IllegalStateException("ChartDataUtil");
	}
}
