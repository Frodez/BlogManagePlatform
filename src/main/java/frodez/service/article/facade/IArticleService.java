package frodez.service.article.facade;

import frodez.util.beans.result.Result;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 文章信息服务
 * @author Frodez
 * @date 2019-03-06
 */
public interface IArticleService {

	Result getDetail(@Valid @NotNull Long articleId);

}
