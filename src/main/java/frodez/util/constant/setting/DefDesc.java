package frodez.util.constant.setting;

import lombok.experimental.UtilityClass;

/**
 * 默认提示语设置
 * @author Frodez
 * @date 2019-03-27
 */
@UtilityClass
public class DefDesc {

	/**
	 * 警告
	 * @author Frodez
	 * @date 2019-03-27
	 */
	@UtilityClass
	public class Warn {

		/**
		 * 空对象的默认返回值
		 */
		public static final String NULL_WARN = "不能为空";

		/**
		 * 非法参数的默认返回值
		 */
		public static final String ILLEGAL_PARAM_WARN = "参数错误";

	}

	/**
	 * 信息
	 * @author Frodez
	 * @date 2019-03-27
	 */
	@UtilityClass
	public class Message {

		/**
		 * 分页查询参数的提示
		 */
		public static final String PAGE_QUERY = "分页查询参数";

		/**
		 * 分页查询返回值的提示
		 */
		public static final String PAGE_VO = "分页查询数据";

		/**
		 * 通用返回值的提示
		 */
		public static final String RESULT = "通用返回值包装";

	}

}
