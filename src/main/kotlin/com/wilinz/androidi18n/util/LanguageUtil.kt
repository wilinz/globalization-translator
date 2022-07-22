package com.wilinz.androidi18n.util

import com.google.gson.Gson


data class Language(
    val name: String="",
    val code: String,
    val directoryName: String="",
)

class Languages : ArrayList<Language>()

object LanguageUtil {

    private var _languages: Languages? = null

    val languages: List<Language>
        get() {
            if (_languages == null) {
                _languages = Gson().fromJson(languagesJson, Languages::class.java)
            }
            return _languages!!
        }

    private const val languagesJson = """[
  {
    "name": "阿尔巴尼亚语",
    "code": "sq",
    "directoryName": "values-sq"
  },
  {
    "name": "阿拉伯语",
    "code": "ar",
    "directoryName": "values-ar"
  },
  {
    "name": "阿姆哈拉语",
    "code": "am",
    "directoryName": "values-am"
  },
  {
    "name": "阿塞拜疆语",
    "code": "az",
    "directoryName": "values-az"
  },
  {
    "name": "爱尔兰语",
    "code": "ga",
    "directoryName": "values-ga"
  },
  {
    "name": "爱沙尼亚语",
    "code": "et",
    "directoryName": "values-et"
  },
  {
    "name": "奥利亚语",
    "code": "or",
    "directoryName": "values-or"
  },
  {
    "name": "巴斯克语",
    "code": "eu",
    "directoryName": "values-eu"
  },
  {
    "name": "白俄罗斯语",
    "code": "be",
    "directoryName": "values-be"
  },
  {
    "name": "保加利亚语",
    "code": "bg",
    "directoryName": "values-bg"
  },
  {
    "name": "冰岛语",
    "code": "is",
    "directoryName": "values-is"
  },
  {
    "name": "波兰语",
    "code": "pl",
    "directoryName": "values-pl"
  },
  {
    "name": "波斯尼亚语",
    "code": "bs",
    "directoryName": "values-bs"
  },
  {
    "name": "波斯语",
    "code": "fa",
    "directoryName": "values-fa"
  },
  {
    "name": "布尔语(南非荷兰语)",
    "code": "af",
    "directoryName": "values-af"
  },
  {
    "name": "鞑靼语",
    "code": "tt",
    "directoryName": "values-tt"
  },
  {
    "name": "丹麦语",
    "code": "da",
    "directoryName": "values-da"
  },
  {
    "name": "德语",
    "code": "de",
    "directoryName": "values-de"
  },
  {
    "name": "俄语",
    "code": "ru",
    "directoryName": "values-ru"
  },
  {
    "name": "法语",
    "code": "fr",
    "directoryName": "values-fr"
  },
  {
    "name": "菲律宾语",
    "code": "tl",
    "directoryName": "values-tl"
  },
  {
    "name": "芬兰语",
    "code": "fi",
    "directoryName": "values-fi"
  },
  {
    "name": "弗里西语",
    "code": "fy",
    "directoryName": "values-fy"
  },
  {
    "name": "高棉语",
    "code": "km",
    "directoryName": "values-km"
  },
  {
    "name": "格鲁吉亚语",
    "code": "ka",
    "directoryName": "values-ka"
  },
  {
    "name": "古吉拉特语",
    "code": "gu",
    "directoryName": "values-gu"
  },
  {
    "name": "哈萨克语",
    "code": "kk",
    "directoryName": "values-kk"
  },
  {
    "name": "海地克里奥尔语",
    "code": "ht",
    "directoryName": "values-ht"
  },
  {
    "name": "韩语",
    "code": "ko",
    "directoryName": "values-ko"
  },
  {
    "name": "豪萨语",
    "code": "ha",
    "directoryName": "values-ha"
  },
  {
    "name": "荷兰语",
    "code": "nl",
    "directoryName": "values-nl"
  },
  {
    "name": "吉尔吉斯语",
    "code": "ky",
    "directoryName": "values-ky"
  },
  {
    "name": "加利西亚语",
    "code": "gl",
    "directoryName": "values-gl"
  },
  {
    "name": "加泰罗尼亚语",
    "code": "ca",
    "directoryName": "values-ca"
  },
  {
    "name": "捷克语",
    "code": "cs",
    "directoryName": "values-cs"
  },
  {
    "name": "卡纳达语",
    "code": "kn",
    "directoryName": "values-kn"
  },
  {
    "name": "科西嘉语",
    "code": "co",
    "directoryName": "values-co"
  },
  {
    "name": "克罗地亚语",
    "code": "hr",
    "directoryName": "values-hr"
  },
  {
    "name": "库尔德语",
    "code": "ku",
    "directoryName": "values-ku"
  },
  {
    "name": "拉丁语",
    "code": "la",
    "directoryName": "values-la"
  },
  {
    "name": "拉脱维亚语",
    "code": "lv",
    "directoryName": "values-lv"
  },
  {
    "name": "老挝语",
    "code": "lo",
    "directoryName": "values-lo"
  },
  {
    "name": "立陶宛语",
    "code": "lt",
    "directoryName": "values-lt"
  },
  {
    "name": "卢森堡语",
    "code": "lb",
    "directoryName": "values-lb"
  },
  {
    "name": "卢旺达语",
    "code": "rw",
    "directoryName": "values-rw"
  },
  {
    "name": "罗马尼亚语",
    "code": "ro",
    "directoryName": "values-ro"
  },
  {
    "name": "马尔加什语",
    "code": "mg",
    "directoryName": "values-mg"
  },
  {
    "name": "马耳他语",
    "code": "mt",
    "directoryName": "values-mt"
  },
  {
    "name": "马拉地语",
    "code": "mr",
    "directoryName": "values-mr"
  },
  {
    "name": "马拉雅拉姆语",
    "code": "ml",
    "directoryName": "values-ml"
  },
  {
    "name": "马来语",
    "code": "ms",
    "directoryName": "values-ms"
  },
  {
    "name": "马其顿语",
    "code": "mk",
    "directoryName": "values-mk"
  },
  {
    "name": "毛利语",
    "code": "mi",
    "directoryName": "values-mi"
  },
  {
    "name": "蒙古语",
    "code": "mn",
    "directoryName": "values-mn"
  },
  {
    "name": "孟加拉语",
    "code": "bn",
    "directoryName": "values-bn"
  },
  {
    "name": "缅甸语",
    "code": "my",
    "directoryName": "values-my"
  },
  {
    "name": "苗语",
    "code": "hmn",
    "directoryName": "values-hmn"
  },
  {
    "name": "南非科萨语",
    "code": "xh",
    "directoryName": "values-xh"
  },
  {
    "name": "南非祖鲁语",
    "code": "zu",
    "directoryName": "values-zu"
  },
  {
    "name": "尼泊尔语",
    "code": "ne",
    "directoryName": "values-ne"
  },
  {
    "name": "挪威语",
    "code": "no",
    "directoryName": "values-no"
  },
  {
    "name": "旁遮普语",
    "code": "pa",
    "directoryName": "values-pa"
  },
  {
    "name": "葡萄牙语",
    "code": "pt",
    "directoryName": "values-pt"
  },
  {
    "name": "普什图语",
    "code": "ps",
    "directoryName": "values-ps"
  },
  {
    "name": "齐切瓦语",
    "code": "ny",
    "directoryName": "values-ny"
  },
  {
    "name": "日语",
    "code": "ja",
    "directoryName": "values-ja"
  },
  {
    "name": "瑞典语",
    "code": "sv",
    "directoryName": "values-sv"
  },
  {
    "name": "萨摩亚语",
    "code": "sm",
    "directoryName": "values-sm"
  },
  {
    "name": "塞尔维亚语",
    "code": "sr",
    "directoryName": "values-sr"
  },
  {
    "name": "塞索托语",
    "code": "st",
    "directoryName": "values-st"
  },
  {
    "name": "僧伽罗语",
    "code": "si",
    "directoryName": "values-si"
  },
  {
    "name": "世界语",
    "code": "eo",
    "directoryName": "values-eo"
  },
  {
    "name": "斯洛伐克语",
    "code": "sk",
    "directoryName": "values-sk"
  },
  {
    "name": "斯洛文尼亚语",
    "code": "sl",
    "directoryName": "values-sl"
  },
  {
    "name": "斯瓦希里语",
    "code": "sw",
    "directoryName": "values-sw"
  },
  {
    "name": "苏格兰盖尔语",
    "code": "gd",
    "directoryName": "values-gd"
  },
  {
    "name": "宿务语",
    "code": "ceb",
    "directoryName": "values-ceb"
  },
  {
    "name": "索马里语",
    "code": "so",
    "directoryName": "values-so"
  },
  {
    "name": "塔吉克语",
    "code": "tg",
    "directoryName": "values-tg"
  },
  {
    "name": "泰卢固语",
    "code": "te",
    "directoryName": "values-te"
  },
  {
    "name": "泰米尔语",
    "code": "ta",
    "directoryName": "values-ta"
  },
  {
    "name": "泰语",
    "code": "th",
    "directoryName": "values-th"
  },
  {
    "name": "土耳其语",
    "code": "tr",
    "directoryName": "values-tr"
  },
  {
    "name": "土库曼语",
    "code": "tk",
    "directoryName": "values-tk"
  },
  {
    "name": "威尔士语",
    "code": "cy",
    "directoryName": "values-cy"
  },
  {
    "name": "维吾尔语",
    "code": "ug",
    "directoryName": "values-ug"
  },
  {
    "name": "乌尔都语",
    "code": "ur",
    "directoryName": "values-ur"
  },
  {
    "name": "乌克兰语",
    "code": "uk",
    "directoryName": "values-uk"
  },
  {
    "name": "乌兹别克语",
    "code": "uz",
    "directoryName": "values-uz"
  },
  {
    "name": "西班牙语",
    "code": "es",
    "directoryName": "values-es"
  },
  {
    "name": "希伯来语",
    "code": "iw",
    "directoryName": "values-iw"
  },
  {
    "name": "希腊语",
    "code": "el",
    "directoryName": "values-el"
  },
  {
    "name": "夏威夷语",
    "code": "haw",
    "directoryName": "values-haw"
  },
  {
    "name": "信德语",
    "code": "sd",
    "directoryName": "values-sd"
  },
  {
    "name": "匈牙利语",
    "code": "hu",
    "directoryName": "values-hu"
  },
  {
    "name": "修纳语",
    "code": "sn",
    "directoryName": "values-sn"
  },
  {
    "name": "亚美尼亚语",
    "code": "hy",
    "directoryName": "values-hy"
  },
  {
    "name": "伊博语",
    "code": "ig",
    "directoryName": "values-ig"
  },
  {
    "name": "意大利语",
    "code": "it",
    "directoryName": "values-it"
  },
  {
    "name": "意第绪语",
    "code": "yi",
    "directoryName": "values-yi"
  },
  {
    "name": "印地语",
    "code": "hi",
    "directoryName": "values-hi"
  },
  {
    "name": "印尼巽他语",
    "code": "su",
    "directoryName": "values-su"
  },
  {
    "name": "印尼语",
    "code": "id",
    "directoryName": "values-id"
  },
  {
    "name": "印尼爪哇语",
    "code": "jw",
    "directoryName": "values-jw"
  },
  {
    "name": "英语",
    "code": "en",
    "directoryName": "values-en"
  },
  {
    "name": "约鲁巴语",
    "code": "yo",
    "directoryName": "values-yo"
  },
  {
    "name": "越南语",
    "code": "vi",
    "directoryName": "values-vi"
  },
  {
    "name": "中文（繁体）",
    "code": "zh-TW",
    "directoryName": "values-zh-rTW"
  },
  {
    "name": "中文（简体）",
    "code": "zh-CN",
    "directoryName": "values-zh-rCN"
  }
]"""
}
