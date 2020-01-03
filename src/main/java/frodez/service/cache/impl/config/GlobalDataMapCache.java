package frodez.service.cache.impl.config;

import frodez.constant.keys.config.GlobalDataKey;
import frodez.dao.mapper.config.GlobalDataMapper;
import frodez.dao.model.table.config.GlobalData;
import frodez.service.cache.facade.config.IGlobalDataCache;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("globalDataMapCache")
public class GlobalDataMapCache implements IGlobalDataCache {

	private Map<Enum<? extends GlobalDataKey<?>>, GlobalData> cache = new HashMap<>();

	private GlobalDataMapper globalDataMapper;

	@Autowired
	public GlobalDataMapCache(GlobalDataMapper globalDataMapper) {
		this.globalDataMapper = globalDataMapper;
		init();
	}

	private void init() {
		List<GlobalData> globalDatas = globalDataMapper.selectAll();
		Set<String> keys = new HashSet<>();
		for (GlobalData globalData : globalDatas) {
			if (!keys.add(globalData.getName())) {
				throw new IllegalArgumentException("存在重复的配置字段名!");
			}
		}
		cache = globalDatas.stream().collect(Collectors.toMap((item) -> GlobalDataKey.check(item.getType(), item.getName()), (item) -> item));
	}

	@Override
	public void clear() {
		cache.clear();
		init();
	}

	@Override
	public List<GlobalData> getAll() {
		List<GlobalData> globalDatas = new ArrayList<>();
		globalDatas.addAll(cache.values());
		return globalDatas;
	}

	@Override
	public <V> void save(Enum<? extends GlobalDataKey<V>> key, V value) {
		GlobalData data = getData(key);
		if (data.getContent().equals(value.toString())) {
			//如果相等就直接返回
			return;
		}
		//保存缓存
		data.setContent(value.toString());
		//写入db
		GlobalData record = new GlobalData();
		record.setId(data.getId());
		record.setContent(value.toString());
		globalDataMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <V> V get(Enum<? extends GlobalDataKey<V>> key) {
		GlobalData data = getData(key);
		GlobalDataKey<V> reverter = (GlobalDataKey<V>) key;
		return reverter.revert(data.getContent());
	}

	private <V> GlobalData getData(Enum<? extends GlobalDataKey<V>> key) {
		GlobalData data = cache.get(key);
		if (data == null) {
			data = globalDataMapper.selectOneEqual("name", key.name());
			if (data == null) {
				throw new IllegalStateException("该配置在缓存及数据库中无数据!");
			}
			cache.put(key, data);
		}
		return data;
	}

}
