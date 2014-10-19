
# The purpose of this script is to walk through json common files that were get from Polish Corpus dataset
# and replace partia ids' fields so then point out to the disambiguated parties ids

require 'JSON'
require './app/fields_updater'
class App

  def initialize path
    @path = path

  end

  def update_fields!
    filenames = Dir.glob @path
    puts filenames.inspect

    updater = FieldsUpdater.new "old_corpus_parties_ids_to_new_ids.json"

    filenames.each {|filename|
      old_json = JSON.parse(IO.read(filename))
      new_json = updater.get_updated_json old_json, filename

      File.open(filename, "w") do |f|
        f.write(new_json.to_json)
      end
      updater.display_stats
    }
  end
end


app = App.new "../../dataFromCorpus/processed/3/*"
# app = App.new "test_data/*"
app.update_fields!
