package rodsousa.dev.br.fitnessapp.ui.presentation.fragment

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getColor
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
        binding.myToolBar.setOnMenuItemClickListener {
            alertDialogImcDescription()
            false
        }

        defaultBtnSave()

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

        binding.btnSave.setOnClickListener {
            //TODO alert dialog pegando nome
        }
    }

    private fun calculateImcAndPositionIndicatorLayout() {
        val weight = binding.edtWeight.text.toString()
        val height = binding.edtHeight.text.toString()
        val age = binding.edtAge.text.toString()

        if (validate(weight, height, age) || SexSelected.getSelected().isBlank()) {
            return
        }
        val weightDouble = if (weight.isBlank()) 0.0 else weight.toDouble()
        val heightDouble = if (height.isBlank()) 0.0 else height.toDouble()
        val ageInt = if (age.isBlank()) 0 else height.toInt()

        val result = calculateImc(weightDouble, heightDouble)

        if (result < 2 || result > 100) {
            defaultData()
            return
        }

        discoverCorrespondingImcFootage(result)

        setResultWithAnimation(result)

        enabledBtnSave()
    }

    private fun setResultWithAnimation(result: Double) {
        val numberAnimation = ValueAnimator.ofFloat(0.0f, result.toFloat())

        numberAnimation.addUpdateListener{
            val animatedValue = numberAnimation.animatedValue.toString().toDouble()
            val resultFormated = String.format("%.1f", animatedValue)
            binding.tvResult.text = resultFormated
        }

        numberAnimation.duration = 800
        numberAnimation.start()
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
            calculateImcAndPositionIndicatorLayout()
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
            calculateImcAndPositionIndicatorLayout()
        }
    }

    private fun discoverCorrespondingImcFootage(resultCalcImc: Double) {
        bgDefinitionDefault()

        val grayColor = R.color.gray_300

        val resultCalcBiasPercent = when {
            resultCalcImc < 15.0 -> {
                binding.bgUnderWeight.setBackgroundResource(grayColor)
                0.05f
            }
            resultCalcImc < 16.0 -> {
                binding.bgUnderWeight.setBackgroundResource(grayColor)
                0.095f
            }
            resultCalcImc < 18.5 -> {
                binding.bgUnderWeight.setBackgroundResource(grayColor)
                0.14f
            }
            resultCalcImc < 20.0 -> {
                binding.bgNormalWeight.setBackgroundResource(grayColor)
                0.25f
            }
            resultCalcImc < 22.5 -> {
                binding.bgNormalWeight.setBackgroundResource(grayColor)
                0.29f
            }
            resultCalcImc < 25.0 -> {
                binding.bgNormalWeight.setBackgroundResource(grayColor)
                0.35f
            }
            resultCalcImc < 27.5 -> {
                binding.bgOverweight.setBackgroundResource(grayColor)
                0.46f
            }
            resultCalcImc < 30.0 -> {
                binding.bgOverweight.setBackgroundResource(grayColor)
                0.54f
            }
            resultCalcImc < 35.0 -> {
                binding.bgObesity.setBackgroundResource(grayColor)
                0.68f
            }
            resultCalcImc < 40.0 -> {
                binding.bgObesity.setBackgroundResource(grayColor)
                0.75f
            }
            resultCalcImc < 45.0 -> {
                binding.bgMorbidObesity.setBackgroundResource(grayColor)
                0.87f
            }
            resultCalcImc < 55.0 -> {
                binding.bgMorbidObesity.setBackgroundResource(grayColor)
                0.95f
            }
            resultCalcImc > 55.0 -> {
                binding.bgMorbidObesity.setBackgroundResource(grayColor)
                1.0f
            }
            else -> 0.0f
        }
        setImcChartLayout(resultCalcBiasPercent)
    }

    private fun bgDefinitionDefault() {
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

    private fun enabledBtnSave(){
        val btnSave = binding.btnSave
        btnSave.isEnabled = true
        btnSave.setBackgroundResource(R.drawable.bg_btn_enabled)

        val colorBlack = getColor(requireContext(), R.color.black)
        btnSave.setTextColor(colorBlack)
    }

    private fun defaultData(){
        defaultBtnSave()

        binding.tvResult.text = "0.0"

        bgDefinitionDefault()

        setImcChartLayout(0.0f)
    }

    private fun defaultBtnSave() {
        val btnSave = binding.btnSave
        btnSave.isEnabled = false
        btnSave.setBackgroundResource(R.drawable.bg_btn_disabled)

        val colorGray500 = getColor(requireContext(), R.color.gray_500)
        btnSave.setTextColor(colorGray500)
    }
}