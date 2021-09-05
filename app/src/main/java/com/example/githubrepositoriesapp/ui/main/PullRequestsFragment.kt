package com.example.githubrepositoriesapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubrepositoriesapp.adapters.PullRequestAdapter
import com.example.githubrepositoriesapp.databinding.FragmentSecondBinding
import com.example.githubrepositoriesapp.data.model.PullRequest
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.githubrepositoriesapp.R
import org.koin.core.parameter.parametersOf


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class PullRequestsFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val viewModel by sharedViewModel<SharedViewModel>(){
        parametersOf(findNavController())
    }
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading(true)

        viewModel.changeActionBarTitle(getString(R.string.fragment_pull_requests_name))

        val adapter = PullRequestAdapter(onClick = {onPRClicked(it)})

        binding.pullRequestRecyclerView.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter= adapter
        }

        viewModel.refreshStatus.observe(viewLifecycleOwner, Observer {
            when(it.state){
                BaseViewModel.State.STATES.Error -> showError(it.message) //Substituir por msg ao filtrada para user
                BaseViewModel.State.STATES.Success -> showError(getString(R.string.sucess_message))
            }
            loading()
        })

        viewModel.prs.observe(viewLifecycleOwner, Observer {
                state ->
            when(state.state){
                BaseViewModel.State.STATES.Loading -> {
                    loading(true)
                }
                BaseViewModel.State.STATES.Error -> {
                    loading()
                    Log.e("ERROR",state.message?:"")
                    showError(state.message)
                }
                BaseViewModel.State.STATES.Success -> {
                    loading()
                    state.data.apply {
                        adapter.submitList(this)
                        showEmptyList(this.isEmpty())
                    }

                }
            }
        })

        viewModel.selectedRepo.observe(viewLifecycleOwner, Observer {
            viewModel.getPullRequests(it.owner.login,it.name)

            binding.swiperefresh.setOnRefreshListener {
                viewModel.refreshPullRequests(it.owner.login,it.name)
            }
        })


        viewModel.selectedRepo.value?.apply {
            viewModel.getPullRequests(this.owner.login,this.name)

            binding.swiperefresh.setOnRefreshListener {
                viewModel.refreshPullRequests(this.owner.login,this.name)
            }
        }
    }

    private fun showEmptyList(empty: Boolean) {
        binding.emptyList.root.visibility = if (empty) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showError(message: String? = getString(R.string.generic_error)) {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
    }

    private fun loading(isLoading: Boolean = false){
        binding.loading.root.visibility = if (isLoading) View.VISIBLE else View.GONE

        binding.swiperefresh.apply {
            this.isRefreshing = if (isLoading) this.isRefreshing else false
        }

    }

    private fun onPRClicked(pullRequest: PullRequest){
        Intent(Intent.ACTION_VIEW).apply {
            this.data = Uri.parse(pullRequest.url)
            startActivity(this)
        }
    }
}