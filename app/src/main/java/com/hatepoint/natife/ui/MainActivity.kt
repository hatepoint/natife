package com.hatepoint.natife.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hatepoint.natife.R
import com.hatepoint.natife.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var gifAdapter: GifAdapter
    private val vm by viewModel<MainViewModel>()
    private var spanCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupChangeViewButtonListener()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupChangeViewButtonListener() {
        lifecycleScope.launch {
            vm.spanCount.collect {
                spanCount = it
                updateChangeViewButtonIcon()
            }
        }
        binding.changeViewButton.setOnClickListener {
            val newSpanCount = if (spanCount == 2) 1 else 2
            vm.setSpanCount(newSpanCount)
            binding.recyclerView.layoutManager =
                StaggeredGridLayoutManager(newSpanCount, StaggeredGridLayoutManager.VERTICAL)
        }
    }

    private fun setupRecyclerView() {
        gifAdapter = GifAdapter()
        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = gifAdapter
        gifAdapter.onItemClick = { position ->
            val gif = gifAdapter.gifs[position]
            val intent = Intent(this, GifActivity::class.java)
            intent.putExtra("gif", gif)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                vm.uiState.collect { state ->
                    when (state) {
                        is MainState.Success -> handleSuccess(state)
                        is MainState.Error -> handleError(state.error)
                        is MainState.Loading -> handleLoading()
                    }
                }
            }
        }
    }

    private fun updateChangeViewButtonIcon() {
        val drawableRes = if (spanCount == 2) R.drawable.baseline_list_24 else R.drawable.baseline_grid_view_24
        binding.changeViewButton.setImageDrawable(
            AppCompatResources.getDrawable(
                this@MainActivity, drawableRes
            )
        )
    }

    private fun handleSuccess(state: MainState.Success) {
        binding.progressBar.visibility = View.GONE
        gifAdapter.gifs = state.data
    }

    private fun handleError(error: String) {
        binding.progressBar.visibility = View.GONE
        binding.errorImageView.visibility = View.VISIBLE
        showSnackbar("Error: $error")
    }

    private fun handleLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_INDEFINITE).show()
    }
}