package rodsousa.dev.br.fitnessapp.ui.presentation.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import rodsousa.dev.br.fitnessapp.R
import rodsousa.dev.br.fitnessapp.ui.viewmodel.ImcViewModel

class ImcFragment : Fragment() {

    companion object {
        fun newInstance() = ImcFragment()
    }


    private lateinit var viewModel: ImcViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_imc, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ImcViewModel::class.java)
        // TODO: Use the ViewModel
    }
}