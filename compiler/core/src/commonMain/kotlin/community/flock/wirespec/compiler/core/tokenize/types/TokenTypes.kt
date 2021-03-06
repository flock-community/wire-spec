package community.flock.wirespec.compiler.core.tokenize.types

sealed interface TokenType
object LeftCurly : TokenType
object RightCurly : TokenType
object Colon : TokenType
object Comma : TokenType
object CustomValue : TokenType
object EndOfProgram : TokenType

sealed interface WhiteSpace : TokenType
object WhiteSpaceExceptNewLine : WhiteSpace
object NewLine : WhiteSpace

sealed interface Keyword : TokenType
object WsTypeDef : Keyword

sealed interface WsType : Keyword
object WsString : WsType
object WsInteger : WsType
object WsBoolean : WsType
object CustomType : WsType
