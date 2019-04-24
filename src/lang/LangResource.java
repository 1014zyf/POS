package lang;

import java.util.HashMap;
import java.util.Map.Entry;

public class LangResource {

	/**
	 * Maps ISO 639-3 to I10n
	 */
	private static final HashMap<String, String> m_l10nMap = new HashMap<String, String>(){
		{
			/* Afrikaans */ put("afr", "af");
			/* Albanian */ put("sqi", "sq");
			/* Albanian - bibliographic */ put("alb", "sq");
			/* Arabic */ put("ara", "ar");
			/* Armenian/Armenia */ put("hye", "hy");
			/* Basque */ put("eus", "eu");
			/* Basque */ put("baq", "eu");
			/* Tibetan */ put("bod", "bo");
			/* Tibetan - bibliographic */ put("tib", "bo");
			/* Bosnian */ put("bos", "bs");
			/* Bulgarian */ put("bul", "bg");
			/* Byelorussian */ put("bel", "be");
			/* Catalan */ put("cat", "ca");
			/* Chinese */ put("zho", "zh");
			/* Chinese - bibliographic */ put("chi", "zh");
			/* Croatian */ put("hrv", "hr");
			/* Czech */ put("ces", "cs");
			/* Czech - bibliographic */ put("cze", "cs");
			/* Danish */ put("dan", "da");
			/* Dutch (Standard) */ put("nld", "nl");
			/* Dutch (Standard) - bibliographic */ put("dut", "nl");
			/* English */ put("eng", "en");
			/* Estonian */ put("est", "et");
			/* Faeroese */ put("fao", "fo");
			/* Farsi/Persian */ put("fas", "fa");
			/* Farsi/Persian - bibliographic */ put("per", "fa");
			/* Finnish */ put("fin", "fi");
			/* French (Standard) */ put("fra", "fr");
			/* French (Standard)  - bibliographic */ put("fre", "fr");
			/* Gaelic (Scots) */ put("gla", "gd");
			/* Galician */ put("glg", "gl");
			/* German (Standard) */ put("deu", "de");
			/* German (Standard) - bibliographic */ put("ger", "de");
			/* Greek */ put("gre", "el");
			/* Greek */ put("ell", "el");
			/* Hebrew */ put("heb", "he");
			/* Hindi */ put("hin", "hi");
			/* Hungarian */ put("hun", "hu");
			/* Icelandic */ put("isl", "is");
			/* Icelandic - bibliographic */ put("ice", "is");
			/* Indonesian */ put("ind", "id");
			/* Irish */ put("gle", "ga");
			/* Italian */ put("ita", "it");
			/* Japanese */ put("jpn", "ja");
			/* Kazakh */ put("kaz", "kk");
			/* Kalaallisut (Greenlandic) */ put("kal", "kl");
			/* Korean */ put("kor", "ko");
			/* Latvian */ put("lav", "lv");
			/* Limburgish */ put("lim", "li");
			/* Lithuanian */ put("lit", "lt");
			/* Macedonian */ put("mkd", "mk");
			/* Macedonian - bibliographic */ put("mac", "mk");
			/* Malaysian */ put("msa", "ms");
			/* Malaysian - bibliographic */ put("may", "ms");
			/* Maltese */ put("mlt", "mt");
			/* Norwegian */ put("nor", "no");
			/* Norwegian Bokmal */ put("nob", "nb");
			/* Norwegian Nynorsk */ put("nno", "nn");
			/* Polish */ put("pol", "pl");
			/* Portuguese (Portugal) */ put("por", "pt");
			/* Rhaeto-Romanic */ put("roh", "rm");
			/* Romanian */ put("ron", "ro");
			/* Romanian - bibliographic */ put("rum", "ro");
			/* Russian */ put("rus", "ru");
			/* Sami */ put("sme", "se");
			/* Serbian */ put("srp", "sr");
			/* Slovak */ put("slk", "sk");
			/* Slovak - bibliographic */ put("slo", "sk");
			/* Slovenian */ put("slv", "sl");
			/* Sorbian */ put("wen", "sb");
			/* Spanish (Spain - Traditional) */ put("spa", "es");
			/* Swedish */ put("swe", "sv");
			/* Thai */ put("tha", "th");
			/* Tsonga */ put("tso", "ts");
			/* Tswana */ put("tsn", "tn");
			/* Turkish */ put("tur", "tr");
			/* Ukrainian */ put("ukr", "uk");
			/* Urdu */ put("urd", "ur");
			/* Venda */ put("ven", "ve");
			/* Vietnamese */ put("vie", "vi");
			/* Welsh */ put("cym", "cy");
			/* Welsh - bibliographic */ put("wel", "cy");
			/* Xhosa */ put("xho", "xh");
			/* Yiddish */ put("yid", "yi");
			/* Zulu */ put("zul", "zu");
		}
	};
	
