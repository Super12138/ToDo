package cn.super12138.todo.views.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.super12138.todo.databinding.FragmentProgressBinding


class ProgressFragment : Fragment() {
    private lateinit var viewModel: ProgressFragmentViewModel
    private lateinit var binding: FragmentProgressBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProgressBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ProgressFragmentViewModel::class.java)

        viewModel.progress.observe(viewLifecycleOwner, Observer { value ->
            binding.progressBar.setProgressCompat(value, true)
        })

        viewModel.totalCount.observe(viewLifecycleOwner, Observer { total ->
            binding.totalTextView.text = total.toString()
        })

        viewModel.completeCount.observe(viewLifecycleOwner, Observer { complete ->
            binding.completeTextView.text = complete.toString()
        })

        viewModel.updateProgress()
    }
}