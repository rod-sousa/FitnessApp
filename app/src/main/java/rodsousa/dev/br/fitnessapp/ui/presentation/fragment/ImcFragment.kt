package rodsousa.dev.br.fitnessapp.ui.presentation.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import rodsousa.dev.br.fitnessapp.R
import rodsousa.dev.br.fitnessapp.databinding.FragmentImcBinding
import rodsousa.dev.br.fitnessapp.ui.viewmodel.FitnessViewModel

class ImcFragment : Fragment() {

    private lateinit var viewModel: FitnessViewModel
    private lateinit var binding: FragmentImcBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImcBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this).get(FitnessViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnConfirmed.setOnClickListener{

            val weight = binding.edtWeight.text.toString()
            val height = binding.edtHeight.text.toString()

            val weightDouble = if(weight.isBlank()) 0.0 else weight.toDouble()
            val heightDouble = if(height.isBlank()) 0.0 else height.toDouble()

            if(!validate(weightDouble, heightDouble)){
                Toast.makeText(context, R.string.fields_message, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val result = calculateImc(weightDouble, heightDouble)

            val responseStringImc = imcResponse(result)
            Toast.makeText(context, responseStringImc, Toast.LENGTH_SHORT).show()
        }
    }

    @StringRes
    private fun imcResponse(resultCalcImc: Double):Int{
        return when {
            resultCalcImc < 15.0 -> R.string.imc_severely_low_weight
            resultCalcImc < 16.0 -> R.string.imc_very_low_weight
            resultCalcImc < 18.5 -> R.string.imc_low_weight
            resultCalcImc < 25.0 -> R.string.normal
            resultCalcImc < 30.0 -> R.string.imc_high_weight
            resultCalcImc < 35.0 -> R.string.imc_so_high_weight
            resultCalcImc < 40.0 -> R.string.imc_severely_high_weight
            else -> R.string.imc_extreme_weight
        }
    }

    private fun calculateImc(weight: Double, height: Double) : Double {
        return weight / ( (height/100) * (height/100) )
    }

    private fun validate(weight: Double, height: Double): Boolean{
        return weight != 0.0 && height != 0.0
    }
}