package cn.super12138.todo.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import cn.super12138.todo.R
import cn.super12138.todo.databinding.FragmentProgressBinding
import cn.super12138.todo.views.BaseFragment
import cn.super12138.todo.views.viewmodels.ProgressViewModel


class ProgressFragment : BaseFragment<FragmentProgressBinding>() {
    private val viewModel: ProgressViewModel by viewModels({ requireActivity() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.progress.observe(viewLifecycleOwner, Observer { value ->
            binding.progressBar.setProgressCompat(value, true)
        })

        viewModel.totalCount.observe(viewLifecycleOwner, Observer { total ->
            binding.totalCount.text = total.toString()
        })

        viewModel.completeCount.observe(viewLifecycleOwner, Observer { complete ->
            binding.completeCount.text = complete.toString()
        })

        viewModel.remainCount.observe(viewLifecycleOwner, Observer { remain ->
            if (remain == 0) {
                binding.remainCount.visibility = View.GONE
            } else {
                binding.remainCount.visibility = View.VISIBLE
                binding.remainCount.text =
                    String.format(getString(R.string.remain_text), remain.toString())
            }
        })

        viewModel.updateProgress()
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): FragmentProgressBinding {
        return FragmentProgressBinding.inflate(inflater, container, attachToRoot)
    }
}