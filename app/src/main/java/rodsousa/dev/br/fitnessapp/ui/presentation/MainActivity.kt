package rodsousa.dev.br.fitnessapp.ui.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import rodsousa.dev.br.fitnessapp.R
import rodsousa.dev.br.fitnessapp.databinding.ActivityMainBinding
import rodsousa.dev.br.fitnessapp.ui.presentation.fragment.HistoryFragment
import rodsousa.dev.br.fitnessapp.ui.presentation.fragment.MoreInformationFragment
import rodsousa.dev.br.fitnessapp.ui.presentation.fragment.ImcFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        replaceFragment(ImcFragment())
        setContentView(binding.root)

        selectedItemBottomNav()
    }

    private fun selectedItemBottomNav() {
        binding.bottomNv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.information -> replaceFragment(MoreInformationFragment())
                R.id.imc -> replaceFragment(ImcFragment())
                R.id.history -> replaceFragment(HistoryFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTrasaction = fragmentManager.beginTransaction()
        fragmentTrasaction.replace(R.id.frame_layout, fragment)
        fragmentTrasaction.commit()
    }
}