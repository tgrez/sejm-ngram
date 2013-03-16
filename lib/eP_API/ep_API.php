<?
  // TUTAJ WKLEJ SWOJE KLUCZE API. ABY JE UZYSKAĆ, MUSISZ ZAREJETROWAĆ KONTO NA PORTALU http://sejmometr.pl
  // WIĘCEJ INFORMACJI NA http://sejmometr.pl/api
  
  define('eP_API_KEY', '76b56d34fe1c3a0d7ea478d9634ebbce');
  define('eP_API_SECRET', '37a8ed18bea732319ae0a2344ff143ae');









  @include_once('classes/ep_API.php');
  @include_once('classes/ep_Dataset.php');
  @include_once('classes/ep_Object.php');
  @include_once('classes/ep_Search.php');

  @include_once('classes/objects/ep_Gmina.php');
	@include_once('classes/objects/ep_Powiat.php');
	@include_once('classes/objects/ep_Wojewodztwo.php');
	@include_once('classes/objects/ep_Posel.php');
	@include_once('classes/objects/ep_Legislacja_Projekt.php');
	@include_once('classes/objects/ep_Czlowiek.php');
	@include_once('classes/objects/ep_Sejm_Druk.php');
	@include_once('classes/objects/ep_Senat_Druk.php');
	@include_once('classes/objects/ep_Sejm_Interpelacja.php');
	@include_once('classes/objects/ep_Sejm_Klub.php');
	@include_once('classes/objects/ep_Prawo.php');
	@include_once('classes/objects/ep_Ustawa.php');
	@include_once('classes/objects/ep_Senator.php');
	@include_once('classes/objects/ep_Sejm_Komisja.php');
	@include_once('classes/objects/ep_Sejm_Posiedzenie.php');
	@include_once('classes/objects/ep_Sejm_Wystapienie.php');
	@include_once('classes/objects/ep_Sejm_Glosowanie.php');
	@include_once('classes/objects/ep_Sejm_Sprawozdanie.php');
	@include_once('classes/objects/ep_Sejm_Dezyderat.php');
	@include_once('classes/objects/ep_ISIP_Plik.php');
	@include_once('classes/objects/ep_Posel_Rejestr_Korzysci.php');
	@include_once('classes/objects/ep_Posel_Oswiadczenie_Majatkowe.php');
	@include_once('classes/objects/ep__Dataset.php');
	@include_once('classes/objects/ep__Object.php');
	@include_once('classes/objects/ep_Stanowisko.php');
	@include_once('classes/objects/ep_Posel_Komisja_Stanowisko.php');
	@include_once('classes/objects/ep_Wspolpracownik_Posla.php');
	@include_once('classes/objects/ep_Sejm_Okreg_Wyborczy.php');
	@include_once('classes/objects/ep_Posel_Glos.php');
	@include_once('classes/objects/ep_KRS_Wpis.php');
	@include_once('classes/objects/ep_Instytucja.php');
	@include_once('classes/objects/ep_SA_Orzeczenie.php');
	@include_once('classes/objects/ep_SA_Sad.php');
	@include_once('classes/objects/ep_SA_Skarzony_Organ.php');
	@include_once('classes/objects/ep_SA_Orzeczenie_Wynik.php');
	@include_once('classes/objects/ep_Sejm_Posiedzenie_Punkt.php');
	@include_once('classes/objects/ep_SA_Sedzia.php');
	@include_once('classes/objects/ep_NIK_Raport.php');
	@include_once('classes/objects/ep_RCL_Projekt.php');
	@include_once('classes/objects/ep_RCL_Projekt_Etap.php');
	@include_once('classes/objects/ep_RCL_Dokument.php');
	@include_once('classes/objects/ep_Miejscowosc.php');
	@include_once('classes/objects/ep_PNA.php');
  @include_once('classes/objects/ep_Kod_Pocztowy.php');
  @include_once('classes/objects/ep_Bip_Instytucja.php');
  @include_once('classes/objects/ep_RCL_Projekt_Status.php');
  @include_once('classes/objects/ep_Sejm_Glosowanie_Typ.php');
  @include_once('classes/objects/ep_Sejm_Druk_Typ.php');
  @include_once('classes/objects/ep_Sejm_Posiedzenie_Debata.php');
  @include_once('classes/objects/ep_Glosowanie_Sejmowe_Glos.php');
  @include_once('classes/objects/ep_Prawo_Typ.php');
  @include_once('classes/objects/ep_Sejm_Pytanie_Biezace.php');
  @include_once('classes/objects/ep_Sejm_Interpelacja_Pismo.php');
  @include_once('classes/objects/ep_Legislacja_Projekt_Etap.php');
  @include_once('classes/objects/ep_Legislacja_Projekt_Status.php');
  @include_once('classes/objects/ep_Legislacja_Projekt_Podpis.php');
  @include_once('classes/objects/ep_Posel_Aktywnosc.php');
  @include_once('classes/objects/ep_Sejm_Dzien.php');
  @include_once('classes/objects/ep_Sejm_Wniesiony_Projekt.php');
  @include_once('classes/objects/ep_Posel_Wspolpracownik.php');  
  @include_once('classes/objects/ep_SN_Orzeczenie_Autor.php');
  @include_once('classes/objects/ep_SN_Orzeczenie_Forma.php');
  @include_once('classes/objects/ep_SN_Orzeczenie_Izba.php');
  @include_once('classes/objects/ep_SN_Izba.php');
  @include_once('classes/objects/ep_SN_Jednostka.php');
  @include_once('classes/objects/ep_SN_Osoba.php');
  @include_once('classes/objects/ep_SN_Sklad.php');
  @include_once('classes/objects/ep_SN_Orzeczenie_Sedzia.php');
  @include_once('classes/objects/sp_Orzeczenie_SN_Sprawozdawca.php');
  @include_once('classes/objects/ep_SN_Wspolsprawozdawca.php');  
  @include_once('classes/objects/ep_SN_Orzeczenie.php');
  @include_once('classes/objects/ep_SP_Stanowisko.php');
  @include_once('classes/objects/ep_SP_Orzeczenie_Czesc.php');
  @include_once('classes/objects/ep_SP_Orzeczenie_Przepis.php');
  @include_once('classes/objects/ep_SP_Haslo_Tematyczne.php');
  @include_once('classes/objects/ep_SP_Haslo.php');
  @include_once('classes/objects/ep_SP_Sad.php');
  @include_once('classes/objects/ep_Orzeczenie_sp_osoba_stanowisko.php');
  @include_once('classes/objects/ep_SP_Osoba.php');
  @include_once('classes/objects/ep_SP_Orzeczenie.php');
  @include_once('classes/objects/ep_SP_Teza.php');
  @include_once('classes/objects/ep_BDL_Kategoria.php');
  @include_once('classes/objects/ep_BDL_Podgrupa.php');
  @include_once('classes/objects/ep_BDL_Grupa.php');
  @include_once('classes/objects/ep_BDL_Wskaznik_Wariacja.php');
  @include_once('classes/objects/ep_Twitt.php');
  @include_once('classes/objects/ep_Twitt_Tag.php');
  @include_once('classes/objects/ep_Sejm_Komunikat.php');
  @include_once('classes/objects/ep_Senat_senator_komisja.php'); 
  @include_once('classes/objects/ep_Senat_komisja.php');   
  @include_once('classes/objects/ep_Senat_senator_zespol_parlamentarny.php');
  @include_once('classes/objects/ep_Senat_zespol_parlamentarny.php');
  @include_once('classes/objects/ep_Senat_senator_zespol_senacki.php');
  @include_once('classes/objects/ep_Senat_Zespol.php');
  @include_once('classes/objects/ep_Senat_Oswiadczenie_Majatkowe.php');  
  @include_once('classes/objects/ep_Senat_rejestr_korzysci.php'); 
  @include_once('classes/objects/ep_Senat_Wystapienie.php');
  @include_once('classes/objects/ep_Sejm_Glosowanie_Glos.php'); 
