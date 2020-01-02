package frodez.dao.mapper.article;

import frodez.config.mybatis.mapper.DataMapper;
import frodez.dao.model.table.article.Article;
import org.springframework.stereotype.Repository;

/**
 * @description 文章表
 * @table tb_article
 * @date 2019-03-06
 */
@Repository
public interface ArticleMapper extends DataMapper<Article> {
}