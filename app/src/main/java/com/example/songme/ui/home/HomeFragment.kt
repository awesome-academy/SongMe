package com.example.songme.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.songme.R
import com.example.songme.data.model.Track
import com.example.songme.data.repository.TrackRepository
import com.example.songme.data.source.local.TrackLocalDataSource
import com.example.songme.data.source.remote.TrackRemoteDataSource
import com.example.songme.service.MusicService
import com.example.songme.ui.adapter.TrackAdapter
import com.example.songme.ui.adapter.TrackAdapter.OnSendDataSelectedListener
import com.example.songme.utils.Constants.FLAG_REMOTE
import com.example.songme.utils.showMessage
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment private constructor() : Fragment(), HomeContentContract.View {

    private val presenter: HomeContentContract.Presenter by lazy {
        HomeContentPresenter(
            this,
            TrackRepository.getInstance(
                TrackLocalDataSource.getInstance(activity!!.contentResolver),
                TrackRemoteDataSource.getInstance()
            )
        )
    }
    private lateinit var callback: OnSendDataSelectedListener

    fun setOnSendDataSelectedListener(callback: OnSendDataSelectedListener) {
        this.callback = callback
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getTrending()
    }

    override fun showTracks(tracks: List<Track>) {
        recyclerTrending.adapter = TrackAdapter(FLAG_REMOTE, { item, position ->
            activity?.let { it.startService(MusicService.getIntent(it, item, position)) }
            callback.sendData(item, position)
        }).apply { updateData(tracks) }
    }

    override fun showError(exception: Exception) {
        context?.showMessage(exception.message.toString())
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}
