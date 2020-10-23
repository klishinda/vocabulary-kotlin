package gq.learningEnglish.common.infrastructure

import gq.learningEnglish.common.infrastructure.interfaces.Logger
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.web.filter.AbstractRequestLoggingFilter
import org.springframework.web.filter.CommonsRequestLoggingFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import org.springframework.web.util.WebUtils
import java.io.UnsupportedEncodingException
import java.lang.StringBuilder
import java.nio.charset.Charset
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomizableRequestLoggingFilter(
    includeLogHeaders: Boolean = true,
    includePayload: Boolean = false,
    includeQueryString: Boolean = true
) : CommonsRequestLoggingFilter(), Logger {

    private val loggedHeaders = mutableListOf<String>()

    init {
        loggedHeaders.addAll(DEFAULT_LOGGED_HEADERS)
        isIncludeHeaders = includeLogHeaders
        isIncludePayload = includePayload
        isIncludeQueryString = includeQueryString
    }

    fun addLoggedHeaders(vararg headers: String) = loggedHeaders.addAll(listOf(*headers))

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestToUse = ContentCachingRequestWrapper(request)
        val responseToUse = ContentCachingResponseWrapper(response)
        val v = getBeforeMessage(requestToUse, responseToUse)
        println(v)
        beforeRequest(requestToUse, v)
        filterChain.doFilter(requestToUse, responseToUse)
        val vv = getAfterMessage(requestToUse, responseToUse)
        println(vv)
        afterRequest(requestToUse, getAfterMessage(requestToUse, responseToUse))
        //filterChain.doFilter(request, response)
    }

    private fun createMessage(
        request: HttpServletRequest,
        response: HttpServletResponse,
        prefix: String?,
        suffix: String?
    ): String {
        return StringBuilder().apply {
            append(prefix)
            append("method=").append(request.method)
            append(";uri=").append(request.requestURI)
            includeQueryString(request)
            includeClientInfo(request)
            includeHeaders(request)
            includePayload(request)
            logResponse(response)
            append(suffix)
        }.toString()
    }

    private fun StringBuilder.includeQueryString(request: HttpServletRequest) {
        val queryString = request.queryString
        if (queryString != null) append("?").append(queryString)
    }

    private fun StringBuilder.includeClientInfo(request: HttpServletRequest) {
        val client = request.remoteAddr
        val session = request.getSession(false)
        val user = request.remoteUser
        if (client.isNullOrEmpty()) append(";client=").append(client)
        if (session != null) append(";session=").append(session)
        if (user != null) append(";user=").append(user)
    }

    private fun getRequestPayload(request: HttpServletRequest): String? {
        val wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper::class.java)
        return wrapper?.let {
            val buffer = wrapper.contentAsByteArray
            if (buffer.isNotEmpty()) {
                val length = buffer.size.coerceAtMost(MAXIMUM_PAYLOAD_LENGTH)
                String(buffer, 0, length, Charset.forName(wrapper.characterEncoding))
            } else null
        }
    }

    private fun getResponsePayload(request: HttpServletResponse): String? {
        val wrapper = WebUtils.getNativeResponse(request, ContentCachingResponseWrapper::class.java)
        if (wrapper != null) {
            val buffer = wrapper.contentAsByteArray
            if (buffer.isNotEmpty()) {
                val length = buffer.size.coerceAtMost(MAXIMUM_PAYLOAD_LENGTH)
                return try {
                    String(buffer, 0, length, Charset.forName(wrapper.characterEncoding))
                } catch (e: UnsupportedEncodingException) {
                    "[UnsupportedEncodingException]"
                } finally {
                    wrapper.copyBodyToResponse()
                }
            }
        }
        return null
    }

    private fun StringBuilder.includePayload(request: HttpServletRequest) {
        val payload = getRequestPayload(request)
        if (payload != null) append(";payload=").append(payload.replace("[\r\n]".toRegex(), ""))
    }

    private fun StringBuilder.includeHeaders(request: HttpServletRequest) {
        val headers = ServletServerHttpRequest(request).headers
        append(";headers=")
        headers.forEach { k: String, v: List<String?>? ->
            if (loggedHeaders.contains(k.toLowerCase())) append("($k=$v)")
        }
    }

    private fun StringBuilder.logResponse(response: HttpServletResponse) {
        val payload = getResponsePayload(response)
        if (payload != null) append(";response=").append(payload)
    }

    private fun getBeforeMessage(request: HttpServletRequest, response: HttpServletResponse): String =
        createMessage(
            request,
            response,
            AbstractRequestLoggingFilter.DEFAULT_BEFORE_MESSAGE_PREFIX,
            AbstractRequestLoggingFilter.DEFAULT_BEFORE_MESSAGE_SUFFIX
        )

    private fun getAfterMessage(request: HttpServletRequest, response: HttpServletResponse): String =
        createMessage(
            request,
            response,
            AbstractRequestLoggingFilter.DEFAULT_AFTER_MESSAGE_PREFIX,
            AbstractRequestLoggingFilter.DEFAULT_AFTER_MESSAGE_SUFFIX
        )
}

private val DEFAULT_LOGGED_HEADERS = listOf("host", "content-length", "content-type")
private const val MAXIMUM_PAYLOAD_LENGTH = 10000
