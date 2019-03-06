package frodez.service.article.facade;

import frodez.config.aop.validation.annotation.common.NotNullParam;
import frodez.util.beans.result.Result;
import javax.validation.Valid;

/**
 * 文章信息服务
 * @author Frodez
 * @date 2019-03-06
 */
public interface IArticleService {

	Result getDetail(@Valid @NotNullParam Long articleId);

}
