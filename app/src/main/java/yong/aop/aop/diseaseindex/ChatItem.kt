package yong.aop.aop.diseaseindex

data class ChatItem(
    val senderId: String,
    val message: String,
    val name : String,
    val date : String
) {

    constructor(): this("", "","","")
}
