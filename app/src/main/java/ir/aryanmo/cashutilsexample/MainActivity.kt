package ir.aryanmo.cashutilsexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ir.aryanmo.cashutilslibrary.CashUtils

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val h = HashMap<String,String>()
        h["a"] = "sfa"
        CashUtils.getFile("somfoasfs")
    }
}
