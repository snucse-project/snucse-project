package wiki.domain.article.exception

import wiki.global.exception.DataNotFoundException
import wiki.global.exception.ErrorType

class ArticleNotFoundByTitleException(detail: String = "") :
    DataNotFoundException(ErrorType.DATA_NOT_FOUND, detail)
