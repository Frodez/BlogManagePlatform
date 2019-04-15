package frodez.service.article.facade;

import frodez.util.beans.result.Result;

/**
 * 文章信息服务
 * @author Frodez
 * @date 2019-03-06
 */
public interface IArticleService {

	Result getDetail(Long articleId);

}
