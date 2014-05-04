import org.scalatest.FunSpec
import org.scalatest.matchers._
import scala.collection.GenTraversable
import Matchers._

class PlaylistSpec extends FunSpec with MustMatchers {

  describe("playlist generator") {
    it("includes at least one song from a country album") {
      val playlist: Playlist = new MixedGenrePlaylistGenerator().generate

      playlist must be(aPlaylist.copy(withSongs = contains(aSong.copy(fromAlbum = anAlbum.copy(withGenre = equal("Country"))))))
    }

    it("includes at least one song shorter than 3 minutes from a compilation album") {
      val playlist: Playlist = new MixedGenrePlaylistGenerator().generate

      playlist must be(aPlaylist.copy(withSongs = contains(
        aSong.copy(withDuration = (be <= 180),
                   fromAlbum = anAlbum.copy(isCompilation = be(true), withGenre = equal("Country"))))))
    }
  }

}

object Matchers {
  def alwaysMatches[A] = new AlwaysMatches[A]()
  def aPlaylist = PlaylistMatcher(withSongs = alwaysMatches[GenTraversable[Song]])
  def aSong = SongMatcher(fromAlbum = alwaysMatches[Album], withDuration = alwaysMatches[Int])
  def anAlbum = AlbumMatcher(withGenre = alwaysMatches[String], isCompilation = alwaysMatches[Boolean])

  def contains[A](matcher: Matcher[A]) = new ContainsMatcher(matcher)
  def allOf[A](matchers: Matcher[A]*) = new MatchesAllOf[A](matchers)
}

case class SongMatcher(fromAlbum: Matcher[Album], withDuration: Matcher[Int]) extends Matcher[Song] with MustMatchers {
  def apply(song: Song): MatchResult = {
    val albumMatch = fromAlbum.compose({ (song: Song) => song.fromAlbum })
    val durationMatch = withDuration.compose({ (song: Song) => song.duration })

    allOf(albumMatch, durationMatch).apply(song)
  }
}

case class PlaylistMatcher(withSongs: Matcher[GenTraversable[Song]]) extends BeMatcher[Playlist] {
  def apply(playlist: Playlist): MatchResult = {
    withSongs.apply(playlist.songs)
  }
}

case class AlbumMatcher(withGenre: Matcher[String], isCompilation: Matcher[Boolean]) extends Matcher[Album] with MustMatchers {
  def apply(album: Album): MatchResult = {
    val genreMatch = withGenre.compose({(album: Album) => album.genre})
    val isCompilationmatch = isCompilation.compose({(album: Album) => album.isCompilation})
    allOf(genreMatch, isCompilationmatch).apply(album)
  }
}

class AlwaysMatches[A] extends Matcher[A] {
  def apply(a: A) = MatchResult(true, "", "")
}

class ContainsMatcher[A](right: Matcher[A]) extends Matcher[GenTraversable[A]] {
  def apply(left: GenTraversable[A]) = {
    val matchResults = left map right
    MatchResult(
      matchResults.find(_.matches) != None,
      matchResults.map(_.failureMessage).mkString(" and "),
      matchResults.map(_.negatedFailureMessage).mkString(" and ")
    )
  }
}

class MatchesAllOf[A](matchers: Seq[Matcher[A]]) extends Matcher[A] {

  def apply(left: A) = {
    val matchResults = matchers.map(matcher => matcher.apply(left))
    val failedMatchResults = matchResults.filterNot(_.matches)
    println(failedMatchResults)

    val mismatches = failedMatchResults.map(_.failureMessage)

    MatchResult(failedMatchResults.isEmpty,
      "did not match all of %s".format(mismatches.mkString(", \n")),
      "matched all of %s".format(mismatches.mkString(", \n"))
    )
  }
}