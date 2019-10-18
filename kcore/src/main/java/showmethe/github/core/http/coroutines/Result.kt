package showmethe.github.core.http.coroutines

class Result<T>(var status:String, var response: T? = null,code:Int = -1,var message:String = ""){

    companion object{

        const val Success = "Success"
        const val Failure = "Failure"
        const val OutTime = "OutTime"

        fun  <T>newSuccess(response: T? = null) = Result(Success,response)
        fun  <T>newFailed(response: T? = null) = Result(Failure,response)
        fun  <T>newValue( status:String,response: T? = null) = Result(status,response)

    }

    init {

        var resp : ( (response: T?)->Unit)? = null
        fun response(response : (response: T?)->Unit){
            resp = response
        }
        var failed : ( (code: Int,message:String)->Unit)? = null
        fun failure(failure : (code: Int,message:String)->Unit){
            failed = failure
        }
        var over : ( ()->Unit)? = null
        fun outTime(outTime : ()->Unit){
            over = outTime
        }


        when (status) {
            Success -> {
                resp?.invoke(response)
            }
            Failure -> {
                failed?.invoke(code, message)
            }
            OutTime -> {
                over?.invoke()
            }
        }

    }





}