class KlubIdConverter
  def initialize path_to_json_dict
    @dict = JSON.parse( IO.read(path_to_json_dict) )
  end

  def convert_klub_id_to_psc_klub_id(sejmometr_klub_id)
    @dict[sejmometr_klub_id]
  end


end