package wiki.domain.article.dto

class ArticleResponse {
    data class TitleResponse(
        val title: String
    )

    data class InfoResponse(
        val title: String,
        val ns: Int,
        val id: Int,
        val redirect: RedirectResponse
    )

    data class RedirectResponse(
        val title: String?
    )

    data class ContentResponse(
        val id: Int,
        val parentId: Int,
        val timestamp: String,
        val contributor: ContributorResponse,
        val comment: String,
        val model: String,
        val format: String,
        val text: TextResponse
    )

    data class ContributorResponse(
        val username: String,
        val id: Int
    )

    data class TextResponse(
        val bytes: Int,
        val content: String
    )
}
