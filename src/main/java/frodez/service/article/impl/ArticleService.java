package frodez.service.article.impl;

import com.google.common.collect.Lists;
import frodez.config.aop.exception.annotation.CatchAndReturn;
import frodez.config.aop.validation.annotation.Check;
import frodez.config.security.util.UserUtil;
import frodez.constant.enums.common.DeleteEnum;
import frodez.constant.settings.DefStr;
import frodez.dao.mapper.article.ArticleMapper;
import frodez.dao.model.article.Article;
import frodez.dao.result.article.ArticleInfo;
import frodez.dao.result.user.UserInfo;
import frodez.service.article.facade.IArticleService;
import frodez.util.beans.result.Result;
import frodez.util.common.StrUtil;
import javax.validation.constraints.NotNull;
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

	@Check
	@CatchAndReturn
	@Override
	public Result getDetail(@NotNull Long articleId) {
		Article article = articleMapper.selectByPrimaryKey(articleId);
		if (article == null) {
			return Result.fail("未查询到该文章信息");
		}
		if (UserUtil.get().getRoleLevel() > article.getPermitLevel()) {
			return Result.noAccess();
		}
		if (DeleteEnum.YES.getVal() == article.getIsDelete()) {
			return Result.fail("文章已删除");
		}
		UserInfo userInfo = UserUtil.get(article.getUserId());
		if (userInfo == null) {
			return Result.fail();
		}
		ArticleInfo info = new ArticleInfo();
		info.setTitle(article.getTitle());
		info.setDescription(StrUtil.get(article.getDescription()));
		info.setCreateTime(article.getCreateTime());
		info.setUpdateTime(article.getUpdateTime());
		info.setAuthorName(userInfo.getName());
		info.setTags(Lists.newArrayList(StrUtil.get(article.getTag()).split(DefStr.SEPERATOR)));
		info.setContent(article.getContent());
		return Result.success(info);
	}

}
