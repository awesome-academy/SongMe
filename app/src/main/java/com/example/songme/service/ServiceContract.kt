package com.example.songme.service

import com.example.songme.data.model.Track

interface ServiceContract {
    interface OnMediaPlayChange {
        fun onTrackChange(tracks: List<Track>, position: Int)
        fun onMediaStateChange(isPlaying: Boolean)
        fun setShuffle(isShuffle: Boolean)
        fun setLoop(loopType: Int)
        fun isDownload(isDownload: Boolean?)
        fun onDownloadSucceeded()
    }
}
