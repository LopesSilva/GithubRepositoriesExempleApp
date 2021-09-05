package com.example.githubrepositoriesapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubrepositoriesapp.adapters.RepoAdapter
import com.example.githubrepositoriesapp.databinding.FragmentFirstBinding
import com.example.githubrepositoriesapp.data.model.Repo
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.example.githubrepositoriesapp.R


class ReposFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val viewModel by sharedViewModel<SharedViewModel>(){
        parametersOf(findNavController())
    }
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading(true)

        viewModel.changeActionBarTitle(getString(R.string.fragment_repos_title))
        viewModel.refreshStatus.observe(viewLifecycleOwner, Observer {
            when(it.state){
                BaseViewModel.State.STATES.Error -> showError(it.message)
                BaseViewModel.State.STATES.Success -> showError(getString(R.string.success))
            }
            loading()
        })

        val adapter= RepoAdapter(onClick = {onRepoClicked(it)})

        viewModel.reposList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            loading()})

        binding.reposRecyclerView.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        binding.swiperefresh.setOnRefreshListener {
            viewModel.refreshRepos()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //viewModel.refreshRepos()
        super.onCreate(savedInstanceState)
    }

    private fun showError(message: String? = getString(R.string.generic_error)) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }

    private fun loading(isLoading: Boolean = false){
        binding.loading.root.visibility = if (isLoading) View.VISIBLE else View.GONE

        binding.swiperefresh.apply {
            this.isRefreshing = if (isLoading) this.isRefreshing else false
        }

    }

    private fun onRepoClicked(repo: Repo){
        viewModel.setSelectedRepo(repo)
    }

}