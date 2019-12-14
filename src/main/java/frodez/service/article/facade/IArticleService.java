package frodez.service.article.facade;

import frodez.config.aop.validation.annotation.Check;
import frodez.util.beans.result.Result;
import javax.validation.constraints.NotNull;

/**
 * 文章信息服务
 * @author Frodez
 * @date 2019-03-06
 */
public interface IArticleService {

	@Check
	Result getDetail(@NotNull Long articleId);

}
