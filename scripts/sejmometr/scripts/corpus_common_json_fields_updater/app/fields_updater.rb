require 'JSON'

class FieldsUpdater

  def initialize path_to_dict_json
    @klub_id_transform_dics = JSON.parse(IO.read(path_to_dict_json))
    puts @klub_id_transform_dics.inspect
  end




  def get_updated_json old_json, filename

    @current_filename = filename
    @nr_posels_updated = 0
    @nr_parties_updated = 0

    new_json = []
    old_json.each { |wystapienie|

      puts "#{filename}    #{wystapienie["partia"]} #{wystapienie["id"]}"
      wystapienie["partia"] = get_updated_partia_id wystapienie["partia"].to_s, filename
      wystapienie["posel"] = get_updated_posel_id  wystapienie["posel"].to_s

      new_json << wystapienie
    }

    new_json
  end

  def display_stats
    if (@nr_posels_updated > 0 || @nr_parties_updated > 0)
      puts "for file #{@current_filename} posels_ids updated: #{@nr_posels_updated}, parties_ids updates #{@nr_parties_updated}"
    end
  end


  private
  def get_updated_partia_id old_partia_id_s, filename
    new_partia_id_s = old_partia_id_s
    if ( @klub_id_transform_dics[old_partia_id_s] != nil)
      # puts "old partia Id: #{old_partia_id_s}, new one #{@klub_id_transform_dics[old_partia_id_s]}, filename: #{filename}"
      new_partia_id_s = @klub_id_transform_dics[old_partia_id_s]
      @nr_parties_updated += 1
    end
    new_partia_id_s
  end

  def get_updated_posel_id old_posel_id_s

    if (old_posel_id_s.split("_").size == 1)
      # puts "updating posel_id"
      old_posel_id_s = generate_psc_posel_id old_posel_id_s
      @nr_posels_updated += 1
    end

    old_posel_id_s
  end

  def generate_psc_posel_id old_posel_id
    "psc_" + old_posel_id
  end

end