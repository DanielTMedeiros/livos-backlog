package br.edu.infnet.livrosbacklog.fragments



import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.edu.infnet.livrosbacklog.ApiMainActivity
import br.edu.infnet.livrosbacklog.R
import br.edu.infnet.livrosbacklog.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        setup()
        return view
    }

    private fun setup() {
        setupOnClickListeners()
    }

    private fun setupOnClickListeners() {
        binding.btnLivros.setOnClickListener{
            nav(R.id.action_homeFragment_to_livrosFragment)
        }
        binding.btnBuscaApi.setOnClickListener {
            val intent = Intent(activity, ApiMainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}