package rodsousa.dev.br.fitnessapp.data.model

import androidx.annotation.IntegerRes
import rodsousa.dev.br.fitnessapp.R

object SexSelected {

    const val IC_MEN = R.drawable.ic_men
    const val IC_WOMAN = R.drawable.ic_woman
    private const val IC_WOMAN_SELECTED = R.drawable.ic_woman_selected
    private const val IC_MEN_SELECTED = R.drawable.ic_men_selected

    const val WOMAN = "WOMAN_SELECTED"
    const val MEN = "MEN_SELECTED"

    private var selected : String = ""

    @IntegerRes
    fun selectedSex(sex : String) : Int?{
        selected = sex
        return when(sex){
            WOMAN -> IC_WOMAN_SELECTED
            MEN   -> IC_MEN_SELECTED
            else  -> null
        }
    }

    fun getSelected() = selected
}