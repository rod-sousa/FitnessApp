package rodsousa.dev.br.fitnessapp.ui.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import rodsousa.dev.br.fitnessapp.R
import rodsousa.dev.br.fitnessapp.data.model.SexSelected
import rodsousa.dev.br.fitnessapp.data.model.SexSelected.IC_MEN
import rodsousa.dev.br.fitnessapp.data.model.SexSelected.IC_WOMAN
import rodsousa.dev.br.fitnessapp.data.model.SexSelected.MEN
import rodsousa.dev.br.fitnessapp.data.model.SexSelected.WOMAN
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

        binding.myToolBar.inflateMenu(R.menu.appbar_imc)
        binding.myToolBar.setOnMenuItemClickListener{
            alertDialogImcDescription()
            false
        }

        selectedSex()

        binding.edtHeight.addTextChangedListener {
            calculateImcAndPositionIndicatorLayout()
        }

        binding.edtWeight.addTextChangedListener {
            calculateImcAndPositionIndicatorLayout()
        }

        binding.edtAge.addTextChangedListener {
            calculateImcAndPositionIndicatorLayout()
        }
    }

    private fun calculateImcAndPositionIndicatorLayout() {
        val weight = binding.edtWeight.text.toString()
        val height = binding.edtHeight.text.toString()
        val age = binding.edtAge.text.toString()

        if (validate(weight, height, age)) {
            return
        }
        val weightDouble = if (weight.isBlank()) 0.0 else weight.toDouble()
        val heightDouble = if (height.isBlank()) 0.0 else height.toDouble()
        val ageInt = if (age.isBlank()) 0 else height.toInt()

        val result = calculateImc(weightDouble, heightDouble)

        if (result < 10 || result > 100) {
            return
        }
        discoverCorrespondingImcFootage(result)

        val resultFormated = String.format("%.1f", result)
        binding.tvResult.text = resultFormated

    }

    private fun selectedSex() {
        binding.ivMen.setOnClickListener {
            val selected = SexSelected.getSelected()
            if (selected.isNotBlank()) {
                when (selected) {
                    MEN -> return@setOnClickListener
                    WOMAN -> binding.ivWoman.setImageResource(IC_WOMAN)
                }
            }
            binding.ivMen.setImageResource(SexSelected.selectedSex(MEN)!!)
        }

        binding.ivWoman.setOnClickListener {
            val selected = SexSelected.getSelected()
            if (selected.isNotBlank()) {
                when (selected) {
                    WOMAN -> return@setOnClickListener
                    MEN -> binding.ivMen.setImageResource(IC_MEN)
                }
            }
            binding.ivWoman.setImageResource(SexSelected.selectedSex(WOMAN)!!)
        }
    }

    private fun discoverCorrespondingImcFootage(resultCalcImc: Double) {
        bgDefinitionDefault()

        val resultCalcBiasPercent = when {
            resultCalcImc < 15.0 -> {
                binding.bgUnderWeight.setBackgroundResource(R.color.gray_300)
                0.05f
            }
            resultCalcImc < 16.0 -> {
                binding.bgUnderWeight.setBackgroundResource(R.color.gray_300)
                0.095f
            }
            resultCalcImc < 18.5 -> {
                binding.bgUnderWeight.setBackgroundResource(R.color.gray_300)
                0.14f
            }
            resultCalcImc < 20.0 -> {
                binding.bgNormalWeight.setBackgroundResource(R.color.gray_300)
                0.25f
            }
            resultCalcImc < 22.5 -> {
                binding.bgNormalWeight.setBackgroundResource(R.color.gray_300)
                0.29f
            }
            resultCalcImc < 25.0 -> {
                binding.bgNormalWeight.setBackgroundResource(R.color.gray_300)
                0.35f
            }
            resultCalcImc < 27.5 -> {
                binding.bgOverweight.setBackgroundResource(R.color.gray_300)
                0.46f
            }
            resultCalcImc < 30.0 -> {
                binding.bgOverweight.setBackgroundResource(R.color.gray_300)
                0.54f
            }
            resultCalcImc < 35.0 -> {
                binding.bgObesity.setBackgroundResource(R.color.gray_300)
                0.68f
            }
            resultCalcImc < 40.0 -> {
                binding.bgObesity.setBackgroundResource(R.color.gray_300)
                0.75f
            }
            resultCalcImc < 45.0 -> {
                binding.bgMorbidObesity.setBackgroundResource(R.color.gray_300)
                0.87f
            }
            resultCalcImc > 45.0 -> {
                binding.bgMorbidObesity.setBackgroundResource(R.color.gray_300)
                0.95f
            }
            else -> 0.0f
        }
        setImcChartLayout(resultCalcBiasPercent)
    }

    private fun bgDefinitionDefault(){
        binding.bgUnderWeight.background = null
        binding.bgMorbidObesity.background = null
        binding.bgNormalWeight.background = null
        binding.bgObesity.background = null
        binding.bgOverweight.background = null
    }

    private fun setImcChartLayout(bias: Float) {
        val params = binding.ivMeter.layoutParams as ConstraintLayout.LayoutParams
        params.horizontalBias = bias
        binding.ivMeter.layoutParams = params
    }

    private fun hideKeyboard() {
        val service = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        service.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

    private fun alertDialogImcDescription() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.imc)
            .setMessage(R.string.imc_desc)
            .setPositiveButton(android.R.string.ok) { _, _ -> }
            .create().show()
    }

    private fun calculateImc(weight: Double, height: Double) =
        weight / ((height / 100) * (height / 100))

    private fun validate(weight: String, height: String, age: String) =
        weight.isBlank() || height.isBlank() || age.isBlank()

}