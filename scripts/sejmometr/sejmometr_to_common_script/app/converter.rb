require_relative 'utils/klub_id_converter'
require 'JSON'
require 'date'
require 'Set'


class Converter
  def initialize path_to_klubs_id_dict, sorted_filenames, output_path
    @klub_ids_converter = KlubIdConverter.new path_to_klubs_id_dict
    @sorter_filenames = sorted_filenames
    @output_path = output_path
    @skipped_zero_bytes_files = 0
    @skipped_fields = Hash.new
    @stanowisko_hash = Hash.new
  end


  def convert! save_to_file

    common_json_file = []
    current_common_json_date_str = nil
    current_common_json_id = 1

    all_files_size = @sorter_filenames.size
    i = 0

    @sorter_filenames.each do |filename|
      i += 1
      puts "#{i} / #{all_files_size} files converted" if (i % 100 == 0)
      current_json = read_json filename
      next if (current_json.nil?)

      current_json_date_str = current_json["_data"]["data"]
      current_common_json_date_str = current_json_date_str if current_common_json_date_str.nil?

      if (current_json_date_str != current_common_json_date_str)
        output_file save_to_file, current_common_json_date_str, common_json_file
        common_json_file = []
        current_common_json_id = 1
        current_common_json_date_str = current_json_date_str
      end


      current_common_json_date = Date.parse(current_common_json_date_str)
      current_common_json =
          {
              :id => "" + current_common_json_id.to_s + current_common_json_date.strftime("%Y%m%d"),
              :data => current_common_json_date.strftime("%Y-%m-%d"),
              :tytul => current_json["_data"]["tytul"],
              :stanowisko => current_json["_data"]["stanowiska.nazwa"],
              :partia => (@klub_ids_converter.convert_klub_id_to_psc_klub_id current_json["_data"]["klub_id"]),
              :posel => generate_posel_id(current_json["_data"]["ludzie.posel_id"])
          }
      validate_fields filename, current_common_json, current_json

      common_json_file << current_common_json
      current_common_json_id += 1
    end

    # display last file
    output_file save_to_file, current_common_json_date_str, common_json_file
    display_stats
  end


  private

  def display_stats
    puts "---------------------- STATISTICS ---------------------- "
    puts "Nr skipped (zero bytes) files: #{@skipped_zero_bytes_files}"
    @skipped_fields.each do |key, value|
      puts "Nr emtpy #{key} fields: #{value}"
    end
    puts "Stanowiska without party_id: (size: #{@stanowisko_hash.size}) :  " + @stanowisko_hash.inspect
  end

  def generate_posel_id(posel_id_str)
    if (posel_id_str.to_i == 0)
      return ""
    else
      return "api_" + posel_id_str
    end
  end

  def read_json filename
    begin
      JSON.parse(IO.read(filename))
    rescue JSON::ParserError => error
      @skipped_zero_bytes_files += 1
      nil
    end
  end

  def output_file save_to_file, date_str, common_json_file
    if save_to_file
      save_to_file date_str, common_json_file
    else
      display_file date_str, common_json_file
    end
  end

  def display_file date, common_json_file
    puts "Displaying file: " + date + ".json -----------------------------------"
    puts common_json_file.inspect
  end

  def save_to_file date_str, common_json_file
    new_filename = date_str + ".json"
    File.open("#{@output_path}/#{new_filename}", "w") do |f|
      f.write(common_json_file.to_json)
    end
  end

  def validate_fields filename, current_common_json_date, current_json
    current_common_json_date.each do |key, value|
      if (value.nil?)
        @skipped_fields[key] = 0 unless  @skipped_fields.has_key? key
        @skipped_fields[key] += 1
        if (key == :partia)
          # puts "partia is null for this guy in #{filename}"
          stanowisko = current_common_json_date[:stanowisko]
          @stanowisko_hash[stanowisko] = 0 unless @stanowisko_hash.has_key? stanowisko
          @stanowisko_hash[stanowisko] += 1
        end
      end
    end
  end


end