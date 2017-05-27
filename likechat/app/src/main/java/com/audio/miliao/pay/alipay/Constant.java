package com.audio.miliao.pay.alipay;

/**
 * 支付宝的参数
 */
public class Constant
{
    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APP_ID = "2017041406721142";

    /**
     * 商户私钥，pkcs8格式
     * 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个
     * 如果商户两个都设置了，优先使用 RSA2_PRIVATE
     * RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE
     * 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成，
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA_PRIVATE = "";
    public static final String RSA2_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC4tE/4Pi7xgIVipL6SkL3Ds9dnX7iW72TPel+zip313I3Om67aR59krjKLByrqREJHK9RYfglWY/CLm/XtIqe3yZkrcNFZmkikYDw1Qjk3bfZ5uKcM4CPugaJwHld2OsdYgCcRp7FLlZeXYGqgeVozJglXAVJDxEH3V+Lu8QJid5r5L7xfR1BtOfnueoEsnA82NJ43DaTnkYrY+R9X8CFDqKQXk2n9Dwn7MheyVosnKNdBBfVZvHGVg67rHentYt88dx/8tv6WQy11mx/4E7ll3+uGYjIm9610eSd5v8hxwlUqUu79eKCXd2BXOuImWZkodzfH1HqA9bCe7PRdOGFpAgMBAAECggEAdjNOCfshZ4Nr2Ni87GacSiVI7+qd4lmJFg2mOOu4cqKWTWXr29yB8JhZ2AbaO9k64Pj3hzoBF7wVMAK6uuq2kjL9RoOINeM6IZdEcIDUxNgmJ3c30FGQLQ3gugdwyUSMVeKNHgtGtqwgussE8XU3eAwbZgZ+csehv3wKygaKFCCIMH75fdaCymucQheY2qDIA5v3NA1kwLbaWmJB90uks6yHPdEdzspDlZ1kt2G39ooAf4svpsserzbR2HuME6qp6LXzIK+4eO0QapMlfJKFfBk23qrDd4ff94TzKhZdNNu5+IPE8zonJGYElajKO+nWjJ1a7WST++yaYrNJOet6AQKBgQDfE+Om7NjP1Ou2iiDJLIV1IYvuw2G8vOUl7MRHvuD7TEkTYN1MRlzl0vWbYBdeS/bDf/sfCcoXCVhtTQI3aifeXEKXdOJRngWqhYnxAtPbtZDTg/T66KW4GWmJ7H/MQ+X6jyfOxw+8cDjmaj/OArGUjU7CehCTSTrRXiMckcuEcQKBgQDT9qM2NUUdGEEitpV4gW7yV2+0v/3j7g0irMt2GJK/6Yvi+xLTRTXsZGQS6dRu5kS2WqTuLcvjxlF8K8jmWiu1oUlWK0cCB87mSbmHvFYMItPAVTwzP+lEMrhvPaUeqLcs/vICHStTDxC3Cl555nIFAZVCCcUqFu+Yn1D8KrdIeQKBgFwQUkimwMftIKaCFQvRcvanNKACRIjnEE75ivvtG966WgJEXLBI7bfvu2s3Wg2RnjKK0/G4UY32Tmc1e++wwOl0/AqTs39pjv8UA5zSxOqM4UtZzzXp49K5siO41flQIE6q3AvrfWQQkHsPaGu2EuGy+8kpJUQT2dFkX2tABFURAoGAQxQWap1JFgAK/PVTMCOl/MN28nif3HYFaifM676zElDSmC5ksr+AZSMQGHwvYjk1HZBqCYYstBSazPT+SwfWNVCpaL+hZR8bdv113WLYyFZtNLzEpGNhateyTmHv01gJQBAeflTE1TA7VfHOHD21b6gkmBWHc952TJJ+io7qGeECgYEAhzX3dHfzBcsp9cxikTR/QD+tqQ9uKnI2JD5aRKZji5PUkDdMXaE12D8fENcF5ME3H/kp3QQL2KMnks9QsKW3ppldy+52TLrGhFe39BYwz/9zLEmHBTiE2nqkn9xQr2HxW/CighQsRbqH7RPCcoqBDZ7htu+DwWow4DHMhFcYu+o=";

}
