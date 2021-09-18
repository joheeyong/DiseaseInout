package yong.aop.aop.diseaseindex.Model

data class Chat(
    val senderId: String,
    val message: String,
    val name : String,
    val date : String
) {

    constructor(): this("", "","","")
}