	/**
	 * Maps language code to locale (folder name of HERO POS language files)
	 */
	private static final HashMap<String, String> m_l10nCatalog = new HashMap<String, String>(){
		{
			put("af", "afr");
			put("ar", "ara");
			put("ar-ae", "ar_ae");
			put("ar-bh", "ar_bh");
			put("ar-dz", "ar_dz");
			put("ar-eg", "ar_eg");
			put("ar-iq", "ar_iq");
			put("ar-jo", "ar_jo");
			put("ar-kw", "ar_kw");
			put("ar-lb", "ar_lb");
			put("ar-ly", "ar_ly");
			put("ar-ma", "ar_ma");
			put("ar-om", "ar_om");
			put("ar-qa", "ar_qa");
			put("ar-sa", "ar_sa");
			put("ar-sy", "ar_sy");
			put("ar-tn", "ar_tn");
			put("ar-ye", "ar_ye");
			put("be", "bel");
			put("bg", "bul");
			put("bo", "bod");
			put("bo-cn", "bo_cn");
			put("bo-in", "bo_in");
			put("bs", "bos");
			put("ca", "cat");
			put("cs", "ces");
			put("da", "dan");
			put("de", "deu");
			put("de-at", "de_at");
			put("de-ch", "de_ch");
			put("de-de", "de_de");
			put("de-li", "de_li");
			put("de-lu", "de_lu");
			put("el", "ell");
			put("en", "eng");
			put("en-au", "en_au");
			put("en-bz", "en_bz");
			put("en-ca", "en_ca");
			put("en-gb", "en_gb");
			put("en-ie", "en_ie");
			put("en-jm", "en_jm");
			put("en-nz", "en_nz");
			put("en-tt", "en_tt");
			put("en-us", "en_us");
			put("en-za", "en_za");
			put("es", "spa");
			put("es-ar", "es_ar");
			put("es-bo", "es_bo");
			put("es-cl", "es_cl");
			put("es-co", "es_co");
			put("es-cr", "es_cr");
			put("es-do", "es_do");
			put("es-ec", "es_ec");
			put("es-es", "es_es");
			put("es-gt", "es_gt");
			put("es-hn", "es_hn");
			put("es-mx", "es_mx");
			put("es-ni", "es_ni");
			put("es-pa", "es_pa");
			put("es-pe", "es_pe");
			put("es-pr", "es_pr");
			put("es-py", "es_py");
			put("es-sv", "es_sv");
			put("es-uy", "es_uy");
			put("es-ve", "es_ve");
			put("et", "est");
			put("eu", "eus");
			put("fa", "fas");
			put("fi", "fin");
			put("fo", "fao");
			put("fr", "fra");
			put("fr-be", "fr_be");
			put("fr-ca", "fr_ca");
			put("fr-ch", "fr_ch");
			put("fr-fr", "fr_fr");
			put("fr-lu", "fr_lu");
			put("ga", "gle");
			put("gd", "gla");
			put("gd-ie", "gd_ie");
			put("gl", "glg");
			put("he", "heb");
			put("hi", "hin");
			put("hr", "hrv");
			put("hu", "hun");
			put("hy", "hye");
			put("id", "ind");
			put("is", "isl");
			put("it", "ita");
			put("it-ch", "it_ch");
			put("ja", "jpn");
			put("kk", "kaz");
			put("kl", "kal");
			put("ko", "kor");
			put("ko-kp", "ko_kp");
			put("ko-kr", "ko_kr");
			put("koi8-r", "koi8_r");
			put("li", "lim");
			put("lt", "lit");
			put("lv", "lav");
			put("mk", "mkd");
			put("mk-mk", "mk_mk");
			put("ms", "msa");
			put("mt", "mlt");
			put("nb", "nob");
			put("nl", "nld");
			put("nl-be", "nl_be");
			put("nn", "nno");
			put("no", "nor");
			put("pl", "pol");
			put("pt", "por");
			put("pt-br", "pt_br");
			put("rm", "roh");
			put("ro", "ron");
			put("ro-mo", "ro_mo");
			put("ru", "rus");
			put("ru-mo", "ru_mo");
			put("sb", "wen");
			put("sk", "slk");
			put("sl", "slv");
			put("sq", "sqi");
			put("sr", "srp");
			put("sv", "swe");
			put("sv-fi", "sv_fi");
			put("se", "sme");
			put("th", "tha");
			put("tn", "tsn");
			put("tr", "tur");
			put("ts", "tso");
			put("uk", "ukr");
			put("ur", "urd");
			put("ve", "ven");
			put("vi", "vie");
			put("cy", "cym");
			put("xh", "xho");
			put("yi", "yid");
			put("zh", "zho");
			put("zh-cn", "zh_cn");
			put("zh-hk", "zh_hk");
			put("zh-sg", "zh_sg");
			put("zh-tw", "zh_tw");
			put("zu", "zul");
		}
	};
	
