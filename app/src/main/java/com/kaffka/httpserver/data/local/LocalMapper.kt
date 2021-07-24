import com.kaffka.httpserver.domain.Call
import com.kaffka.httpserver.domain.CallType
import java.util.*
import com.kaffka.httpserver.data.local.Call as CallEntity

fun CallEntity.toDomain(): Call =
    Call(
        startDate = Date(startDate),
        duration = duration,
        number = number,
        name = name,
        type = type.toCalType(),
        timesQueried = null
    )

private fun Int.toCalType() = when (this) {
    1 -> CallType.INCOMING
    2 -> CallType.OUTGOING
    3 -> CallType.MISSED
    4 -> CallType.VOICEMAIL
    5 -> CallType.REJECTED
    6 -> CallType.BLOCKED
    else -> CallType.UNKNOWN
}
