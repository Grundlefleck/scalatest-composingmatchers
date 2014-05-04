case class Song(title: String, duration: Int, fromAlbum: Album, band: Band)

case class Band(name: String)

case class Album(name: String, band: Band, genre: String, isCompilation: Boolean)

case class Playlist(songs: Seq[Song]) {
  override def toString = songs.map(song => "%s - %s (%s)".format(song.title, song.band.name, song.fromAlbum.name)).mkString(",")
}

trait PlaylistGenerator {
  def generate: Playlist
}

class MixedGenrePlaylistGenerator {

  val SamCooke = Band("Sam Cooke")
  val BobDylan = Band("Bob Dylan")
  val TheBeatles = Band("The Beatles")

  val allSongs = Seq(
    Song("A Change Is Gonna Come", 234, Album("Ain't That Good News", SamCooke, "Soul", false), SamCooke),
    Song("Desolation Row", 612, Album("Highway 61 Revisited", BobDylan, "Folk", false), BobDylan),
    Song("Eleanor Rigby", 156, Album("Revolver", TheBeatles, "Pop", false), TheBeatles))

  val JohnnyCash = Band("Johnny Cash")
  val anyCountrySong = Song("Ring Of Fire", 176, Album("Ring of Fire: The Best of Johnny Cash", JohnnyCash, "Country", true), JohnnyCash)

  def generate: Playlist = {
    Playlist(allSongs)
  }
}