	private static final String DEFAULT_LANG_CODE = "en"; 
	
	private static final String LOCALE_FOLDER_PATH = "messages" + java.io.File.separator + "locale" + java.io.File.separator;
	private static final String PO_FILE_PATH = java.io.File.separator + "LC_MESSAGES" + java.io.File.separator + "pos_app.po";
	
	private HashMap<Integer, String> m_oLangArrayList;
	private HashMap<String, MessageLocale> m_oMessageLocaleList;
	private MessageLocale m_oCurrentMessageLocale;
	
	public LangResource(){
		m_oLangArrayList = new HashMap<Integer, String>();
		m_oMessageLocaleList = new HashMap<String, MessageLocale>();
		
		initLangByCode(DEFAULT_LANG_CODE);
		m_oCurrentMessageLocale = m_oMessageLocaleList.get("en");
	}
	
	public void init(HashMap<Integer, String> oLangCodeList) {
		for(Entry<Integer, String> entry:oLangCodeList.entrySet()) {
			Integer iLangIndex = entry.getKey();
			String sLangCode = entry.getValue();
			m_oLangArrayList.put(iLangIndex, sLangCode);
			initLangByCode(sLangCode);
		}
	}
	
	// Initial the language by provide language code
	private void initLangByCode(String sLangCode){
		String sLocaleCode;
		
		if(m_oMessageLocaleList.containsKey(sLangCode))
			return;
		
		String sMappedLangCode = "";
		if(m_l10nMap.containsKey(sLangCode)){
			// Found in map table
			sMappedLangCode = m_l10nMap.get(sLangCode);
		}else {
			// Cannot found in mapping table, use back original
			sMappedLangCode = sLangCode;
		}
		
		if(m_l10nCatalog.containsKey(sMappedLangCode)){
			sLocaleCode = m_l10nCatalog.get(sMappedLangCode);
		}else{
			// Cannot found the locale, use default
			sLocaleCode = m_l10nCatalog.get(DEFAULT_LANG_CODE);
		}
			
		String sFilePath = LOCALE_FOLDER_PATH + sLocaleCode + PO_FILE_PATH;
		MessageLocale oMessageLocale = new MessageLocale(sFilePath);
		m_oMessageLocaleList.put(sLangCode, oMessageLocale);
	}
	
	public void switchLocale(String sLangCode){
		if(m_oMessageLocaleList.containsKey(sLangCode))
			m_oCurrentMessageLocale = m_oMessageLocaleList.get(sLangCode);
		else{
			// No such lang code,
			// use default
			for(Entry<String, MessageLocale> entry:m_oMessageLocaleList.entrySet()){
				m_oCurrentMessageLocale = entry.getValue();
				break;
			}
		}
	}
	
	public String _(String sText){
		return m_oCurrentMessageLocale.getMessage(sText);
	}
	
	// For compatible, need to add a dummy parameter to determine the old function and new function
	public String[] _(String sText, Object... oAppendObjects) {
		String[] oValueList = new String[5];
		
		for (int i=0; i<5; i++) {
			if (m_oLangArrayList.get(Integer.valueOf(i+1)) != null) {
				MessageLocale oMessageLocale = m_oMessageLocaleList.get(m_oLangArrayList.get(Integer.valueOf(i+1)));
				oValueList[i] = oMessageLocale.getMessage(sText);
			}
		}
		
		for (int i=0; i<oAppendObjects.length; i++) {
			if (oAppendObjects[i] == null)
				break;
			
			if (oAppendObjects[i].getClass().isArray()) {
				// Handle the append array
				String []oAppendStrings = (String[]) oAppendObjects[i];
				for (int j=0; j<oAppendStrings.length; j++) {
					oValueList[j] += oAppendStrings[j];
				}
			} else {
				// Handle the append string
				for (int j=0; j<oValueList.length; j++) {
					oValueList[j] += String.valueOf(oAppendObjects[i]);
				}
			}
		}
		
		return oValueList;
	}
}
