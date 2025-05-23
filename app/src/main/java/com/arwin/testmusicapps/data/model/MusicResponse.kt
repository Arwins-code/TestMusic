package com.arwin.testmusicapps.data.model

data class MusicResponse(
    var resultCount: Int?,
    var results: List<MusicResult>
)

data class MusicResult(
    var artistName: String?,
    var artworkUrl100: String?,
    var artworkUrl30: String?,
    var artworkUrl60: String?,
    var collectionArtistId: Int?,
    var collectionArtistViewUrl: String?,
    var collectionCensoredName: String?,
    var collectionExplicitness: String?,
    var collectionHdPrice: Double?,
    var collectionId: Int?,
    var collectionName: String?,
    var collectionPrice: Double?,
    var collectionViewUrl: String?,
    var contentAdvisoryRating: String?,
    var country: String?,
    var currency: String?,
    var discCount: Int?,
    var discNumber: Int?,
    var hasITunesExtras: Boolean?,
    var kind: String?,
    var longDescription: String?,
    var previewUrl: String?,
    var primaryGenreName: String?,
    var releaseDate: String?,
    var shortDescription: String?,
    var trackCensoredName: String?,
    var trackCount: Int?,
    var trackExplicitness: String?,
    var trackHdPrice: Double?,
    var trackHdRentalPrice: Double?,
    var trackId: Int?,
    var trackName: String?,
    var trackNumber: Int?,
    var trackPrice: Double?,
    var trackRentalPrice: Double?,
    var trackTimeMillis: Int?,
    var trackViewUrl: String?,
    var wrapperType: String?
)