package frodez.service.article.impl;

import frodez.config.security.util.UserUtil;
import frodez.constant.enums.common.DeleteStatus;
import frodez.dao.mapper.article.ArticleMapper;
import frodez.dao.mapper.user.UserMapper;
import frodez.dao.model.result.article.ArticleInfo;
import frodez.dao.model.table.article.Article;
import frodez.service.article.facade.IArticleService;
import frodez.util.beans.result.Result;
import frodez.util.common.StrUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 文章信息服务
 * @author Frodez
 * @date 2019-03-06
 */
@Service
public class ArticleService implements IArticleService {

	@Autowired
	private ArticleMapper articleMapper;

	@Autowired
	private UserMapper userMapper;

	@Override
	public Result getDetail(Long articleId) {
		Article article = articleMapper.selectByPrimaryKey(articleId);
		if (article == null) {
			return Result.fail("未查询到该文章信息");
		}
		//未登录用户为最低等级
		if (UserUtil.role().getLevel() > article.getPermitLevel()) {
			return Result.noAccess();
		}
		if (DeleteStatus.YES.getVal().equals(article.getIsDelete())) {
			return Result.fail("文章已删除");
		}
		String authorName = userMapper.partialOneEqual("name", "id", article.getId());
		ArticleInfo info = new ArticleInfo();
		info.setTitle(article.getTitle());
		info.setDescription(StrUtil.orEmpty(article.getDescription()));
		info.setCreateTime(article.getCreateTime());
		info.setUpdateTime(article.getUpdateTime());
		info.setAuthorName(authorName);
		info.setTags(List.of(StrUtil.orEmpty(article.getTag())));
		info.setContent(article.getContent());
		return Result.success(info);
	}

}
