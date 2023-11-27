val Int.timeText: String
    get() = StringBuilder()
        .append(
            if(this >= 86400 * 30) "${this / 86400 / 30}개월 "
            else ""
        )
        .append(
            if(this >= 86400) "${this / 86400}일 "
            else ""
        )
        .append(
            if(this >= 3600) "${this / 3600}시간 "
            else ""
        )
        .append(
            if(this >= 60) "${(this % 3600) / 60}분 "
            else ""
        )
        .append(
            if(this % 60 != 0 || this < 60) "${(this % 60)}초"
            else ""
        )
        .toString().trim()