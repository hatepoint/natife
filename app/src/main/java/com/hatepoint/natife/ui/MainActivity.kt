package com.hatepoint.natife.ui

import android.content.Intent
import android.os.Bundle
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
    private val vm by viewModel<MainViewModel>()
    private var spanCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch {
            vm.spanCount.collect {
                spanCount = it
                if (spanCount == 2) {
                    binding.changeViewButton.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this@MainActivity,
                            R.drawable.baseline_list_24
                        )
                    )
                } else {
                    binding.changeViewButton.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this@MainActivity,
                            R.drawable.baseline_grid_view_24
                        )
                    )
                }
            }
        }

        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = GifAdapter()
        (binding.recyclerView.adapter as GifAdapter).onItemClick = { position ->
            val gif = (binding.recyclerView.adapter as GifAdapter).gifs[position]
            val intent = Intent(this, GifActivity::class.java)
            intent.putExtra("gif", gif)
            startActivity(intent)
        }

        binding.changeViewButton.setOnClickListener {
            if (spanCount == 2) {
                vm.setSpanCount(1)
                (binding.recyclerView.layoutManager as StaggeredGridLayoutManager).spanCount = spanCount
            } else {
                vm.setSpanCount(2)
                (binding.recyclerView.layoutManager as StaggeredGridLayoutManager).spanCount = spanCount
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                vm.uiState.collect { state ->
                    when (state) {
                        is MainState.Success -> {
                            binding.progressBar.visibility = android.view.View.GONE
                            (binding.recyclerView.adapter as GifAdapter).gifs = state.data
                            vm.spanCount.collect {
                                spanCount = it
                            }
                        }
                        is MainState.Error -> {
                            binding.progressBar.visibility = android.view.View.GONE
                            binding.errorImageView.visibility = android.view.View.VISIBLE
                            val snackbar = Snackbar.make(
                                binding.root,
                                "Error: ${state.error}",
                                Snackbar.LENGTH_INDEFINITE
                            )
                            snackbar.show()
                        }

                        is MainState.Loading -> {
                            binding.progressBar.visibility = android.view.View.VISIBLE
                        }
                        else -> {}
                    }
                }
            }
        }

    }
}