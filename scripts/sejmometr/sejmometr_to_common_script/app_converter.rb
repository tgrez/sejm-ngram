#
# Common File format
# [{"id":"119911126",
# "posel":3,
#     "tytul":"Sprawozdanie stenograficzne z obrad Sejmu RP z 26 listopada 1991 r. (kadencja I, Posiedzenie Plenarne 1, dzień 2) Marszałek",
#     "data":"1991-11-26",
#     "stanowisko":"Marszałek",
#     "partia":2,
#     "tresc":"Proszę państwa, otwieram drugą część pierwszego posiedzenia Sejmu pierwszej kadencji."
#
require_relative 'app/converter'
class AppConverter

  def initialize path, output_path
    @path = path
    @output_path = output_path
  end

  def convert
    filenames = Dir.glob @path

    puts "Sorting files..."
    sorted_filenames = sort_files filenames

    puts "Starts converting..."
    converter = Converter.new "app/sejmometr_klub_id_to_psc_klub_id.json", sorted_filenames, @output_path
    converter.convert! true
  end


  private

  def sort_files filenames
    sorted_filenames = filenames.sort_by {
        |file|
      # works for ../wystapienia_all/wystapienie_9580.json
      file.split("/")[2].split("_")[1].split(".")[0].to_i
    }
    sorted_filenames
  end

end

converter = AppConverter.new "../wystapienia_all/*", "output_files/"
# converter = AppConverter.new "test_files/*", "output_files/"
converter.convert


