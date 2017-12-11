package skubyev.anton.guesstherace.model.data.auth

interface AuthHolder {
    var token: String
    var isToken: Boolean
    var idUser: Int
}