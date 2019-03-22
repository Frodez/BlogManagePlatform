package frodez.util.mybatis;

import frodez.config.mybatis.DataMapper;
import frodez.util.common.EmptyUtil;
import frodez.util.spring.ContextUtil;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class MybatisHelper {

	private static final int minBatchSize = 8;

	private static final int maxBatchSize = 64;

	private static SqlSessionFactory factory;

	@PostConstruct
	private void init() {
		factory = ContextUtil.get(SqlSessionFactory.class);
		Assert.notNull(factory, "factory must not be null");
	}

	/**
	 * 优化后的智能批量插入<br>
	 * 如果list为空,不会进行插入,避免sql解析异常。<br>
	 * 如果list尺寸小于一定限度,会使用mybatis的批量插入模式。<br>
	 * 如果list尺寸大于一定限度,会将其切割成最大长度不大于某个值的若干片段依次插入,同时也会采取批量插入模式。<br>
	 * @author Frodez
	 * @date 2019-03-22
	 */
	public static <T> void batchInsert(DataMapper<T> mapper, @Nullable List<T> list) {
		Assert.notNull(mapper, "mapper must not be null");
		if (EmptyUtil.yes(list)) {
			return;
		}
		if (list.size() < minBatchSize) {
			SqlSession session = factory.openSession(ExecutorType.BATCH, false);
			for (T object : list) {
				mapper.insertUseGeneratedKeys(object);
			}
			session.commit();
		} else if (list.size() < maxBatchSize) {
			mapper.insertList(list);
		} else {
			SqlSession session = factory.openSession(ExecutorType.BATCH, false);
			int offset = 0;
			while (offset < list.size()) {
				mapper.insertList(list.subList(offset, offset + maxBatchSize > list.size() ? list.size() : offset
					+ maxBatchSize));
				offset = offset + maxBatchSize;
			}
			session.commit();
		}
	}

}
