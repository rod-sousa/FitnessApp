package rodsousa.dev.br.fitnessapp.ui.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import rodsousa.dev.br.fitnessapp.R
import rodsousa.dev.br.fitnessapp.ui.viewmodel.FitnessViewModel

class MoreInformationFragment : Fragment() {

    private lateinit var viewModel: FitnessViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(FitnessViewModel::class.java)
        return inflater.inflate(R.layout.fragment_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO
    }
}