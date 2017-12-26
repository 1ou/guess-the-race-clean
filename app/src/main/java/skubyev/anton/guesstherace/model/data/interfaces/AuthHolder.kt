package skubyev.anton.guesstherace.model.data.interfaces

interface AuthHolder {
    var token: String
    var isToken: Boolean
    var idUser: Int
    var userName: String
}